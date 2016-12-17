
package com.shishu.utility.jdbc;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

public class JdbcUtil {
    private static String getSqlTypeName(int sqlType) {
        try {
            Integer val = new Integer(sqlType);
            for (Field field : Types.class.getFields()) {
                if (val.equals(field.get(null))) {
                    return field.getName();
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not get sqlTypeName ", e);
        }
        throw new RuntimeException("Unknown sqlType " + sqlType);
    }

    public static Class getJavaType(int sqlType) {
        switch (sqlType) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                return String.class;
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                return byte[].class;
            case Types.BIT:
                return Boolean.class;
            case Types.TINYINT:
            case Types.SMALLINT:
                return Short.class;
            case Types.INTEGER:
                return Integer.class;
            case Types.BIGINT:
                return Long.class;
            case Types.REAL:
                return Float.class;
            case Types.DOUBLE:
            case Types.FLOAT:
                return Double.class;
            case Types.DATE:
                return Date.class;
            case Types.TIME:
                return Time.class;
            case Types.TIMESTAMP:
                return Timestamp.class;
            case Types.DECIMAL:
                return BigDecimal.class;
            default:
                throw new RuntimeException("We do not support tables with SqlType: " + getSqlTypeName(sqlType));
        }
    }
}
