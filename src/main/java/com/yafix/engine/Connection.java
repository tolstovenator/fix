package com.yafix.engine;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Physical connection for the session
 */
public class Connection {

    private SocketChannel socketChannel;
    private final Session session = new Session();
    private final ByteBuffer inputBuffer = ByteBuffer.allocate(65536);
    private final boolean checkHeader = false;//check the header formally; otherwise rely on what is required by standard
    private final FixMessage inMessage = new FixMessage(inputBuffer);

    public Connection() {
    }

    public void init(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        session.init();
        inputBuffer.clear();
    }

    public void readMessage(SocketChannel channel) throws IOException {
        int bytesRead = channel.read(inputBuffer);
        try {
            while (inMessage.readMessage(bytesRead, !session.isActive())) {
                processInMessage();
                if (inputBuffer.remaining() > 0) {
                    inputBuffer.compact();
                } else {
                    break;
                }
            }
            if (inputBuffer.remaining() > 0) {
                inputBuffer.position(inputBuffer.limit());
                inputBuffer.limit(inputBuffer.capacity());
            } else {
                inputBuffer.clear();
            }
        } catch (FixParseException e) {
            //TODO
        }

    }

    private void processInMessage() {

    }


    private void readLogonMessage(SocketChannel channel) throws IOException {

    }
}
