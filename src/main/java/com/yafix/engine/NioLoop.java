package com.yafix.engine;

import com.yafix.engine.util.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioLoop implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(NioLoop.class);

    private final EngineConfig engineConfig;
    private final ObjectPool<Connection> connectionPool;

    private ServerSocketChannel serverSocketChannel;

    public NioLoop(EngineConfig engineConfig) {
        this.engineConfig = engineConfig;
        this.connectionPool = new ObjectPool<>(10, Connection.class);
    }

    public void start() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(this);
    }

    @Override
    public void run() {
        try {
            Selector selector = Selector.open();
            if (engineConfig.isAcceptor()) {
                serverSocketChannel = ServerSocketChannel.open();
                serverSocketChannel.configureBlocking(false);
                serverSocketChannel.bind(new InetSocketAddress(engineConfig.getServerHost(), engineConfig.getPort()));
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            }
            while (true) {
                int opsNum = selector.select();//todo: with the introduction of timers - block for close to timer expiration
                if (opsNum > 0) {
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iter = selectedKeys.iterator();
                    while (iter.hasNext()) {

                        SelectionKey key = iter.next();

                        if (key.isAcceptable()) {
                            acceptNewConnection(selector);
                        }

                        if (key.isReadable()) {
                            Connection connection = (Connection) key.attachment();
                            connection.readMessage((SocketChannel) key.channel());
                        }
                        iter.remove();
                    }
                }
            }
        } catch (IOException e) {
            log.error("Exception in selector thread, exiting...", e);
        }

    }

    private void acceptNewConnection(Selector selector) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        InetSocketAddress remoteAddress = (InetSocketAddress) socketChannel.getRemoteAddress();
        if (engineConfig.acceptConnection(remoteAddress)) {
            Connection connection = connectionPool.getObject();
            connection.init(socketChannel);
            socketChannel.register(selector, SelectionKey.OP_READ, connection);
            log.info("New connection established from {}", remoteAddress);
        } else {
            log.error("Cannot accept connection from {}", remoteAddress);
            socketChannel.close();
        }
    }
}
