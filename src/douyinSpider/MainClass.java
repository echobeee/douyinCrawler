package douyinSpider;

import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.LongSummaryStatistics;

import javax.swing.JFrame;

import org.apache.commons.lang.time.DateFormatUtils;
import org.jfree.chart.ChartUtilities;

import net.sf.json.JSONObject;


public class MainClass {

	public MainClass() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws Exception {
//		DataAnalyzer dataAnalyzer = new DataAnalyzer();
//		dataAnalyzer.ExcutingAnalyzing("CalculatingData");
		
		
		
		for(int i = 1; i <= 1; i++ ) {
			JFrame frame=new JFrame("Java数据统计图");
			frame.setLayout(new GridLayout(1,2,10,5));
//			frame.add(new TimeSeriesChart(3 * i).getChartPanel());    //添加折线图
			frame.add(new BarChart().getChartPanel());
			frame.setBounds(50, 50, 800 , 600);
			frame.setVisible(true);
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		}
		
//		dataAnalyzer.analyzeRanking();
		
	}
	
}
