package com.maestro.xml.xlog;

import javax.swing.*;

/**
 * MessageEvent class
 */
public class MessageEvent
        implements IMessage {
    // public fields
    public long timeStamp;
    public String category;
    public String message;
    public Object source;
    public String exceptionText;
    public Icon icon;
    public int type;
    public Throwable exception;

    /**
     * Constructor
     */
    public MessageEvent() {
        category = null; // "application";
        message = null; // "";
        source = null;
        icon = null;
        exception = null;
        type = TYPE_DEBUG;
        timeStamp = System.currentTimeMillis();
    }

    /**
     * getCategory
     *
     * @return String
     */
    public String getCategory() {
        return category;
    }

    /**
     * getDescription
     *
     * @return String
     */
    public String getDescription() {
        return message;
    }

    /**
     * getSource
     *
     * @return Object
     */
    public Object getSource() {
        return source;
    }

    /**
     * getIcon
     *
     * @return Icon
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * getType
     *
     * @return int
     */
    public int getType() {
        return type;
    }

    public String getExeptionText() {
        return exceptionText;
    }

    /**
     * timeStamp
     *
     * @return long
     */
    public long getTimeStamp() {
        return timeStamp;
    }

    public Throwable getException() {
        return exception;
    }
}
