

public class MinHeap {
	private Building[] buildings =  null;
	private int maxSize,size,executionTime,root;

	private MinHeap()throws Exception{
		maxSize = 2000;
		size = 0;
		root = 0;
		executionTime = 0;
		this.buildings = new Building[2000];
	}

	public static MinHeap getInstance()throws Exception{
		MinHeap mh1 = new MinHeap();
		return mh1;
	}

	//total size of the heap
	public int getSize()throws Exception{
		return size;
	}

	//get parent of the element
	private int getParent(int elem)throws Exception{
		return (elem-1)/2;
	}

	//get the left child of the element
	private int getLeftChild(int elem)throws Exception{
		return 2*elem + 1;
	}

	//get the right child of the element
	private int getRightChild(int elem)throws Exception{
		if(2*elem + 2 > size - 1){
			return -1;
		}
		return 2*elem + 2;
	}

	//swap the two buildings
	private void swap(int elem1, int elem2)throws Exception{
		Building temp;
		temp = buildings[elem1];
		buildings[elem1] = buildings[elem2];
		buildings[elem2] = temp;
	}

	//get the root building number
	public int getRootBuildingNumber()throws Exception{
		return buildings[root].getBuildingNumber();
	}

	//check if the element is leaf or not
	private boolean isLeaf(int elem)throws Exception{
		if(size >= elem && elem >= size/2){
			return true;
		}
		return false;
	}

	//insert the building into the heap and move it up if its executed time or building number is less than the parent
	public Building buildingInsert(Building b1)throws Exception{
		if(size > maxSize){
			return null;
		}
		buildings[size] = b1; 
		int position = size;
		int parent = getParent(position);
		size++;
		while(parent != root && (buildings[parent].getExecutedTime() > buildings[position].getExecutedTime() || (buildings[parent].getExecutedTime() == buildings[position].getExecutedTime() && buildings[parent].getBuildingNumber() > buildings[position].getBuildingNumber()))){ //check for parent with greater execution time or (equal execution time && building number)
			swap(parent,position);
			position = parent;
			parent = getParent(position);
		}
		return buildings[position];
	}

	//building is constructed and its execution time is incremented by one
	public boolean execute(RBTBuilding rbt)throws Exception{
		if(size > 0){
			buildings[root].incrementExecutedTime();
			executionTime = executionTime + 1;
			//if the building finishes execution or if the executed time is equal to 5 then call heapify  
			if(executionTime > 4 || (buildings[root].getExecutedTime() == buildings[root].getTotalTime())){
				removeMinOnCompletion(rbt);
				heapify();
			}
			return true;
		}
		return false;
	}

	//check if the building executed time is equal to total time if yes remove it from the rbt, write to file and remove from the heap 
	private void removeMinOnCompletion(RBTBuilding rbt)throws Exception{
		if(buildings[root].getExecutedTime() == buildings[root].getTotalTime()){		
			rbt.deleteBuildingFromRBT(buildings[root].getBuildingNumber());	
			System.out.println("("+buildings[root].getBuildingNumber()+", "+ (risingCity.globalCounter + 1) +")");
			int lastElement = size-1;
			swap(root,lastElement);
			size--;	
			if(size == 0){
				executionTime = 0;
			}
		}		
	}
	
/*heapify function is used to select the building after 5 days or when a building finishes construction
	check if the left child execution time is greater than right child if yes select right child as smaller child else left child
	if execution time is equal break the tie by comparing the building numbers
	once smallest of the left and right child is taken, now compare it with parents execution time and again if
	tie occurs break it with building number
	swap the parent with the smallest child and keep comparing the parent with the children and pushing it down if necessary */
	private void heapify()throws Exception{
		if(size > 0){
			int leftChild = getLeftChild(root);
			int rightChild = getRightChild(root);
			int parent = root;
			int smallestChild = -1;
			boolean changeRequired = true;
			while(!isLeaf(parent) && (buildings[parent].getExecutedTime() >= buildings[leftChild].getExecutedTime() || (rightChild != -1 && buildings[parent].getExecutedTime() >= buildings[rightChild].getExecutedTime())) && (changeRequired)){
				if(rightChild != -1 && buildings[leftChild].getExecutedTime() > buildings[rightChild].getExecutedTime()){ 
					smallestChild = rightChild;
				}
				else{
					smallestChild = leftChild;
					if(rightChild != -1 && buildings[leftChild].getExecutedTime() == buildings[rightChild].getExecutedTime()){
						int buildingNumLChild = buildings[leftChild].getBuildingNumber();
						int buildingNumRChild = buildings[rightChild].getBuildingNumber(); 
						smallestChild = buildingNumLChild > buildingNumRChild ? rightChild : leftChild;
					}
				}
				if(buildings[parent].getExecutedTime() > buildings[smallestChild].getExecutedTime() || (buildings[parent].getExecutedTime() == buildings[smallestChild].getExecutedTime() && buildings[parent].getBuildingNumber() > buildings[smallestChild].getBuildingNumber())){
					swap(parent,smallestChild);
					parent = smallestChild;
					leftChild = getLeftChild(parent);
					rightChild = getRightChild(parent);
				}
				else{
					changeRequired = false;
				}
			}
			executionTime = 0;
		}

	}
}
