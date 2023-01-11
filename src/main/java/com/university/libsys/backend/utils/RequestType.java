package com.university.libsys.backend.utils;

public enum RequestType {
    WRITER_ROLE("Request for Writer Role");

    public final String requestType;


    RequestType(String requestType) {
        this.requestType = requestType;
    }
}
