package mygui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.title.LegendTitle;
import org.jfree.util.StringUtils;

public class MainFrame2 {
	private static JFrame mainFrame;
	private static JPanel westPanel,southPanel,northPanel,centerPanel;
	private static JLabel westLabel,northLabel;
	private static JButton taskNumberButton,metricsCalculatorButton=new JButton("calculate Metrics");
	private static JButton checkSchedulabilityButton=new JButton("Check Schedulability");
	private static JLabel taskNumberLabel,utilizationUpperBoundLabel,utilizationTotalLabel,hyperPeriodLabel,rmCheckLabel,tickDiagramOutputLabel;
	private static JTextField taskNumberTextField;
	private static DefaultTableModel tableModel1,tableModel3,tableModel4,semaphoreTableModel1;
	private static Object data1[][],data2[][],data3[][],semaphore1[][];
	private static String[] columnNames1,semaphoreColumns1;
	private static String[] columnNames2;
	private static JTable table1,table3,semaphoreTable1;
	private static JComboBox algorithmList;
	private static int number=0,hyperPeriod=0;
	private static double utilizationUpperBound,utilizationTotal;
	private static ArrayList<Task> finalTaskList;
	public static JTextArea rmCheckTextArea,tickDiagramOutputTextArea;
	public static String algorithmSelected;
	public static JPanel buttonPanel;
	public static int taskNumberButtonCount=0,metricsCalculatorButtonCount=0,checkSchedulabilityButtonCount=0;
	private static JCheckBox chkBlocking;
	private static boolean ShouldDoRTABlocking=false;
	
	public void inputPanel(){
		taskNumberLabel=new JLabel("Number of Tasks=");
		taskNumberTextField = new JTextField(5);
		taskNumberButton=new JButton("enter");			
		JPanel taskNumberPanel=new JPanel();
		taskNumberPanel.setLayout(new FlowLayout());
	
		taskNumberPanel.add(taskNumberLabel);
		taskNumberPanel.add(taskNumberTextField);
		if(algorithmList.getSelectedItem().toString()=="Rate Monotonic" || algorithmList.getSelectedItem().toString()=="Dead Line Monotonic" ){
			 taskNumberPanel.add(chkBlocking);
			 chkBlocking.setSelected(false);
			}
		
		taskNumberPanel.add(taskNumberButton);	
		 
		westPanel.add(taskNumberPanel,2);		
	}	
	
	public static void createJTable(int number){
		//System.out.println("The Number of tasks is="+ number);
		if(algorithmSelected=="Rate Monotonic"){
			columnNames1=new String[3];
			columnNames1[0]="Task";
			columnNames1[1]="ExecTime";
			columnNames1[2]="TimeDuration";
			
			columnNames2=new String[5];
			columnNames2[0]="Task";
			columnNames2[1]="ExecTime";
			columnNames2[2]="TimeDuration";
			columnNames2[3]="Priority";
			columnNames2[4]="InstanceCount";
			
			data1= new Object[number][3];
			for(int i=0;i<number;i++){
				for (int j=0;j<3;j++){
					if(j==0){
						StringBuilder sb = new StringBuilder (String.valueOf ("Task"));
						data1[i][j]=sb.append(i+1).toString();
					}
					else{
						data1[i][j]="  ";
					}
				}
			}
		}
		else if(algorithmSelected=="Dead Line Monotonic"){
			columnNames1=new String[4];
			columnNames1[0]="Task";
			columnNames1[1]="ExecTime";			
			columnNames1[2]="TimeDuration";	
			columnNames1[3]="DeadLine";
			
			columnNames2=new String[6];
			columnNames2[0]="Task";
			columnNames2[1]="ExecTime";			
			columnNames2[2]="TimeDuration";
			columnNames2[3]="DeadLine";
			columnNames2[4]="Priority";
			columnNames2[5]="InstanceCount";
			
			data1= new Object[number][4];
			for(int i=0;i<number;i++){
				for (int j=0;j<4;j++){
					if(j==0){
						StringBuilder sb = new StringBuilder (String.valueOf ("Task"));
						data1[i][j]=sb.append(i+1).toString();
					}
					else{
						data1[i][j]="  ";
					}
				}
			}
		}
		
		else if(algorithmSelected=="Earliest Dead Line First"){
			columnNames1=new String[4];
			columnNames1[0]="Task";
			columnNames1[1]="ExecTime";			
			columnNames1[2]="TimeDuration";	
			columnNames1[3]="DeadLine";
			
			columnNames2=new String[5];
			columnNames2[0]="Task";
			columnNames2[1]="ExecTime";			
			columnNames2[2]="TimeDuration";
			columnNames2[3]="DeadLine";
			columnNames2[4]="InstanceCount";
			
			data1= new Object[number][4];
			for(int i=0;i<number;i++){
				for (int j=0;j<4;j++){
					if(j==0){
						StringBuilder sb = new StringBuilder (String.valueOf ("Task"));
						data1[i][j]=sb.append(i+1).toString();
					}
					else{
						data1[i][j]="  ";
					}
				}
			}
		}
		if(chkBlocking.isSelected()){
			//System.out.println("creating table method:. checkbox selected");
			if(algorithmSelected=="Rate Monotonic"||algorithmSelected=="Dead Line Monotonic"){
				//System.out.println("Looks like RM or DM is selected. so proceeding with table creation");
				ShouldDoRTABlocking=true;
				semaphore1=new Object[number][3];
				semaphoreColumns1=new String[3];
				semaphoreColumns1[0]="Ti";
				semaphoreColumns1[1]="Sk(s1,s2 etc)";
				semaphoreColumns1[2]="CS(Ti,Sk)";
				for(int i=0;i<number;i++){
					for (int j=0;j<3;j++){
						if(j==0){
							StringBuilder sb = new StringBuilder (String.valueOf ("Task"));
							semaphore1[i][j]=sb.append(i+1).toString();
						}
						else{
							semaphore1[i][j]="  ";
						}
					}
				}
				semaphoreTableModel1=new DefaultTableModel(semaphore1, semaphoreColumns1);
				semaphoreTable1=new JTable(semaphoreTableModel1){
				    @Override
				    public boolean isCellEditable(int row, int column) {
				        return column == 0 ? false : true;
				    }
				};
				
			}
			else{
				//System.out.println("Looks like EDF is selected. so not proceeding with table creation");
			}
		}
		else{
			//System.out.println("creating table method:. checkbox is not selected");
		}
		
		
		tableModel1 = new DefaultTableModel(data1, columnNames1);  
		table1 = new JTable(tableModel1){
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return column == 0 ? false : true;
		    }
		};
//		//System.out.println("table1 created. adding it to westPanel");
//		for(int i=0;i<tableModel1.getRowCount();i++){
//			for (int j=0;j<tableModel1.getColumnCount();j++){
//				//System.out.println(data1[i][j]+"\t");
//			}
//			//System.out.println("\n");
//		}		
	}
	
	
	public static int lcmFunction(DefaultTableModel m,int n){
		int lcm=0;
		int[] arr=new int[m.getRowCount()];
		String temp1;		
		for(int i=0;i<m.getRowCount();i++){
			//System.out.println("i="+i);
			temp1=m.getValueAt(i, n).toString().trim();
			arr[i]=Integer.parseInt(temp1);
			//System.out.println("arr["+i+"]:"+arr[i]);
		}
		lcm=lcm(arr);		
		return lcm;		
	}	
	
	
	private static int lcm(int a, int b){
	    return a * (b / gcd(a, b));
	}

	
	private static int lcm(int[] input)
	{
	    int result = input[0];
	    for(int i = 1; i < input.length; i++) {
	    	
	    	result = lcm(result, input[i]);
	    	//System.out.println"lcm result="+result);
	    }
	    return result;
	}
	private static int gcd(int a, int b)
	{
	    while (b > 0)
	    {
	        int temp = b;
	        b = a % b; // % is remainder
	        a = temp;
	    }
	    return a;
	}
	
	private static DefaultTableModel sort(DefaultTableModel m,int n1,int n2){
//		for(int i=0;i<m.getRowCount();i++){
//			for (int j=0;j<m.getColumnCount();j++){
//				//System.out.println(data2[i][j]+"\t");
//			}
//			//System.out.println("\n");
//		}
		if(algorithmSelected=="Rate Monotonic"){
			Arrays.sort(data2, new Comparator<Object[]>() {
	            @Override
	            public int compare(final Object[] entry1, final Object[] entry2) {
	                final Integer time1 = Integer.parseInt((entry1[n1].toString().trim()));
	                final Integer time2 = Integer.parseInt((entry2[n1].toString().trim()));
	                return time1.compareTo(time2);
	            }
	        });
		}
		else if(algorithmSelected=="Dead Line Monotonic"){
			Arrays.sort(data2, new Comparator<Object[]>() {
	            @Override
	            public int compare(final Object[] entry1, final Object[] entry2) {
	                final Integer time1 = Integer.parseInt((entry1[n1].toString().trim()));
	                final Integer time2 = Integer.parseInt((entry2[n1].toString().trim()));	
	                int n1Compare=time1.compareTo(time2);
	                if(n1Compare !=0 ){
	                	return n1Compare;
	                }else{
	                	final Integer time3 = Integer.parseInt((entry1[n2].toString().trim()));
		                final Integer time4 = Integer.parseInt((entry2[n2].toString().trim()));	
		                int n2Compare=time3.compareTo(time4);
		                return n2Compare;
	                }
	            }
	        });			
		}	
//		for(int i=0;i<m.getRowCount();i++){
//			for (int j=0;j<m.getColumnCount();j++){
//				//System.out.println(data2[i][j]+"\t");
//			}
//			//System.out.println("\n");
//		}
		return m;		
	}
	
	public static void checkSchedulability(){
	
	
		//Doing RM initial test Uub
		//may or may not be schedulable if Utotal<=Uub is false
		//Schedulable if Utotal<=Uub is true
		
		rmCheckLabel=new JLabel();
		if(algorithmSelected=="Rate Monotonic"){
			rmCheckLabel.setText("Rate Mnotonic Feasibility check");	
		}
		else if(algorithmSelected=="Dead Line Monotonic"){
			rmCheckLabel.setText("Dead Line Monotonic Feasibility check");
		}
		else if(algorithmSelected=="Earliest Dead Line First"){
			rmCheckLabel.setText("Earliest Dead Line First Algorithm Feasibility check");
		}
		
		if(algorithmSelected=="Rate Monotonic"|algorithmSelected=="Dead Line Monotonic"){
			if(utilizationTotal<=utilizationUpperBound){
				//System.out.println("utilizatinTotal and bound inside check schedulability"+utilizationTotal+","+utilizationUpperBound);
				String result=String.format("utilizationTotal(%.3f)<=utilizationUpperBound(%.3f) \n -Given Task Set is Schedulable\n ",utilizationTotal,utilizationUpperBound );
				//System.out.println("the RM initial check result is="+result);
				//System.out.println("utilizationTotal<=utilizationUpperBound \n Given Task Set is Schedulable under RM policy ");
				rmCheckTextArea.append(result+"\n");
				}
			else{
				//System.out.println("utilizatinTotal and bound inside check schedulability"+utilizationTotal+","+utilizationUpperBound);
				String result=String.format("utilizationTotal(%.3f)>utilizationUpperBound(%.3f) \n Given Task Set may/may not be Schedulable \n we have to check RTA and Tick Diagram \n",utilizationTotal,utilizationUpperBound );
				//System.out.println("the RM initial check result is="+result);
				//System.out.println("utilizationTotal>utilizationUpperBound \n -Given Task Set may/may not be Schedulable\n -we have to check RTA and Tick diagram");
				rmCheckTextArea.append(result+"\n");
				}	
			}
		else if(algorithmSelected=="Earliest Dead Line First"){
			if(utilizationTotal<=1){
				//System.out.println("inside check schedulability--utilizatinTotal="+utilizationTotal);
				String result=String.format("utilizationTotal(%.3f)<=1 \n -Given Task Set is Schedulable under EDF policy\n ",utilizationTotal);
				//System.out.println("the EDF initial check result is="+result);
				//System.out.println("utilizationTotal<=1 \n Given Task Set is Schedulable under EDF policy ");
				rmCheckTextArea.append(result+"\n");
				}
			else{
				//System.out.println("inside check schedulability--utilizatinTotal="+utilizationTotal);
				String result=String.format("utilizationTotal(%.3f)>1 \n -Given Task Set is not Schedulable under EDF policy\n ",utilizationTotal );
				//System.out.println("the EDF initial check result is="+result);
				//System.out.println("utilizationTotal(%.3f)>1 \n -Given Task Set is not Schedulable under EDF policy\n ",utilizationTotal);
				rmCheckTextArea.append(result+"\n");
				}	
		}
		rmCheckTextArea.setEditable(false);
		westPanel.add(rmCheckLabel);
		westPanel.add(rmCheckTextArea);
		westPanel.revalidate();
        westPanel.repaint();		
		
       
        if(algorithmSelected=="Rate Monotonic"){
        	sort(tableModel1,2,0);	
        }
        else if(algorithmSelected=="Dead Line Monotonic"){
        	sort(tableModel1,3,2);        	
        }
		tableModel3=new DefaultTableModel(data2,columnNames2);
		for(int i=0;i<tableModel3.getRowCount();i++){
			for (int j=0;j<tableModel3.getColumnCount();j++){
				//System.out.println("data2["+i+"]["+j+"]= "+data2[i][j]+"\t");
			}
			//System.out.println("\n");
		}
		
		int taskCount=tableModel3.getRowCount();
        int priority=0;
        int numberOfInstances=0;
        //System.out.println("Number of tasks="+taskCount);
        data3=new Object[tableModel3.getRowCount()][tableModel3.getColumnCount()];
        if(algorithmSelected=="Rate Monotonic"){
        for(int i=0;i<tableModel3.getRowCount();i++){			
			for (int j=0;j<tableModel3.getColumnCount();j++){
						if(j<3){
							data3[i][j]=data2[i][j];
						}
						if(j==3){
							priority=taskCount;
							//System.out.println("priority"+priority);
							data3[i][j]=new Integer(priority);
							//System.out.println("data3[i"+i+"]]j["+j+"]="+data3[i][j]+"and priority set is priority");
							taskCount--;
							
						}
						else if(j==4){
							numberOfInstances=(hyperPeriod/(Integer.parseInt(data3[i][2].toString().trim())));
							data3[i][j]=numberOfInstances;							
						}																				
				}			
			}
        }
        else if(algorithmSelected=="Dead Line Monotonic"){
            for(int i=0;i<tableModel3.getRowCount();i++){			
    			for (int j=0;j<tableModel3.getColumnCount();j++){
    						if(j<4){
    							data3[i][j]=data2[i][j];
    						}
    						if(j==4){
    							priority=taskCount;
    							//System.out.println("priority"+priority);
    							data3[i][j]=new Integer(priority);
    							//System.out.println("data3[i"+i+"]]j["+j+"]="+data3[i][j]+"and priority set is priority");
    							taskCount--;
    							
    						}
    						else if(j==5){
    							numberOfInstances=(hyperPeriod/(Integer.parseInt(data3[i][2].toString().trim())));
    							data3[i][j]=numberOfInstances;							
    						}																				
    				}			
    			}
        }
        else if(algorithmSelected=="Earliest Dead Line First"){
            for(int i=0;i<tableModel3.getRowCount();i++){			
    			for (int j=0;j<tableModel3.getColumnCount();j++){
    						if(j<=3){
    							data3[i][j]=data2[i][j];
    						}
    						else if(j==4){
    							numberOfInstances=(hyperPeriod/(Integer.parseInt(data3[i][2].toString().trim())));
    							data3[i][j]=numberOfInstances;							
    						}																				
    				}			
    			}
        }
        for(int i1=0;i1<tableModel3.getRowCount();i1++){
			for (int j=0;j<tableModel3.getColumnCount();j++){
				//System.out.println("data3["+i1+"]["+j+"]="+data3[i1][j]+"\t");
				}
			//System.out.println("\n");			
		}
        
        tableModel4=new DefaultTableModel(data3,columnNames2);
        
        table3=new JTable(tableModel4){
            private static final long serialVersionUID = 1L;

            public boolean isCellEditable(int row, int column) {                
                    return false;               
            };
        };
        table3.setPreferredScrollableViewportSize(table3.getPreferredSize());
        JScrollPane scroll3 = new JScrollPane(table3);
        
        //changed eastpanel here 
        westPanel.add(scroll3);
        //eastPanel.add(table3);
        westPanel.revalidate();
        westPanel.repaint();
        //changes eastPanel here
        //Plotter plotter=new Plotter(tableModel4,hyperPeriod);        
        
        if(algorithmSelected=="Earliest Dead Line First"){
        	EDFScheduling tempEDFScheduling=new EDFScheduling(tableModel4,hyperPeriod);
        	westPanel.add(tempEDFScheduling.processorDemandAnalysis());
        	tempEDFScheduling.EDFSchedulingFunction();
        	ArrayList<EDFTask> tasksList=tempEDFScheduling.tasksList;
        	ArrayList<Number> idleTicksList=tempEDFScheduling.idleTicksList;
        	
        	CombinedXYPlotDemo4 test=new CombinedXYPlotDemo4("EDF");
        	final JFreeChart chart1 = test.createCombinedChart(tasksList);
            final ChartPanel panel1 = new ChartPanel(chart1, true, true, true, false, true);             
            panel1.getChart().removeLegend();              
            centerPanel.add(panel1);
        	
        	tickDiagramOutputTextArea=new JTextArea(taskCount,20);
            tickDiagramOutputTextArea.setEditable(false);
            tickDiagramOutputLabel=new JLabel("Tick Diagram Result");
            int countp=0,countc=0;
        	for(int task=0;task<tasksList.size();task++){
        		
        		ArrayList<Number> preEmptionTicksList=new ArrayList<Number>();
        		ArrayList<Number> contextSwitchList=new ArrayList<Number>();
        		for(int instance=1;instance<=tasksList.get(task).getNoOfInstances();instance++){
					for(int p=0;p<tasksList.get(task).getTaskInstanceList().get(instance-1).getTaskInstancePreEmptionTicks().size();p++){
						preEmptionTicksList.add(tasksList.get(task).getTaskInstanceList().get(instance-1).getTaskInstancePreEmptionTicks().get(p));
						countp++;
					}
					
        		}
        		for(int instance=1;instance<=tasksList.get(task).getNoOfInstances();instance++){
					for(int p=0;p<tasksList.get(task).getTaskInstanceList().get(instance-1).getTaskInstanceContextSwitchTicks().size();p++){
						contextSwitchList.add(tasksList.get(task).getTaskInstanceList().get(instance-1).getTaskInstanceContextSwitchTicks().get(p));
						countc++;
					}					
        		}
        		if(!preEmptionTicksList.isEmpty()){
        			String preEmptionMessage="Total Count of PreEmptions="+countp+" for "+tasksList.get(task).getTaskName()+" :Pre-Empted at ticks=";
        			for(int p=0;p<preEmptionTicksList.size();p++){
        				preEmptionMessage+=" "+preEmptionTicksList.get(p);
        			}
        			tickDiagramOutputTextArea.append(preEmptionMessage+"\n");
        		}
        		else{
        			tickDiagramOutputTextArea.append("no Pre-emptions for "+tasksList.get(task).getTaskName()+"\n");
        		}
        		if(!contextSwitchList.isEmpty()){
        			String contextSwitchMessage="Total Count of context switches="+countc+" for "+tasksList.get(task).getTaskName()+" context switch at ticks=";
        			for(int p=0;p<contextSwitchList.size();p++){
        				contextSwitchMessage+=" "+contextSwitchList.get(p);
        			}
        			tickDiagramOutputTextArea.append(contextSwitchMessage+"\n");
        		}
        		else{
        			tickDiagramOutputTextArea.append("no Context Switches for "+tasksList.get(task).getTaskName()+"\n");
        		}
        		
        		        		
        	}
        	
        	if(!idleTicksList.isEmpty()){
    			String idleTicksMessage="The Final Idle ticks are=";
    			for(int i=0;i<idleTicksList.size();i++){
    				idleTicksMessage+=" "+idleTicksList.get(i);
    			}	
    			tickDiagramOutputTextArea.append(idleTicksMessage);
    		}
        	else{
        		String idleTicksMessage="No Slack Time/idle ticks=0";
        		tickDiagramOutputTextArea.append(idleTicksMessage+"\n");
        	}
        	
        	//Checking dead line miss details
//        	for(int task=0;task<tasksList.size();task++){
//        		String deadLineMissMessage="Checking if there was any Dead Line Miss\n";
//        		ArrayList<EDFTaskInstance> instanceList=tasksList.get(task).getTaskInstanceList();
//        		for(int instance=0;instance<tasksList.get(task).getNoOfInstances();instance++){
//        			if(instanceList.get(instance).isCurrentInstanceMissedDeadLine==true){
//        				deadLineMissMessage+="Dead Line Miss Details: \n" ;
//        				
//        			}
//        		}
//        	}
        	if(tempEDFScheduling.deadLineMissDetails[0]!=null){
        		String deadLineMissMessage="There is a Dead Line Miss \n";
        		deadLineMissMessage+="taskName="+tempEDFScheduling.deadLineMissDetails[0]+"\n";
        		deadLineMissMessage+="Instance Number=="+tempEDFScheduling.deadLineMissDetails[1]+"\n";
        		deadLineMissMessage+="DeadLine Miss Tick="+tempEDFScheduling.deadLineMissDetails[2]+"\n";
        		deadLineMissMessage+="NoOfticks needed="+tempEDFScheduling.deadLineMissDetails[3]+"\n";   
        		tickDiagramOutputTextArea.append(deadLineMissMessage);
        	}
        	else{
        		String deadLineMissMessage="No Dead Line Miss. \n Given Task Set is Successfully Scheduled using EDF";
        		tickDiagramOutputTextArea.append(deadLineMissMessage);
        	}
        	
        	westPanel.add(tickDiagramOutputLabel);
        	westPanel.add(tickDiagramOutputTextArea);
        } //end of if condition : isalgorithmSelected="EDF"
        
        else if(algorithmSelected=="Rate Monotonic"|algorithmSelected=="Dead Line Monotonic"){
        ResponseTimeAnalysis();          
        createRMArrayList();            
        
        CombinedXYPlotDemo3 test=new CombinedXYPlotDemo3("RM");
        
        final JFreeChart chart1 = test.createCombinedChart(finalTaskList);
        final ChartPanel panel1 = new ChartPanel(chart1, true, true, true, false, true);             
        panel1.getChart().removeLegend();        
        
        centerPanel.add(panel1);        
        //lets print the Tick diagram result
        taskCount=finalTaskList.size();
        tickDiagramOutputTextArea=new JTextArea(taskCount,20);
        tickDiagramOutputTextArea.setEditable(false);
        tickDiagramOutputLabel=new JLabel("Tick Diagram Result");
        //System.out.println("test point 1="+taskCount);
        int tickResultcount=0;
        for(int i=0;i<taskCount;i++){
        	Task temp=(Task)finalTaskList.get(i);
        	//System.out.println("test point");
        	String preEmptionTicks=" ";
        	String contextSwitchTicks=" ";        	  	
        	String message=null; 
        	if(temp.isSchedulingSuccessful){        		
        		if(algorithmSelected=="Rate Monotonic"){
            		message=temp.taskName+" is Successful to Schedule according to RM \n";
            		}
            		else if(algorithmSelected=="Dead Line Monotonic"){
            			message=temp.taskName+" is Successful to Schedule according to DM \n";	
            		}
        		//System.out.println("inside tickDiagramOutputTextArea:"+message);
        		tickDiagramOutputTextArea.append(message);
        		tickResultcount++;
        	}
        	else{
        		
        		if(algorithmSelected=="Rate Monotonic"){
        		message=temp.taskName+" is Failed to Schedule according to RM \n";
        		}
        		else if(algorithmSelected=="Dead Line Monotonic"){
        		message=temp.taskName+" is Failed to Schedule according to DM \n";	
        		}
        		//System.out.println("inside tickDiagramOutputTextArea:"+message);
        		tickDiagramOutputTextArea.append(message);
        	}
        	if(!temp.preEmptionList.isEmpty()){
    			//System.out.println("There are some pre-emtions for this task");
    			for(int j=0;j<temp.preEmptionList.size();j++){
    				String[] temp1=temp.preEmptionList.get(j);
    				//System.out.println("current Task="+temp1[0]+"is pre-empted by="+temp1[1]+"at tick Number="+temp1[2]+"with preEmptonCount="+temp1[3]);
    				preEmptionTicks=preEmptionTicks+" "+temp1[2];
    				
    			}
    			tickDiagramOutputTextArea.append(temp.taskName+" was Pre-empted at ticks-"+preEmptionTicks+"\n");
    			
    		}
    		if(!temp.contextSwitchList.isEmpty()){
    			//System.out.println("There are some context Switches for this task");
    			for(int j=0;j<temp.contextSwitchList.size();j++){
    				String[] temp1=temp.contextSwitchList.get(j);
    				//System.out.println("current Task="+temp1[0]+"is pre-empted by="+temp1[1]+"at tick Number="+temp1[2]+"with context Switch Count="+temp1[3]+"resulting in contextSwitch");
    				contextSwitchTicks=contextSwitchTicks+" "+temp1[2];
    				
    			}
    			tickDiagramOutputTextArea.append(temp.taskName+" had contextSwitch at ticks-"+contextSwitchTicks+"\n");
    		}
        	
        	
        	
        }
        if(tickResultcount==taskCount){
        	tickDiagramOutputTextArea.append("All tasks are schedulable according to tick diagram"); 
        	Task lastTask=(Task)finalTaskList.get(finalTaskList.size()-1);
        	if(!lastTask.idleTicksList.isEmpty()){
    			//System.out.println("There are some idle ticks for this task");
    			String idleTicksMessage=" ";
    			for(int i=0;i<lastTask.idleTicksList.size();i++){
    				int temp1=Integer.parseInt(lastTask.idleTicksList.get(i).toString());
    				idleTicksMessage+=temp1+" ";
    				//System.out.println("current Task="+lastTask.taskName+"=idle ticks="+idleTicksMessage);    				
    			}
    			tickDiagramOutputTextArea.append("\n idle ticks="+idleTicksMessage);
    		}
        }
        else if(tickResultcount<taskCount){
        	tickDiagramOutputTextArea.append("deadLine Miss for "+finalTaskList.get(tickResultcount).taskName);
        	String deadLineMiss=" \n deadLineMissDetails: \n Task="+finalTaskList.get(tickResultcount).deadLineMissTick[0]+" --instanceNumber= "+finalTaskList.get(tickResultcount).deadLineMissTick[1]+". it need "+finalTaskList.get(tickResultcount).deadLineMissTick[2]+" more ticks to complete";
        	//System.out.println(deadLineMiss);
        	tickDiagramOutputTextArea.append(deadLineMiss);
        }
        
        
        centerPanel.add(tickDiagramOutputLabel);
        centerPanel.add(tickDiagramOutputTextArea);        
      }
    }
	public static void calculateMetrics(){
		if(algorithmSelected=="Rate Monotonic"){
		data2=new Object[number][5];
		}
		else if(algorithmSelected=="Dead Line Monotonic"){
		data2=new Object[number][6];
		}
		else if(algorithmSelected=="Earliest Dead Line First"){
			data2=new Object[number][5];
		}
		
		int taskCount=tableModel1.getRowCount();
		for(int i=0;i<tableModel1.getRowCount();i++){			
			for (int j=0;j<tableModel1.getColumnCount();j++){
					if(j==0){
						data2[i][j]=tableModel1.getValueAt(i, j);
						}
						else{
							int num;
							String s=tableModel1.getValueAt(i, j).toString().trim();
							//System.out.println(s);
							num=Integer.parseInt(s);
							data2[i][j]=num;
						}
																					
				}
			}
		for(int i=0;i<tableModel1.getRowCount();i++){
			for (int j=0;j<tableModel1.getColumnCount();j++){
				//System.out.println("data2["+i+"]["+j+"]="+data2[i][j]+"\t");
			}
			//System.out.println("\n");
		}
		
		
		hyperPeriod=lcmFunction(tableModel1,2);  
		
		
		//System.out.println("The Final LCM is=hyperPeriod="+hyperPeriod);
		utilizationTotal=0;
		if(algorithmSelected=="Rate Monotonic"){
		for(int i1=0;i1<tableModel1.getRowCount();i1++){
        	double ci=Integer.parseInt(data2[i1][1].toString().trim());
        	double ti=Integer.parseInt(data2[i1][2].toString().trim());	  	
        	double ciByTi=(ci)/(ti);
			//System.out.println("ci,ti,Utilization Total of processor="+ci+":::"+ti+":::"+utilizationTotal);
			utilizationTotal+=ciByTi;			
			}
		}
		else if(algorithmSelected=="Dead Line Monotonic"){
			for(int i1=0;i1<tableModel1.getRowCount();i1++){
	        	double ci=Integer.parseInt(data2[i1][1].toString().trim());
	        	double ti=Integer.parseInt(data2[i1][2].toString().trim());	
	        	double di=Integer.parseInt(data2[i1][3].toString().trim());
	        	double ciByDi=(ci)/(di);
				//System.out.println("ci,ti,Utilization Total of processor="+ci+":::"+ti+":::"+utilizationTotal);
				utilizationTotal+=ciByDi;			
				}
		}
		else if(algorithmSelected=="Earliest Dead Line First"){
			for(int i1=0;i1<tableModel1.getRowCount();i1++){
	        	double ci=Integer.parseInt(data2[i1][1].toString().trim());
	        	double ti=Integer.parseInt(data2[i1][2].toString().trim());	
	        	double di=Integer.parseInt(data2[i1][3].toString().trim());
	        	double ciByDi=(ci)/(di);
				//System.out.println("ci,ti,Utilization Total of processor="+ci+":::"+ti+":::"+utilizationTotal);
				utilizationTotal+=ciByDi;			
				}
		}
        //System.out.println("Utilization Total of processor="+utilizationTotal); 
        
        hyperPeriodLabel=new JLabel("hyperPeriod="+hyperPeriod);
        rmCheckTextArea=new JTextArea(taskCount,30);
        rmCheckTextArea.append(hyperPeriodLabel.getText()+"\n");        
        utilizationTotalLabel=new JLabel();
        utilizationTotalLabel.setText(String.format("utilizationTotal= %.3f",utilizationTotal));
        rmCheckTextArea.append(utilizationTotalLabel.getText()+"\n");               
        double noOfTasks=tableModel1.getRowCount();
        //System.out.println("noOfTasks:Uub="+noOfTasks);
        //System.out.println(Math.pow(2, 1));
        utilizationUpperBound= noOfTasks*(Math.pow(2, (1/noOfTasks))-1);
        //System.out.println("utilizationUpperBound"+utilizationUpperBound);
        utilizationUpperBoundLabel=new JLabel();
        utilizationUpperBoundLabel.setText(String.format("utilizationUpperBound= %.3f",utilizationUpperBound));   
        if(algorithmSelected=="Rate Monotonic"|algorithmSelected=="Dead Line Monotonic"){
        rmCheckTextArea.append(utilizationUpperBoundLabel.getText()+"\n");
        }
        if(metricsCalculatorButtonCount>0){
        	int maxPanelSize=westPanel.getComponentCount();
	    	   //System.out.println("westPanel component count in calculate metrics="+maxPanelSize);
	    	   int someNumber=5;
	    	   if(ShouldDoRTABlocking){
	    		   someNumber=7;
	    	   }
	    	   else{
	    		   someNumber=5;
	    	   }
	    	   for(int i=someNumber;i<maxPanelSize;i++){
	    		   //System.out.println(westPanel.getComponentCount());	  
	    		   //System.out.println("i="+i);
	    		   westPanel.remove(someNumber);  		    		   
	    		   //buttonPanel.removeAll();    		   
	    	   }
	    	   centerPanel.removeAll();
        }
        westPanel.add(rmCheckTextArea);        
        
        westPanel.revalidate();
        westPanel.repaint();      
        centerPanel.revalidate();
        centerPanel.repaint();  
        
	}
	
	//start:RM PLotting: Array creation
	public static void createRMArrayList(){
		int noOfTasks=tableModel4.getRowCount();
		String taskName = null;
		int execTime = 0,deadLine = 0,timePeriod = 0,noOfInstances=0,priority=0;		
		
		//System.out.println("Entered in to RM/DM Array Creation: no of tasks is="+noOfTasks);
		
		finalTaskList=new ArrayList<Task>();
		
		for(int i=0;i<noOfTasks;i++){
			ArrayList<Object> taskList=new ArrayList<Object>();
			if(algorithmSelected=="Rate Monotonic"){
				taskName=tableModel4.getValueAt(i, 0).toString();
				execTime=Integer.parseInt(tableModel4.getValueAt(i, 1).toString());
				timePeriod=Integer.parseInt(tableModel4.getValueAt(i, 2).toString());				
				priority=Integer.parseInt(tableModel4.getValueAt(i, 3).toString());
				noOfInstances=Integer.parseInt(tableModel4.getValueAt(i, 4).toString());
				deadLine=timePeriod;
			}
			else if(algorithmSelected=="Dead Line Monotonic"){
				taskName=tableModel4.getValueAt(i, 0).toString();
				execTime=Integer.parseInt(tableModel4.getValueAt(i, 1).toString());
				timePeriod=Integer.parseInt(tableModel4.getValueAt(i, 2).toString());
				deadLine=Integer.parseInt(tableModel4.getValueAt(i, 3).toString());
				priority=Integer.parseInt(tableModel4.getValueAt(i, 4).toString());
				noOfInstances=Integer.parseInt(tableModel4.getValueAt(i, 5).toString());
			}			
			taskList.add(0, taskName);
			taskList.add(1, execTime);
			taskList.add(2, timePeriod);
			taskList.add(3, deadLine);
			taskList.add(4, priority );
			taskList.add(5, noOfInstances);
			
			Task task$i=new Task(taskList);
			finalTaskList.add(i,task$i);
			//System.out.println("printing Current task="+finalTaskList.get(i).taskName);
			
			for(int j=0;j<=finalTaskList.size()-1;j++){
				Task tempTask=finalTaskList.get(j);
				//System.out.println("printing all task names till now="+tempTask.taskName);
			}
						
			task$i.setTaskSchedule(finalTaskList,hyperPeriod);
			
		}		
		
	
	}
	
	// end: RM Plotting: Array Creation
	//RTA Analysis function starts here
	public static void ResponseTimeAnalysis(){
		int noOfTasks=tableModel4.getRowCount();
		String currentTaskName;
		double rinp1,rin,cin,tj,cj,din = 0,temprinp1,tin=0;  
		boolean hasAnyTaskFailed=false;
		 //bin is the blocking time used for RTA with Blocking case
		Integer bin[]=new Integer[noOfTasks];
		String semaphores="";
		JLabel RTALabel=new JLabel();
		
		JTextArea RTATextArea=new JTextArea(noOfTasks,20);
		if(ShouldDoRTABlocking==true){
			 RTALabel.setText("RTA with Blocking Result \n");
		}
		else{
			RTALabel.setText("RTA Result \n");
		}
		//RTALabel.setHorizontalAlignment(SwingConstants.LEFT);
		String newline = "\n";
		
		
		
		
		if(ShouldDoRTABlocking==true){	
			SortedSet<String> semaphoreSet = new TreeSet<String>();
			 
			ArrayList<Object> tiSi1=new ArrayList<Object>();
			ArrayList<String> lptasks;
			//getting all semaphores first
			for(int i=0;i<noOfTasks;i++){
				if(semaphoreTableModel1.getValueAt(i, 1).toString()!=null){
					semaphores+=semaphoreTableModel1.getValueAt(i, 1).toString().trim();
					if(i<noOfTasks-1){
						semaphores+=",";
					}
				}
							
			}
			//System.out.println("semaphores="+semaphores);
			String[] sk=semaphores.split(",[ ]*");
			
			
			for (String num : sk) {
				semaphoreSet.add(num);
			}
			//System.out.println(semaphoreSet);
			for(String str:semaphoreSet){
				ArrayList<String> tiSi=new ArrayList<String>();
				ArrayList<Integer> priority=new ArrayList<Integer>();
				for(int i=0;i<noOfTasks;i++){
					if(semaphoreTableModel1.getValueAt(i, 1).toString().trim().contains(str)){
						tiSi.add(semaphoreTableModel1.getValueAt(i, 0).toString());  //taskName							
					}
				}
				tiSi1.add(str);
				tiSi1.add(tiSi);
				for(String task:tiSi){
					for(int i=0;i<noOfTasks;i++){
						//System.out.println("task="+task);
						//System.out.println("tableModel4.getValueAt(i, 0)"+tableModel4.getValueAt(i, 0).toString());
						if(task.equals(tableModel4.getValueAt(i, 0).toString().trim())){
							priority.add(Integer.parseInt(tableModel4.getValueAt(i, 3).toString()));
							//System.out.println("in if priority="+Integer.parseInt(tableModel4.getValueAt(i, 3).toString()));
						}
						else{
							//System.out.println("in else");
						}
					}
				}
				tiSi1.add(priority);
				int ceil=Collections.max(priority);
				//System.out.println("ceil="+ceil);
				tiSi1.add(ceil);
			}
			//System.out.println(tiSi1);
			//now we got ceil(si). we have to now calculate blocking time.
			for(int i=0;i<noOfTasks;i++){
				lptasks=new ArrayList<String>();
				//System.out.println("checking blocking time for task="+semaphoreTableModel1.getValueAt(i, 0));
				String task=semaphoreTableModel1.getValueAt(i, 0).toString();
				int ctpriority=0;
				for(int j=0;j<noOfTasks;j++){
					if(task.equals(tableModel4.getValueAt(j, 0).toString().trim())){
						ctpriority=Integer.parseInt(tableModel4.getValueAt(j, 3).toString());
					//System.out.println("in if priority="+Integer.parseInt(tableModel4.getValueAt(i, 3).toString()));
					}
				}
				//System.out.println("current task priority="+ctpriority);
				for(int j=0;j<noOfTasks;j++){
					if(ctpriority>Integer.parseInt(tableModel4.getValueAt(j, 3).toString())){
						lptasks.add(tableModel4.getValueAt(j, 0).toString());
					//System.out.println("in if priority="+Integer.parseInt(tableModel4.getValueAt(i, 3).toString()));
					}
				}
				//System.out.println("taskName="+task);
				//System.out.println(lptasks);
				
				//now we have all lp tasks for current task. 
				//check semaphores used by lp tasks
				ArrayList<Integer> blockCS=new ArrayList<Integer>(); 
				
				
				//----------->
				for(String task1:lptasks){
					String semaphores1=null;
					String csLength=null;
					for(int j=0;j<noOfTasks;j++){
						
						//System.out.println("task1="+task1);
						//System.out.println("semaphoreTableModel1.getValueAt(j, 0)"+semaphoreTableModel1.getValueAt(j, 0));
						if(task1.equals(semaphoreTableModel1.getValueAt(j, 0).toString().trim())){
							if(semaphoreTableModel1.getValueAt(j, 1).toString()!=null){
								semaphores1=semaphoreTableModel1.getValueAt(j, 1).toString().trim();
								csLength=semaphoreTableModel1.getValueAt(j, 2).toString().trim();
								//System.out.println("Semaphores1="+semaphores1);		
								//System.out.println("csLength="+csLength);
								String[] sk2=semaphores1.split(",[ ]*");
								String[] ck2=csLength.split(",[ ]*");
								if(semaphores1 != null && !semaphores1.isEmpty()){
									//System.out.println("in if condition: semaphore length is not 0="+sk2.length);								
									//priority of current task
									//System.out.println("current task priority="+ctpriority);
									
									//checking ceil now
									for(String sem:sk2){
										//System.out.println("sem="+sem);
										int temp=Integer.parseInt(sem.substring(1));
										//System.out.println("temp="+temp);
										int value=4*temp-1;
										//System.out.println("value="+value);
										//ceil is the 4th element i.e.,4x-1 th element as index starts from 0.
										double ceil=Double.parseDouble(tiSi1.get(value).toString());
										//System.out.println("ceil of "+sem+" is ="+ceil);
										if(ctpriority>ceil){
											//System.out.println("this semaphore can not block task");
											
										}
										else{
											//System.out.println("This semaphore will block: taskName= "+task1+"semaphore= "+sem);
											int csl=0;
											if(semaphores1 != null && !semaphores1.isEmpty()){
												int index=Arrays.asList(sk2).indexOf(sem);
												//System.out.println("index="+index);
												if(csLength != null && !csLength.isEmpty()){
													csl=Integer.parseInt(ck2[index]);
													//System.out.println("csl= "+csl);
													blockCS.add(csl);
												}
												else{
													//System.out.println("csLength is zero");
												}
											}
											else{
												//System.out.println("cs lenth calculation: semaphores1 is zero");
											}
											
										}
									}
							}
							else{
								//System.out.println("this task does not have semaphores");
							}
							
								
							}
							
						}
						
					}
					
					
				}//end of lptasks
				if(!blockCS.isEmpty()){
					bin[i]=Collections.max(blockCS);
					//System.out.println("task="+i+" the bin is= "+bin[i]);
				}
				else{
					bin[i]=0;
					//System.out.println("task="+i+" the bin is= "+bin[i]);
				}
				
			}
			
			
			
				
		}//end of RTA blocking bin calculation				
		
		for(int i=0;i<noOfTasks;i++){
			currentTaskName=tableModel4.getValueAt(i, 0).toString();
			cin=Integer.parseInt(tableModel4.getValueAt(i, 1).toString());  //cin =exec time of current task
			tin=Integer.parseInt(tableModel4.getValueAt(i, 2).toString());  
			if(algorithmSelected=="Rate Monotonic"){
				din=tin;    
			}else if(algorithmSelected=="Dead Line Monotonic"){
				din=Integer.parseInt(tableModel4.getValueAt(i, 3).toString());
			}
			int value=0;
			if(ShouldDoRTABlocking){
			value=Integer.parseInt(currentTaskName.substring(4));
			rin=cin+bin[value-1]; 
			rinp1=cin+bin[value-1];
			
			boolean continueIteration=true;
			if(i==0){
				continueIteration=false;
			}
			
			
			double temprinp2=cin+bin[value-1];
			while(continueIteration){
				temprinp1= cin+bin[value-1];
				//System.out.println("BEFORE temprinp1,temprinp2="+temprinp1+"\t"+temprinp2);
				for(int j=i-1;j>=0;j--){
					 
					tj=Integer.parseInt(tableModel4.getValueAt(j, 2).toString());  ///time period hp task
					cj=Integer.parseInt(tableModel4.getValueAt(j, 1).toString());   //exec time of hp task
					temprinp1+=(java.lang.Math.ceil(temprinp2/tj)*cj);
					//System.out.println("hp task is"+tableModel4.getValueAt(j, 0));
					//System.out.println("tj,cj="+tj+"\t"+cj);
					//System.out.println("in hp temprinp1="+temprinp1);
				}
				if(temprinp1==temprinp2){
					//System.out.println("after temprinp1,temprinp2="+temprinp1+"\t"+temprinp2);
					rin=temprinp1;
					continueIteration=false;
				}
				else{
					temprinp2=temprinp1;
					continueIteration=true;
				}				
			}
			
			}
			else{
				rin=cin; 
				rinp1=cin;
				
				boolean continueIteration=true;
				if(i==0){
					continueIteration=false;
				}
				
				
				double temprinp2=cin;
				while(continueIteration){
					temprinp1= cin;
					//System.out.println("BEFORE temprinp1,temprinp2="+temprinp1+"\t"+temprinp2);
					for(int j=i-1;j>=0;j--){
						 
						tj=Integer.parseInt(tableModel4.getValueAt(j, 2).toString());  ///time period hp task
						cj=Integer.parseInt(tableModel4.getValueAt(j, 1).toString());   //exec time of hp task
						temprinp1+=(java.lang.Math.ceil(temprinp2/tj)*cj);
						//System.out.println("hp task is"+tableModel4.getValueAt(j, 0));
						//System.out.println("tj,cj="+tj+"\t"+cj);
						//System.out.println("in hp temprinp1="+temprinp1);
					}
					if(temprinp1==temprinp2){
						//System.out.println("after temprinp1,temprinp2="+temprinp1+"\t"+temprinp2);
						rin=temprinp1;
						continueIteration=false;
					}
					else{
						temprinp2=temprinp1;
						continueIteration=true;
					}				
				}
			}
			
			//System.out.println("value in RTA="+value);
			//System.out.println("inside RTA: bin["+(value-1)+"]="+bin[value-1]);
			//System.out.println("current task="+currentTaskName);
			
			//temprinp1= rinp1;
			//System.out.println("cin="+cin);
			//System.out.println("rin="+rin);
			
			//System.out.println("rinp1="+rinp1);		
			
			
		
			
			//System.out.println("rin="+rin);
			//System.out.println("cin="+cin);
			if(rin<din){
				//System.out.println("Task is successfull="+currentTaskName);
				RTATextArea.append(currentTaskName+"--Ri="+rin+"<Di="+din+"==>Successful"+newline);
				
			}
			else{
				//System.out.println("Task is failed="+currentTaskName);
				RTATextArea.append(currentTaskName+"--Ri="+rin+">Di="+din+"==>Failed"+newline);
				hasAnyTaskFailed=true;
				//continueIteration=false;	
				
			}		
			
		}
		if(algorithmSelected=="Rate Monotonic"){
			if(hasAnyTaskFailed){
				RTATextArea.append("RTA Result \n :looks like some tasks are not Schedulable under RM policy");
			}
			else{
				RTATextArea.append("All the Tasks are Schedulable under RM policy");
			}
		}else if(algorithmSelected=="Dead Line Monotonic"){
			if(hasAnyTaskFailed){
				RTATextArea.append("RTA Result \n :looks like some tasks are not Schedulable under DM policy");
			}
			else{
				RTATextArea.append("All the Tasks are Schedulable under DM policy");
			}
		}		
		westPanel.add(RTALabel);
		RTATextArea.setEditable(false);
		westPanel.add(RTATextArea);
	}
		
	
	//RTA analysis end here
	
	
	public static void main(String args[]){
		mainFrame=new JFrame("Testing BorderLayout");
		mainFrame.setLayout(new BorderLayout());		
		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		westPanel=new JPanel();
		northPanel=new JPanel();
		southPanel=new JPanel();
		centerPanel=new JPanel();		
		northLabel=new JLabel();	
		buttonPanel=new JPanel();
		
		chkBlocking = new JCheckBox("RTA with Blocking");
		
		westLabel=new JLabel("User input");
		westPanel.add(westLabel,0);
		northLabel.setText("Task Scheduling GUI");
		northPanel.add(northLabel);		
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
		String[] algorithmSelector = { "Rate Monotonic", "Dead Line Monotonic", "Earliest Dead Line First"};
		algorithmList=new JComboBox(algorithmSelector){
			@Override
            public Dimension getMaximumSize() {
                Dimension max = super.getMaximumSize();
                max.height = getPreferredSize().height;
                return max;
            }
        };
		algorithmList.setPreferredSize(new Dimension(1,25));
		algorithmList.setSelectedIndex(0);
		
		westPanel.add(algorithmList,1);		
		
		MainFrame2 mainFrame2Obj=new MainFrame2();
		mainFrame2Obj.inputPanel();	
		
		taskNumberButtonCount=0;
		taskNumberButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				
				algorithmSelected=algorithmList.getSelectedItem().toString();
				
				//System.out.println("Algorithm selected is= "+algorithmSelected);				
				number=Integer.parseInt(taskNumberTextField.getText());				
				createJTable(number);					
				table1.setPreferredScrollableViewportSize(table1.getPreferredSize());	
				
		        JScrollPane scroll1 = new JScrollPane(table1);			        
		        buttonPanel.setLayout(new FlowLayout());
		        
		        if(algorithmSelected=="Earliest Dead Line First"){
		        	//System.out.println("inside checking RTA blocking check box: EDF: status is"+chkBlocking.isSelected());
		        	chkBlocking.setEnabled(false);
		        }
		        else{
		        	//System.out.println("inside checking RTA blocking check box: RM or Dm: status is"+chkBlocking.isSelected());
		        	chkBlocking.setEnabled(true);
		        }
		       if(taskNumberButtonCount>0){		    	   
		    	   int maxPanelSize=westPanel.getComponentCount();
		    	   //System.out.println("westPanel component count"+maxPanelSize);
		    	   for(int i=3;i<maxPanelSize;i++){	
		    		   //System.out.println("westPanel.getComponentCount()="+westPanel.getComponentCount());
		    		   //System.out.println("i="+i);
		    		   westPanel.remove(3);		    		   		    		   
		    	   }
		    	   buttonPanel.removeAll();
	    		   centerPanel.removeAll();
		       }               
		              
		       buttonPanel.add(metricsCalculatorButton);
		      
		        int wpNumber=3;
		       westPanel.add(scroll1,wpNumber);
		       wpNumber++;
		        
		        if(algorithmSelected=="Rate Monotonic"||algorithmSelected=="Dead Line Monotonic"){
		        	if(chkBlocking.isSelected()){
		        		//System.out.println("Adding semaphore table ");
		        		ShouldDoRTABlocking=true;
		        		semaphoreTable1.setPreferredScrollableViewportSize(semaphoreTable1.getPreferredSize());
		        		JScrollPane scroll2 = new JScrollPane(semaphoreTable1);
		        		JLabel note=new JLabel("Note:Sk and CS(Ti,Sk) are comma separated incase of multiple. null for no entries");
		        		westPanel.add(note,wpNumber);
		        		wpNumber++;
		        		westPanel.add(scroll2,wpNumber);
		        		wpNumber++;
		        	}
		        }
		        westPanel.add(buttonPanel,wpNumber);
		        wpNumber++;	        
		        taskNumberButtonCount++;		        
				westPanel.revalidate();
				westPanel.repaint();
				centerPanel.revalidate();
				centerPanel.repaint();
				
			}			
		});		
		
		metricsCalculatorButtonCount=0;
		metricsCalculatorButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e1) {
				//System.out.println("inside metricscalculation button");				
				buttonPanel.add(checkSchedulabilityButton);
				
				buttonPanel.revalidate();
				buttonPanel.repaint();
				checkSchedulabilityButtonCount=0;
				calculateMetrics();
				metricsCalculatorButtonCount++;
				
			}
			
		});
		checkSchedulabilityButtonCount=0;
		checkSchedulabilityButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e2) {
				//System.out.println("inside check schedulability button");
				if(checkSchedulabilityButtonCount==0){
				checkSchedulability();
				
				checkSchedulabilityButtonCount++;
				}
			}
			
		});
		
		northPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		westPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		southPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		centerPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
		
		mainFrame.add(westPanel, BorderLayout.WEST);
		mainFrame.add(northPanel,BorderLayout.NORTH);
		mainFrame.add(southPanel, BorderLayout.SOUTH);
		mainFrame.add(centerPanel, BorderLayout.CENTER);
		
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		mainFrame.pack();
		mainFrame.setVisible(true);
		
	}
}


class Task{
	public ArrayList<Object> finalTaskList;
	public ArrayList<Number> releaseTimeList,deadLineList,timePeriodList;
	public String taskName;
	public int execTime,timePeriod,noOfInstances,priority,deadLine;
	public ArrayList<Task> highPriorityTasksList;
	//public ArrayList<String> usedTicksList;
	public int noOfTicksRan=0,noOfTicksNeeded=0,currentInstanceNumber=1,currentInstanceReleaseTick=0,currentInstanceDeadLineTick=0;
	public boolean isSchedulingSuccessful=true;
	public double hyperPeriod;
	public ArrayList<String[]> preEmptionList=new ArrayList<String[]>();
	public ArrayList<String[]> contextSwitchList=new ArrayList<String[]>();
	public ArrayList<Number> idleTicksList=new ArrayList<Number>();
	public String[] deadLineMissTick=new String[3];
	
	
	public Task(ArrayList<Object> taskList){
		finalTaskList=new ArrayList<Object>();
		releaseTimeList=new ArrayList<Number>();
		deadLineList=new ArrayList<Number>();
		timePeriodList=new ArrayList<Number>();
		taskName=taskList.get(0).toString();
		execTime=Integer.parseInt(taskList.get(1).toString());
		timePeriod=Integer.parseInt(taskList.get(2).toString());
		deadLine=Integer.parseInt(taskList.get(3).toString());
		priority=Integer.parseInt(taskList.get(4).toString());
		noOfInstances=Integer.parseInt(taskList.get(5).toString());
		
		
		setReleaseTimeList();
		setDeadLineList();
		setTimePeriodList();
	}
	
	public void setReleaseTimeList(){
		//System.out.println("entered in to setting release time list for task no:="+taskName);
		int initialReleaseTime=0;
		int releaseTime=0;
		for(int i=1;i<=noOfInstances+1;i++){
			releaseTime=initialReleaseTime+(i-1)*timePeriod;
			releaseTimeList.add(i-1, releaseTime);
			//System.out.println("releaseTmeList("+(i)+")"+releaseTimeList.get(i-1));
		}
	}
	public void setDeadLineList(){
		//System.out.println("entered in to setting Dead Line time list for task no:="+taskName);
		int initialReleaseTime=0;
		int deadLine1=0;
		int diff=timePeriod-deadLine;
		for(int i=1;i<=noOfInstances;i++){
			deadLine1=initialReleaseTime+(i)*timePeriod-diff;
			deadLineList.add(i-1, deadLine1);
			//System.out.println("DeadLineList("+(i)+")"+deadLineList.get(i-1));
		}
	}
	
	public void setTimePeriodList(){
		//System.out.println("entered in to setting time period list for task no:="+taskName);
		int initialReleaseTime=0;
		int timePeriod1=0;
		for(int i=1;i<=noOfInstances;i++){
			timePeriod1=initialReleaseTime+(i)*timePeriod;
			timePeriodList.add(i-1, timePeriod1);
			//System.out.println("timePeriodList("+(i)+")"+timePeriodList.get(i-1));
		}
	}
	
	public void setTaskSchedule(ArrayList<Task> taskList,int hyperPeriod1){
		//System.out.println("entered in to setting task execution time list for task no:="+taskName);
		hyperPeriod=hyperPeriod1;
		for(int i=0;i<=hyperPeriod;i++){
			finalTaskList.add(i,0);
			//usedTicksList.add(i,"free");
			//System.out.println("setting 0's initially tick no="+i+"=is="+finalTaskList.get(i));
		}
		int taskListSize=taskList.size();
		//System.out.println("taskListSize="+taskListSize);
		Task currentTask=(Task) taskList.get(taskListSize-1);
		//System.out.println("Printing current Task="+currentTask.taskName);
		for(int i=0;i<taskListSize;i++){
			Task tempTask=(Task) taskList.get(i);
			//System.out.println("Printing temp Task="+tempTask.taskName);
		}
		highPriorityTasksList=new ArrayList<Task>();
		for(int i=0;i<=taskListSize-2;i++){
			Task highPriorityTask=(Task) taskList.get(i);
			
			highPriorityTasksList.add(highPriorityTask);
			//System.out.println(currentTask.taskName+"has high priority tasks"+highPriorityTask.taskName);
		}
		noOfTicksNeeded=currentTask.execTime;
		currentInstanceNumber=1;
		noOfTicksRan=0;
		currentInstanceReleaseTick=Integer.parseInt(releaseTimeList.get(currentInstanceNumber-1).toString());
		currentInstanceDeadLineTick=Integer.parseInt(deadLineList.get(currentInstanceNumber-1).toString());
		//System.out.println("Starting to create schedule for task="+currentTask.taskName);
		boolean iscurrentInstanceComplete=false;
		for(int tick=0;tick<=hyperPeriod;tick++){			
			//checking higher priority task value at tick 
			
			boolean isTickFree=true;
			
			int hpValue=0;
			//System.out.println("currentInstanceNumber="+currentInstanceNumber);
			//int nextInstanceReleaseTick=currentInstanceReleaseTick;
			//if(currentInstanceNumber<currentTask.noOfInstances ){
				//nextInstanceReleaseTick=Integer.parseInt(releaseTimeList.get(currentInstanceNumber).toString());
			//}
			//System.out.println("next Instance Release tick="+nextInstanceReleaseTick);
			//System.out.println("current Instance Release tick="+currentInstanceNumber);
			//System.out.println("current tick number="+tick);
			//System.out.println("iscurrentInstanceComplete"+iscurrentInstanceComplete);
			if(tick==(currentInstanceDeadLineTick)){
				if(iscurrentInstanceComplete==true){
					//System.out.println("Current task instance is executed successfully");
					//System.out.println("currentInstanceNumber="+currentInstanceNumber+"total no of instances="+currentTask.noOfInstances);
					//System.out.println("currentInstanceReleaseTick="+currentInstanceReleaseTick+"currentInstanceDeadLineTick="+currentInstanceDeadLineTick+"tick="+tick);
					noOfTicksNeeded=currentTask.execTime;
					noOfTicksRan=0;
					iscurrentInstanceComplete=false;
					//System.out.println("Just now has set iscurrentInstanceComplete"+iscurrentInstanceComplete);
					if(currentInstanceNumber<currentTask.noOfInstances ){						
							currentInstanceNumber++;								
							currentInstanceReleaseTick=Integer.parseInt(releaseTimeList.get(currentInstanceNumber-1).toString());
							currentInstanceDeadLineTick=Integer.parseInt(deadLineList.get(currentInstanceNumber-1).toString());					
						
					}
					
				}
				else{
					//System.out.println("at next instance release time:looks like its a dead line miss");
					isSchedulingSuccessful=false;
					break;
				}
			}			
			if(!highPriorityTasksList.isEmpty()){				
			for(int hp=0;hp<highPriorityTasksList.size();hp++){
				Task highPriorityTask= highPriorityTasksList.get(hp);				
				hpValue+=Integer.parseInt(highPriorityTask.finalTaskList.get(tick).toString());
				//System.out.println("checking value for high priority task("+hp+"="+highPriorityTask.taskName+"="+hpValue);
				}
			}
			//System.out.println("isTickFree and noOfTicksNeeded="+isTickFree+"\t"+noOfTicksNeeded);
			//System.out.println("currentInstanceReleaseTick="+currentInstanceReleaseTick+"\t currentInstanceDeadLineTick="+currentInstanceDeadLineTick+"\t tick="+tick);
			//System.out.println("Final hpValue"+hpValue);
			
			if(hpValue == 0){
				isTickFree=true;
				//System.out.println("hpValue is 0 tick Number="+tick);
			}
			else if(hpValue==5)
			{
				//System.out.println("hpValue is 5 tick Number="+tick);
				isTickFree=false;
			}
			else{
				//System.out.println("Warning: Something wrong. please check");
			}
			
			
			if(isTickFree==true){
					//System.out.println("tick is free: not used by HP="+isTickFree);				
					if(noOfTicksNeeded>0)
						{
							//System.out.println("No of ticks needed is gt 0"+noOfTicksNeeded);
							if( tick >= currentInstanceReleaseTick  && tick< currentInstanceDeadLineTick)
								{
									//System.out.println("setting value 5");
									finalTaskList.set(tick, 5);
									noOfTicksNeeded--;
									noOfTicksRan++;
									//System.out.println("reduced no of ticke needed now:"+noOfTicksNeeded);
									if(noOfTicksNeeded==0){
										
										iscurrentInstanceComplete=true;
										//System.out.println("iscurrentInstanceComplete="+iscurrentInstanceComplete);
										}
								}	
							else{
								//System.out.println("warning: please check");
								//System.out.println("currentInstanceReleaseTick="+currentInstanceReleaseTick+"currentInstanceDeadLineTick="+currentInstanceDeadLineTick+"tick="+tick);
								
							}
						}
					else
						{	
						//System.out.println("No of ticks needed is lt 0"+noOfTicksNeeded);
							
							//System.out.println("currentInstanceNumber="+currentInstanceNumber+"\t total no of instances="+currentTask.noOfInstances);
													
						}
				}
			else{
				//System.out.println("tick is not free");
				continue;
			}
			
			
		}
		
		for(int i=0;i<hyperPeriod;i++){
			//System.out.println("i="+i+"\t"+Integer.parseInt(finalTaskList.get(i).toString()));
		}
		checkPreemtionAndContextSwitch();
		//checking pre-emtions and context switches
		
		}  //setTaskSchedule function ends here
	public void checkPreemtionAndContextSwitch(){
		int currentInstanceNumber=1,noOfTicksRan=0,NoOfTicksNeeded=0;
		double currentTaskValue,previousTaskValue;
		boolean iscurrentInstanceComplete=false;
		double currentInstanceReleaseTick,currentInstanceDeadLineTick;
		double preEmptionCount=0,contextSwitchCount=0;
		
		
		
		noOfTicksNeeded=execTime;
		noOfTicksRan=0;
		currentInstanceReleaseTick=Integer.parseInt(releaseTimeList.get(currentInstanceNumber-1).toString());
		currentInstanceDeadLineTick=Integer.parseInt(deadLineList.get(currentInstanceNumber-1).toString());
		
		for(int tick=0;tick<hyperPeriod;tick++){
			Task finalHpTask = null;
			String[] preEmtion=new String[4];   //each String stores currentTaskName,hpTaskName(preEmtingTaskName),tickNumber,preEmptionCount
			currentTaskValue=Integer.parseInt(finalTaskList.get(tick).toString());
			
			//checking hpTasks
			if(!highPriorityTasksList.isEmpty()){
			
			Task hpTask;
			double hpValue1 = 0;
			for(int hp=0;hp<highPriorityTasksList.size();hp++){
				hpTask=(Task)highPriorityTasksList.get(hp);
				hpValue1=Integer.parseInt(hpTask.finalTaskList.get(tick).toString());
				if(hpValue1==5){
					//System.out.println("Tick number=\t"+tick+"\t is used by"+hpTask.taskName);
					finalHpTask=hpTask;					
					break;
					}				
				}
			//System.out.println("hpValue1="+hpValue1);
			//System.out.println("currentTaskValue="+currentTaskValue);
			double tickValue=currentTaskValue+hpValue1;
			if(tickValue==0){
				//System.out.println("looks like tick= "+tick+" is idle for task="+taskName);
				idleTicksList.add(tick);
			}
			
			}
			else{
				//System.out.println("no High Priority tasks");
			}
			if(tick==currentInstanceDeadLineTick){
				if(iscurrentInstanceComplete){
					//System.out.println("Current Instance completed: incrementing instance number"+currentInstanceNumber);
					if(currentInstanceNumber<noOfInstances){
					currentInstanceNumber++;
					}
					noOfTicksNeeded=execTime;
					noOfTicksRan=0;
					iscurrentInstanceComplete=false;
					//System.out.println("currentInstanceNumber updated="+currentInstanceNumber);
				}
				else{
					//System.out.println("Current Instance Failed:DeadLine Miss"+taskName+":"+currentInstanceNumber);
					//System.out.println("deadLine Miss"+taskName+"tick="+tick+"instanceNumber="+currentInstanceNumber);
					deadLineMissTick[0]=taskName;
					deadLineMissTick[1]=Double.toString(currentInstanceNumber);
					deadLineMissTick[2]=Integer.toString(noOfTicksNeeded);					
					break;
				}
			}
			
			currentInstanceReleaseTick=Integer.parseInt(releaseTimeList.get(currentInstanceNumber-1).toString());
			currentInstanceDeadLineTick=Integer.parseInt(deadLineList.get(currentInstanceNumber-1).toString());
			
			
			//System.out.println("tick="+tick+"\t"+Integer.parseInt(finalTaskList.get(tick).toString()));
			
			if(tick==0){
				if(currentTaskValue==5){
					noOfTicksNeeded--;
					noOfTicksRan++;
					//iscurrentInstanceComplete=false;
					if(noOfTicksNeeded==0){
						iscurrentInstanceComplete=true;
						//System.out.println("tick Number+iscurrentInstanceComplete="+tick+":"+iscurrentInstanceComplete);
					}
					else{
						continue;
					}
				}
				else{
					if(finalHpTask!=null){
					//System.out.println(finalHpTask.taskName+"has value="+finalHpTask.finalTaskList.get(tick));
					}
					continue;
				}
				
			}
			else{
				previousTaskValue=Integer.parseInt(finalTaskList.get(tick-1).toString());
				//System.out.println("in else condition: PE and cs");
				//System.out.println("currentTaskValue="+currentTaskValue);
				//System.out.println("previousTaskValue="+previousTaskValue);
				//System.out.println("currentInstanceNumber="+currentInstanceNumber);
				//System.out.println("iscurrentInstanceComplete="+iscurrentInstanceComplete);
				
				if(previousTaskValue==5){    //55 and 50 situation : 55 :reduce ticks needed no pre-emption and cs  
											//50: if noOfTicksRan>0 then just increment pre-emption or if noofTicksRan=0 increment cs also
					if(currentTaskValue==5){
						//System.out.println("inside 55");
						//System.out.println(noOfTicksNeeded+"\t before \t"+noOfTicksRan);
						noOfTicksNeeded--;
						noOfTicksRan++;
						//iscurrentInstanceComplete=false;
						//System.out.println(noOfTicksNeeded+"\t after \t"+noOfTicksRan);
						if(noOfTicksNeeded==0){
							iscurrentInstanceComplete=true;
							//System.out.println("tick Number+iscurrentInstanceComplete="+tick+":"+iscurrentInstanceComplete);
						}
					}
					else{
						//System.out.println("inside 50");
						//System.out.println("noOfTicksRan="+noOfTicksRan+"\t noOfTicksNeeded="+noOfTicksNeeded);
						if(noOfTicksRan>0 & noOfTicksNeeded>0){	
							
							preEmptionCount++;
							contextSwitchCount++;
							//System.out.println("Pre-emption+context Switch at tick number="+tick+"taskName="+taskName+"currentInstanceNumber="+currentInstanceNumber);
							if(finalHpTask!=null){
								//System.out.println(finalHpTask.taskName+"has value="+finalHpTask.finalTaskList.get(tick));
								preEmtion[0]=taskName;
								preEmtion[1]=finalHpTask.taskName;
								preEmtion[2]=Integer.toString(tick);
								preEmtion[3]=Double.toString(preEmptionCount);
								preEmptionList.add(preEmtion);
								contextSwitchList.add(preEmtion);  
								}
						
						}
						
					}
				}
				else{                       //05 and 00 cases: 05 case--> this instance is executing now;increment noofTicksRan and reduce no of ticks needed
											//00 case: if noofTicksRan==0 --> task is released and waiting else increment pre-emption
					if(currentTaskValue==5){
						//System.out.println("inside 05");
						//System.out.println("noOfTicksNeeded="+noOfTicksNeeded+"\t before \t"+"noOfTicksRan="+noOfTicksRan);
						noOfTicksNeeded--;
						noOfTicksRan++;
						//iscurrentInstanceComplete=false;
						//System.out.println("noOfTicksNeeded="+noOfTicksNeeded+"\t after \t"+"noOfTicksRan="+noOfTicksRan);
						if(noOfTicksNeeded==0){
							//System.out.println("inside 05 complete?");
							iscurrentInstanceComplete=true;
							//System.out.println("tick Number+iscurrentInstanceComplete="+tick+":"+iscurrentInstanceComplete);
						}
					}
					else{
						//System.out.println("inside 00");
						if(noOfTicksRan>0 & noOfTicksNeeded>0){
							preEmptionCount++;
							//contextSwitchCount++;
							//System.out.println("Pre-emption at tick number="+tick+"taskName="+taskName+"currentInstanceNumber="+currentInstanceNumber);
							if(finalHpTask!=null){
								//System.out.println(finalHpTask.taskName+"has value="+finalHpTask.finalTaskList.get(tick));
								preEmtion[0]=taskName;
								preEmtion[1]=finalHpTask.taskName;
								preEmtion[2]=Integer.toString(tick);
								preEmtion[3]=Double.toString(preEmptionCount);
								preEmptionList.add(preEmtion);
								}
							
						}
						else if(noOfTicksRan>0 & noOfTicksNeeded==0 ){
							continue;
						}
					}
				}
				
				
				
			}
			
		}		
		//System.out.println("Pre emption count and context switch count="+preEmptionCount+"\t"+contextSwitchCount);
		if(!preEmptionList.isEmpty()){
			//System.out.println("There are some pre-emtions for this task");
			for(int i=0;i<preEmptionList.size();i++){
				String[] temp1=preEmptionList.get(i);
				//System.out.println("current Task="+temp1[0]+"is pre-empted by="+temp1[1]+"at tick Number="+temp1[2]+"with preEmptonCount="+temp1[3]);
			}
		}
		if(!contextSwitchList.isEmpty()){
			//System.out.println("There are some context Switches for this task");
			for(int i=0;i<contextSwitchList.size();i++){
				String[] temp1=contextSwitchList.get(i);
				//System.out.println("current Task="+temp1[0]+"is pre-empted by="+temp1[1]+"at tick Number="+temp1[2]+"with context Switch Count="+temp1[3]+"resulting in contextSwitch");
			}
		}
		if(!idleTicksList.isEmpty()){
			//System.out.println("There are some idle ticks for this task");
			for(int i=0;i<idleTicksList.size();i++){
				int temp1=Integer.parseInt(idleTicksList.get(i).toString());
				//System.out.println("current Task="+taskName+"=idle tick="+temp1);
			}
		}
	}//context function ends 
		
	
	
}//Task class ends here