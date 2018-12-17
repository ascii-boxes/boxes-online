package com.thomasjensen.boxes.online;

import com.fasterxml.jackson.annotation.JsonProperty;


public enum HorzAlign
{
    @JsonProperty("left")
    Left,

    @JsonProperty("center")
    Center,

    @JsonProperty("right")
    Right;
}
