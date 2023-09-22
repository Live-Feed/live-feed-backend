package com.livefeed.livefeedcommon.kafka.exception;

public class ConsumerRecordValueParsingException extends RuntimeException {

    public ConsumerRecordValueParsingException(String message) {
        super(message);
    }
}
