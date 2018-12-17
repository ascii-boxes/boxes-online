package com.thomasjensen.boxes.online;

import com.fasterxml.jackson.annotation.JsonProperty;


public enum VertAlign
{
    @JsonProperty("top")
    Top,

    @JsonProperty("center")
    Center,

    @JsonProperty("bottom")
    Bottom;
}
