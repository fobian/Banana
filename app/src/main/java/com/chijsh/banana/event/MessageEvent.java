package com.chijsh.banana.event;

/**
 * Created by chijsh on 11/8/14.
 */
public class MessageEvent {
    private String mMessage;

    public MessageEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}
