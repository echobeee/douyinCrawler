package douyinSpider;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;

public class HttpClientUtils {
	/*
	 * dest:请求参数为表单参数的post请求
	 * params: 表单参数
	 * post请求
	 */
	public static String doPost(String url, Map<String, String> params, String charset) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		CloseableHttpResponse response = null;
		String responseString = null;
		HttpPost post = new HttpPost(url);
		
		// 更改cookie策略
		RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
		post.setConfig(config);
		
		post.addHeader("user-agent", "Mozilla/5.0 "
			    + "(iPad; U; CPU OS 4_3_3 like Mac OS X; en-us) "
			    + "AppleWebKit/533.17.9"
			    + " (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5");
		try {
			if(params != null && params.size() != 0) {
				List<NameValuePair> list = new ArrayList<>();
				for(String key : params.keySet()) {
					list.add(new BasicNameValuePair(key, params.get(key)));
				}
				
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list);
					post.setEntity(entity);
			}
			response = httpClient.execute(post);
			if(response != null) {
				HttpEntity entity = response.getEntity();
				if(entity != null) {
					responseString = EntityUtils.toString(entity, charset);
				}
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return responseString;
	}
	
	/**
	 * dest： 请求参数为JSON数据的post请求
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 */
	public static String doJsonPost(String url, Map<String, String> params, String charset) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		CloseableHttpResponse response = null;
		String responseString = null;
		HttpPost post = new HttpPost(url);
		
		// 更改cookie策略
		RequestConfig config = RequestConfig.custom().setConnectTimeout(5000)
				.setConnectionRequestTimeout(1000)
				.setSocketTimeout(5000)
				.setCookieSpec(CookieSpecs.STANDARD).build();
	
		
		
		post.setConfig(config);
		
		post.addHeader("user-agent", "Mozilla/5.0 "
			    + "(iPad; U; CPU OS 4_3_3 like Mac OS X; en-us) "
			    + "AppleWebKit/533.17.9"
			    + " (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5");
		post.addHeader("Content-Type","application/json");
		try {
			if(params != null && params.size() != 0) {
				JsonObject json = new JsonObject();
				
				for(String key : params.keySet()) {
					json.addProperty(key, params.get(key));
				}
					
					StringEntity entity = new StringEntity(json.toString(), charset);
					post.setEntity(entity);
			}
			System.out.println("Method: jsonPost, connceting: " + url);
			
			response = httpClient.execute(post);
			if(response != null) {
				System.out.println("Method: jsonPost, connceting, return " + response.getStatusLine());
				HttpEntity entity = response.getEntity();
				if(entity != null) {
					responseString = EntityUtils.toString(entity, charset);
				}
			}
			
			
		} catch (Exception e) {
			System.err.println("Uh Oh... 连接超时了!");
			e.printStackTrace();
			return doJsonPost(url, params, charset);// reconnect
		}
		return responseString;
	}
}
