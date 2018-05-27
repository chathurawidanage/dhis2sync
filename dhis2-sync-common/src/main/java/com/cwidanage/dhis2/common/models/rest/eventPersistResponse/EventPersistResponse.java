package com.cwidanage.dhis2.common.models.rest.eventPersistResponse;

/**
 * @author Chathura Widanage
 */
public class EventPersistResponse {

    private int httpStatusCode;
    private String message;
    private Response response;

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "EventPersistResponse{" +
                "httpStatusCode=" + httpStatusCode +
                ", message='" + message + '\'' +
                ", response=" + response +
                '}';
    }
}



