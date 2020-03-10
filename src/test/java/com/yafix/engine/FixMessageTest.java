package com.yafix.engine;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;


public class FixMessageTest {

    private ByteBuffer byteBuffer;
    private FixMessage message;

    @Test
    public void testReadMessage() throws FixParseException {
        byte[] bMsg = toByteString("8=FIX.4.4\u00019=67\u000135=A\u000134=7\u000149=Client1\u000152=20200301-18:20:45.155\u000156=YAFIX\u000198=0\u0001108=10\u000110=015\u0001");
        byteBuffer.put(bMsg);
        Assert.assertTrue(message.readMessage(bMsg.length, true));
        Assert.assertTrue(byteBuffer.remaining() == 0);
        Assert.assertEquals("FIX.4.4", message.getBeginString());
    }

    private byte[] toByteString(String s) {
        byte[] res = new byte[s.length()];
        for (int i = 0; i < s.length(); i++) {
            res[i] = (byte) s.charAt(i);
        }
        return res;
    }


    @Before
    public void setUp() {
        byteBuffer = ByteBuffer.allocateDirect(65536);
        message = new FixMessage(byteBuffer);
    }

    @After
    public void tearDown() {
        byteBuffer.clear();
    }

}
