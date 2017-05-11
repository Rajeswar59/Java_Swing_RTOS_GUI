package mygui;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

public class EDFScheduling {
	public ArrayList<EDFTask> tasksList,releasedTasksList; //tasksList contains objects of all tasks. releasedTasksList contains 
	public int hyperPeriod;
	EDFTask tempEDFTask=null;
	String tempTaskName=null;
	double tempExecTime=0,tempTimePeriod=0,tempDeadLine=0,tempNoOfInstances=0;	
	public MainFrame2 mainFrame2Obj;
	int[] currentInstance;
	String[] deadLineMissDetails={null,null,null,null};   //taskName,instanceNumber,deadLineMissTick,noOfTicsNeeded
	ArrayList<Number> idleTicksList;
	
	public int getHyperPeriod() {
		return hyperPeriod;
	}

	public void setHyperPeriod(int hyperPeriod) {
		this.hyperPeriod = hyperPeriod;
	}

	public ArrayList<EDFTask> getTasksList() {
		return tasksList;
	}

	public void setTasksList(ArrayList<EDFTask> tasksList) {
		this.tasksList = tasksList;
	}

	//Constructor
	public EDFScheduling(DefaultTableModel tableModel,int hyperPeriod1){
		this.setHyperPeriod(hyperPeriod1);
		tasksList=new ArrayList<EDFTask>();
		releasedTasksList=new ArrayList<EDFTask>();
		int rowCount=tableModel.getRowCount();
		//int columnCount=tableModel.getColumnCount();
		String tempTaskName=null;
		double tempExecTime=0,tempTimePeriod=0,tempDeadLine=0,tempNoOfInstances=0;	
		idleTicksList=new ArrayList<Number>();
		
		mainFrame2Obj=new MainFrame2();
		
			for(int i=0;i<rowCount;i++){
				tempEDFTask=new EDFTask();
				
				tempTaskName=tableModel.getValueAt(i, 0).toString();
				tempExecTime=Integer.parseInt(tableModel.getValueAt(i, 1).toString());
				tempTimePeriod=Integer.parseInt(tableModel.getValueAt(i, 2).toString());
				tempDeadLine=Integer.parseInt(tableModel.getValueAt(i, 3).toString());
				tempNoOfInstances=Integer.parseInt(tableModel.getValueAt(i, 4).toString());
				
				tempEDFTask.setTaskName(tempTaskName);
				tempEDFTask.setExecTime(tempExecTime);
				tempEDFTask.setTimePeriod(tempTimePeriod);
				tempEDFTask.setDeadLine(tempDeadLine);
				tempEDFTask.setNoOfInstances(tempNoOfInstances);
				tempEDFTask.setHyperPeriod(hyperPeriod1);
				tasksList.add(tempEDFTask);	
				
				
			}
		
			//testing: printing
			System.out.println("Inside EDFScheduling-tasksList creation done");
			for(int i=0;i<tasksList.size();i++){
				tempEDFTask=(EDFTask)tasksList.get(i);
				tempTaskName=tempEDFTask.getTaskName();
				tempExecTime=tempEDFTask.getExecTime();
				tempTimePeriod=tempEDFTask.getTimePeriod();
				tempDeadLine=tempEDFTask.getDeadLine();
				tempNoOfInstances=tempEDFTask.getNoOfInstances();
			
				System.out.println("tempTaskName="+tempTaskName);
				System.out.println("tempExecTime="+tempExecTime);
				System.out.println("tempTimePeriod="+tempTimePeriod);
				System.out.println("tempDeadLine="+tempDeadLine);
				System.out.println("tempNoOfInstances="+tempNoOfInstances);
			
				System.out.println( "\n");			
			}
			setReleaseTimeList();
			setTimePeriodList();
			setDeadLineList();
			printLists();	
			createTaskInstances();					
		
		}//constructor ending
	public void setReleaseTimeList(){
		double initialReleaseTick=0,currentInstanceReleaseTick=0;			
		for(int i=0;i<tasksList.size();i++){
			ArrayList<Number> tempReleaseTimeList=new ArrayList<Number>();
			tempEDFTask=(EDFTask)tasksList.get(i);
			tempTimePeriod=tempEDFTask.getTimePeriod();
			tempNoOfInstances=tempEDFTask.getNoOfInstances();
			for(int instanceNo=0;instanceNo<=tempNoOfInstances;instanceNo++){
				currentInstanceReleaseTick=initialReleaseTick+instanceNo*tempTimePeriod;
				tempReleaseTimeList.add(currentInstanceReleaseTick);
			}
			tempEDFTask.setReleaseTimeList(tempReleaseTimeList);			
		}
	}
	public void setDeadLineList(){
		double initialReleaseTick=0,currentInstanceDeadLineTick=0;			
		double diff=0;
		for(int i=0;i<tasksList.size();i++){
			ArrayList<Number> tempDeadLineList=new ArrayList<Number>();
			tempEDFTask=(EDFTask)tasksList.get(i);
			tempTimePeriod=tempEDFTask.getTimePeriod();
			tempDeadLine=tempEDFTask.getDeadLine();
			diff=tempTimePeriod-tempDeadLine;
			tempNoOfInstances=tasksList.get(i).getNoOfInstances();
			for(int instanceNo=1;instanceNo<=tempNoOfInstances;instanceNo++){
				currentInstanceDeadLineTick=initialReleaseTick+instanceNo*tempTimePeriod-diff;
				tempDeadLineList.add(currentInstanceDeadLineTick);
			}
			tempEDFTask.setDeadLineList(tempDeadLineList);			
		}
	}
	public void setTimePeriodList(){
		double initialReleaseTick=0,currentInstanceReleaseTick=0;			
		for(int i=0;i<tasksList.size();i++){
			ArrayList<Number> tempTimePeriodList=new ArrayList<Number>();
			tempEDFTask=(EDFTask)tasksList.get(i);
			tempTimePeriod=tempEDFTask.getTimePeriod();
			tempNoOfInstances=tempEDFTask.getNoOfInstances();
			for(int instanceNo=1;instanceNo<=tempNoOfInstances;instanceNo++){
				currentInstanceReleaseTick=initialReleaseTick+instanceNo*tempTimePeriod;
				tempTimePeriodList.add(currentInstanceReleaseTick);
			}
			tempEDFTask.setTimePeriodList(tempTimePeriodList);			
		}
	}
	public void printLists(){
		ArrayList<Number> tempReleaseTimeList,tempTimePeriodList,tempDeadLineList;
		for(int i=0;i<tasksList.size();i++){	
			
			tempReleaseTimeList=tasksList.get(i).getReleaseTimeList();
			tempTimePeriodList=tasksList.get(i).getTimePeriodList();
			tempDeadLineList=tasksList.get(i).getDeadLineList();
			tempTaskName=tasksList.get(i).getTaskName();
			
			System.out.println("printing release times for task="+tempTaskName);
			double tempReleaseTime=0;
			String releaseTicksMessage=" ";
			for(int j=0;j<tempReleaseTimeList.size();j++){
				tempReleaseTime=(double) tempReleaseTimeList.get(j);
				releaseTicksMessage+=tempReleaseTime+"  ";
			}
			System.out.println(releaseTicksMessage);
			releaseTicksMessage=null;
			
			System.out.println("printing Time-periods for task="+tempTaskName);
			String timePeriodTicksMessage=" ";
			for(int j=0;j<tempTimePeriodList.size();j++){
				tempTimePeriod=(double) tempTimePeriodList.get(j);
				timePeriodTicksMessage+=tempTimePeriod+"  ";
			}
			System.out.println(timePeriodTicksMessage);
			
			System.out.println("printing Dead Lines for task="+tempTaskName);
			String deadLineTicksMessage=" ";
			for(int j=0;j<tempDeadLineList.size();j++){
				tempDeadLine=(double) tempDeadLineList.get(j);
				deadLineTicksMessage+=tempDeadLine+"  ";
			}
			System.out.println(deadLineTicksMessage);
			
			
		}
	}
	public JPanel processorDemandAnalysis(){
		ArrayList<Double> deadLineMatrix=new ArrayList<Double>();
		JLabel pdaResultLabel=new JLabel("Processor Demand Analysis Result");
		JTextArea pdaResultTextArea=new JTextArea(1,25);
		JPanel pdaResultPanel=new JPanel();
		
		for(int i=0;i<tasksList.size();i++){
			ArrayList<Number> tempDeadLineList=tasksList.get(i).getDeadLineList();			
			tempTaskName=tasksList.get(i).getTaskName();
			System.out.println("adding deadLines of task to deadLineMatrix="+tempTaskName);
			for(int j=0;j<tempDeadLineList.size();j++){
				tempDeadLine=(double) tempDeadLineList.get(j);
				deadLineMatrix.add(tempDeadLine);
			}			
		}
		System.out.println("Before Sorting deadLineMatrix");
		for(int i=0;i<deadLineMatrix.size();i++){
			System.out.print(deadLineMatrix.get(i)+" ");
		}
		Collections.sort(deadLineMatrix);
		System.out.println("\n After Sorting deadLineMatrix");
		for(int i=0;i<deadLineMatrix.size();i++){
			System.out.print(deadLineMatrix.get(i)+" ");
		}
		//removing duplicates
		for(int i=0;i<deadLineMatrix.size();i++){
			for(int j=i+1;j<deadLineMatrix.size();j++){
				if(deadLineMatrix.get(i).equals(deadLineMatrix.get(j))){
					deadLineMatrix.remove(j);
					j--;
				}
			}
		}
		System.out.println("\n After Removing duplicates from deadLineMatrix");
		for(int i=0;i<deadLineMatrix.size();i++){
			System.out.print(deadLineMatrix.get(i)+" ");
		}
		//now we have a deadLineMatrix ready. So we will start PDA : L>=sum(i=1 to noOfTasks)(floor((L-Di)/Ti)+1)*ci
		double l=0,templ = 0;
		for(int i=1;i<deadLineMatrix.size();i++){
			templ=0;
			l=deadLineMatrix.get(i);
			int noOfTasks=tasksList.size();
			for(int taskNumber=0;taskNumber<noOfTasks;taskNumber++){
				tempDeadLine=tasksList.get(taskNumber).getDeadLine();
				tempTimePeriod=tasksList.get(taskNumber).getTimePeriod();
				tempExecTime=tasksList.get(taskNumber).getExecTime();
				templ+=((java.lang.Math.floor((l-tempDeadLine)/tempTimePeriod))+1)*tempExecTime;					
			}	
			System.out.println("L="+l+"and templ="+templ);
			if(l>=templ){				
				System.out.println("Successfull till L="+l);
				continue;
			}
			else{
				//System.out.println("This Task is Not schedulable according to Processor Demand Analysis");
				System.out.println("deadLine Miss at L="+l);
				break;
			}
		}	
		if(l==deadLineMatrix.get(deadLineMatrix.size()-1)&l>=templ){
			System.out.println("This Task set is schedulable according to Processor Demand Analysis ");
			pdaResultTextArea.append("This Task set is schedulable according to Processor Demand Analysis");
		}
		else if(l<deadLineMatrix.get(deadLineMatrix.size()-1) & l<templ){
			System.out.println("This Task is Not schedulable according to Processor Demand Analysis+deadLine Miss at L="+l);
			pdaResultTextArea.append("This Task is Not schedulable according to Processor Demand Analysis \n -deadLine Miss at L="+l);
		}
		
		//adding result to the Panel
		
		pdaResultPanel.add(pdaResultLabel);
		pdaResultPanel.add(pdaResultTextArea);
		pdaResultPanel.setLayout(new BoxLayout(pdaResultPanel, BoxLayout.Y_AXIS));
		return pdaResultPanel;
	}
	
	public void createTaskInstances(){
		String instanceIdentifier=null;
		int taskInstanceNumber, taskInstanceReleaseTick,taskInstanceDeadLineTick; 
		int taskInstanceNoOfTicksNeeded;
		ArrayList<Number> tempReleaseTimeList,tempDeadLineList;
					
		for(int i=0;i<tasksList.size();i++){
			tempEDFTask=(EDFTask)tasksList.get(i);	
			EDFTaskInstance tempEDFTaskInstance;
			ArrayList<EDFTaskInstance> taskInstanceList=new ArrayList<EDFTaskInstance>();
			
			tempReleaseTimeList=tempEDFTask.getReleaseTimeList();
			//tempTimePeriodList=tempEDFTask.getTimePeriodList();
			tempDeadLineList=tempEDFTask.getDeadLineList();
		
			
			tempTaskName=tasksList.get(i).getTaskName();
			tempNoOfInstances=tasksList.get(i).getNoOfInstances();	
			tempExecTime=tasksList.get(i).getExecTime();
			taskInstanceNoOfTicksNeeded= (int) tempExecTime;
			
			for(int instance=1;instance<=tempNoOfInstances;instance++){
				tempEDFTaskInstance=new EDFTaskInstance();
				instanceIdentifier=tempTaskName+"-Instance"+instance;
				taskInstanceNumber=instance;
				taskInstanceReleaseTick= tempReleaseTimeList.get(instance-1).intValue();
				taskInstanceDeadLineTick= tempDeadLineList.get(instance-1).intValue();
				
				tempEDFTaskInstance.setInstanceIdentifier(instanceIdentifier);
				tempEDFTaskInstance.setTaskInstanceNumber(taskInstanceNumber);
				tempEDFTaskInstance.setTaskInstanceReleaseTick(taskInstanceReleaseTick);
				tempEDFTaskInstance.setTaskInstanceDeadLineTick(taskInstanceDeadLineTick);	
				tempEDFTaskInstance.setTaskInstanceNoOfTicksNeeded(taskInstanceNoOfTicksNeeded);
				
				System.out.println("instanceIdentifier="+tempEDFTaskInstance.getInstanceIdentifier());
				System.out.println("taskInstanceNumber="+tempEDFTaskInstance.getTaskInstanceNumber());
				System.out.println("taskInstanceReleaseTick="+tempEDFTaskInstance.getTaskInstanceReleaseTick());
				System.out.println("taskInstanceDeadLineTick="+tempEDFTaskInstance.getTaskInstanceDeadLineTick());
				System.out.println("taskInstanceNoOfTicksNeeded="+tempEDFTaskInstance.getTaskInstanceNoOfTicksNeeded()+"\n");
				taskInstanceList.add(tempEDFTaskInstance);
			}	
			tempEDFTask.setTaskInstanceList(taskInstanceList);
		}
		
		
	}
	public void EDFSchedulingFunction(){
		releasedTasksList=new ArrayList<EDFTask>();
		EDFTaskInstance	tempEDFTaskInstance;
		currentInstance=new int[tasksList.size()];
		for(int i=0;i<tasksList.size();i++){
			currentInstance[i]=1;
		}
		
		System.out.println("\n Inside EDFSchedulingFunction ");
		
		outerloop:
		for(int tick=0;tick<hyperPeriod;tick++){
			
			//find released and uncompleted tasks at each tick
			System.out.println("\n checking if tick=release time for any task at tick="+tick);
			for(int task=0;task<tasksList.size();task++){
				
				
				double temp=(double)tick;				
				tempEDFTask=(EDFTask)tasksList.get(task);
				
				//just set 0's for final sequence list
				tempEDFTask.getFinalTaskSequenceList().add(tick,0);
				
				
				System.out.println("checking if tick=release time for taskName="+tempEDFTask.getTaskName());				
				System.out.println("double temp="+temp+"\t normal tick="+tick);

				System.out.println("instance Number="+currentInstance[task]);
				if(currentInstance[task]<=tasksList.get(task).getNoOfInstances()){
					
					if(tempEDFTask.getDeadLineList().get(currentInstance[task]-1).intValue()==tick ){
						System.out.println("inside tempEDFTask.getDeadLineList().get(currentInstance[task]-1).intValue()"+tempEDFTask.getDeadLineList().get(currentInstance[task]-1).intValue()+"="+tick);
						if(tempEDFTask.getTaskInstanceList().get(currentInstance[task]-1).isCurrentInstanceComplete ==true){
							System.out.println("removing Task="+tempEDFTask.getTaskName()+"before instance Number="+currentInstance[task]);
							if(releasedTasksList.contains(tempEDFTask)){
							releasedTasksList.remove(tempEDFTask);
							System.out.println("Removed task from releasedTasksList="+tempEDFTask.getTaskName());
							}
							else{
								System.out.println("chances are that the task has already been removed");
							}
							currentInstance[task]++;
							System.out.println("updated instance number="+currentInstance[task]);
						}
						else{
							System.out.println("at dead line: Task="+tempEDFTask.getTaskName()+"before instance Number="+currentInstance[task]);
							System.out.println("but looks like dead line miss-please check.");
							tempEDFTask.getTaskInstanceList().get(currentInstance[task]-1).setCurrentInstanceMissedDeadLine(true);
							
							System.out.println("just set isCurrentInstanceMissedDeadLine="+tempEDFTask.getTaskInstanceList().get(currentInstance[task]).isCurrentInstanceMissedDeadLine);
							deadLineMissDetails[0]=tempEDFTask.getTaskName();
							deadLineMissDetails[1]=Integer.toString(currentInstance[task]);
							//deadLineMissDetails[2]=Integer.toString(tempEDFTask.getTaskInstanceList().get(currentInstance[task]).getTaskInstanceLastExecutedTick());
							deadLineMissDetails[2]=Integer.toString(tick);
							deadLineMissDetails[3]=Integer.toString(tempEDFTask.getTaskInstanceList().get(currentInstance[task]-1).getTaskInstanceNoOfTicksNeeded());
							
							
							break outerloop;
						}
					}
					else{
						System.out.println("inside tempEDFTask.getDeadLineList().get(currentInstance[task]-1).intValue()"+tempEDFTask.getDeadLineList().get(currentInstance[task]-1).intValue()+"!="+tick);
						System.out.println("not removed Task="+tempEDFTask.getTaskName());
					}
					
					System.out.println("release tick value="+tempEDFTask.getReleaseTimeList().get(currentInstance[task]-1).intValue());
					if(tempEDFTask.getReleaseTimeList().get(currentInstance[task]-1).intValue()==tick){
						System.out.println("added Task="+tempEDFTask.getTaskName()+"instance Number="+currentInstance[task]);
						releasedTasksList.add(tempEDFTask);
					}
					else{
						System.out.println("not added Task="+tempEDFTask.getTaskName()+"instance Number="+currentInstance[task]);
					}	
					
					
				}
			}				
			if(!releasedTasksList.isEmpty()){
				System.out.println("Released tasks currently in queue at tick after checking all tasks= "+tick);
				for(int i=0;i<releasedTasksList.size();i++){
					System.out.print(releasedTasksList.get(i).getTaskName()+"  ");
				}
				System.out.println(" ");
			}
			else{
				System.out.println("ReleasedTaskslist is empty");
			}
			
			//now that releasedTasksList is ready we have to check which task has early dead line and if same deadline then check which task is waiting from long time
			if(!releasedTasksList.isEmpty()){
				int deadLine1=0,deadLine2=0;
				int currentInstanceNumber=0,currentInstanceNumber2=0;
				System.out.println("Released tasks currently in queue at tick= "+tick);
				for(int i=0;i<releasedTasksList.size();i++){
					int temp=Integer.parseInt(releasedTasksList.get(i).getTaskName().substring(4));  //extracting task number from task name
					System.out.println("temp from string="+temp);
					currentInstanceNumber=currentInstance[temp-1];
					deadLine2= releasedTasksList.get(i).getDeadLineList().get(currentInstanceNumber-1).intValue();
					if(i==0){
						tempEDFTask=releasedTasksList.get(i);    // deadLine2--> 
						deadLine1=deadLine2;
						currentInstanceNumber2=currentInstanceNumber;
						System.out.println("i="+i+",deadLine="+deadLine1+",taskName"+releasedTasksList.get(i).getTaskName()+",instanceNumber"+currentInstanceNumber);
					}
					else{
						if(deadLine2<deadLine1){
							System.out.println("deadline2="+deadLine2+" deadLine1="+deadLine1);
							System.out.println(releasedTasksList.get(i).getTaskName()+"has lower deadLine than "+tempEDFTask.getTaskName()+"replacing it now");
							tempEDFTask=releasedTasksList.get(i);
							deadLine1=deadLine2;
						}
						else if(deadLine2==deadLine1){
							System.out.println("deadline2="+deadLine2+" deadLine1="+deadLine1);
							System.out.println(releasedTasksList.get(i).getTaskName()+"has same deadLine as "+tempEDFTask.getTaskName()+"we have to check which one was waiting for long time");
							//according to our setting. releasedTasksList has order as older--> newer. so no need to swap the tasks. let the long waiting task run.
							
						}
						else{
							System.out.println("deadline2="+deadLine2+" deadLine1="+deadLine1);
							System.out.println(releasedTasksList.get(i).getTaskName()+"has higher deadLine than "+tempEDFTask.getTaskName()+"continue");
						}
					}					
				}
				System.out.println("task="+tempEDFTask.getTaskName()+" has lowest priority at tick="+tick+"\n");
				
				int temp=Integer.parseInt(tempEDFTask.getTaskName().substring(4));  //extracting task number from task name
				System.out.println("temp from string="+temp);
				currentInstanceNumber=currentInstance[temp-1];
				System.out.println("currentInstanceNumber="+currentInstanceNumber);
				
				System.out.println("no of instances 1="+tempEDFTask.getTaskInstanceList().size());
				//System.out.println("no of instances 2="+tasksList.get(temp-1).getTaskInstanceList().size());
				System.out.println("checking if we can schedule task taskName="+tempEDFTask.getTaskName());
				
				tempEDFTaskInstance	=tempEDFTask.getTaskInstanceList().get(currentInstanceNumber-1);
				System.out.println("tempEDFTaskInstance"+tempEDFTaskInstance.getInstanceIdentifier());
				System.out.println("TaskInstanceNoOfTicksRan="+tempEDFTaskInstance.getTaskInstanceNoOfTicksRan());
				
				if(tempEDFTaskInstance.getTaskInstanceNoOfTicksRan()==0){
				tempEDFTaskInstance.setTaskInstanceExecutionStartTick(tick);
				tempEDFTaskInstance.setTaskInstanceLastExecutedTick(tick);
				System.out.println("taskName="+tempEDFTask.getTaskName());
				System.out.println("Instance Number="+tempEDFTaskInstance.getTaskInstanceNumber());
				System.out.println("TaskInstanceNoOfTicksRan==0 so just set setTaskInstanceExecutionStartTick="+tempEDFTaskInstance.getTaskInstanceExecutionStartTick());
				}
				else{
					System.out.println("getTaskInstanceNoOfTicksRan!=0 so just continue");
				}
				if(tempEDFTaskInstance.isCurrentInstanceComplete==false){
				tempEDFTask.getFinalTaskSequenceList().set(tick, 5);
				tempEDFTaskInstance.setTaskInstanceLastExecutedTick(tick);
				tempEDFTaskInstance.setTaskInstanceNoOfTicksNeeded((tempEDFTaskInstance.getTaskInstanceNoOfTicksNeeded()-1));
				tempEDFTaskInstance.setTaskInstanceNoOfTicksRan((tempEDFTaskInstance.getTaskInstanceNoOfTicksRan()+1));
				System.out.println("isCurrentInstanceComplete is false: so setting value 5");
				System.out.println("value set at tick="+tempEDFTask.getFinalTaskSequenceList().set(tick, 5)+" tick="+tick);
				System.out.println("Start tick set="+tempEDFTaskInstance.getTaskInstanceExecutionStartTick());
				System.out.println("setTaskInstanceNoOfTicksNeeded="+tempEDFTaskInstance.getTaskInstanceNoOfTicksNeeded());
				System.out.println("setTaskInstanceNoOfTicksRan="+tempEDFTaskInstance.getTaskInstanceNoOfTicksRan());				
				}
				else{
					System.out.println("isCurrentInstanceComplete=true");
				}
				
				
				if(tempEDFTaskInstance.getTaskInstanceNoOfTicksNeeded()==0){
					tempEDFTaskInstance.setCurrentInstanceComplete(true);
					tempEDFTaskInstance.setTaskInstanceCompleteTick(tick);
					releasedTasksList.remove(tempEDFTask);
					
					System.out.println("inside tempEDFTaskInstance.getTaskInstanceNoOfTicksNeeded()==0");
					System.out.println("setCurrentInstanceComplete="+tempEDFTaskInstance.isCurrentInstanceComplete);
					System.out.println("setTaskInstanceCompleteTick="+tempEDFTaskInstance.getTaskInstanceCompleteTick());
					System.out.println("removing Task="+tempEDFTask.getTaskName()+"instance Number="+currentInstanceNumber);
				}
				else {
					
					System.out.println("no of ticks needed>0 continue");
//						tempEDFTaskInstance.getTaskInstancePreEmptionTicks().add(tick);
//						tempEDFTaskInstance.getTaskInstanceContextSwitchTicks().add(tick);
//					
//					System.out.println("setting pre-emption and context switch lists");
//					System.out.println("added tempEDFTaskInstance.getTaskInstancePreEmptionTicks="+tick);
//					System.out.println("added tempEDFTaskInstance.getTaskInstanceContextSwitchTicks="+tick);
					
				}
				
				for(int i=0;i<releasedTasksList.size() &tick>0;i++){
					temp=Integer.parseInt(releasedTasksList.get(i).getTaskName().substring(4));  //extracting task number from task name
					System.out.println("temp from string="+temp);
					currentInstanceNumber=currentInstance[temp-1];
					System.out.println("currentInstance[temp-1]"+currentInstance[temp-1]);
					System.out.println("releasedTasksList.get(i).getTaskName()="+releasedTasksList.get(i).getTaskName());
					System.out.println("tempEDFTask.getTaskName()="+tempEDFTask.getTaskName());
					if(releasedTasksList.get(i).getTaskName()!=tempEDFTask.getTaskName()){
						tempEDFTaskInstance=releasedTasksList.get(i).getTaskInstanceList().get(currentInstanceNumber-1);
						System.out.println("instance identifier="+tempEDFTaskInstance.getInstanceIdentifier());
						if(tempEDFTaskInstance.isCurrentInstanceComplete==false & releasedTasksList.get(i).getFinalTaskSequenceList().get(tick).intValue()==0 ){
							if(tempEDFTaskInstance.getTaskInstanceNoOfTicksRan()>0 & releasedTasksList.get(i).getFinalTaskSequenceList().get(tick-1).intValue()==5 ){
								tempEDFTaskInstance.getTaskInstancePreEmptionTicks().add(tick);
								tempEDFTaskInstance.getTaskInstanceContextSwitchTicks().add(tick);
								System.out.println("setting pre-emption and context switch lists");
								System.out.println("added tempEDFTaskInstance.getTaskInstancePreEmptionTicks="+tick);
								System.out.println("added tempEDFTaskInstance.getTaskInstanceContextSwitchTicks="+tick);
								
							}
							else if(tempEDFTaskInstance.getTaskInstanceNoOfTicksRan()>0){
								tempEDFTaskInstance.getTaskInstancePreEmptionTicks().add(tick);
								//tempEDFTaskInstance.getTaskInstanceContextSwitchTicks().add(tick);
								System.out.println("setting pre-emption list");
								System.out.println("added tempEDFTaskInstance.getTaskInstancePreEmptionTicks="+tick);
								//System.out.println("added tempEDFTaskInstance.getTaskInstanceContextSwitchTicks="+tick);
							}
						}
						
					}				
				}
				
				
			}
			else{
				System.out.println("ReleasedTaskslist is empty");
			}		
			
		}
		for(int i=0;i<tasksList.size();i++){
			System.out.println("printing final sequence for task="+tasksList.get(i).getTaskName());
			for(int j=0;j<tasksList.get(i).getFinalTaskSequenceList().size();j++){
				System.out.print(j+"-"+tasksList.get(i).getFinalTaskSequenceList().get(j)+" ");
			}
			System.out.println("\n");
			
			System.out.println("printing pre-emptions for task="+tasksList.get(i).getTaskName());				
				tempNoOfInstances=tasksList.get(i).getNoOfInstances();
				ArrayList<EDFTaskInstance> tempTaskInstanceList=tasksList.get(i).getTaskInstanceList();
				int countp=0,countc=0;
				System.out.println("pre-emptions at ticks=");
				for(int instance=1;instance<=tempNoOfInstances;instance++){
					ArrayList<Number> preEmptionTicksList=tempTaskInstanceList.get(instance-1).getTaskInstancePreEmptionTicks();
					
					if(!preEmptionTicksList.isEmpty()){
						
						for(int p=0;p<preEmptionTicksList.size();p++){
							System.out.print(" "+preEmptionTicksList.get(p));
							countp++;
						}						
					}
					else{
						System.out.println("no pre-emptions for this instance="+instance);
					}
				}
				System.out.println("\n context switches at ticks=");
				for(int instance=1;instance<=tempNoOfInstances;instance++){					
					ArrayList<Number> contextSwitchTicksList=tempTaskInstanceList.get(instance-1).getTaskInstanceContextSwitchTicks();	
					if(!contextSwitchTicksList.isEmpty()){
						
						for(int p=0;p<contextSwitchTicksList.size();p++){
							System.out.print(" "+contextSwitchTicksList.get(p));
							countc++;
						}	
						
					}
					
					else{
					System.out.println("no context switchesfor this instance="+instance);
					}
					
				}
				System.out.print("\n");
				
				if(countp==0){
					System.out.println("No pre-emptions for task="+tasksList.get(i).getTaskName());
				}
				else{
					System.out.println("Task="+tasksList.get(i).getTaskName()+" has "+countp+" pre-emptions");
				}
				if(countc==0){
					System.out.println("No context-switches for task="+tasksList.get(i).getTaskName());
				}
				else{
					System.out.println("Task="+tasksList.get(i).getTaskName()+" has "+countc+" context switches");
				}
			System.out.println("\n");		
			
			if(deadLineMissDetails[0]!=null){
				System.out.println("There is a deadLine Miss");
				System.out.println("taskName="+deadLineMissDetails[0]);
				System.out.println("instanceNumber="+deadLineMissDetails[1]);
				System.out.println("DeadLineMissTick="+deadLineMissDetails[2]);
				System.out.println("NoOfticks needed="+deadLineMissDetails[3]);
			}
			
		}
		
		//idle ticks calculation
		System.out.println("idle ticks calculation=deadLineMissDetails[0]="+deadLineMissDetails[0]+"deadLine Miss At tick="+deadLineMissDetails[2]);
		
		int tempx=0;
		if(deadLineMissDetails[0]==null){
			tempx=hyperPeriod;
		}
		else{
			tempx=java.lang.Math.min(hyperPeriod,Integer.parseInt(deadLineMissDetails[2]));
		}
		System.out.println("tempx="+tempx);
		
		for(int tick=0;tick<tempx ;tick++){
			boolean isIdleTick=false;
			for(int task=0;task<tasksList.size();task++){
				if(tasksList.get(task).getFinalTaskSequenceList().get(tick).intValue()==5){
					continue;
				}
				else{
					isIdleTick=true;
				}
			}
			if(isIdleTick==false){
				idleTicksList.add(tick);
				System.out.println("tick="+tick+" is a idle tick");
			}			
		}
		if(!idleTicksList.isEmpty()){
			System.out.println("idle Ticks are=");
			for(int tick=0;tick<idleTicksList.size();tick++){
				System.out.print(" "+tick);
			}
			System.out.println("\n");
		}
		
		
	}//edfscheduling function ends here	
	
} //EDFScheduling class ending

class EDFTask {
	public String taskName=null;
	public double deadLine=0,timePeriod=0,execTime=0,noOfInstances=0,priority=0,hyperPeriod=0;
	//public ArrayList<EDFTask> finalTaskList,releasedTasksList,highPriorityTasksList;
	public ArrayList<Number> releaseTimeList,deadLineList,timePeriodList,finalTaskSequenceList;
	//public ArrayList<Number> tempDeadLineMatrixList;
	//public ArrayList<ArrayList> deadLineMatrixList;
	public ArrayList<EDFTaskInstance> taskInstanceList;
	public boolean isTaskSchedulablePDA;
	
	
	public ArrayList<Number> getFinalTaskSequenceList() {
		return finalTaskSequenceList;
	}
	public void setFinalTaskSequenceList(ArrayList<Number> finalTaskSequenceList) {
		this.finalTaskSequenceList = finalTaskSequenceList;
	}
	
	public boolean isTaskSchedulablePDA() {
		return isTaskSchedulablePDA;
	}
	public void setTaskSchedulablePDA(boolean isTaskSchedulablePDA) {
		this.isTaskSchedulablePDA = isTaskSchedulablePDA;
	}
	public String getTaskName() {
		return taskName;
	}
	public double getDeadLine() {
		return deadLine;
	}
	public double getTimePeriod() {
		return timePeriod;
	}
	public double getExecTime() {
		return execTime;
	}
	public double getNoOfInstances() {
		return noOfInstances;
	}
	public double getPriority() {
		return priority;
	}
	public double getHyperPeriod() {
		return hyperPeriod;
	}
//	public ArrayList<EDFTask> getFinalTaskList() {
//		return finalTaskList;
//	}
//	public ArrayList<EDFTask> getReleasedTasksList() {
//		return releasedTasksList;
//	}
//	public ArrayList<EDFTask> getHighPriorityTasksList() {
//		return highPriorityTasksList;
//	}
	public ArrayList<Number> getReleaseTimeList() {
		return releaseTimeList;
	}
	public ArrayList<Number> getDeadLineList() {
		return deadLineList;
	}
	public ArrayList<Number> getTimePeriodList() {
		return timePeriodList;
	}
//	public ArrayList<Number> getTempDeadLineMatrixList() {
//		return tempDeadLineMatrixList;
//	}
//	public ArrayList<ArrayList> getDeadLineMatrixList() {
//		return deadLineMatrixList;
//	}
	public ArrayList<EDFTaskInstance> getTaskInstanceList() {
		return taskInstanceList;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public void setDeadLine(double deadLine) {
		this.deadLine = deadLine;
	}
	public void setTimePeriod(double timePeriod) {
		this.timePeriod = timePeriod;
	}
	public void setExecTime(double execTime) {
		this.execTime = execTime;
	}
	public void setNoOfInstances(double noOfInstances) {
		this.noOfInstances = noOfInstances;
	}
	public void setPriority(double priority) {
		this.priority = priority;
	}
	public void setHyperPeriod(double hyperPeriod) {
		this.hyperPeriod = hyperPeriod;
	}
//	public void setFinalTaskList(ArrayList<EDFTask> finalTaskList) {
//		this.finalTaskList = finalTaskList;
//	}
//	public void setReleasedTasksList(ArrayList<EDFTask> releasedTasksList) {
//		this.releasedTasksList = releasedTasksList;
//	}
//	public void setHighPriorityTasksList(ArrayList<EDFTask> highPriorityTasksList) {
//		this.highPriorityTasksList = highPriorityTasksList;
//	}
	public void setReleaseTimeList(ArrayList<Number> releaseTimeList) {
		this.releaseTimeList = releaseTimeList;
	}
	public void setDeadLineList(ArrayList<Number> deadLineList) {
		this.deadLineList = deadLineList;
	}
	public void setTimePeriodList(ArrayList<Number> timePeriodList) {
		this.timePeriodList = timePeriodList;
	}
//	public void setTempDeadLineMatrixList(ArrayList<Number> tempDeadLineMatrixList) {
//		this.tempDeadLineMatrixList = tempDeadLineMatrixList;
//	}
//	public void setDeadLineMatrixList(ArrayList<ArrayList> deadLineMatrixList) {
//		this.deadLineMatrixList = deadLineMatrixList;
//	}
	public void setTaskInstanceList(ArrayList<EDFTaskInstance> taskInstanceList) {
		this.taskInstanceList = taskInstanceList;
	}
	
	//Constructor
	public EDFTask() {
		super();
		this.taskName = null;
		this.deadLine = 0;
		this.timePeriod = 0;
		this.execTime = 0;
		this.noOfInstances = 0;
		this.priority = 0;
		this.hyperPeriod = 0;
//		this.finalTaskList = new ArrayList<EDFTask>();
//		this.releasedTasksList = new ArrayList<EDFTask>();
//		this.highPriorityTasksList = new ArrayList<EDFTask>();
		this.releaseTimeList = new ArrayList<Number>();
		this.deadLineList = new ArrayList<Number>();
		this.timePeriodList = new ArrayList<Number>();
		//this.tempDeadLineMatrixList = tempDeadLineMatrixList;
		//this.deadLineMatrixList = deadLineMatrixList;
		this.taskInstanceList =new ArrayList<EDFTaskInstance>();
		this.isTaskSchedulablePDA=false;
		this.finalTaskSequenceList=new ArrayList<Number>();
	}
	
}




class EDFTaskInstance{
	
	//Variables
	public String instanceIdentifier;   // something like for information ---> Task1-Instance1 
	public int taskInstanceNumber, taskInstanceReleaseTick,taskInstanceDeadLineTick;  //varables to identify currentInstanceNumber,release tick and dead line for current instace
	public int taskInstanceExecutionStartTick,taskInstanceCompleteTick,taskInstanceLastExecutedTick; //variables to track start and end of a instance and lastExecuted Tick
	public ArrayList<Number> taskInstancePreEmptionTicks,taskInstanceContextSwitchTicks; // variables to track pre-emptions and context switches for this instance
	public int taskInstanceNoOfTicksNeeded,taskInstanceNoOfTicksRan;  // temporary variables to track how many ticks current instance need and how many ticks current instance has ran at any perticular tick
	public boolean isCurrentInstanceComplete,isCurrentInstanceMissedDeadLine; //true-false variables to check the task-instance schedulability status
	
	
	//Getters and Setters

		
	public String getInstanceIdentifier() {
		return instanceIdentifier;
	}

	public int getTaskInstanceNumber() {
		return taskInstanceNumber;
	}

	public int getTaskInstanceReleaseTick() {
		return taskInstanceReleaseTick;
	}

	public int getTaskInstanceDeadLineTick() {
		return taskInstanceDeadLineTick;
	}

	public int getTaskInstanceExecutionStartTick() {
		return taskInstanceExecutionStartTick;
	}

	public int getTaskInstanceCompleteTick() {
		return taskInstanceCompleteTick;
	}

	public int getTaskInstanceLastExecutedTick() {
		return taskInstanceLastExecutedTick;
	}

	public ArrayList<Number> getTaskInstancePreEmptionTicks() {
		return taskInstancePreEmptionTicks;
	}

	public ArrayList<Number> getTaskInstanceContextSwitchTicks() {
		return taskInstanceContextSwitchTicks;
	}

	public int getTaskInstanceNoOfTicksNeeded() {
		return taskInstanceNoOfTicksNeeded;
	}

	public int getTaskInstanceNoOfTicksRan() {
		return taskInstanceNoOfTicksRan;
	}

	public boolean isCurrentInstanceComplete() {
		return isCurrentInstanceComplete;
	}

	public boolean isCurrentInstanceMissedDeadLine() {
		return isCurrentInstanceMissedDeadLine;
	}

	public void setInstanceIdentifier(String instanceIdentifier) {
		this.instanceIdentifier = instanceIdentifier;
	}

	public void setTaskInstanceNumber(int taskInstanceNumber) {
		this.taskInstanceNumber = taskInstanceNumber;
	}

	public void setTaskInstanceReleaseTick(int taskInstanceReleaseTick) {
		this.taskInstanceReleaseTick = taskInstanceReleaseTick;
	}

	public void setTaskInstanceDeadLineTick(int taskInstanceDeadLineTick) {
		this.taskInstanceDeadLineTick = taskInstanceDeadLineTick;
	}

	public void setTaskInstanceExecutionStartTick(int taskInstanceExecutionStartTick) {
		this.taskInstanceExecutionStartTick = taskInstanceExecutionStartTick;
	}

	public void setTaskInstanceCompleteTick(int taskInstanceCompleteTick) {
		this.taskInstanceCompleteTick = taskInstanceCompleteTick;
	}

	public void setTaskInstanceLastExecutedTick(int taskInstanceLastExecutedTick) {
		this.taskInstanceLastExecutedTick = taskInstanceLastExecutedTick;
	}

	public void setTaskInstancePreEmptionTicks(ArrayList<Number> taskInstancePreEmptionTicks) {
		this.taskInstancePreEmptionTicks = taskInstancePreEmptionTicks;
	}

	public void setTaskInstanceContextSwitchTicks(ArrayList<Number> taskInstanceContextSwitchTicks) {
		this.taskInstanceContextSwitchTicks = taskInstanceContextSwitchTicks;
	}

	public void setTaskInstanceNoOfTicksNeeded(int taskInstanceNoOfTicksNeeded) {
		this.taskInstanceNoOfTicksNeeded = taskInstanceNoOfTicksNeeded;
	}

	public void setTaskInstanceNoOfTicksRan(int taskInstanceNoOfTicksRan) {
		this.taskInstanceNoOfTicksRan = taskInstanceNoOfTicksRan;
	}

	public void setCurrentInstanceComplete(boolean isCurrentInstanceComplete) {
		this.isCurrentInstanceComplete = isCurrentInstanceComplete;
	}

	public void setCurrentInstanceMissedDeadLine(boolean isCurrentInstanceMissedDeadLine) {
		this.isCurrentInstanceMissedDeadLine = isCurrentInstanceMissedDeadLine;
	}

	//Constructor
	public EDFTaskInstance() {
		this.instanceIdentifier = null;
		this.taskInstanceNumber = 0;
		this.taskInstanceReleaseTick = 0;
		this.taskInstanceDeadLineTick = 0;
		this.taskInstanceExecutionStartTick = 0;
		this.taskInstanceCompleteTick = 0;
		this.taskInstanceLastExecutedTick = 0;
		this.taskInstancePreEmptionTicks = new ArrayList<Number>();
		this.taskInstanceContextSwitchTicks = new ArrayList<Number>();
		this.taskInstanceNoOfTicksNeeded = 0;
		this.taskInstanceNoOfTicksRan = 0;
		this.isCurrentInstanceComplete = false;
		this.isCurrentInstanceMissedDeadLine = false;
	
	}	
}
