package mygui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYTextAnnotation;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.CombinedRangeXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.VectorRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYStepAreaRenderer;
import org.jfree.data.xy.VectorSeries;
import org.jfree.data.xy.VectorSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;


public class CombinedXYPlotDemo3 extends ApplicationFrame {
	public ArrayList<Number> releaseTimeList,deadLineList;
	public ArrayList<Object> finalTaskList;
	public String taskName;
	public String xAxisLabel,yAxisLabel;
	public VectorSeries vectorSeries1,vectorSeries2;
	public VectorSeriesCollection vectorDataSet1,vectorDataSet2;
	public XYPlot subplot;
	public JFreeChart chart;
	public  XYDataset data;
	public VectorRenderer vr1,vr2;
	public CombinedDomainXYPlot plot;
	//public CombinedRangeXYPlot plot;
	 public XYStepAreaRenderer renderer;
	 public NumberAxis domain;
	
    public CombinedXYPlotDemo3(final String title) {
    	super(title);
    	}  
    public JFreeChart createCombinedChart(ArrayList<Task> finalTaskSet) {        
    	int noOfTasks=finalTaskSet.size();
    	//NumberAxis axis=new NumberAxis("Time");
    	plot = new CombinedDomainXYPlot();
    	
    	
    	plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        
    	//plot = new CombinedRangeXYPlot(new NumberAxis("Time"));
    	plot.setGap(10.0);
    	plot.setOrientation(PlotOrientation.VERTICAL);
    	
    
    	//System.out.println("Entered in to createCombinedChart: noOfTasks="+noOfTasks);
    	XYStepAreaRenderer renderer=new XYStepAreaRenderer();
    	domain = (NumberAxis) plot.getDomainAxis();
    	
    	for(int i=0;i<noOfTasks;i++){
    		Task task=(Task)finalTaskSet.get(i);
    		releaseTimeList=task.releaseTimeList;
    		deadLineList=task.deadLineList;
    		finalTaskList=task.finalTaskList;
    		taskName=task.taskName;
    		data=createDataset(finalTaskList,taskName);
    		 xAxisLabel= "Tick Number";
    		 yAxisLabel=taskName;
    		 chart = ChartFactory.createXYStepChart(
    	                "title",
    	                xAxisLabel, yAxisLabel,
    	                data,
    	                PlotOrientation.VERTICAL,
    	                false,   // legend
    	                false,   // tooltips
    	                false   // urls
    	            );
    		 
    		 subplot = chart.getXYPlot();
    		 subplot.setRenderer(renderer);
    		   		 
   	        
    	     int hyperPeriod=Integer.parseInt(releaseTimeList.get(releaseTimeList.size()-1).toString());
       		 //System.out.println("lets see the tick size="+hyperPeriod);
   			 domain.setRange(0, hyperPeriod);
   			
    		 renderer.setSeriesFillPaint(0, new GradientPaint(0f, 0f, Color.green, 0f, 0f, Color.blue));
    	        renderer.setOutline(true);
    	        renderer.setSeriesOutlinePaint(0, Color.black);
    		 
    		 //release cycles
    		 vectorDataSet1= new VectorSeriesCollection();
    		 vectorSeries1=new VectorSeries(taskName+"-Release Time");
    		 
			 
    		 int temp=0;
    		 
    		 for(int j=0;j<releaseTimeList.size();j++){    			  			 
    			 temp=Integer.parseInt(releaseTimeList.get(j).toString());
    			 //System.out.println("temp"+temp);
    			 vectorSeries1.add(temp, 0, 0, 7);
    		 }
    		 vectorDataSet1.addSeries(vectorSeries1);
    		 vr1=new VectorRenderer();
    	      subplot.setDataset(1, vectorDataSet1);
    	      subplot.setRenderer(1, vr1);
    	      vr1.setSeriesPaint(0, Color.blue);
    	      //release cycles done
    	      //deadLine cycles start
    	 	 vectorDataSet2= new VectorSeriesCollection();
    		 vectorSeries2=new VectorSeries(taskName+"-Dead line");
    		 temp=0;
    		 for(int j=0;j<deadLineList.size();j++){
    			 temp=Integer.parseInt(deadLineList.get(j).toString());
    			 //System.out.println("temp"+temp);
    			 vectorSeries2.add(temp, 11, 0, -3);
    		 }
    		 vectorDataSet2.addSeries(vectorSeries2);
    		 vr2=new VectorRenderer();
    	      subplot.setDataset(2, vectorDataSet2);
    	      subplot.setRenderer(2, vr2);
    	      vr2.setSeriesPaint(0, Color.RED);
    	     
    	      plot.add(subplot, 1);
       	}
    	
    	
    	return new JFreeChart("Execution Trace",
                JFreeChart.DEFAULT_TITLE_FONT, plot, true); 	    	
    }   
    
    private XYDataset createDataset(ArrayList<Object> finalTaskList,String taskName) {
    	int value=0;
    	//System.out.println("TaskName="+taskName);
       final XYSeries s = new XYSeries(taskName);
       	for(int i=0;i<finalTaskList.size();i++){
       		value=Integer.parseInt(finalTaskList.get(i).toString());
       		////System.out.println("value="+value);
       		s.add(i, value);
       	}   
       	s.add(finalTaskList.size(),value);
        return new XYSeriesCollection(s);
    }    
 }