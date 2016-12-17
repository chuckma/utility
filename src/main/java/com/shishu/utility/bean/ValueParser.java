package com.shishu.utility.bean;


import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


public class ValueParser {
    
    private String fields[];
    private String values[];

    public ValueParser(String fields[],String values[]) {
    	this.fields = fields;
    	this.values = values;

    }
    
    public Object getValue(Field field) {
    	for(int i=0; i< fields.length; i++) {
    		if(field.getName().equals(fields[i])) {
    			return getValue(field.getType(),values[i]);
    		}
    	}
    	return null;
    }

  
    private Object getValue(Class type, String value) {
    	
    	if(value == null || value.trim().length() < 1) {
    		return null;
    	}
    	
    	if (type.equals(CharSequence.class)) {
            return value;
        }

        /*
         * String and Character types
         */
        if (type.equals(String.class)) {
            return value;
        }
        if (type.equals(Character.TYPE) || type.equals(Character.class)) {
            return value;
        }

        /*
         * Boolean type
         */
        if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
        	if("true".equals(value)) {
        		return true;
        	} else {
        		return false;
        	}

        }

        /*
         * Numeric types
         */
        if (type.equals(Byte.TYPE) || type.equals(Byte.class)) {
            return (byte) (Integer.parseInt(value));
        }
        if (type.equals(Short.TYPE) || type.equals(Short.class)) {
            return (short) (Integer.parseInt(value));
        }
        if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return Integer.parseInt(value);
        }
        if (type.equals(Long.TYPE) || type.equals(Long.class)) {
            return Long.parseLong(value);
        }
        if (type.equals(Double.TYPE) || type.equals(Double.class)) {
            return Double.parseDouble(value);
        }
        if (type.equals(Float.TYPE) || type.equals(Float.class)) {
            return Float.parseFloat(value);
        }
        if (type.equals(BigInteger.class)) {
            return BigInteger.valueOf(Long.parseLong(value));
        }
        if (type.equals(BigDecimal.class)) {
            return new BigDecimal(Double.parseDouble(value));
        }
        if (type.equals(AtomicLong.class)) {
            return new AtomicLong(Long.parseLong(value));
        }
        if (type.equals(AtomicInteger.class)) {
            return new AtomicInteger(Integer.parseInt(value));
        }

        /*
         * Return null for any unsupported type
         */
        return null;

    }



}
