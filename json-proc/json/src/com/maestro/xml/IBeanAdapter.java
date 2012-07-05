package com.maestro.xml;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>Title: IBeanAdapter </p>
 * <p>Description: </p>
 * <p>Author: Andrii Gusiev </p>
 * Date: 8/23/11
 */
public interface IBeanAdapter {

    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_BOOLEAN = 1;
    public static final int TYPE_BOOLEAN_OBJ = 2;
    public static final int TYPE_BYTE = 3;
    public static final int TYPE_BYTE_OBJ = 4;
    public static final int TYPE_CHAR = 5;
    public static final int TYPE_CHAR_OBJ = 6;
    public static final int TYPE_DOUBLE = 7;
    public static final int TYPE_DOUBLE_OBJ = 8;
    public static final int TYPE_FLOAT = 9;
    public static final int TYPE_FLOAT_OBJ = 10;
    public static final int TYPE_INT = 11;
    public static final int TYPE_INT_OBJ = 12;
    public static final int TYPE_LONG = 13;
    public static final int TYPE_LONG_OBJ = 14;
    public static final int TYPE_SHORT = 15;
    public static final int TYPE_SHORT_OBJ = 16;
    public static final int TYPE_STRING = 17;
    public static final int TYPE_LIST = 18;

    public boolean setAttrFValue(Field field, Object aDest, int opType, String aValue);

    public boolean setAttrMValue(Method method, Object aDest, int opType, String aValue);

    public boolean setPropFValue(Field field, Object aDest, Object aValue);

    public  boolean setPropMValue(Method method, Object aDest, Object aValue);

    public Object getPropValue(Field field, Object aDest);


    public String getTypeAsDTD(int type);

    public int getTypeFor(Class opType);


}
