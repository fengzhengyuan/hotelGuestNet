package com.hotel.util;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hotel.bean.RespBean;

public class SignUtils {
	private static Logger log = LoggerFactory.getLogger(SignUtils.class.getName());

	public static String priKey = "";
	public static String pubKey = "";

	/**
	 * 验证签名
	 * 
	 * @param respBean
	 * @throws Exception
	 */
	public static boolean verifySign(RespBean respBean) {
		boolean flag = true;
		try {
			if (!verifySignByPublicKey((respBean.getSiteCode() + respBean.getVersion() + respBean.getIdentityCode()
					+ respBean.getReqdata()).getBytes("UTF-8"), pubKey, respBean.getSignature())) {
				flag = false;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * 数字签名验证
	 * 
	 * @param data
	 * @param publicKey
	 * @param sign
	 * @return
	 */
	private static boolean verifySignByPublicKey(final byte[] data, final String publicKey, final String sign) {
		boolean flag = true;
		byte[] encodedKey = Base64.decodeBase64(publicKey.getBytes());
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
		KeyFactory keyFactory;

		try {
			keyFactory = KeyFactory.getInstance("RSA");
			PublicKey key = keyFactory.generatePublic(keySpec);
			Signature signature = Signature.getInstance("MD5withRSA");
			signature.initVerify(key);
			signature.update(data);
			flag = signature.verify(Base64.decodeBase64(Base64.decodeBase64(sign.getBytes())));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

}
