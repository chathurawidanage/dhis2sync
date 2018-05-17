package com.cwidanage.dhis2.common.models.dhis2;

/**
 * @author Chathura Widanage
 */
public class TrackedEntityAttribute {

    private String attribute;
    private String value;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
