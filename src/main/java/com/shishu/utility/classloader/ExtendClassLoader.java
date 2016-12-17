package com.shishu.utility.classloader;

import java.security.ProtectionDomain;

/**
 * 
 * 类加载工具类
 * @author: qiaoel@zjhcsosft.com
 * @date: 2014年2月25日 上午9:28:57
 */
public class ExtendClassLoader extends ClassLoader {
	
	private static ProtectionDomain protectionDomain = null;
	
	
	/**
	 * 获取域的特征
	 * @see java.security.ProtectionDomain.ProtectionDomain
	 */
	public static ProtectionDomain getProtectionDomain()
	{
		if (protectionDomain == null)
		{
			protectionDomain = ExtendClassLoader.class.getProtectionDomain();
		}
		
		return protectionDomain;
	}
	
	/**
	 * Sets the protection to be used for classes loaded via
	 * the {loadClassFromBytes} method.
	 *
	 * By default, the protection domain of this class is used for the loaded classes. 
	 * 
	 * @param protectionDomain the protection domain to be used
	 * @see #loadClassFromBytes(String, byte[])
	 */
	public static void setProtectionDomain(ProtectionDomain protectionDomain)
	{
		ExtendClassLoader.protectionDomain = protectionDomain;
	}
	
	protected ExtendClassLoader()
	{
		super();
	}

	/**
	 *
	 */
	protected ExtendClassLoader(ClassLoader parent)
	{
		super(parent);
	}


	/**
	 * 加载class
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 * @return: Class
	 */
	public static Class loadClassForName(String className) throws ClassNotFoundException
	{
		Class clazz = null;

		String classRealName = className;
		ClassNotFoundException initialEx = null;

		try
		{
			clazz = loadClassForRealName(classRealName);
		}
		catch (ClassNotFoundException e)
		{
			initialEx = e;
		}
		
		int lastDotIndex = 0;
		while (clazz == null && (lastDotIndex = classRealName.lastIndexOf('.')) > 0)
		{
			classRealName = 
				classRealName.substring(0, lastDotIndex) + "$" + classRealName.substring(lastDotIndex + 1);
			try
			{
				clazz = loadClassForRealName(classRealName);
			}
			catch (ClassNotFoundException e)
			{
			}
		}
		
		if (clazz == null)
		{
			throw initialEx;
		}
		
		return clazz;
	}


	/**
	 * 类加载
	 * @param className
	 * @return
	 * @throws ClassNotFoundException
	 * @return: Class
	 */
	public static Class loadClassForRealName(String className) throws ClassNotFoundException
	{
		Class clazz = null;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null)
		{
			try
			{
				clazz = Class.forName(className, true, classLoader);
			}
			catch (ClassNotFoundException e)
			{
				//if (log.isWarnEnabled())
				//	log.warn("Failure using Thread.currentThread().getContextClassLoader() in JRClassLoader class. Using JRClassLoader.class.getClassLoader() instead.");
			}
		}

		if (clazz == null)
		{
			classLoader = ExtendClassLoader.class.getClassLoader();
			if (classLoader == null)
			{
				clazz = Class.forName(className);
			}
			else
			{
				clazz = Class.forName(className, true, classLoader);
			}
		}

		return clazz;
	}

	/**
	 *
	 */
	public static Class loadClassFromBytes(String className, byte[] bytecodes)
	{
		Class clazz = null;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader != null)
		{
			try
			{
				clazz = 
					(new ExtendClassLoader(classLoader))
						.loadClass(className, bytecodes);
			}
			catch(NoClassDefFoundError e)
			{
				//if (log.isWarnEnabled())
				//	log.warn("Failure using Thread.currentThread().getContextClassLoader() in JRClassLoader class. Using JRClassLoader.class.getClassLoader() instead.");
			}
		}
	
		if (clazz == null)
		{
			classLoader = ExtendClassLoader.class.getClassLoader();
			if (classLoader == null)
			{
				clazz = 
					(new ExtendClassLoader())
						.loadClass(className, bytecodes);
			}
			else
			{
				clazz = 
					(new ExtendClassLoader(classLoader))
						.loadClass(className, bytecodes);
			}
		}

		return clazz;
	}


	/**
	 *
	 */
	protected Class loadClass(String className, byte[] bytecodes)
	{
		Class clazz = null;

		clazz = 
			defineClass(
				className, 
				bytecodes, 
				0, 
				bytecodes.length,
				getProtectionDomain()
				);

		return clazz;
	}


	/**
	 *
	 */
	public static String getClassRealName(String className)
	{
		if (className == null)
		{
			return null;
		}
		
		int arrayDimension = 0;
		int classNameEnd = className.length();
		int index = 0;
		int pos = 0;
		while (index < classNameEnd && (pos = className.indexOf('[', index)) >= 0)
		{
			if (index == 0)
			{
				classNameEnd = pos;
			}
			index = pos;
			arrayDimension++;
		}

		if (arrayDimension > 0)
		{
			StringBuffer sbuffer = new StringBuffer();
			
			for(int i = 0; i < arrayDimension; i++)
			{
				sbuffer.append('[');
			}
			
			sbuffer.append('L');
			sbuffer.append(className.substring(0, classNameEnd));
			sbuffer.append(';');

			return sbuffer.toString();
		}
		
		return className;
	}
}
