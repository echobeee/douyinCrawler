package douyinSpider;
 import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
 
public class BarChart {
    ChartPanel frame1;
    public  BarChart(){
        CategoryDataset dataset = getDataSet();
        JFreeChart chart = ChartFactory.createBarChart3D(
                             "明星影响力分值", // 图表标题
                            "明星", // 目录轴的显示标签
                            "分值", // 数值轴的显示标签
                            dataset, // 数据集
                            PlotOrientation.VERTICAL, // 图表方向：水平、垂直
                            true,           // 是否显示图例(对于简单的柱状图必须是false)
                            false,          // 是否生成工具
                            false           // 是否生成URL链接
                            );
         
        //从这里开始
        chart.setBackgroundPaint(ChartColor.WHITE);
        CategoryPlot plot=chart.getCategoryPlot();//获取图表区域对象
        plot.setBackgroundPaint(ChartColor.WHITE);
        CategoryAxis domainAxis=plot.getDomainAxis();         //水平底部列表
         domainAxis.setLabelFont(new Font("黑体",Font.BOLD,12));         //水平底部标题
         domainAxis.setTickLabelFont(new Font("宋体",Font.BOLD,12));  //垂直标题
         domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
         ValueAxis rangeAxis=plot.getRangeAxis();//获取柱状
         rangeAxis.setLabelFont(new Font("黑体",Font.BOLD,15));
          chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
          chart.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体
          BarRenderer renderer = new BarRenderer();
          renderer.setBarPainter(new StandardBarPainter());//取消渐变效果
//          renderer.setIncludeBaseInRange(true); // 显示每个柱的数值，并修改该数值的字体属性
//          renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
          renderer.setDrawBarOutline(true); // 设置柱子边框可见
          renderer.setItemMargin(0.05); // 组内柱子间隔为组宽的10%
          renderer.setMaximumBarWidth(0.05);// 设置条形柱最大宽度
          renderer.setBaseItemLabelsVisible(true, true);
          renderer.setShadowVisible(false);//不显示阴影
          plot.setRenderer(renderer);
          try {
			ChartUtilities.saveChartAsJPEG(new File("E:/douyin/hotsearch_191.jpg"), chart, 800, 600);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
          //到这里结束，虽然代码有点多，但只为一个目的，解决汉字乱码问题
           
         frame1=new ChartPanel(chart,true);        //这里也可以用chartFrame,可以直接生成一个独立的Frame
          
    }
       private static CategoryDataset getDataSet() {
           DefaultCategoryDataset dataset = new DefaultCategoryDataset();
           try {
			List<String> lines = Files.readAllLines(Paths.get("E:/douyin/hotsearch_19_stat.xml"));
			
			lines.remove(0);
			String[] words;
			int count = 0;
			int len ;
			List<String> newLines;
			for(String line : lines) {
				
				words = line.split("#");
				
				dataset.addValue(Integer.parseInt(words[2]), "影响力",words[0]);
				dataset.addValue(Integer.parseInt(words[3]), "热度分",words[0]);
				dataset.addValue(Integer.parseInt(words[4]), "互动分",words[0]);
			}
			
			
           } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           
           return dataset;
}
public ChartPanel getChartPanel(){
    return frame1;
     
}
}