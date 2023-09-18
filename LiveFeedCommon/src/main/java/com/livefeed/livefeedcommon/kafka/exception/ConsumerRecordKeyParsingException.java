package com.livefeed.livefeedcommon.kafka.exception;

public class ConsumerRecordKeyParsingException extends RuntimeException {

    public ConsumerRecordKeyParsingException(String message) {
        super(message);
    }
}
