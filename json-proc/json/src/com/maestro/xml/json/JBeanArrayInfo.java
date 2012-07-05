package com.maestro.xml.json;


public class JBeanArrayInfo {

    private boolean isStringArray = false;
    private boolean name;
    private JBeanInfo beanInfo = new JBeanInfo();

    public JBeanArrayInfo() {
    }

    public JBeanArrayInfo(JBeanInfo beanInfo) {
        this.beanInfo = beanInfo;
    }

    public boolean isStringArray() {
        return isStringArray;
    }

    public void setStringArray(boolean isStringArray) {
        this.isStringArray = isStringArray;
    }

    public boolean isName() {
        return name;
    }

    public void setName(boolean name) {
        this.name = name;
    }

    public JBeanInfo getBeanInfo() {
        return beanInfo;
    }

    public void setBeanInfo(JBeanInfo beanInfo) {
        this.beanInfo = beanInfo;
    }

}
