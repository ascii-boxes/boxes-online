package com.thomasjensen.boxes.online;
/*
 * boxes-online - A Web UI for the 'boxes' tool
 * Copyright (C) 2018  Thomas Jensen and the contributors
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License, version 2, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

import java.util.List;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeoutException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * The REST service exposed by the backend for use by the frontend.
 */
@RestController
@EnableAutoConfiguration
public class RestService
{
    private static final Logger LOG = LoggerFactory.getLogger(RestService.class);

    private final BoxesRunnerService boxesRunnerService;

    private final Validator validator;



    @Autowired
    public RestService(final BoxesRunnerService pBoxesRunnerService, final Validator pValidator)
    {
        super();
        Assert.notNull(pBoxesRunnerService, "BoxesRunnerService not injected");
        boxesRunnerService = pBoxesRunnerService;

        Assert.notNull(pValidator, "Validator not injected");
        validator = pValidator;
    }



    @PostMapping(value = "/draw", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> drawBox(@NonNull @RequestBody final Invocation pInvocation)
    {
        if (LOG.isDebugEnabled()) {
            String json = "";
            try {
                json = new ObjectMapper().writeValueAsString(pInvocation);
            }
            catch (JsonProcessingException | RuntimeException e) {
                json = "<ERROR>";
            }
            LOG.debug("drawBox() - received request: " + json);
        }

        try {
            validator.validate(pInvocation);
            List<String> cmdLine = new CommandLineBuilder(pInvocation).build();
            String resultBody = boxesRunnerService.execute(cmdLine, pInvocation.getContent());
            return new ResponseEntity<>(resultBody, HttpStatus.OK);
        }
        catch (InvalidInvocationException e) {
            if (LOG.isDebugEnabled()) {
                String json = "";
                try {
                    json = new ObjectMapper().writeValueAsString(pInvocation);
                }
                catch (JsonProcessingException | RuntimeException ex) {
                    LOG.debug("Error serializing invocation for error message: " + ex.getMessage(), ex);
                }
                LOG.debug("Invocation received did not validate. " + e.getMessage() + (json.isEmpty() ? ""
                    : (" in " + json)));
            }
            return new ResponseEntity<>("bad request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch (RejectedExecutionException | TimeoutException e) {
            LOG.error("Boxes worker thread timed out or was rejected, which means the server is overloaded", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
        }
        catch (InterruptedException | RuntimeException e) {
            LOG.error("internal error: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
