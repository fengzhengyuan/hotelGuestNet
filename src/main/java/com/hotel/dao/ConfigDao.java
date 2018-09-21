package com.hotel.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotel.bean.ConfigRowBean;
import com.hotel.bean.Sessions;

public class ConfigDao {
	// 使用缓冲
	public static boolean pool = false;
	// 数据库配置文件
	public static String connectfile = "";
	// 行对象
	public static List<ConfigRowBean> listRow = new ArrayList<ConfigRowBean>();

	// 日志
	public static boolean toInfo = true;
	public static boolean toWarn = true;
	public static boolean toError = true;

	private static ConfigDao dao = null;
	private Logger log = LoggerFactory.getLogger(this.getClass().getName());

	private File fileConfig = null;

	public static synchronized ConfigDao getInstance() {
		if (dao == null) {
			dao = new ConfigDao();
		}
		return dao;
	}

	/**
	 * 查找文件
	 * 
	 * @param filename
	 *            文件名称
	 * @return
	 */
	public File searchFile(String filename) {
		File res = null;
		try {
			// 扩展名
			String extName = FilenameUtils.getExtension(filename);
			// 递归查找
			Collection<File> list = null;
			list = FileUtils.listFiles(new File(
					new File(Thread.currentThread().getContextClassLoader().getResource("").getPath()).getParent()),
					new String[] { extName.toLowerCase(), extName.toUpperCase() }, true);
			// 确定文件
			for (File file : list) {
				if (file.getName().equalsIgnoreCase(filename)) {
					res = file;
					break;
				}
			}
		} catch (Exception e) {
		}
		return res;
	}

	public String decodeConnectToString() {
		String res = "";
		try {
			if (Sessions.connectfile == null || Sessions.connectfile.length() < 1) {
				readConfigToSession();
			}
			File file = searchFile(Sessions.connectfile);
			if (file == null)
				return "";
			if (!file.isFile())
				return "";
			if (file.length() < 2)
				return "";

			res = FileUtils.readFileToString(file, "UTF-8");
			for (ConfigRowBean row : Sessions.listRow) {
				if (row.getType().compareToIgnoreCase("password") == 0) {
					String passwd = new String(Base64.decodeBase64(row.getText().getBytes()));
					res = StringUtils.replace(res, ":" + row.getId(), passwd);
				}
			}
		} catch (Exception e) {
		}
		return res;
	}

	/**
	 * 参数读入内存
	 * 
	 * @param textXml
	 */
	@SuppressWarnings("unchecked")
	public void readConfigToSession() {
		fileConfig = searchFile("Config.xml");

		if (fileConfig == null)
			return;
		if (!fileConfig.isFile())
			return;
		if (fileConfig.length() < 2)
			return;

		try {
			Document doc = DocumentHelper.parseText(FileUtils.readFileToString(fileConfig, "UTF-8"));
			Sessions.toInfo = doc.getRootElement().element("head").elementTextTrim("log_info_open")
					.equalsIgnoreCase("1");
			Sessions.toWarn = doc.getRootElement().element("head").elementTextTrim("log_warn_open")
					.equalsIgnoreCase("1");
			Sessions.toError = doc.getRootElement().element("head").elementTextTrim("log_error_open")
					.equalsIgnoreCase("1");
			Sessions.connectfile = doc.getRootElement().element("head").elementTextTrim("connectfile");

			Sessions.listRow.clear();
			for (Iterator<Element> elements = doc.getRootElement().element("body").element("rows")
					.elementIterator(); elements.hasNext();) {
				Element element = elements.next();
				ConfigRowBean row = new ConfigRowBean();
				row.setId(element.attributeValue("id"));
				row.setEnable(element.attributeValue("enable"));
				row.setType(element.attributeValue("type"));
				row.setText(element.attributeValue("text"));
				row.setValue(element.getTextTrim());
				Sessions.listRow.add(row);
			}
		} catch (Exception e) {
			log.error("解析Config.xml异常-{}", e.getMessage());
		}
	}

	public static void main(String[] args) {
		System.out.println(new String(Base64.encodeBase64("15501302313wechat".getBytes())));
	}
}