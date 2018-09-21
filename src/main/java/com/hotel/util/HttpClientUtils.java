package com.hotel.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientUtils {

	public static String sendPost(String url, String param) throws Exception {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse resp = null;
		BufferedReader br = null;
		StringBuffer result = new StringBuffer();
		try {
			httpClient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			StringEntity entity = new StringEntity(param, "UTF-8");
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/x-www-form-urlencoded");
			httpPost.setEntity(entity);
			resp = httpClient.execute(httpPost);
			br = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
			String line;
			while ((line = br.readLine()) != null) {
				result.append(line);
			}
			return result.toString();
		} catch (Exception e) {
			throw new Exception("请求出错！message->{}" + e.getMessage());
		} finally {
			try {
				if (httpClient != null)
					httpClient.close();
			} catch (Exception e) {
			}
			try {
				if (resp != null)
					resp.close();
			} catch (Exception e) {
			}
			try {
				if (br != null)
					br.close();
			} catch (Exception e) {
			}
		}
	}

}