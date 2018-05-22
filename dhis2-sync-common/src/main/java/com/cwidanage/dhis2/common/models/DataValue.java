package com.cwidanage.dhis2.common.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author Chathura Widanage
 */
@Entity
public class DataValue {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    private String id;
    private String dataElement;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDataElement() {
        return dataElement;
    }

    public void setDataElement(String dataElement) {
        this.dataElement = dataElement;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataValue dataValue = (DataValue) o;

        if (!id.equals(dataValue.id)) return false;
        if (!dataElement.equals(dataValue.dataElement)) return false;
        return value.equals(dataValue.value);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + dataElement.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}
