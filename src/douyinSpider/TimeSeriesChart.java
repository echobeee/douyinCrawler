package douyinSpider;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.Minute;
import org.jfree.data.time.Month;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

public class TimeSeriesChart {
	ChartPanel frame1;
	List<String> star_list;
	String nickname = "陈赫";
	public TimeSeriesChart(int num){
//		star_list = new DataAnalyzer().analyzeRanking();
//		star_list = new ArrayList<>();
//		star_list.add("罗志祥");
//		star_list.add("Dear-迪丽热巴");
//		star_list.add("陈赫");
		XYDataset xydataset = createNewDataset();
//		XYDataset xydataset = createDataset(num);
//		nickname = Integer.toString(num/3);
//		nickname = "陈-罗-迪";
//		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("抖音明星爱DOU榜明星排名变化", "明星爱DOU榜期数", "排名",xydataset, true, true, true);
		JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("点赞数变化", "时间", "点赞数",xydataset, true, true, true);
		jfreechart.setBackgroundPaint(ChartColor.WHITE);
		XYPlot xyplot = (XYPlot) jfreechart.getPlot();
		xyplot.setBackgroundPaint(ChartColor.WHITE);
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)xyplot.getRenderer(); 
//		renderer.setBaseShapesVisible(true);
		renderer.setDrawOutlines(true); 
		renderer.setUseFillPaint(true); 
		renderer.setFillPaint(java.awt.Color.white); 
		renderer.setSeriesStroke(0, new BasicStroke(3F));
		renderer.setSeriesStroke(1, new BasicStroke(3F));
		renderer.setSeriesStroke(2, new BasicStroke(3F));
		renderer.setSeriesPaint(2, Color.YELLOW);
		DateAxis dateaxis = (DateAxis) xyplot.getDomainAxis();
        dateaxis.setDateFormatOverride(new SimpleDateFormat("M-dd HH:mm"));
        frame1=new ChartPanel(jfreechart,true);
        dateaxis.setLabelFont(new Font("黑体",Font.BOLD,14));         //水平底部标题
        dateaxis.setTickLabelFont(new Font("宋体",Font.BOLD,12));  //垂直标题
        ValueAxis rangeAxis=xyplot.getRangeAxis();//获取柱状
        rangeAxis.setLabelFont(new Font("黑体",Font.BOLD,15));
        jfreechart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
        jfreechart.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体
        try {
			ChartUtilities.saveChartAsJPEG(new File("E:/douyin/新建文件夹/" + nickname + "1.jpg"), jfreechart, 1200, 1000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	} 
	 @SuppressWarnings("deprecation")
	private XYDataset createDataset(int num){  //这个数据集有点多，但都不难理解
	        
	        TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
	        List<String> file;
	        
	        int cnt = 0;
	        
			try {
				
				int len;
				
				for(String star : star_list) {
					
					if(cnt < num-3) { // starts from where
						cnt++;
						continue;
					}
					if(cnt >= num) // ends till where
						break;
					cnt++; // 5 stars
					len = star.length() > 3 ? 3 : star.length();
					TimeSeries timeseries = new TimeSeries(star,
			                org.jfree.data.time.Day.class);
					
					file = Files.readAllLines(Paths.get("E:/douyin/" + star.substring(0, len) +  "_info.xml"));
					 String line = file.get(file.size()-1);
				        String[] words = line.split("-");
				        for(int i = 0; i < 20;i++) {
				        	if(words[i].equals("NULL")) {
				        		timeseries.add(new Day(i+1, 1,2019), 0);
				        	} else {
				        		timeseries.add(new Day(i+1, 1,2019), Integer.parseInt(words[i]));
				        	}
				        	
				        }
				        
				        timeseriescollection.addSeries(timeseries);
				}
	       
	        
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return timeseriescollection;
	    }
	 
	 private XYDataset createNewDataset() {
		 TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		 TimeSeries timeSeries = new TimeSeries(nickname,org.jfree.data.time.Second.class);
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String temp;
		 String[] words;
		 try {
			List<String> lines = Files.readAllLines(Paths.get("E:/douyin/新建文件夹/" + nickname + ".xml"));
			lines.remove(0);
			temp = lines.get(0).split("#")[1];
//			temp = "6671142341950131459";
			for(String line : lines) {
				words = line.split("#");
				if(!temp.equals(words[1])) {
					continue;
//					break;
				}
			
				timeSeries.addOrUpdate(new Second(sdf.parse(words[7])), Integer.parseInt(words[4]));
				
			}
			
			timeseriescollection.addSeries(timeSeries);
		 
		 
		 } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return timeseriescollection;
	 }
	 
	 
	  public ChartPanel getChartPanel(){
	    	return frame1;
	    	
	    }
}

