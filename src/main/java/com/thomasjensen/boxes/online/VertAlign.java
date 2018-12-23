package com.thomasjensen.boxes.online;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Specify vertical alignment.
 */
public enum VertAlign
    implements ArgumentValue
{
    @JsonProperty("top")
    Top('t'),

    @JsonProperty("center")
    Center('c'),

    @JsonProperty("bottom")
    Bottom('b');


    private final char shortForm;



    private VertAlign(final char pShortForm)
    {
        shortForm = pShortForm;
    }



    public char getShortForm()
    {
        return shortForm;
    }
}
