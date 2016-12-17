package com.shishu.utility.bean;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PopulateBean {
	
	public static final Logger log = LoggerFactory.getLogger(PopulateBean.class);
	
	public <T> T populateBean(final Class<T> type, String fields[],String values[]) {
		
		ValueParser parser = new ValueParser(fields,values);
		try {
			T result = type.newInstance();
			List<Field> declaredFields = getDeclaredFields(result);

			for (Field field : declaredFields) {
				
				try {
					populateSimpleType(result, field,parser.getValue(field));
				} catch(Exception e) {
					log.error("populateSimpleType error " + field.getName());
					//log.error(e.getMessage(),e);
				}
			}
			
			return result;
			
		} catch (InstantiationException e) {
			log.error("populateBean InstantiationException:" + e.getMessage());
		} catch (IllegalAccessException e) {
			log.error("populateBean IllegalAccessException:" + e.getMessage());
		}
		return null;
		
	}
	
	private static void populateSimpleType(Object result, final Field field,Object object) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        String fieldName = field.getName();
        
        if("SCHEMA$".equals(fieldName) || "_ALL_FIELDS".equals(fieldName) || "TOMBSTONE".equals(fieldName) || object == null) {
        	
        } else {
        	PropertyUtils.setProperty(result, fieldName, object);
        }
        
        
	}
	
	private <T> ArrayList<Field> getDeclaredFields(T result) {
        return new ArrayList<Field>(Arrays.asList(result.getClass().getDeclaredFields()));
    }
}
