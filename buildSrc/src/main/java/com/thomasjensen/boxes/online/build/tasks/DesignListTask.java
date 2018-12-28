package com.thomasjensen.boxes.online.build.tasks;
/*
 * boxes-online - A Web UI for the 'boxes' tool
 * Copyright (C) 2018  Thomas Jensen and the contributors
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.tools.ant.taskdefs.condition.Os;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.Task;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.zeroturnaround.exec.ProcessExecutor;


public class DesignListTask
    extends DefaultTask
{
    /** path to the <i>boxes</i> executable */
    private static final String BOXES_EXECUTABLE = "boxes/boxes" + (Os.isFamily(Os.FAMILY_WINDOWS) ? ".exe" : "");

    /** <i>boxes</i> config file to use */
    private static final String BOXES_CONFIG = "boxes/boxes.cfg";

    private static final Pattern AUTHOR_REGEX = Pattern.compile("^Author:\\s+(.*?)$", Pattern.MULTILINE);

    private static final Pattern DESIGNER_REGEX = Pattern.compile("^Original Designer:\\s+(.*?)$", Pattern.MULTILINE);

    private static final Pattern NO_EMAIL_REGEX = Pattern.compile("^(.+?)(?: <.*)?$");

    private static final Pattern PADDING_REGEX = Pattern.compile("^Default Padding:\\s+(.*?)$", Pattern.MULTILINE);

    private static final Pattern PADDING_PARSE_REGEX = Pattern.compile("(left|top|right|bottom) (\\d+)");

    private static final Pattern SAMPLE_REGEX = Pattern.compile("^Sample:(.*)", Pattern.MULTILINE | Pattern.DOTALL);

    /** Designs to ignore */
    private static final Set<String> BLACKLIST = Set.of("retest", "right", "test1", "test2", "test3", "test4", "test5",
        "test6");

    private final File outFile = new File(getTemporaryDir(), "box-designs.json");



    public DesignListTask()
    {
        super();
        setGroup(BasePlugin.BUILD_GROUP);
        setDescription("Creates " + outFile.getName() + " from the bundled boxes executable");

        getInputs().files(BOXES_EXECUTABLE, BOXES_CONFIG);
        getOutputs().file(outFile);

        getProject().getTasks().all((Task t) -> {
            if (JavaPlugin.PROCESS_RESOURCES_TASK_NAME.equals(t.getName())) {
                t.dependsOn(this);
                // add generated json to resources:
                SourceSetContainer sourceSets = (SourceSetContainer) getProject().getProperties().get("sourceSets");
                SourceSet mainSourceSet = sourceSets.getByName(SourceSet.MAIN_SOURCE_SET_NAME);
                mainSourceSet.getResources().srcDir(outFile.getParentFile());
            }
        });

        doLast((Task t) -> createDesignJson());
    }



    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createDesignJson()
    {
        try {
            Map<String, BoxDesign> boxDesigns = readDesigns();
            outFile.getParentFile().mkdirs();
            String json = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(boxDesigns);
            Files.writeString(Paths.get(outFile.toURI()), json, StandardCharsets.UTF_8, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        }
        catch (JsonProcessingException e) {
            throw new GradleException("error serializing box design information to JSON", e);
        }
        catch (IOException e) {
            throw new GradleException("error writing box design information file", e);
        }
        catch (TimeoutException | InterruptedException e) {
            throw new GradleException("error interacting with boxes executable", e);
        }
    }



    private Map<String, BoxDesign> readDesigns()
        throws InterruptedException, IOException, TimeoutException
    {
        Map<String, BoxDesign> result = new TreeMap<>();
        SortedSet<String> designs = fetchDesignList();
        for (String designName : designs) {
            result.put(designName, fetchDesign(designName));
        }
        return result;
    }



    private SortedSet<String> fetchDesignList()
        throws InterruptedException, TimeoutException, IOException
    {
        List<String> linesRead = new ProcessExecutor().command(BOXES_EXECUTABLE, "-f", BOXES_CONFIG, "-ql")//
            .readOutput(true)//
            .timeout(5, TimeUnit.SECONDS)//
            .exitValueNormal()//
            .execute()//
            .getOutput().getLines(StandardCharsets.US_ASCII.name());
        SortedSet<String> result = linesRead.stream()//
            .filter(line -> line != null && line.length() > 0)//
            .filter(design -> !BLACKLIST.contains(design))//
            .collect(Collectors.toCollection(TreeSet::new));
        return result;
    }



    private BoxDesign fetchDesign(final String pDesignName)
        throws InterruptedException, TimeoutException, IOException
    {
        String read = new ProcessExecutor().command(BOXES_EXECUTABLE, "-f", BOXES_CONFIG, "-qld", pDesignName)//
            .readOutput(true)//
            .timeout(5, TimeUnit.SECONDS)//
            .exitValueNormal()//
            .execute()//
            .getOutput().getString(StandardCharsets.US_ASCII.name());
        BoxDesign result = parseDesignInfo(read);
        return result;
    }



    private BoxDesign parseDesignInfo(final String pBoxesOutput)
    {
        final BoxDesign result = new BoxDesign();

        Matcher matcher = AUTHOR_REGEX.matcher(pBoxesOutput);
        if (matcher.find()) {
            result.setAuthor(removeEmail(matcher.group(1)));
        }

        matcher = DESIGNER_REGEX.matcher(pBoxesOutput);
        if (matcher.find()) {
            result.setDesigner(removeEmail(matcher.group(1)));
        }

        matcher = PADDING_REGEX.matcher(pBoxesOutput);
        if (matcher.find()) {
            result.setDefaultPadding(parsePadding(matcher.group(1)));
        }

        matcher = SAMPLE_REGEX.matcher(pBoxesOutput);
        if (matcher.find()) {
            result.setSample(parseSample(matcher.group(1)));
        }
        return result;
    }



    private String removeEmail(final String pPerson)
    {
        String result = pPerson;
        Matcher matcher = NO_EMAIL_REGEX.matcher(pPerson);
        if (matcher.matches()) {
            result = matcher.group(1);
        }
        return result;
    }



    private BoxDesign.DefaultPadding parsePadding(final String pPaddingStr)
    {
        BoxDesign.DefaultPadding result = new BoxDesign.DefaultPadding();
        Matcher matcher = PADDING_PARSE_REGEX.matcher(pPaddingStr);
        while (matcher.find()) {
            switch (matcher.group(1)) {
                case "top":
                    result.setTop(Integer.parseInt(matcher.group(2)));
                    break;
                case "right":
                    result.setRight(Integer.parseInt(matcher.group(2)));
                    break;
                case "bottom":
                    result.setBottom(Integer.parseInt(matcher.group(2)));
                    break;
                case "left":
                    result.setLeft(Integer.parseInt(matcher.group(2)));
                    break;
                default:
                    throw new IllegalStateException("bug");
            }
        }
        return result;
    }



    private String parseSample(final String pRawSample)
    {
        String result = pRawSample.replace("\r\n", "\n");
        StringBuilder sb = new StringBuilder(result);
        int start = 0;
        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) != '\n') {
                start = i;
                break;
            }
        }
        int end = sb.length();
        for (int i = sb.length() - 1; i > start; i--) {
            if (sb.charAt(i) != '\n') {
                end = i + 1;
                break;
            }
        }
        result = result.substring(start, end) + '\n';
        return result;
    }



    public File getOutFile()
    {
        return outFile;
    }
}
