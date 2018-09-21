package com.hotel.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.hotel.bean.GuestsInfo;
import com.hotel.bean.RespBean;
import com.hotel.util.HttpClientUtils;
import com.hotel.util.StringUtil;

public class GuestsCheckinService {

	public static String photoSaveURL = "";

	private Logger log = LoggerFactory.getLogger(this.getClass().getName());

	/**
	 * 将接收到的信息读取成
	 * 
	 * @param respBean
	 * @return
	 * @throws Exception
	 */
	public GuestsInfo analysisHotelData(RespBean respBean) throws Exception {

		respBean.setReqdata(new String(Base64.decodeBase64(respBean.getReqdata().getBytes(respBean.getCharset())),
				respBean.getCharset()));
		try {
			JSONObject obj = JSONObject.parseObject(respBean.getReqdata());

			GuestsInfo guestsInfo = JSONObject.toJavaObject(obj, GuestsInfo.class);

			// 身份证照片
			if (!StringUtil.judgeIsEmpty(guestsInfo.getPaperPhoto())) {
				String paperPhoto = saveImage(guestsInfo.getPaperPhoto(), guestsInfo.getPaperId(), "hotel");
				JSONObject jsonPaper = JSONObject.parseObject(paperPhoto);
				if ("200".equals(jsonPaper.getString("code"))) {
					guestsInfo.setPaperPhoto(jsonPaper.getString("msg"));
				}

			}

			// 现场照片
			if (!StringUtil.judgeIsEmpty(guestsInfo.getScenePhoto())) {
				String scenePhoto = saveImage(guestsInfo.getScenePhoto(), guestsInfo.getPaperId(), "hotel");
				JSONObject jsonScene = JSONObject.parseObject(scenePhoto);
				if ("200".equals(jsonScene.getString("code"))) {
					guestsInfo.setScenePhoto(jsonScene.getString("msg"));
				}
			}
			return guestsInfo;
		} catch (Exception e) {
			throw new Exception("解析respData失败！");
		}
	}

	/**
	 * 认证成功后将人像图片保存
	 * 
	 * @param zpxq
	 * @param sfzmhm
	 * @param typeId
	 *            存储图片类型 identity、hotel
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String saveImage(String zpxq, String sfzmhm, String typeId) throws UnsupportedEncodingException {
		String msg = "";
		String param = "zpxq=" + URLEncoder.encode(zpxq, "UTF-8") + "&sfzmhm=" + sfzmhm + "&typeId=" + typeId;
		try {
			msg = HttpClientUtils.sendPost(photoSaveURL, param);
		} catch (Exception e) {
			log.error("保存人像图片出错", e.getMessage());
		}
		return msg;
	}

}
