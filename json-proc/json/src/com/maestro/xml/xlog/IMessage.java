package com.maestro.xml.xlog;

import javax.swing.*;


public interface IMessage {
    // public const
    public static final int TYPE_DEBUG = 0;
    public static final int TYPE_INFO = 1;
    public static final int TYPE_WARNING = 2;
    public static final int TYPE_ERROR = 3;
    public static final int TYPE_FATAL = 4;

    public String getCategory();

    public String getDescription();

    public Object getSource();

    public String getExeptionText();

    public Icon getIcon();

    public int getType();

    public long getTimeStamp();

    public Throwable getException();
}
