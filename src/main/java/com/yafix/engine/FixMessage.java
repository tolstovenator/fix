package com.yafix.engine;

import java.nio.ByteBuffer;

public class FixMessage {
    public static final int TAG_BeginString = 8;

    public static final int SIZE_BeginString = 10; // "8=FIX.4.4^"
    public static final int SIZE_CheckSum = 7; // "10=XXX^"
    private final ByteBuffer inputBuffer;
    private int bodyLength = 0;

    private String beginString;


    public FixMessage(ByteBuffer inputBuffer) {
        this.inputBuffer = inputBuffer;
    }


    public static void checkBeginString(ByteBuffer inputBuffer) {

    }

    public boolean readMessage(int bytesRead, boolean parseBeginString) {
        inputBuffer.flip();
        int lengthLength = extractBodyLength();
        int msgSize = SIZE_BeginString + (3 + lengthLength) +  bodyLength + SIZE_CheckSum;
        if (msgSize <= bytesRead) {
            parseMessage(msgSize, parseBeginString ? 0 : SIZE_BeginString + (3 + lengthLength));
            return true;
        } else {
            return false;
        }
    }

    private void parseMessage(int msgSize, int offset) {
        inputBuffer.position(offset);
        for (int i = 0; i < msgSize - offset; i ++) {
            inputBuffer.get();
        }
    }

    private int extractBodyLength() {
        int pos = FixMessage.SIZE_BeginString + 2;
        bodyLength = 0;
        byte r;
        int lengthLength = 0;
        while (pos < inputBuffer.limit() && (r = inputBuffer.get(pos ++)) != 0x01) {
            bodyLength = bodyLength * 10 + (r - '0');
            lengthLength ++;
        }
        return lengthLength;
    }
}
