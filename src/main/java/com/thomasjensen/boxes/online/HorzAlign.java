package com.thomasjensen.boxes.online;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Specify horizontal alignment.
 */
public enum HorzAlign
    implements ArgumentValue
{
    @JsonProperty("left")
    Left('l'),

    @JsonProperty("center")
    Center('c'),

    @JsonProperty("right")
    Right('r');


    private final char shortForm;



    private HorzAlign(final char pShortForm)
    {
        shortForm = pShortForm;
    }



    public char getShortForm()
    {
        return shortForm;
    }
}
