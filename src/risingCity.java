
import java.util.Scanner;

import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.PrintStream;
/*main class from where input is read and buildings are pushed to min heap and red black tree 
 * @author Mohammed Haroon Rasheed K*/
public class risingCity{
	static public int globalCounter = 0,noofdays = 0; //global counter that is used to read the input and run the construction
	public static enum Operation{
		INSERT(0), PRINT(1);
		private final int operationType;

		private Operation(int operationType){
			this.operationType = operationType;
		}

		public int getValue(){
			return this.operationType;
		}
	}

	static PrintStream writeOutputtoFile;

	public static void main(String[] args) throws Exception {
		if(args.length > 0){
			File inpFile = new File(args[0]);
			//File inpFile = new File("src/ads/input.txt");
			Scanner sc = new Scanner(inpFile);
			int total = 0;
			MinHeap mh = MinHeap.getInstance();
			RBTBuilding rbt = new RBTBuilding();
			writeOutputtoFile = new PrintStream(new File("output_file.txt"));
			System.setOut(writeOutputtoFile);
			/*calculating the total time of all buildings needed to run*/
			while(sc.hasNext()){ 
				String inpLine = sc.nextLine();
				if(inpLine.contains("Insert")){
					total = total + Integer.parseInt(inpLine.split(",")[1].replace(")", ""));
				}			
			} 
			Scanner sc2 = new Scanner(inpFile);
			/*Buildings are read from the input file and checked with global counter if the 
			 *counter is less than the input time, then the counter is increased so as to read the input otherwise building is inserted into rbt and min heap and is ready for 
			 *execution*/
			while(noofdays < total){
				if(sc2.hasNext()){
					String inpLine = sc2.nextLine();
					int inputTime = Integer.parseInt(inpLine.split(":")[0]);
					int operation = inpLine.contains("Insert")?Operation.INSERT.getValue():Operation.PRINT.getValue();
					while(globalCounter < inputTime-1){ //increase the counter till input time is reached so that u can read the input
						boolean executed = mh.execute(rbt);
						if(executed){ // increment no of days only if executed
							noofdays++;
						}
						globalCounter++;
						if(mh.getSize() == 0 && globalCounter == inputTime - 1){
							globalCounter++;
						}
					}
					if(operation == Operation.INSERT.getValue()){
						int beginning = inpLine.indexOf("(");
						int end = inpLine.indexOf(")");
						String[] numandTime = inpLine.substring(beginning+1, end).split(",");
						Building b1 = Building.getInstance(Integer.parseInt(numandTime[0]),Integer.parseInt(numandTime[1]));
						if(b1 != null){
							rbt.insert(b1);
							b1 = mh.buildingInsert(b1);
						}
						else{
							System.out.println("(0, 0, 0)"); //duplicate building
						}
					}
					else if(operation == Operation.PRINT.getValue()){
						if(inpLine.contains(",")){
							int beginning = inpLine.indexOf("(");
							int end = inpLine.indexOf(")");
							String[] key1andKey2 = inpLine.substring(beginning+1, end).split(",");
							int num1 = Integer.parseInt(key1andKey2[0]);
							int num2 = Integer.parseInt(key1andKey2[1]);
							List<Building> printList = new ArrayList<Building>();						
							printList = rbt.searchInOrder(num1, num2);
							int rootBN = mh.getRootBuildingNumber();
							int k = 0;
							if(printList.size() > 0){
								for(Building b:printList){
									int execTime = b.getExecutedTime();
									if(rootBN == b.getBuildingNumber()){
										execTime += 1;
									}
									System.out.print("("+b.getBuildingNumber()+", "+execTime+", "+b.getTotalTime()+")");
									k++;
									if(k < printList.size()){
										System.out.print(",");
									}
								}
								System.out.print("\n");
							}
							else{
								System.out.println("(0, 0, 0)");
							}
						}else{
							int beginning = inpLine.indexOf("(");
							int end = inpLine.indexOf(")");
							int buildingNum = Integer.parseInt(inpLine.substring(beginning+1, end));
							Building find = rbt.printaBuilding(buildingNum);
							int rootBN = mh.getRootBuildingNumber();

							if(find != null){
								int execTime = find.getExecutedTime();
								if(rootBN == find.getBuildingNumber()){
									execTime += 1;
								}
								System.out.println("("+find.getBuildingNumber()+", "+execTime+", "+find.getTotalTime()+")");
							}
							else if(find == null){

								System.out.println("(0, 0, 0)");
							}
						}
					}

				} 
				boolean executed = mh.execute(rbt);
				if(executed){
					noofdays++;
				}
				globalCounter++;
			}
			sc.close();
			sc2.close();
		}
	}
}
