package com.maestro.xml.xlog;


public interface IMessageListener {
    public void processMessage(IMessage aMessage);

    public void setLogLevel(int aLevel);

    public void cleanUp();
}
