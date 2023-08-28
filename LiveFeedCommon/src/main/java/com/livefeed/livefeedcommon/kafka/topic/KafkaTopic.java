package com.livefeed.livefeedcommon.kafka.topic;


public enum KafkaTopic {
    LIVEFEED_URL("LIVEFEED.STREAM.ARTICLE.URL"),
    LIVEFEED_HTML("LIVEFEED.STREAM.ARTICLE.HTML");

    private final int FROM_INDEX = 10;

    private final String topic;

    public String getTopic() {
        return topic;
    }

    KafkaTopic(String topic) {
        this.topic = topic;
    }

    public String getRetryTopic() {
        int secondDotLocation = topic.indexOf(".", FROM_INDEX);
        return topic.substring(0, secondDotLocation + 1) + "RETRY";
    }

    public String getDlqTopic() {
        int secondDotLocation = topic.indexOf(".", FROM_INDEX);
        return topic.substring(0, secondDotLocation + 1) + "DLQ";
    }
}
