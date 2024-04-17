package com.livefeed.livefeedservice.newarticle.service;

public interface MessageDelegate {
    void handleMessage(String message);
    // pass the channel/pattern as well
    void handleMessage(String message, String channel);
}
