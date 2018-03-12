package com.aidan.basetools.models.event;

/**
 * Created by Aidan on 2018/3/10.
 */
public class ServiceEvent {

    public final static String SERVICE_MSG_TOKEN_INVALID = "TOKEN_INVALID";
    public final String message;

    public ServiceEvent(String message) {
        this.message = message;
    }
}
