package com.thomasjensen.boxes.online;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Tells *boxes* how to treat the leading tabs in input text.
 */
public enum LeadingTabHandlingMode
{
    /** expand tabs into spaces */
    @JsonProperty("expand")
    Expand,

    /** keep tabs as close to what they were as possible */
    @JsonProperty("keep")
    Keep,

    /** Unexpand tabs. This makes boxes turn as many spaces as possible into tabs. */
    @JsonProperty("unexpand")
    Unexpand;
}
