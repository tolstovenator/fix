package org.yafix.quickfix;

import quickfix.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class QfixTestClient implements Application {

    volatile boolean finished = false;

    public static void main(String[] args) throws Exception {
        Application application = new QfixTestClient();
        SessionSettings settings = new SessionSettings(QfixTestClient.class.getResourceAsStream(args[0]));
        MessageFactory mf = new DefaultMessageFactory();
        MessageStoreFactory messageStoreFactory = new NoopStoreFactory();
        LogFactory logFactory = new SLF4JLogFactory(settings);
        Initiator initiator = new SocketInitiator(application, messageStoreFactory, settings, mf);
        initiator.start();
        while (!((QfixTestClient) application).finished) {
            synchronized (application) {
                application.wait();
            }
        }


    }

    @Override
    public void onCreate(SessionID sessionId) {

    }

    @Override
    public void onLogon(SessionID sessionId) {

    }

    @Override
    public void onLogout(SessionID sessionId) {

    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {

    }

    @Override
    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {

    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {

    }

    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {

    }
}
