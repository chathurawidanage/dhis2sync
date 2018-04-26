package com.cwidanage.dhis2.publisher.models;

import com.cwidanage.dhis2.common.models.DataElement;

import java.util.List;

public class DataElementResponse {

    private Pager pager;

    private List<DataElement> dataElements;

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public List<DataElement> getDataElements() {
        return dataElements;
    }

    public void setDataElements(List<DataElement> dataElements) {
        this.dataElements = dataElements;
    }
}
