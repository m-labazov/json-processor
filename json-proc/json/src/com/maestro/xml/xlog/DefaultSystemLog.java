package com.maestro.xml.xlog;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DefaultSystemLog
        implements IMessageListener {
    // protected field
    protected int fDebugLevel;
    public static final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS");

    /**
     * Constructor
     */
    DefaultSystemLog() {
        fDebugLevel = MessageEvent.TYPE_DEBUG;
    }

    DefaultSystemLog(int aLevel) {
        fDebugLevel = aLevel;
    }

    /**
     * processMessage
     *
     * @param aMessage IMessage
     */
    public void processMessage(IMessage aMessage) {
        if ((aMessage != null) && (fDebugLevel <= IMessage.TYPE_FATAL)) {
            PrintStream out = System.out;
            printMessage(aMessage, fDebugLevel, out);
            out.flush();
        }
    }

    public void cleanUp() {
        // do nothing
    }

    public static void printMessage(IMessage aMessage, int aLevel, PrintStream out) {
        if ((aMessage == null) || (out == null))
            return;
        // check debug level
        if (aMessage.getType() < aLevel)
            return;
        // process exception
        String srcLine = aMessage.getExeptionText();
        if (srcLine != null)
            out.println(srcLine);
        // process type
        String sMode = null;
        int iType = aMessage.getType();
        switch (iType) {
            case IMessage.TYPE_DEBUG:
                sMode = "[Debug]";
                break;
            case IMessage.TYPE_INFO:
                Date now = new Date(aMessage.getTimeStamp());
                sMode = "[" + TIMESTAMP_FORMAT.format(now) + "]";
                break;
            case IMessage.TYPE_WARNING:
                sMode = "[Warning]";
                break;
            case IMessage.TYPE_ERROR:
                sMode = "[Error]";
                break;
            case IMessage.TYPE_FATAL:
                sMode = "[FATAL]";
                break;
            default:
                return;
        }
        // build header
        StringBuilder sb = new StringBuilder(512);
        sb.append(sMode);
        srcLine = null;
        String beginMessage = (iType < IMessage.TYPE_ERROR) ? " " : "\n ";
        if (aMessage.getCategory() != null)
            sb.append('-').append(aMessage.getCategory()).append('-');
        else if (aLevel == IMessage.TYPE_DEBUG) {
            beginMessage = "\n ";
            // use stack trace customerinfo
            Throwable t = new Throwable();
            StackTraceElement[] elements = t.getStackTrace();
            if (elements.length > 3) {
                srcLine = elements[3].toString();
                // resolve inclass onError() method declaration
                if (((srcLine.indexOf("rror") > 0) || (srcLine.indexOf("Log") > 0)) && (elements.length > 4))
                    srcLine = elements[4].toString();
            }
        }
        // process message body
        if (aMessage.getDescription() != null)
            sb.append(beginMessage + " ").append(aMessage.getDescription());
        // process trace customerinfo
        if (srcLine != null)
            sb.append(beginMessage + " at ").append(srcLine);
        // process source element
        Object source = aMessage.getSource();
        if (source != null)
            sb.append(beginMessage + " Source at ").append(source.toString());
        // make system output
        out.println(sb.toString());
    }

    /**
     * setLogLevel
     *
     * @param aLevel int
     */
    public void setLogLevel(int aLevel) {
        fDebugLevel = aLevel;
    }
}
