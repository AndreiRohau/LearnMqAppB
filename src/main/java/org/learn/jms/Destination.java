package org.learn.jms;

public enum Destination {
    QUEUE, TOPIC;

    private int emitted = 0;
    private int processed = 0;
    private final StringBuilder stringBuilder = new StringBuilder();

    public synchronized void riseEmitted() {
        emitted++;
    }
    public int getEmitted() {
        return emitted;
    }

    public synchronized void riseProcessed() {
        processed++;
    }
    public int getProcessed() {
        return processed;
    }

    public synchronized void append(String string) {
        stringBuilder.append(string);
        stringBuilder.append("<br/>");
    }
    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }
}
