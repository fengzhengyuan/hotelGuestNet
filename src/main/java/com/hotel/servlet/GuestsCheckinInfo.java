package com.hotel.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hotel.bean.GuestsInfo;
import com.hotel.bean.RespBean;
import com.hotel.dao.LocalDataDao;
import com.hotel.service.GuestsCheckinService;
import com.hotel.util.SignUtils;
import com.hotel.util.StringUtil;

public class GuestsCheckinInfo extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7116849706691752565L;

	/**
	 * 旅客入住信息添加
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/x-www-form-urlencoded;charset=utf-8");
		resp.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter pw = resp.getWriter();
		try {
			// 获得数据
			RespBean respBean = RespBean.class.newInstance();
			BeanUtils.populate(respBean, req.getParameterMap());
			
			// 判断必要字段是否为空
			if (!StringUtil.checkObjFieldIsNull(respBean)) {
				// 进行数字验签
				if (SignUtils.verifySign(respBean)) {
					GuestsCheckinService guestsCheckinService = new GuestsCheckinService();
					//解析并把部分字段的图片存入文件服务器
					GuestsInfo guestsInfo = guestsCheckinService.analysisHotelData(respBean);
					// 将入住信息记录在表里
					LocalDataDao.getInstance().insertGuestsInfo(guestsInfo);
					pw.write(
							"{\"respCode\":\"200\",\"respMsg\":\"操作成功\",\"respData\":{\"code\":\"0\",\"err\":\"写入成功\"}");
					
				}
			}
		} catch (Exception e) {
			pw.write("{\"respCode\":\"500\",\"respMsg\":\"" + e.getMessage() + "\"}");
		}finally{
			pw.close();
		}
	}
}
