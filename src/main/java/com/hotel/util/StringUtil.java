package com.hotel.util;

import java.lang.reflect.Field;


public class StringUtil {

	public static boolean judgeIsEmpty(String str) {
		boolean flag=true;
		if (str != null && str.length() != 0) {
			flag = false;
		} 
		return flag;
	}

	/**
	 * 判断JAVABean的值是否为空
	 * 
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 */

	public static boolean checkObjFieldIsNull(Object obj) throws IllegalAccessException {

		boolean flag = false;
		for (Field f : obj.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			if (f.get(obj) == null) {
				flag = true;
				return flag;
			}
		}
		return flag;
	}

	

   

}
