package com.yafix.engine;

import it.unimi.dsi.fastutil.ints.*;

import java.nio.ByteBuffer;

public class FixMessage {
    public static final int TAG_BeginString = 8;

    public static final int SIZE_BeginString = 10; // "8=FIX.4.4^"
    public static final int SIZE_CheckSum = 7; // "10=XXX^"
    public static final int SOH = 0x01;
    private final ByteBuffer inputBuffer;
    private int bodyLength = 0;

    private long partialChecksum;

    private final Int2LongMap intFieldsMap = new Int2LongArrayMap();
    private final Int2DoubleMap floatFieldsMap = new Int2DoubleArrayMap();
    private final Int2IntMap stringFieldStartMap = new Int2IntArrayMap();
    private final Int2IntMap stringFieldLenMap = new Int2IntArrayMap();
    public String beginString;

    public String getBeginString() {
        return beginString;
    }


    public FixMessage(ByteBuffer inputBuffer) {
        this.inputBuffer = inputBuffer;
    }


    public static void checkBeginString(ByteBuffer inputBuffer) {

    }

    /**
     *
     * @param bytesRead
     * @param parseBeginString
     * @return true, if the message was completely read and successfully parsed
     */
    public boolean readMessage(int bytesRead, boolean parseBeginString) throws FixParseException {
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

    private void parseMessage(int msgSize, int offset) throws FixParseException {
        long checkSum = offset == 0 ? 0 : partialChecksum;
        inputBuffer.position(offset);
        int tagNum = 0;
        int stringLen = 0;
        long longVal = 0;
        double doubleVal = 0;
        boolean readTag = true;
        FixTag tag = null;
        for (int i = offset; i < msgSize; i ++) {
            byte b = inputBuffer.get();
            if (i < msgSize - SIZE_CheckSum) {
                checkSum += b;
            }
            if (readTag) {
                if (b == '=') {
                    readTag = false;
                    tag = FixTags.getByNumber(tagNum);
                    if (tag == null || tag.fixSubType.parent == FixType.String) {//unknown tag, let's parse it as byte array
                        stringFieldStartMap.put(tagNum, i + 1);
                        stringLen = 0;
                    } else if (tag.fixSubType.parent == FixType.Int) {
                        longVal = 0;
                    }//TODO: other cases
                } else {
                    tagNum = tagNum * 10 + (b - '0');
                }
            } else {
                if (b == SOH) {
                    if (tag == null || tag.fixSubType.parent == FixType.String) {
                        stringFieldLenMap.put(tagNum, stringLen);
                    } else if (tag.fixSubType.parent == FixType.Int) {
                        intFieldsMap.put(tagNum, longVal);
                    }
                    if (tag == FixTag.BeginString) {
                        byte[] beginStringBytes = new byte[stringLen];
                        int pos = inputBuffer.position();
                        inputBuffer.position(stringFieldStartMap.get(tagNum));
                        inputBuffer.get(beginStringBytes);
                        inputBuffer.position(pos);
                        beginString = new String(beginStringBytes);
                    }
                    readTag = true;
                }
                else {
                    if (tag == null || tag.fixSubType.parent == FixType.String) {
                        stringLen ++;
                    } else if (tag.fixSubType.parent == FixType.Int) {
                        longVal = longVal * 10 + (b - '0');
                    }
                }
            }
        }
    }

    private int extractBodyLength() {
        int pos = FixMessage.SIZE_BeginString + 2;
        bodyLength = 0;
        byte r;
        int lengthLength = 0;
        while (pos < inputBuffer.limit() && (r = inputBuffer.get(pos ++)) != SOH) {
            bodyLength = bodyLength * 10 + (r - '0');
            lengthLength ++;
        }
        return lengthLength;
    }

    public long getIntField(int tagNum) {
        return intFieldsMap.get(tagNum);
    }
}
