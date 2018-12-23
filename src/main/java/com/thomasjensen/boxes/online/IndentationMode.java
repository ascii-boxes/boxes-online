package com.thomasjensen.boxes.online;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;


/**
 * The possible indentation modes (take effect when input text is indented).
 */
public enum IndentationMode
{
    /** indent box, not text inside of box */
    @JsonProperty("box")
    Box("box"),

    /** indent text inside of box */
    @JsonProperty("text")
    Text("text"),

    /** throw away indentation */
    @JsonProperty("none")
    None("none");


    private final String argumentString;



    private IndentationMode(@NonNull final String pArgumentString)
    {
        argumentString = pArgumentString;
    }



    @NonNull
    public String getArgumentString()
    {
        return argumentString;
    }
}
