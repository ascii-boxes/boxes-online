package com.thomasjensen.boxes.online;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * The possible indentation modes (take effect when input text is indented).
 */
public enum IndentationMode
{
    /** indent box, not text inside of box */
    @JsonProperty("box")
    Box,

    /** indent text inside of box */
    @JsonProperty("text")
    Text,

    /** throw away indentation */
    @JsonProperty("none")
    None;
}
