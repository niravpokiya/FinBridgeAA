package com.FinBridgeAA.Account_aggregator_service.Streaming;

public class InMemoryDataPipe {

    private byte[] buffer;

    public void write(byte[] data) {
        this.buffer = data;
    }

    public byte[] read() {
        return buffer;
    }

    public void destroy() {
        if (buffer != null) {
            for (int i = 0; i < buffer.length; i++) {
                buffer[i] = 0;
            }
            buffer = null;
        }
    }
}