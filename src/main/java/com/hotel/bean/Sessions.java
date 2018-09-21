package com.hotel.bean;

import java.util.ArrayList;
import java.util.List;

public class Sessions {
	//使用缓冲
	public static boolean pool = false;
	//数据库配置文件
	public static String connectfile = "";
	//行对象
	public static List<ConfigRowBean> listRow = new ArrayList<ConfigRowBean>();
	
	
	//日志
	public static boolean toInfo = true;
	public static boolean toWarn = true;
	public static boolean toError = true;
	
}
