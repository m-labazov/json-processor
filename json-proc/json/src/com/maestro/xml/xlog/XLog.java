package com.maestro.xml.xlog;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


final public class XLog {
    // set log manager
    protected static ArrayList fListeners = init();
    protected static Timer fCleanUpManager = null;

    // policy
    protected static int LOG_LEVEL = MessageEvent.TYPE_DEBUG;
    protected static boolean SAVE_EXCEPTION = true;

    /**
     * init
     */
    protected static ArrayList init() {
        ArrayList result = new ArrayList();
        result.add(new DefaultSystemLog());
        return result;
    }

    /**
     * addListener
     *
     * @param aListener
     * @param aCleanUpMsec - must be more 30000 msec
     * @return true if add
     */
    public static boolean addListener(IMessageListener aListener, int aCleanUpMsec) {
        boolean result = false;
        if (fListeners != null) {
			if (fListeners.indexOf(aListener) == -1) {
                result = fListeners.add(aListener);
                if (aCleanUpMsec > 30000) {
					if(fCleanUpManager == null) {
						//create it as a daemon
						fCleanUpManager = new Timer(true);
					}
                    TimerTask task = new CleanUpTask(aListener);
                    fCleanUpManager.scheduleAtFixedRate(task, aCleanUpMsec, aCleanUpMsec);
                }
            }
			if (fListeners.size() > 1) {
				//Remove DefaultSystemLog
				for(int i =0; i< fListeners.size(); i++) {
					IMessageListener element = (IMessageListener) fListeners.get(i);
					if(element instanceof DefaultSystemLog) {
						removeListener(element);
						break;
					}
				}
			}
        }
        return result;
    }

    /**
     * removeListener
     */
    public static boolean removeListener(IMessageListener aListener) {
        boolean result = false;
        if (fListeners != null) {
            result = fListeners.remove(aListener);
        }
        return result;
    }

    /**
     * setLogLevel
     */
    public static void setLogLevel(int aLevel, boolean saveExceptions) {
        LOG_LEVEL = aLevel;
        SAVE_EXCEPTION = saveExceptions;
        for (int i = 0; i < fListeners.size(); i++) {
            IMessageListener item = (IMessageListener) fListeners.get(i);
            item.setLogLevel(aLevel);
        }
    }

    public static int getLogLevel() {
        return LOG_LEVEL;
    }

    /**
     * onDebug
     *
     * @param aMessage String
     */
    public static void onDebug(String aMessage) {
        MessageEvent event = new MessageEvent();
        event.message = aMessage;
        event.type = IMessage.TYPE_DEBUG;
        forseMessage(event);
    }

    public static void onDebug(String aMessage, String aCategory) {
        MessageEvent event = new MessageEvent();
        event.message = aMessage;
        event.category = aCategory;
        event.type = IMessage.TYPE_DEBUG;
        forseMessage(event);
    }

    /**
     * onLog
     *
     * @param aMessage String
     */
    public static void onLog(String aMessage) {
        MessageEvent event = new MessageEvent();
        event.message = aMessage;
        event.type = IMessage.TYPE_INFO;
        forseMessage(event);
    }

    public static void onLog(String aMessage, String aCategory) {
        MessageEvent event = new MessageEvent();
        event.message = aMessage;
        event.category = aCategory;
        event.type = IMessage.TYPE_INFO;
        forseMessage(event);
    }

    /**
     * onWarning
     *
     * @param aMessage String
     */
    public static void onWarning(String aMessage) {
        MessageEvent event = new MessageEvent();
        event.message = aMessage;
        event.type = IMessage.TYPE_WARNING;
        forseMessage(event);
    }

    public static void onWarning(String aMessage, String aCategory) {
        MessageEvent event = new MessageEvent();
        event.message = aMessage;
        event.category = aCategory;
        event.type = IMessage.TYPE_WARNING;
        forseMessage(event);
    }

    public static void onWarning(Throwable aException, String aMessage, String aCategory) {
        MessageEvent event = new MessageEvent();
        event.message = aMessage;
        event.category = aCategory;
        event.type = IMessage.TYPE_WARNING;
        if (SAVE_EXCEPTION)
            event.exception = aException;
        buildStackTrace(event, aException);
        forseMessage(event);
    }


    /**
     * onWarning
     *
     * @param aException Exception
     * @param aMessage   String
     */
    public static void onWarning(Throwable aException, String aMessage) {
        MessageEvent event = new MessageEvent();
        event.message = aMessage;
        event.type = IMessage.TYPE_WARNING;
        if (SAVE_EXCEPTION)
            event.exception = aException;
        buildStackTrace(event, aException);
        forseMessage(event);
    }

    protected static void buildStackTrace(MessageEvent event, Throwable ex) {
        if (ex == null)
            return;
        StringBuilder sb = new StringBuilder(512);
        sb.append("[Exception]-");
        sb.append(ex);
        sb.append("-\n");
        StackTraceElement[] stack = ex.getStackTrace();
        if ((stack != null) && (stack.length > 1)) {
            int levels = stack.length > 3 ? 3 : stack.length;
            for (int i = 0; i < levels; i++) {
                sb.append(stack[i].toString());
                sb.append('\n');
            }
            if (stack.length > levels)
                sb.append("...");
            event.exceptionText = sb.toString();
        }
    }

    /**
     * onError
     *
     * @param aMessage String
     */
    public static void onError(String aMessage) {
        MessageEvent event = new MessageEvent();
        event.message = aMessage;
        event.type = IMessage.TYPE_ERROR;
        forseMessage(event);
    }

    public static void onError(String aMessage, String aCategory) {
        MessageEvent event = new MessageEvent();
        event.message = aMessage;
        event.category = aCategory;
        event.type = IMessage.TYPE_ERROR;
        forseMessage(event);
    }

    /**
     * onError
     *
     * @param aException Exception
     * @param aMessage   String
     */
    public static void onError(Throwable aException, String aMessage) {
        MessageEvent event = new MessageEvent();
        event.message = aMessage;
        event.type = IMessage.TYPE_ERROR;
        if (SAVE_EXCEPTION)
            event.exception = aException;
        buildStackTrace(event, aException);
        forseMessage(event);
    }

    /**
     * onError
     *
     * @param aException Exception
     * @param aMessage   String
     */
    public static void onError(Throwable aException, String aMessage, String aCategory) {
        MessageEvent event = new MessageEvent();
        event.message = aMessage;
        event.category = aCategory;
        event.type = IMessage.TYPE_ERROR;
        if (SAVE_EXCEPTION)
            event.exception = aException;
        buildStackTrace(event, aException);
        forseMessage(event);
    }

    /**
     * onFatal
     *
     * @param aException
     * @param aMessage
     * @param aCategory
     */
    public static void onFatal(Throwable aException, String aMessage, String aCategory) {
        MessageEvent event = new MessageEvent();
        event.message = aMessage;
        event.category = aCategory;
        event.type = IMessage.TYPE_FATAL;
        if (SAVE_EXCEPTION)
            event.exception = aException;
        buildStackTrace(event, aException);
        forseMessage(event);
    }


    /**
     * forseLogEvent
     */
    public static void forseMessage(IMessage aMessage) {
        // check
        if (aMessage == null) {
            return;
        }
        // process
        for (int i = 0; i < fListeners.size(); i++) {
            IMessageListener item = (IMessageListener) fListeners.get(i);
            if (item != null) {
                item.processMessage(aMessage);
            }
        }
    }

	public static ArrayList getfListeners() {
		return fListeners;
	}

	/**
     * CleanUpTask class
     */
    protected static class CleanUpTask
            extends TimerTask {
        protected IMessageListener fListener;

        /**
         * Constructor
         */
        protected CleanUpTask(IMessageListener aValue) {
            fListener = aValue;
        }

        public void run() {
            try {
                fListener.cleanUp();
            } catch (Exception e) {
                XLog.onError(e, "Can't clearUp  messages");
            }
        }
    }

}
