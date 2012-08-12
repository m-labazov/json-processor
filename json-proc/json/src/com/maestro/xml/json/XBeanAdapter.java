package com.maestro.xml.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.maestro.xml.IBeanAdapter;
import com.maestro.xml.JsonException;

/**
 * <p>Title: XBeanAdapter </p>
 * <p>Description: </p>
 * <p>Author: Andrii Gusiev </p>
 * Date: 8/23/11
 */
public class XBeanAdapter implements IBeanAdapter {


    // singleton
    protected static IBeanAdapter fInstance = new XBeanAdapter();

    /**
     * getInstance
     *
     * @return IBeanAdapter
     */
    public static IBeanAdapter getInstance() {
        return fInstance;
    }


    /**
     * Constructor
     */
    public XBeanAdapter() {
        // do nothing
    }


    /**
     * Set attribute via field
     *
     * @param field
     * @param aDest
     * @param opType
     * @param aValue
     * @return
     * @throws JsonException 
     */
    @Override
    public boolean setAttrValue(Field field, Object aDest, int opType, String aValue) throws JsonException {
        boolean result = false;
        // set accessible
        boolean oldAccessible = field.isAccessible();
        if (!oldAccessible) try {
            field.setAccessible(true);
        } catch (Exception e) {
            throw new JsonException("Can't set accessible for " + field.getName(), e);
        }
        // set value
        try {
            switch (opType) {
                case TYPE_BOOLEAN:
                case TYPE_BOOLEAN_OBJ:
                    field.setBoolean(aDest, Boolean.valueOf(aValue));
                    break;
                case TYPE_BYTE:
                case TYPE_BYTE_OBJ:
                    field.setByte(aDest, Byte.valueOf(aValue));
                    break;
                case TYPE_CHAR:
                case TYPE_CHAR_OBJ:
                    if (aValue.length() > 0)
                        field.setChar(aDest, aValue.charAt(0));
                    break;
                case TYPE_SHORT:
                case TYPE_SHORT_OBJ:
                    field.setShort(aDest, Short.valueOf(aValue));
                    break;
                case TYPE_DOUBLE:
                case TYPE_DOUBLE_OBJ:
                    field.setDouble(aDest, Double.valueOf(aValue));
                    break;
                case TYPE_FLOAT:
                case TYPE_FLOAT_OBJ:
                    field.setFloat(aDest, Float.valueOf(aValue));
                    break;
                case TYPE_INT:
                case TYPE_INT_OBJ:
                    field.setInt(aDest, Integer.valueOf(aValue));
                    break;
                case TYPE_LONG:
                case TYPE_LONG_OBJ:
                    field.setLong(aDest, Long.valueOf(aValue));
                    break;
                case TYPE_STRING:
                    field.set(aDest, aValue);
                    break;
            }
            result = true;
        } catch (Exception e) {
        	throw new JsonException("Can't set '" + field.getName() + "' attribute to value: " + aValue, e);
        }
        // restore accessible
        if ((!oldAccessible) && field.isAccessible()) {
            field.setAccessible(false);
        }
        // result
        return result;
    }

    /**
     * Set property via field
     *
     * @param field
     * @param aDest
     * @param aValue
     * @return
     * @throws JsonException 
     */
    @Override
    public boolean setPropValue(Field field, Object aDest, Object aValue) throws JsonException {
        boolean result = false;
        // set accessible
        boolean oldAccessible = field.isAccessible();
        if (!oldAccessible) try {
            field.setAccessible(true);
        } catch (Exception e) {
        	throw new JsonException("Can't set accessible for " + field.getName(), e);
        }
        // set value
        try {
            field.set(aDest, aValue);
            result = true;
        } catch (Exception e) {
        	throw new JsonException("Can't set '" + field.getName() + "' attribute to value: " + aValue, e);
        }
        // restore accessible
        if ((!oldAccessible) && field.isAccessible()) {
            field.setAccessible(false);
        }
        // result
        return result;
    }


    /**
     * setPropMValue
     *
     * @param method
     * @param aDest
     * @param aValue
     * @return
     * @throws JsonException 
     */
    @Override
    public boolean setPropMValue(Method method, Object aDest, Object aValue) throws JsonException {
        boolean result = false;
        // set accessible
        boolean oldAccessible = method.isAccessible();
        if (!oldAccessible) try {
            method.setAccessible(true);
        } catch (Exception e) {
        	throw new JsonException("Can't set accessible for " + method.getName(), e);
        }
        // set value
        try {
            method.invoke(aDest, aValue);
            result = true;
        } catch (Exception e) {
        	throw new JsonException("Can't invoke'" + method.getName() + "' method with param: " + aValue, e);
        }
        // restore accessible
        if ((!oldAccessible) && method.isAccessible()) {
            method.setAccessible(false);
        }
        // result
        return result;
    }


    /**
     * setAttrMValue
     *
     * @param method
     * @param aDest
     * @param opType
     * @param aValue
     * @return
     * @throws JsonException 
     */
    @Override
    public boolean setAttrMValue(Method method, Object aDest, int opType, String aValue) throws JsonException {
        boolean result = false;
        // set accessible
        boolean oldAccessible = method.isAccessible();
        if (!oldAccessible) try {
            method.setAccessible(true);
        } catch (Exception e) {
        	throw new JsonException("Can't set accessible for " + method.getName(), e);
        }
        // set value
        try {
            switch (opType) {
                case TYPE_BOOLEAN:
                    method.invoke(aDest, Boolean.parseBoolean(aValue));
                    break;
                case TYPE_BOOLEAN_OBJ:
                    method.invoke(aDest, Boolean.valueOf(aValue));
                    break;
                case TYPE_BYTE:
                    method.invoke(aDest, Byte.parseByte(aValue));
                    break;
                case TYPE_BYTE_OBJ:
                    method.invoke(aDest, Byte.valueOf(aValue));
                    break;
                case TYPE_CHAR:
                case TYPE_CHAR_OBJ:
                    if (aValue.length() > 0)
                        method.invoke(aDest, aValue.charAt(0));
                    break;
                case TYPE_SHORT:
                    method.invoke(aDest, Short.parseShort(aValue));
                    break;
                case TYPE_SHORT_OBJ:
                    method.invoke(aDest, Short.valueOf(aValue));
                    break;
                case TYPE_DOUBLE:
                    method.invoke(aDest, Double.parseDouble(aValue));
                    break;
                case TYPE_DOUBLE_OBJ:
                    method.invoke(aDest, Double.valueOf(aValue));
                    break;
                case TYPE_FLOAT:
                    method.invoke(aDest, Float.parseFloat(aValue));
                    break;
                case TYPE_FLOAT_OBJ:
                    method.invoke(aDest, Float.valueOf(aValue));
                    break;
                case TYPE_INT:
                    method.invoke(aDest, Integer.parseInt(aValue));
                    break;
                case TYPE_INT_OBJ:
                    method.invoke(aDest, Integer.valueOf(aValue));
                    break;
                case TYPE_LONG:
                    method.invoke(aDest, Long.parseLong(aValue));
                    break;
                case TYPE_LONG_OBJ:
                    method.invoke(aDest, Long.valueOf(aValue));
                    break;
                case TYPE_STRING:
                    method.invoke(aDest, aValue);
                    break;
            }
            result = true;
        } catch (Exception e) {
        	throw new JsonException("Can't invoke'" + method.getName() + "' method with param: " + aValue, e);
        }
        // restore accessible
        if ((!oldAccessible) && method.isAccessible()) {
            method.setAccessible(false);
        }
        // result
        return result;
    }


    /**
     * Get property via field
     *
     * @param field
     * @param aDest
     * @return
     * @throws JsonException 
     */
    @Override
    public Object getPropValue(Field field, Object aDest) throws JsonException {
        Object result = null;

        boolean oldAccessible = field.isAccessible();
        if (!oldAccessible) try {
            field.setAccessible(true);
        } catch (Exception e) {
        	throw new JsonException("Can't set accessible for " + field.getName(), e);
        }
        // set value
        try {
            result = field.get(aDest);
        } catch (Exception e) {
        	throw new JsonException("Can't get '" + field.getName() + "' attribute value.", e);
        }
        // restore accessible
        if ((!oldAccessible) && field.isAccessible()) {
            field.setAccessible(false);
        }

        return result;
    }

    /**
     * getTypeAsDTD
     *
     * @param type
     * @return
     */
    @Override
    public String getTypeAsDTD(int type) {
        String result = "CDATA";
        switch (type) {
            case TYPE_CHAR:
            case TYPE_CHAR_OBJ:
                result = "%Char;";
                break;
            case TYPE_BOOLEAN:
            case TYPE_BOOLEAN_OBJ:
                result = "%Boolean;";
                break;
            case TYPE_BYTE:
            case TYPE_BYTE_OBJ:
                result = "%Byte;";
                break;
            case TYPE_SHORT:
            case TYPE_SHORT_OBJ:
                result = "%Short;";
                break;
            case TYPE_INT:
            case TYPE_INT_OBJ:
                result = "%Integer;";
                break;
            case TYPE_LONG:
            case TYPE_LONG_OBJ:
                result = "%Long;";
                break;
            case TYPE_DOUBLE:
            case TYPE_DOUBLE_OBJ:
                result = "%Double;";
                break;
            case TYPE_FLOAT:
            case TYPE_FLOAT_OBJ:
                result = "%Float;";
                break;
        }
        return result;
    }

    /**
     * getTypeFor
     *
     * @param opType
     * @return
     */
    @Override
    public int getTypeFor(Class<?> opType) {
        int result = TYPE_UNKNOWN;
        String name = opType.getName();
        int index = name.lastIndexOf('.');
        if (index > 0) {
            if ((name.charAt(0) == 'j') && (index == 9)) {
                char ch = name.charAt(10);
                int ilen = name.length();
                char ch0;
                switch (ch) {
                    case 'I':
                        if (ilen == 17) result = TYPE_INT_OBJ;
                        break;
                    case 'B':
                        ch0 = name.charAt(11);
                        if ((ch0 == 'o') && (ilen == 17)) result = TYPE_BOOLEAN_OBJ;
                        else if ((ch0 == 'y') && (ilen == 14)) result = TYPE_BYTE_OBJ;
                        break;
                    case 'C':
                        if (ilen == 19) result = TYPE_CHAR_OBJ;
                        break;
                    case 'D':
                        if (ilen == 16) result = TYPE_DOUBLE_OBJ;
                        break;
                    case 'F':
                        if (ilen == 15) result = TYPE_FLOAT_OBJ;
                        break;
                    case 'S':
                        ch0 = name.charAt(11);
                        if ((ch0 == 'h') && (ilen == 15)) result = TYPE_SHORT_OBJ;
                        else if ((ch0 == 't') && (ilen == 16)) result = TYPE_STRING;
                        break;
                    case 'L':
                        ch0 = name.charAt(11);
                        if (ilen == 14) {
                            if (ch0 == 'o') result = TYPE_LONG_OBJ;
                            else if (ch0 == 'i') result = TYPE_LIST;
                        }
                        break;
                }
            }
        } else {
            // process build-in types
            char ch = name.charAt(0);
            switch (ch) {
                case 'i':
                    result = TYPE_INT;
                    break;
                case 'b':
                    char ch0 = name.charAt(1);
                    if (ch0 == 'o') result = TYPE_BOOLEAN;
                    else if (ch0 == 'y') result = TYPE_BYTE;
                    break;
                case 'c':
                    result = TYPE_CHAR;
                    break;
                case 'd':
                    result = TYPE_DOUBLE;
                    break;
                case 'f':
                    result = TYPE_FLOAT;
                    break;
                case 's':
                    result = TYPE_SHORT;
                    break;
                case 'l':
                    result = TYPE_LONG;
                    break;
            }
        }
        return result;
    }

}
