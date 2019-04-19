package douyinSpider;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.stream.Stream;

public class DataAnalyzer {

	private String douyin_directory = "E:/douyin/";
	
	public DataAnalyzer() {
		// TODO Auto-generated constructor stub
	}
	
	public void ExcutingAnalyzing(String method) {
		try {
			
			// NIO
			List<String> lines = Files.readAllLines(Paths.get(douyin_directory + "hotsearch_20_stat.xml"), Charset.forName("utf-8"));
			String[] words = null;
			lines.remove(0);// remove title
				for(String line : lines) {
					words = line.split("#");
					System.out.println("Analyzing " + words[0] + " " + words[1] + "......");
					switch (method) {
					case "CalculatingData":
						calculateUserAwemeDatum(words[0]);
						break;
					
					default:
						System.out.println("please input right method!!");
						System.exit(0);
					}
					
					System.out.println("polling to " + words[0] + " " + words[1] + ", done!\n");
				}
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void calculateUserAwemeDatum(String nickname) {
		int len = nickname.length();
		len = len > 3 ? 3 : len;
		
		// 总数，平均值，中位数，最大值，最小值，
		try {
			
			System.out.println("Analyzing " + nickname + "'s file...");
			
			List<String> lines = Files.readAllLines(Paths.get(douyin_directory + nickname.substring(0, len) + "_info.xml"), Charset.forName("utf-8"));
			List<String> replaced = new ArrayList<>();
			replaced.addAll(lines);
			lines.remove(0);lines.remove(0);lines.remove(0);
			
			String[] words;
			List<Long> like_count = new ArrayList<>(), comment_count = new ArrayList<>(), 
					forward_count = new ArrayList<>(), share_count = new ArrayList<>();
			for(String line : lines) {
				
				if(line.length() == 0)
					continue;
				
				words = line.split("#");
				like_count.add(Long.parseLong(words[1]));
				comment_count.add(Long.parseLong(words[2]));
				forward_count.add(Long.parseLong(words[3]));
				share_count.add(Long.parseLong(words[4]));
			}
			
			System.out.println("Analyzing finished! Now start updating file");
			
			LongSummaryStatistics like_statisctis = like_count.stream().mapToLong(x->x).summaryStatistics();
			LongSummaryStatistics comment_statisctis = comment_count.stream().mapToLong(x->x).summaryStatistics();
			LongSummaryStatistics forward_statisctis = forward_count.stream().mapToLong(x->x).summaryStatistics();
			LongSummaryStatistics share_statisctis = share_count.stream().mapToLong(x->x).summaryStatistics();
		
			DecimalFormat df = new DecimalFormat("#.00");
			
			replaced.add("---------------------------------------------");
			replaced.add(like_statisctis.getSum() + "#" + comment_statisctis.getSum() + "#"
						+ forward_statisctis.getSum() + "#" + share_statisctis.getSum() + "#SUM");
			replaced.add(df.format(like_statisctis.getAverage()) + "#" + df.format(comment_statisctis.getAverage()) + "#"
						+ df.format(forward_statisctis.getAverage()) + "#" + df.format(share_statisctis.getAverage()) + "#AVERGAE");
			replaced.add(like_statisctis.getMax() + "#" + comment_statisctis.getMax() + "#"
					+ forward_statisctis.getMax() + "#" + share_statisctis.getMax() + "#MAX");
			replaced.add(like_statisctis.getMin() + "#" + comment_statisctis.getMin() + "#"
					+ forward_statisctis.getMin() + "#" + share_statisctis.getMin() + "#MIN");
			
			replaced.add(getMid(like_count) +"#" + getMid(comment_count) +"#" 
						+ getMid(forward_count) +"#"  + getMid(share_count) +"#MID" );
			
			Files.write(Paths.get(douyin_directory + nickname.substring(0, len) + "_info.xml"), replaced);
			
			System.out.println("Analyze " + nickname + "'s file, done!");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
	
	/*金瀚kim count:17
	黄明昊Justin count:15
	关晓彤 count:14
	陈赫 count:20
	Dear-迪丽热巴 count:20
	Angelababy count:17
	蔡徐坤 count:18
	王嘉尔 count:13
	周冬雨 count:10
	摩登兄弟 count:11
	吴亦凡 count:11
	朱正廷 count:18
	小沈阳 count:11
	GEM鄧紫棋 count:16
	杨洋 count:11
	汪东城 count:10
	杨紫 count:15
	杨幂 count:13
	唐嫣 count:12
	何炅 count:13	
	邓伦 count:16
	张韶涵 count:10
	罗志祥 count:20
	范丞丞 count:18
	王鹤棣_Dylan count:14*/
	public List<String> analyzeRanking() {
		File directory = new File("E:/douyin");
		File[] files = directory.listFiles(new MyFilter());
		List<String> lines;
		String[] words;
		Map<String, List<String>> map = new HashMap<>(); // key:nickname,value:pages
		
		String page;
		String filename;
		for(File file : files) {
			try {
				filename = file.getAbsolutePath();
				lines = Files.readAllLines(Paths.get(filename));
				page = filename.substring(filename.indexOf("_")+1, filename.lastIndexOf("_"));
				lines.remove(0);
				for(String line : lines) {
					words = line.split("#");
					List<String> list; 
					if(map.get(words[0]) == null) {
						list = new ArrayList<>();
						list.add(page);
						map.put(words[0], list);
					} else {
						list = map.get(words[0]);
						list.add(page);
						map.put(words[0], list);
					}
				}
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		
		
		return handleRanking(map);
	}
	
	private List<String> handleRanking(Map<String, List<String>> map) {
		List<String> pages;
		List<String> stars = new ArrayList<>();
		for(String key : map.keySet()) {
			pages = map.get(key);
			
			if(pages.size() >= 10) {
//				System.out.print(key + " ");
				stars.add(key);
//				handleRanking(pages, key);
			}
		}
		return stars;
		
	}

	private void handleRanking(List<String> pages, String nickname) {
		List<String> lines;
		pages.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				if(Integer.parseInt(o1) < Integer.parseInt(o2)) {
					return -1;
				} else if(Integer.parseInt(o1) == Integer.parseInt(o2)) {
					return 0;
				} else {
					return 1;
				}
			}
			
		});
		int[] ranks = new int[20];
		int cnt = 0;
		try {
		for(String page : pages) {
			
				lines = Files.readAllLines(Paths.get(douyin_directory + "hotsearch_" + page + "_stat.xml"));
				for(int i = 1; i < lines.size(); i++) {
					if(lines.get(i).contains(nickname)) {
						ranks[Integer.parseInt(page)-1] = i;// 第几周排名是i
					}
				}
			}
		
		int len = nickname.length();
		len = len > 3 ? 3 : len;
		BufferedWriter bw = new BufferedWriter(new FileWriter(douyin_directory + nickname.substring(0, len) + "_info.xml", true));
		for(int rank : ranks) {
			if(rank == 0) {
				bw.write("NULL-");
			} else {
				bw.write(rank + "-");
			}
		}
		bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		
	}

	private long getMid(List<Long> counts) {
		counts.sort(null);
		long mid;
		int len = counts.size();
		if(len == 0)
			return 0;
		if(counts.size()%2 == 0) {
			mid = (counts.get(len/2) + counts.get(len/2 - 1))/2;
		} else {
			mid = counts.get(len/2);
		}
		return mid;
	}
	
	 class MyFilter implements FilenameFilter{
		public MyFilter(){
			
		}
		public boolean accept(File dir,String name){
			return name.contains("_stat");
		}
	}


}
