package douyinSpider;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import com.sun.org.apache.bcel.internal.generic.NEW;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;


public class douyinCrawl {
	
	private static Signature signature = new Signature();
	
	// save star's latest aweme
	private String star_latest_aweme_file;
	
	private String douyin_directory;
	
	
	public douyinCrawl() {
		super();
		String os = System.getProperty("os.name");
		// windows
		if(os.toLowerCase().contains("win")) {	
			star_latest_aweme_file = "E:/douyin/latest.xml";
			douyin_directory = "E:/douyin/";	
		} else { 
			// linux
			star_latest_aweme_file = "/home/bee/douyin/latest.xml";
			douyin_directory = "/home/bee/douyin/";
		}
	}

	private static void getResponse(String url) throws Exception {
		
		 CloseableHttpClient httpClient = org.apache.http.impl.client.HttpClients.createDefault();
	        RequestConfig requestConfig = RequestConfig.custom()
	                .setSocketTimeout(15000)
	                .setConnectTimeout(15000)
	                .setRedirectsEnabled(false)
	                .build();
	        HttpGet get = new HttpGet(url);
	        get.setConfig(requestConfig);
//	        get.setHeader("Accept","*/*");
	        get.setHeader("User-Agent","Aweme 3.4.0 rv:34008 (iPhone; iOS 12.0; zh_CN) Cronet");
	        CloseableHttpResponse response = httpClient.execute(get);
	        System.out.println(response.getStatusLine().getStatusCode());
	        HttpEntity entity = response.getEntity();
	        System.out.println(entity.getContentType());
	        System.out.println();
	}
	
	private static String getVideoUrl(String fakeUrl) {
		
		try {
			// 第一次链接，获取真实的播放视频的网址
			URL url = new URL(fakeUrl);
			URLConnection connection;
			connection = url.openConnection();
			connection.addRequestProperty("User-Agent", Signature.header);
			connection.connect();
			String loc = connection.getHeaderField("Location");
			return loc;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	/*
	 * continued: 进行多线程爬虫，爬取cursor，offset还未确定？
	 * dest:对抖音的feed流进行爬取，存到文件中
	 * 由于github上apisign的签名服务已经停止,此方法失效
	 */
	@Deprecated
	private static String getConnectingUrl() {
		// 参数类
		Params params = new Params();
		
		// 获取token - 60min有效期
		String token = signature.getToken();
		
		// 获取设备信息
		Object device = signature.getDevice();
		
		// 参数填补
		params = (Params) Utils.map2Object((Map<String, Object>) device, Params.class);
		params.intial();
		
		// 请求加密数据的参数
		String json = Utils.object2Url(params);
		Map<String, String> preparedParams = new HashMap<>();
		preparedParams.put("url", Signature.douyin + json);
		System.out.println(signature.getEncryptedUrl(preparedParams));
		// 返回的加密参数 ts,mas,as
		Map secret = (Map) signature.getParams(token, json);
		for(Object key : secret.keySet()) {
			if(key.equals("mas")) {
				// mas参数
				params.setMas((String) secret.get(key));
				// as参数
			} else if (key.equals("as")) {
				params.setAs((String) secret.get(key));
				// ts参数 
			} else if (key.equals("ts")) {
				params.setTs((String) secret.get(key));
				
			} 
		}
		
//		// 抖音url - 将所有参数转换为url
		String getParams = Utils.object2Url(params);
		//signature.connect(getParams,savepath);
		return getParams;
	}
	
	
	/**
	 * 将存在文件中的json数据extract得到需要的特定数据，如评论数，点赞数，转发数
	 * @param filename
	 */
	private int extractStats(JSONObject content, String nickname) {
//		File file = new File(filename);
		
		BufferedWriter bw = null;
		
		try {

			// JSON中的aweme_list转换为JSON对象 --- 就是【视频列表】
			JSONArray arrays = JSONArray.fromObject(content.get("aweme_list"));
			
			int len = nickname.length();
			len = len > 3 ? 3 : len;
			
			File file = new File(douyin_directory + nickname.substring(0, len) + "_info.xml");
			
			int cnt = 1;
			JSONObject oo = null;
			JSONObject stats = null;
			
			System.out.println("*******************************************************");
			
			// append
			bw = new BufferedWriter(new FileWriter(file,true));
			
			
			for(Object object : arrays) {
				
				System.out.println("Writing post " + cnt++ + " of " + nickname + " into " + file.getName());
				
				// 要收集的数据 都是从oo中获取
				oo = (JSONObject) object;
			
				// 抖音数据
				stats = oo.getJSONObject("statistics");
//				String s  = "抖音id#点赞数#评论数#转发数#share数";
				VideoInfo vInfo = (VideoInfo) JSONObject.toBean(stats, VideoInfo.class);
				bw.write(vInfo.getAweme_id() + "#" + vInfo.getDigg_count() 
											+ "#" + vInfo.getComment_count()
											+ "#" + vInfo.getForward_count()
											+ "#" + vInfo.getShare_count() + "\n");
				
				System.out.println("-----------------------------------------------------------------------------");
			}
			
			return cnt-1;
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(bw != null)
					bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return 0;
		
	}

	/**
	 * 解析明星榜的json文件,获取一些key写入到文件
	 * @param filepath
	 */
	public static void extractHotsearchFile(String filepath) {
		File file = new File(filepath);
		
		// 转JSON时候的配置，防止循环
		JsonConfig config = new JsonConfig();
		config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
		
		// rename it 
		File savefile = new File(filepath.replace(".json", "_stat.xml"));
		

		BufferedOutputStream bos = null;
		
		try {
			
			System.out.println(filepath + " starts!");
		
			bos = new BufferedOutputStream(new FileOutputStream(savefile));
			
			String json = FileUtils.readFileToString(file, "utf-8");
			
			// 每一期的排行榜
			JSONObject billboard =  JSONObject.fromObject(json, config);
			
			// 上榜明星单
			JSONArray user_list = billboard.getJSONArray("user_list");
			
			JSONObject user;
			
			StringBuffer sb = new StringBuffer("nickname-uid-hot_value-factor_hot_value-factor_interact_value-rank_diff" + "\n");
			
			for(Object objectUser : user_list) {
				user = (JSONObject) objectUser;
				
				// 热度分
				long factor_hot_value = user.getLong("factor_hot_value");
				// 互动分
				long factor_interact_value = user.getLong("factor_interact_value");
				// 热度
				long hot_value = user.getLong("hot_value");
				// 排名变化
				int rank_diff = user.getInt("rank_diff");
				// 用户id
				String uid = user.getJSONObject("user_info").getString("uid");
				// 用户昵称
				String nickname = user.getJSONObject("user_info").getString("nickname");
				
				sb.append(nickname + "#" + uid + "#" + hot_value + "#" + factor_hot_value + "#" + factor_interact_value + "#" + rank_diff + "\n");
				bos.write(sb.toString().getBytes());
				sb.delete(0, sb.length());
			}
			
			System.out.println(filepath + " done!");
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将uid用户的video的数据更新
	 * @param uid
	 * @param statistics
	 */
	private void freshVideoStats(String uid, Map<String, Object> statistics) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		Object comment_count = statistics.get("comment_count");
		Object share_count = statistics.get("share_count");
		Object digg_count = statistics.get("digg_count");
		Object forward_count = statistics.get("forward_count");
		Object play_count = statistics.get("play_count");
		Object aweme_id = statistics.get("aweme_id");
		String nickname =  (String) statistics.get("nickname");
		
		
		// use nickname's first 3 or lesser character to be file's name
	    int len = nickname.length();
	    len = len > 3 ? 3 : len;
		File file = new File(douyin_directory + nickname.substring(0, len) + ".xml");
		
		BufferedOutputStream bos = null;
		
		try {
			
			if(!file.exists()) {
					file.createNewFile();	
					
					bos = new BufferedOutputStream(new FileOutputStream(file, true));
					
					System.out.println("File does not exist, creating file..");
					
					String sd = "uid#aweme_id#comment_count#share_count#digg_count#forward_count" 
							+ "#play_count#date" + "\n";
					
					bos.write(sd.getBytes());
				} else {
					bos = new BufferedOutputStream(new FileOutputStream(file, true));
				}
			
			System.out.println("Writing to " + file.getName() + "....");
			
			
			StringBuffer sb = new StringBuffer(uid);
			sb.append("#" + aweme_id + "#" + comment_count + "#" + share_count + "#" + digg_count + "#" + forward_count 
					+ "#" + play_count + "#" + sdf.format(new Date()) + "\n");
			bos.write(sb.toString().getBytes());
			
			System.out.println("Fresh datum of " + file.getName() + "  " + uid + ", done!");
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(bos != null)
					bos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
	
	 /**
	  * poll every 30 mins to check star's lateset douyin video's statistics
	  * method: polling
	  * 		getUserInfo
	  *         getAllPosts
	  *         
	  */
	@SuppressWarnings("deprecation")
	public void pollingCheckUserDouyin(String method) {
		
		try {
		
			// NIO
			List<String> lines = Files.readAllLines(Paths.get(douyin_directory + "hotsearch_20_stat.xml"), Charset.forName("utf-8"));
			String[] words = null;
			lines.remove(0);// remove title
			int count = 1;
			while(true) {
				for(String line : lines) {
					words = line.split("#");
					System.out.println("polling to " + words[0] + " " + words[1] + "......");
					switch (method) {
					case "polling":
						compareAndUpdateDatum(words[1], words[0]);
						break;
					case "getAllPosts":
						// 3-23 16:00
						getUserAllPosts(words[1], words[0]);
						break;
					case "getUserInfo" :
						getUsersInfo(words[1]);
						break;
					default:
						System.out.println("please input right method!!");
						System.exit(0);
					}
					
					System.out.println("polling to " + words[0] + " " + words[1] + ", done!\n");
				}
				
				if(!method.equals("polling")) {
					return ;
				}
				
				System.out.println("第" + count++ + "次polling, in " + new Date().toLocaleString());
				Thread.sleep(30 * 60 * 1000);// poll every 30min
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void getUserAllPosts(String user_id, String nickname) {
		String userPosts;
		
		int has_more = 1;
		
		String max_cursor = "0";
		
		int aweme_count = 0;
		
		int current = 0;
		
		while(has_more == 1) {
			
			userPosts = signature.getDecryptedAndConnectDouyin(Signature.getPosts, "user_id=" + user_id
					+ "&max_cursor=" + max_cursor + "&count=20");
			
			if(userPosts == null || userPosts.length() == 0 || !userPosts.startsWith("{")) {
				System.out.println("something went wrong, Json from server is null, maybe server returns 403...");
				return ;
			}
		
			// 转JSON时候的配置，防止循环
			JsonConfig config = new JsonConfig();
			config.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
			
			JSONObject json = JSONObject.fromObject(userPosts, config);
			
			current = extractStats(json, nickname);
			
			System.out.println("Write " + current + " awemes this round..");
			
			aweme_count += current;
			
			has_more = json.getInt("has_more");
			
			max_cursor = json.getString("max_cursor");
			
			System.out.println("has more? " + has_more + (has_more == 1 ? " yes" : " no"));
				
		}
		
		System.out.println("Get user " + nickname + " posts, all sum up to " + aweme_count);
		
		
	}

	private void compareAndUpdateDatum(String uid,  String nickname) {
		
		/**
		 * 只要一个最新视频就ok了
		 * 获取最新的aweme_id,然后与文件的aweme_id,判断是不是旧的,
		 * 若不是旧的,则set boolean标志位为1,开始fresh
		 * 若是,则skip
		 */
		
		String localid = readFromFileId(uid);

		Map<String, Object> stats_and_postid = getNewestAwemeId(uid); 
		
		if(stats_and_postid == null) {
			return ;
		}
		
		String postid = (String) stats_and_postid.get("aweme_id");
		stats_and_postid.put("nickname", nickname);
		
		System.out.println("postid: " + postid + " fileid: " + localid);
		
		// status = 1, already latest post
		if(localid != null && localid.length() == 0) {
			System.out.println(nickname + " is already newest!");
			freshVideoStats(uid, stats_and_postid);
			return ;
		}

		// server failed, or return 403
		if (postid == null) {
			System.out.println("Postid null...means server may fail, exit!");
			return ;
		}
		
		// never be recorded, so go initials
		if(localid == null) {
			
			updateFileStatus(uid, postid ,false);// false for insert
			return ;
		}
		
		
		// not latest
		if(!postid.equalsIgnoreCase(localid))  {
			// true for update
			updateFileStatus(uid, postid, true);
			freshVideoStats(uid, stats_and_postid);
		}
		
		
	}

	/**
	 *
	 * @param uid
	 * @param insert_or_update
	 *  true for update, false for insert
	 */
	private void updateFileStatus(String uid, String aweme_id ,boolean insert_or_update) {
		File file = new File(star_latest_aweme_file);
		
		BufferedWriter bw = null;
		
		try {
			if(!insert_or_update) { // insert
				
				System.out.println("Inserting new record to file");
				
				bw = new BufferedWriter(new FileWriter(file, true));
				bw.write(uid + "-" + aweme_id + "-0\n"); // not newest
			
			} else {
				// NIO to fulfill read&modify

				System.out.println("Modifying record to file, uid =" + uid);
				
				List<String> lines = Files.readAllLines(Paths.get(star_latest_aweme_file));
				List<String> replaced = new ArrayList<>();
				String[] words = null;
				
				for(String line : lines) {
					words = line.split("-");
					if(words[0].equals(uid)) {
						replaced.add(uid+"-"+aweme_id+"-1"); // newest
					} else {
						replaced.add(line);
					}
				}
				
				Files.write(Paths.get(star_latest_aweme_file), replaced);
				System.out.println("Update...Done!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if(bw != null)
					bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		
	}

	/**
	 * according to uid, read its boolean flag to ensure if it is newest
	 * if not, just get the aweme_id
	 * @param uid
	 * @return
	 */
	private String readFromFileId(String uid) {
		System.out.println("Reading latest aweme id from local file...");
		
		File file = new File(star_latest_aweme_file);
		BufferedReader br = null;
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			br = new BufferedReader(new FileReader(file));
			String line;
			String[] splits;
 			while((line = br.readLine()) != null) {
				splits = line.split("-");
				if(splits[0].equals(uid)) { // matched uid
					if(splits[2].equals("1")) { // already newest
						System.out.println("fileId already newest!");
						return "";
					}
					return splits[1];
				}
			}
 			
 			// not found
 			return null;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null)
					br.close();
			} catch (IOException e) {
				// donothing
			}
			
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> getNewestAwemeId(String uid) {
		// video list
		
		String prev20 = signature.getDecryptedAndConnectDouyin(Signature.getPosts, "user_id=" + uid + "&max_cursor=0&count=20");
		
		if(prev20 == null || prev20.length() == 0 || !prev20.startsWith("{")) {
			System.out.println("something went wrong, Json from server is null, maybe server returns 403...");
			return null;
		}
		
		JSONObject data_aweme_list = JSONObject.fromObject(prev20);
		JSONArray aweme_list = data_aweme_list.getJSONArray("aweme_list");
		JSONObject first_aweme = aweme_list.getJSONObject(0);
		
		// get stats
		JSONObject statistics = first_aweme.getJSONObject("statistics");
		
		Map<String, Object> map = (Map<String, Object>) JSONObject.toBean(statistics, Map.class);
		
		return map;
	}
	
	
	public void getUsersInfo(String user_id) {
		
				String userinfoString = signature.getDecryptedAndConnectDouyin(Signature.getUserInfo, "user_id=" + user_id);
				
				if(userinfoString == null || userinfoString.length() == 0 || !userinfoString.startsWith("{")) {
					System.out.println("something went wrong, Json from server is null, maybe server returns 403...");
					return ;
				}
				
				handleUserInfoJson(userinfoString);

	}

		// total_favorited 获赞
		// following_count 关注
		// follower_count 粉丝
		// aweme_count 作品 uid nickname
	private void handleUserInfoJson(String userinfoString) {
		JSONObject userinfo = JSONObject.fromObject(userinfoString);
		JSONObject user = userinfo.getJSONObject("user");
		
		// 获赞数
		String total_favorited = user.getString("total_favorited");
		
		// 关注数
		String following_count = user.getString("following_count");
		
		// 粉丝数
		String follower_count = user.getString("follower_count");
		
		// 作品数
		String aweme_count = user.getString("aweme_count");
		
		String uid = user.getString("uid");
		
		String nickname = user.getString("nickname");
		
		int len = nickname.length();
		
		len = len > 3 ? 3 : len;
		
		File file = new File(douyin_directory + nickname.substring(0, len) + "_info.xml");
		BufferedWriter bw = null;
		System.out.println("Writing to " + file.getName() + "....");
		try {
			 bw = new BufferedWriter(new FileWriter(file));
			 bw.write("nickname#uid#获赞数#关注数#粉丝数#作品数\n");
			 bw.write(nickname + "#" + uid + "#" + total_favorited + "#" + following_count 
					 + "#" + follower_count + "#" + aweme_count + "\n");
			 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(bw != null) {
				try {
					bw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
	}
	

}
