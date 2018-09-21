package com.hotel.listener;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotel.service.GuestsCheckinService;
import com.hotel.util.SignUtils;

public class ApplicationListener implements ServletContextListener {
	private Logger log = LoggerFactory.getLogger(this.getClass().getName());

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		log.info("加载资源文件～");
		Properties prop = new Properties();
		try {
			String path = ApplicationListener.class.getClassLoader().getResource("").toURI().getPath();
			FileInputStream fis = new FileInputStream(new File(path + "param.properties"));
			// 加载文件流的属性
			prop.load(fis);
			SignUtils.pubKey = prop.getProperty("public_key");
			SignUtils.priKey = prop.getProperty("private_key");
			GuestsCheckinService.photoSaveURL = prop.getProperty("photoSaveURL");

			log.info("加载完成～");
		} catch (Exception e) {
			log.error("加载资源失败！{}", e.getMessage());
		}
	}

}
