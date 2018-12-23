package com.thomasjensen.boxes.online;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * Tells <i>boxes</i> how to treat the leading tabs in input text.
 */
public enum LeadingTabHandlingMode
    implements ArgumentValue
{
    /** expand tabs into spaces */
    @JsonProperty("expand")
    Expand('e'),

    /** keep tabs as close to what they were as possible */
    @JsonProperty("keep")
    Keep('k'),

    /** Unexpand tabs. This makes boxes turn as many spaces as possible into tabs. */
    @JsonProperty("unexpand")
    Unexpand('u');


    private final char shortForm;



    private LeadingTabHandlingMode(final char pShortForm)
    {
        shortForm = pShortForm;
    }



    @Override
    public char getShortForm()
    {
        return shortForm;
    }
}
