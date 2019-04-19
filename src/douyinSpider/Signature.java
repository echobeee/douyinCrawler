package douyinSpider;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.emchat.example.httpclient.utils.SslUtils;
import com.google.gson.Gson;

import net.sf.json.JSONObject;



public class Signature {
	// phone's header
	public static final String header = "Aweme 5.2.0 rv:52007 (iPhone; iOS 12.1.2; zh_CN) Cronet";
	
	public static final String signHeader = "okhttp/3.10.0.1";
	
	// pc's header
	public static final String pcHeader = "Mozilla/5.0 "
		    + "(iPad; U; CPU OS 4_3_3 like Mac OS X; en-us) "
		    + "AppleWebKit/533.17.9"
		    + " (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5";
	
	// 获取加密数据的api
	private final String API = "https://api.appsign.vip:2688";
	
	// 爬虫抖音链接1
	public static final String douyin = "https://aweme.snssdk.com/aweme/";
	
	public static final String DOUYIN_USER = "https://api.amemv.com/aweme/";
	
	// 爬虫抖音链接2
	private final String douyin2 = "https://aweme-eagle.snssdk.com/aweme/v1/feed/?";
	
	// 明星爱DOU榜
	public static final String starHotsearch = "https://aweme.snssdk.com/aweme/v1/hotsearch/star/billboard/?";
		
	// new api
	private final String douyinApi = "https://jokeai.zongcaihao.com/douyin/v292/";
	
	private final String finalAPI = "https://sign.vsdouyin.com/api/";
	
	private static final String TOKEN = "999898b4171d8e641ac00d82f429d4ff";
			
	private static final String getDevice = "device";
	
	private static final String getSign = "653d33c/sign/";
	
	// with douyin
	public static final String getPosts = "v1/aweme/post/?";
	
	public static final String getUserInfo = "v1/user/?";
	
	public static final String  params = "&ac=wifi&channel=wandoujia_zhiwei&aid=1128&app_name=aweme&version_code=290&version_name=2.9.0&device_platform=android&ssmix=a&device_type=ONEPLUS+A5000&device_brand=OnePlus&language=zh&os_api=28&os_version=9&manifest_version_code=290&resolution=1080*1920&dpi=420&update_version_code=2902&_rticket=1548672388498";
	
	//	temporary file to write response entity
	private String tempFile = "/home/bee/xx.xml";  
	
	URL url;
	
	DeviceInfo deviceInfo;
	
	
	public Signature() {
		super();
		
		deviceInfo =(DeviceInfo) Utils.map2Object((Map<String, Object>) getDevice(), DeviceInfo.class) ;
		
		String os = System.getProperty("os.name");
		
		if(os.toLowerCase().contains("win")) {	
			// windows
			 tempFile = "E:/xx.xml";
			
		} else { 
			// linux
			tempFile = "/home/bee/xx.xml";  
		}
	}

	/*
	 * dest：get请求的连接，返回JSON数据
	 *       主要给getdevice和gettoken用
	 */
	public String goConnect(String action, Long install_id, int times) {
		String result = "";
		BufferedInputStream reader = null;
		try {
			System.out.println("Method: get, connecting :" + action);
			url = new URL(action);
			
			// configure proxy, so fiddler can listen 
			// when go to linux, just comment this
//			Proxy proxy = new Proxy(java.net.Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1", 8888));
//			SslUtils.ignoreSsl();
//			
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setConnectTimeout(5000);
			
			
			connection.addRequestProperty("User-Agent", signHeader);
			connection.addRequestProperty("Cookie", "install_id="+install_id);
			connection.addRequestProperty("Content-Type","application/json");
//			connection.addRequestProperty( "Accept", "*/*");
//			connection.addRequestProperty("Accept-Charset", "utf-8");
//			connection.addRequestProperty("Accept-Encoding", "gzip");
			connection.connect();
			if(connection.getResponseCode() == 200) {
				reader =  new BufferedInputStream(connection.getInputStream());
				byte[] b = new byte[20 * 1024];
				int len;
				FileOutputStream fos = new FileOutputStream(new File(tempFile));
				
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				
				StringBuffer sb = new StringBuffer();
				
				while((len = reader.read(b)) != -1) {
					sb.append(b);
					bos.write(b, 0, len);
				}
				bos.close();
				result = sb.toString();
					
			} else {
				System.err.println("出现什么问题了... 服务器返回了 " + connection.getResponseCode());
			}
			
			
		} catch (SocketTimeoutException e) {
			System.err.println("Uh Oh... 连接超时了!");
			if(times < 2) // max 2 times
				return goConnect(action, install_id, times+1); // reconnect
			return null;
		} catch (Exception e) {
			System.err.println("服务器应该出错啦....请看详情↓");
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(reader != null)
					reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
//	获取token
//	有效期60min
	@Deprecated
	public String getToken() {	
		return goConnect("/token/douyin", 1L, 0).substring(28,60);
	}
	
	/**
	 * dest：返回设备信息
	 * @return Map
	 */
	@SuppressWarnings("unchecked")
	public Object getDevice() {
		Map<String, Object> map2 = new HashMap<String, Object>();
		
//		try {
//		
//			String string = goConnect(getDevice);
//	
//			if(string != null) {
//				
//				JSONObject arrays = JSONObject.fromObject(string);
//				map2 = (Map<String, Object>) JSONObject.toBean(arrays, Map.class);
//	
//			}
//		} catch (Exception e) {
			
			// 如果服务器崩了，就使用默认的设备信息
			map2.put("device_id",66860619916L); //  53899138286
			map2.put("install_id", 66670668200L);// 64267470949
			map2.put("android_id", "a80f12c4736e95bd");
			map2.put("iid", 66670668200L);
			map2.put("openudid", "9852540981346027"); // c84838f8ce347572b5fb48f0eb8bb827846af6fc
			map2.put("uuid", "446500178521389");
//		}
		
		return map2;
	}
	
	/**
	 * dest: 返回加密参数
	 * @param token
	 * @param params
	 * @return 返回一个Map
	 */
	@Deprecated
	public Object getParams(String token, String params) {
		Map<String, String> post = new HashMap<>();
		post.put("token", token);
		post.put("query", params);		
		Map<String, Object> map = new Gson().fromJson( HttpClientUtils.doJsonPost(API + "/sign", post, "utf-8"), Map.class);
		return map.get("data");
	}
	
	/**
	 * 用新的api，传递一个除了as,mas,ts参数的url,返回一个加密后的url
	 * @param url
	 * @return
	 */
	public String getEncryptedUrl(Map<String, String> params) {
		
		String encryptedJson =  HttpClientUtils.doJsonPost(finalAPI + getSign + TOKEN, params, "utf-8");
		
		JSONObject jsonObject = JSONObject.fromObject(encryptedJson);
		String realencyptedUrl;
		try {
			realencyptedUrl = jsonObject.getString("url");
		} catch (Exception e) {
			realencyptedUrl = null;
		}
		return realencyptedUrl;
	}
	
	
	/**
	 * 获取用户的视频（always top）
	 * @param uid
	 * @return
	 */
	public String getDecryptedAndConnectDouyin(String action, String args) {

		// fill parameters for url to api for encrypeted url
		Map<String, String> preparedParams = fillParams(douyin + action, args);

		String realencyptedUrl = getEncryptedUrl(preparedParams);
		
		if(realencyptedUrl == null) {
			return null;
		}
		
		// add parameter
		goConnect(realencyptedUrl, deviceInfo.getInstall_id(), 0);
		
		File file = new File(tempFile);
		String json = null;
		
		try {
			json = FileUtils.readFileToString(file, "utf-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	private Map<String, String> fillParams(String url, String args) {
		
		Map<String, String> mapParams = new HashMap<>();
		
		//  not begins with '&'
		String stringParams = Utils.object2Url(deviceInfo);
		
		mapParams.put("url", url + args
						  		+ "&retry_type=no_retry&"
						  		+ stringParams
						  		+ Signature.params);
		
		return mapParams;
	}
}
