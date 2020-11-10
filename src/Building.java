
import java.util.ArrayList;

public class Building {
	private int buildingNumber;
	private int totalTime, executedTime;
	private static ArrayList<Integer> buildingNumbers = new ArrayList<Integer>();

	public Building(int buildingNumber,int totalTime){
		this.buildingNumber = buildingNumber;
		this.totalTime = totalTime;
		this.executedTime = 0;
	}

	public Building() throws Exception{}

	public int getExecutedTime()throws Exception{
		return this.executedTime;	
	}

	public int getTotalTime()throws Exception{
		return this.totalTime;
	}

	public int getBuildingNumber()throws Exception{
		return this.buildingNumber;
	}

	public int incrementExecutedTime()throws Exception{
		this.executedTime += 1;
		return this.executedTime;		
	}

	public void setExecutedTime(int executedTime)throws Exception{
		this.executedTime = executedTime;				
	}

	public void setTotalTime(int totalTime)throws Exception{
		this.totalTime = totalTime;
	}

	public void setBuildingNumber(int buildingNum)throws Exception{
		this.buildingNumber = buildingNum;
	}

	public static Building getInstance(int num, int total_time) throws Exception{
		Building b1 = null;
		if(!checkforDuplicates(num)){
			b1 = new Building(num,total_time);
		}
		return b1;
	}

	private static Boolean checkforDuplicates(int num) throws Exception{
		if(!buildingNumbers.contains(num)){
			buildingNumbers.add(num);
			return false;
		}
		return true;
	}
}
