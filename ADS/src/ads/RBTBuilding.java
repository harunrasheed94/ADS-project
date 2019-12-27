

import java.util.List;

import java.util.ArrayList;
public class RBTBuilding {

	private final int rColor = 0;
	private final int bColor = 1;
	public boolean check = false;

	public class RBTNode{
		Building building;
		int nodeColor = bColor;
		RBTNode parentNode = nil;
		RBTNode leftNode = nil, rightNode = nil;
		RBTNode(Building building){
			this.building = building;
		}

	}

	Building emptyBuilding = new Building(-1, -1);
	private final RBTNode nil =  new RBTNode(emptyBuilding);
	private RBTNode rootNode = nil;
	RBTNode psNode = nil;

	RBTNode getGrandPa(RBTNode node){
		return node.parentNode.parentNode;
	}

	RBTNode getPSiblingNode(RBTNode childNode){ //get uncle node or parent sibling node
		//if my granddad's right node is my parent then uncle is in left node else uncle is in right node
		RBTNode psiblingNode = getGrandPa(childNode).rightNode == childNode.parentNode ? getGrandPa(childNode).leftNode : getGrandPa(childNode).rightNode;
		return psiblingNode;
	}

	public void insert(Building b) throws Exception{
		RBTNode currentNode = rootNode;
		RBTNode insertNode = new RBTNode(b);
		int rootBuildingNum = rootNode.building.getBuildingNumber();
		// if root node is nil and there are are no nodes in rbt, then insertNode is the root
		if(rootNode == nil || rootBuildingNum == nil.building.getBuildingNumber()){
			rootNode = insertNode;
			insertNode.parentNode = nil;
			insertNode.nodeColor = bColor;
		}else{
			insertNode.nodeColor = rColor;
			int currentNodeBNum = currentNode.building.getBuildingNumber();
			int newBuildingNum = insertNode.building.getBuildingNumber();
			boolean inserted =false; 
			while(!inserted){
				//if new building number is smaller than current building number then go to left and check for empty
				if(currentNodeBNum > newBuildingNum ){
					if(currentNode.leftNode.building.getBuildingNumber() != -1){ //not empty as i have buildings on left so traverse left
						currentNode = currentNode.leftNode;// assign left node as the current node
					}else{ //if empty insert node 
						currentNode.leftNode = insertNode;
						insertNode.parentNode = currentNode;
						inserted = true;
					}
				}
				//if new building number is greater when compared to current building then go right 
				else if(currentNodeBNum < newBuildingNum){
					if(currentNode.rightNode.building.getBuildingNumber() != -1){ //if not empty traverse right
						currentNode = currentNode.rightNode;

					}else{//if empty insert node as right node
						currentNode.rightNode = insertNode;
						insertNode.parentNode = currentNode;
						inserted = true;
					}
				}
				currentNodeBNum  = currentNode.building.getBuildingNumber();
			}
			balanceRBT(insertNode);
		}
	}

	private RBTNode balanceRBT(RBTNode newNode)throws Exception{

		while(newNode != rootNode && newNode.parentNode.nodeColor == rColor){
			psNode = getPSiblingNode(newNode);
			if(psNode.building.getBuildingNumber() != -1 && psNode.nodeColor == rColor){
				//perform only color change since no rotation is needed since uncle is red Color
				newNode = doColorChange(psNode,newNode);
			}
			else{ 		//when uncle is not red but black, rotation is needed to fix 
				if(newNode == newNode.parentNode.rightNode){ //if inserted node is right node perform rr rotation 
					newNode = newNode.parentNode;
					rotateLeft(newNode); //rr rotation
				}
				else if(newNode == newNode.parentNode.leftNode){ //if inserted node is left node perform ll rotation
					newNode = newNode.parentNode; 	/*change node to parent node */	
					rotateRight(newNode); //ll rotation 
				}
				newNode.parentNode.nodeColor =bColor;
				getGrandPa(newNode).nodeColor = rColor;

				String psNodeLocation = getGrandPa(newNode).leftNode == newNode.parentNode ? "right" : "left"; 
				if(psNodeLocation.equals("right")){ //if uncle is right perform ll rotation with grandparent node
					rotateRight(getGrandPa(newNode));
				}
				else if(psNodeLocation.equals("left")){ //if uncle is left perform rr rotation with grandparent node
					rotateLeft(getGrandPa(newNode));
				}
			}


		}
		rootNode.nodeColor = bColor;
		return rootNode;
	}

	/* Perform only color change when uncle node is red as no rotation is needed. change the uncle color and parent 
	 * color to black and the grandparent color to red*/
	private RBTNode doColorChange(RBTNode psNode, RBTNode newNode)throws Exception{
		psNode.nodeColor = bColor;
		newNode.parentNode.nodeColor = bColor;
		RBTNode grandPa = getGrandPa(newNode);
		grandPa.nodeColor = rColor;
		newNode = grandPa;
		return newNode;
	}

	/*call rotate root method if node to be performed ll rotation is root else
	 * if node is a left child set node's right child as parents left child  
	 * else set node's right child as parent's right child
	 * set node's parent as parent of node's right child
	 * set node's right child as node's parent*/
	private RBTNode rotateLeft(RBTNode newNode)throws Exception{
		int newNodeParentNodeBuildingNumber = newNode.parentNode.building.getBuildingNumber();
		int nillNodeBN = nil.building.getBuildingNumber();
		if (newNodeParentNodeBuildingNumber != nillNodeBN) {
			if (newNode == newNode.parentNode.leftNode) {
				newNode.parentNode.leftNode = newNode.rightNode;
			} else {
				newNode.parentNode.rightNode = newNode.rightNode;
			}
			newNode.rightNode.parentNode = newNode.parentNode;
			newNode.parentNode = newNode.rightNode;
			RBTNode rlNode = newNode.rightNode.leftNode;
			int rlNodebNum = rlNode.building.getBuildingNumber();
			if (rlNode != null && rlNodebNum != nillNodeBN) {
				rlNode.parentNode = newNode;
			}
			newNode.rightNode =rlNode;
			newNode.parentNode.leftNode = newNode;
		} 
		else{ //have to rotate root
			rotateRootLeft();
		}
		return newNode;
	}
	/*call rotate root method if node to be performed rr rotation is root else
	 * if node is a left child set node's left child as parents left child  
	 * else set node's left child as parent's right child
	 * set node's parent as parent of node's left child
	 * set node's left child as node's parent*/
	private RBTNode rotateRight(RBTNode newNode)throws Exception{
		int newNodeParentNodeBuildingNumber = newNode.parentNode.building.getBuildingNumber();
		int nillNodeBN = nil.building.getBuildingNumber();
		if (newNodeParentNodeBuildingNumber != nillNodeBN) {
			if (newNode == newNode.parentNode.leftNode) {
				newNode.parentNode.leftNode = newNode.leftNode;
			} else {
				newNode.parentNode.rightNode = newNode.leftNode;
			}

			newNode.leftNode.parentNode = newNode.parentNode;
			newNode.parentNode = newNode.leftNode;
			RBTNode lrNode = newNode.leftNode.rightNode;
			int lrNodebNum = lrNode.building.getBuildingNumber();
			if ( lrNode != null && lrNodebNum != nillNodeBN) {
				lrNode.parentNode = newNode;
			}
			newNode.leftNode = lrNode;
			newNode.parentNode.rightNode = newNode;
		}
		else{
			rotateRootRight();
		} 
		return newNode;
	}

	/*Root left rotation LL rotation of root node */
	private void rotateRootLeft()throws Exception{
		RBTNode tempRightNode = rootNode.rightNode; //set right node of root as temp variable 
		rootNode.rightNode = tempRightNode.leftNode; //set temp variable's left child as root's right child
		tempRightNode.leftNode.parentNode = rootNode;//set root as parent of temp variable's leftchild
		rootNode.parentNode = tempRightNode;//set temp variable as root's parent
		tempRightNode.leftNode = rootNode;//set root as leftchild of temp right child
		tempRightNode.parentNode = nil;//set temp variable parent as nil since temp is the new root
		rootNode = tempRightNode;
	}

	private void rotateRootRight()throws Exception{
		RBTNode tempLeftNode = rootNode.leftNode;//set left node of root as temp variable
		rootNode.leftNode = rootNode.leftNode.rightNode;//set right child of root's left child as root's left child
		tempLeftNode.rightNode.parentNode = rootNode;//set root as parent of temp's right child 
		rootNode.parentNode = tempLeftNode;//set temp variable as root's parent
		tempLeftNode.rightNode = rootNode;//set root as temp's right child
		tempLeftNode.parentNode = nil;// set temp's parent as nil since temp is new root
		rootNode = tempLeftNode;
	}

/* find a node in RBT
 * if node is greater than parent, node is in right side of parent
 * else if node is smaller than parent, node is in left side of parent so call recursively accordingly
 * until you find the element  */
	private RBTNode findRBT(int findNodeBN, RBTNode parentNode)throws Exception {
		if (rootNode == nil) {
			return rootNode;
		}
		else{
			int parentNodeBN = parentNode.building.getBuildingNumber();
			int nilBN = nil.building.getBuildingNumber();

			if (findNodeBN < parentNodeBN) {
				if (parentNode.leftNode != null && parentNode.leftNode.building.getBuildingNumber() != nilBN ) {
					return findRBT(findNodeBN, parentNode.leftNode);
				}
			}
			else if (findNodeBN > parentNodeBN) {
				if (parentNode.rightNode != null && parentNode.rightNode.building.getBuildingNumber() != nilBN ) {
					return findRBT(findNodeBN, parentNode.rightNode);
				}
			}
			else if (findNodeBN == parentNodeBN) {
				return parentNode;
			}
		}

		return null;
	}
/* */
	private void RBdeleteFixupAlgo(RBTNode childNode)throws Exception{
		while(childNode!=rootNode && childNode.nodeColor == bColor){ 
			if(childNode == childNode.parentNode.leftNode){
				RBTNode siblingNode = childNode.parentNode.rightNode;
				if(siblingNode.nodeColor == rColor){
					siblingNode.nodeColor = bColor;
					childNode.parentNode.nodeColor = rColor;
					rotateLeft(childNode.parentNode);
					siblingNode = childNode.parentNode.rightNode;
				}
				if(siblingNode.leftNode.nodeColor == bColor && siblingNode.rightNode.nodeColor == bColor){
					siblingNode.nodeColor = rColor;
					childNode = childNode.parentNode;
					continue;
				}
				else if(siblingNode.rightNode.nodeColor == bColor){
					siblingNode.leftNode.nodeColor = bColor;
					siblingNode.nodeColor = rColor;
					rotateRight(siblingNode);
					siblingNode = childNode.parentNode.rightNode;
				}
				if(siblingNode.rightNode.nodeColor == rColor){
					siblingNode.nodeColor = childNode.parentNode.nodeColor;
					childNode.parentNode.nodeColor = bColor;
					siblingNode.rightNode.nodeColor = bColor;
					rotateLeft(childNode.parentNode);
					childNode = rootNode;
				}
			}else{
				RBTNode siblingNode = childNode.parentNode.leftNode;
				if(siblingNode.nodeColor == rColor){
					siblingNode.nodeColor = bColor;
					childNode.parentNode.nodeColor = rColor;
					rotateRight(childNode.parentNode);
					siblingNode = childNode.parentNode.leftNode;
				}
				if(siblingNode.rightNode.nodeColor == bColor && siblingNode.leftNode.nodeColor == bColor){
					siblingNode.nodeColor = rColor;
					childNode = childNode.parentNode;
					continue;
				}
				else if(siblingNode.leftNode.nodeColor == bColor){
					siblingNode.rightNode.nodeColor = bColor;
					siblingNode.nodeColor = rColor;
					rotateLeft(siblingNode);
					siblingNode = childNode.parentNode.leftNode;
				}
				if(siblingNode.leftNode.nodeColor == rColor){
					siblingNode.nodeColor = childNode.parentNode.nodeColor;
					childNode.parentNode.nodeColor = bColor;
					siblingNode.leftNode.nodeColor = bColor;
					rotateRight(childNode.parentNode);
					childNode = rootNode;
				}
			}
		}
		childNode.nodeColor = bColor; 
	}

	private void substitute(RBTNode findNode, RBTNode parentNode)throws Exception{ 

		RBTNode targetParentNode = findNode.parentNode;
		int targetParentBN = targetParentNode.building.getBuildingNumber();
		int nilBN = nil.building.getBuildingNumber();
		if(targetParentBN == nilBN){
			rootNode = parentNode;
		}else if(findNode == targetParentNode.leftNode){
			targetParentNode.leftNode = parentNode;
		}else{			
			targetParentNode.rightNode = parentNode;
		}

		parentNode.parentNode = targetParentNode;
	}
/* used to delete building from a red black tree*/
	public boolean deleteBuildingFromRBT(int deleteBuildingNum)throws Exception{
		RBTNode deleteNode= findRBT(deleteBuildingNum, rootNode);
		if(deleteNode!=null) {
			RBTNode tempNode = deleteNode; 
			RBTNode childNode;
			int tempNodeColor = tempNode.nodeColor;
			int dlnodebuildingNumber = deleteNode.leftNode.building.getBuildingNumber();
			int drnodebuildingNumber = deleteNode.rightNode.building.getBuildingNumber();
			int nilBNumber = nil.building.getBuildingNumber();
			if(dlnodebuildingNumber == nilBNumber){ //if deleted node's left child is leaf, replace node with right child
				childNode = deleteNode.rightNode;  
				substitute(deleteNode, deleteNode.rightNode);  
			}else if(drnodebuildingNumber == nilBNumber){ //if deleted node's right child is leaf, replace node with left child
				childNode = deleteNode.leftNode;
				substitute(deleteNode, deleteNode.leftNode); 
			}else{ //if node has both right and left child
				tempNode = deleteNode.rightNode; //set temp node as delete node's right child. Traverse left node until u reach a node with no left child
				while(tempNode.leftNode.building.getBuildingNumber() != nil.building.getBuildingNumber()){
					tempNode = tempNode.leftNode;
				}
				tempNodeColor = tempNode.nodeColor;
				childNode = tempNode.rightNode;
				if(tempNode.parentNode == deleteNode)
					childNode.parentNode = tempNode;
				else{
					substitute(tempNode, tempNode.rightNode);//replace temp node with the temp variable's right node
					tempNode.rightNode = deleteNode.rightNode;
					tempNode.rightNode.parentNode = tempNode;
				}
				substitute(deleteNode, tempNode);//replace delete node and temp node
				tempNode.leftNode = deleteNode.leftNode;
				tempNode.leftNode.parentNode = tempNode;
				tempNode.nodeColor = deleteNode.nodeColor; 
			}
			if(tempNodeColor==bColor){
				RBdeleteFixupAlgo(childNode);  
			}
			return true;
		}
		else{
			return false;	
		}
	}

	//in order traversal to get the buildings and print it
	private List<Building> searchInOrder(RBTNode beginNode,List<Building> buildingPrint, int begin, int finish)throws Exception{
		if(beginNode == nil && beginNode.building.getBuildingNumber() == nil.building.getBuildingNumber()){
			return null;
		}

		if(beginNode.building.getBuildingNumber() > begin){
			searchInOrder(beginNode.leftNode,buildingPrint,begin,finish);
		}
		if(beginNode.building.getBuildingNumber() >= begin && finish >= beginNode.building.getBuildingNumber()){
			buildingPrint.add(beginNode.building);
		}
		if(beginNode.building.getBuildingNumber() < finish){
			searchInOrder(beginNode.rightNode,buildingPrint,begin,finish);
		}
		return buildingPrint;
	}

	public List<Building> searchInOrder(int begin,int finish)throws Exception{
		List<Building> buildingList = new ArrayList<Building>();
		searchInOrder(rootNode, buildingList,begin,finish);
		return buildingList;
	}

	public Building printaBuilding(int num)throws Exception{
		RBTNode buildingNode = findRBT(num,rootNode);
		if(buildingNode != nil){
			return buildingNode.building;
		}
		return null;
	}
}
