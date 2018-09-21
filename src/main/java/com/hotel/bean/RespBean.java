package com.hotel.bean;

public class RespBean {
	
	//身份认证时请求的编码
	private String identityCode;
	
	//终端设备类型，1-PC个人电脑 2-手机终端 3-微信 4-支付宝
	private String deviceType;
	
	//reqdata编码字符集，数字签名和base64编码中使用的字符集，仅支持 utf-8/gbk
	private String charset;
	
	//身份认证时分配给对方的接入编码
	private String siteCode;
	
	//响应数据体，json格式，采用base64编码
	private String reqdata;
	
	//数字签名，采用Base64编码和MD5withRSA算法实现，签名内容顺序为：siteCode、version、identityCode和reqdata
	private String signature;
	
	//身份认证时平台接口版本号
	private String version;
	
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getSiteCode() {
		return siteCode;
	}
	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}
	
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public RespBean() {
		super();
	}
	public String getIdentityCode() {
		return identityCode;
	}
	public void setIdentityCode(String identityCode) {
		this.identityCode = identityCode;
	}
	public String getReqdata() {
		return reqdata;
	}
	public void setReqdata(String reqdata) {
		this.reqdata = reqdata;
	}
	

	
}
