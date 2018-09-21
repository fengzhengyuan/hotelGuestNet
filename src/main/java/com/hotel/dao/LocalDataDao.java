package com.hotel.dao;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotel.bean.ConfigRowBean;
import com.hotel.bean.GuestsInfo;
import com.hotel.bean.Sessions;

public class LocalDataDao {
	private static LocalDataDao dao = null;
	private Logger log = LoggerFactory.getLogger(this.getClass().getName());
	private ListOrderedMap<String, SqlSessionFactory> sessionMap = new ListOrderedMap<String, SqlSessionFactory>();

	// 类工厂
	public static synchronized LocalDataDao getInstance() {
		if (dao == null) {
			dao = new LocalDataDao();
		}
		return dao;
	}
	// 构造
	public LocalDataDao() {
		Reader reader = null;
		try {
			reader = new StringReader(ConfigDao.getInstance().decodeConnectToString());
			for (ConfigRowBean row : Sessions.listRow) {
				if (row.getType().compareToIgnoreCase("password") == 0 && row.getEnable().equalsIgnoreCase("1")) {
					SqlSessionFactory session = null;
					
					session = new SqlSessionFactoryBuilder().build(reader, row.getId());
					sessionMap.put(row.getId(), session);
				}
			}
		} catch (Exception e) {
			log.warn("构造异常-{}", e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
				log.warn("释放异常-{}", e.getMessage());
			}
		}
	}

	
	
	/**
	 * 将旅馆入住信息传入数据库
	 * 
	 * @param pcAcciZp
	 */
	public void insertGuestsInfo(GuestsInfo guestsInfo) {
		try {
			SqlSession sessionTemp = null;
			try {
				SqlSessionFactory session = (SqlSessionFactory) sessionMap.get("wechat");
				sessionTemp = session.openSession();

				sessionTemp.insert("insertGuestsInfo", guestsInfo);
				sessionTemp.commit();
				log.info("信息插入成功");
			} catch (Exception e) {
				log.error("toSelect res-{}", e.getMessage());
			} finally {
				if (sessionTemp != null)
					sessionTemp.close();
			}
		} catch (Exception e) {
			log.error("toSelect res-{}", e.getMessage());
		}
	}
	
	
	

}
