import java.util.LinkedList;

public class Graph {
	int U;
	int F;
	int O;
	Node[] que;
	int K;
	int KK;
	LinkedList<int[]>[] usedEdgeLabels;
	int[][][] edgeOption;
	GraphManager GM;
	
	public Graph(int[][] edgeAdjacency, int K, int KK, int O, GraphManager GM){
		int i, j, k;
		this.GM = GM;
		this.K = K;
		this.KK = KK;
		this.O = O;
		U = edgeAdjacency.length;
		F = 0;
		@SuppressWarnings("unchecked")LinkedList<int[]>[] usedEdgeLabels = new LinkedList[KK];
		this.usedEdgeLabels = usedEdgeLabels;
		for(i = 0; i < usedEdgeLabels.length; i++)
			usedEdgeLabels[i] = new LinkedList<int[]>();
		
		edgeOption = new int[K][KK][KK];
		for(i = 0; i < K; i++)
			for(j = 0; j < KK-1; j++)
				for(k = j+1; k < KK; k++)
					edgeOption[i][j][k] = O;
		for(i = 1; i <= K/2; i++)
			for(j = 0; j < KK; j++)
				edgeOption[i][j][j] = O;
		
		int[] upStreamCount = new int[U];
		for(i = 0; i < U; i++)
			upStreamCount[i] = 0;
		
		for(i = 0; i < U; i++)
			for(j = 0; j < edgeAdjacency[i].length; j++)
				upStreamCount[edgeAdjacency[i][j]]++;

		que = new Node[U];
		for(i = 0; i < U; i++)
			que[i] = new Node(K, KK, i, i, edgeAdjacency[i].length, upStreamCount[i]);
		for(i = 0; i < U; i++)
			for(j = 0; j < edgeAdjacency[i].length; j++)
				que[i].downStream[j] =  que[edgeAdjacency[i][j]];
	}
	
	public Graph(Graph graph){
		int i, j, k;
		GM = graph.GM;
		U = graph.U;
		F = graph.F;
		O = graph.O;
		K = graph.K;
		KK = graph.KK;
		@SuppressWarnings("unchecked")LinkedList<int[]>[] usedEdgeLabels = new LinkedList[KK];
		this.usedEdgeLabels = usedEdgeLabels;
		for(i = 0; i < usedEdgeLabels.length; i++)
			usedEdgeLabels[i] = new LinkedList<int[]>(graph.usedEdgeLabels[i]);
		
		edgeOption = new int[graph.edgeOption.length][][];
		for(i = 0; i < edgeOption.length; i++){
			edgeOption[i] = new int[graph.edgeOption[i].length][];
			for(j = 0; j < edgeOption[i].length; j++){
				edgeOption[i][j] = new int[graph.edgeOption[i][j].length];
				for(k = 0; k < edgeOption[i][j].length; k++)
					edgeOption[i][j][k] = graph.edgeOption[i][j][k];
			}
				
		}
		que = new Node[graph.que.length];
		for(i = 0; i < que.length; i++)
			que[i] = new Node(graph.que[i]);
		
		for(i = 0; i < que.length; i++)
			for(j = 0; j < que[i].downStream.length; j++)
				que[i].downStream[j] = que[graph.que[i].downStream[j].heapIndex];
	}
	
	public void copyEdgeStuff(Graph graph){
		int i, j, k, n;
		for(i = 0; i < edgeOption.length; i++)
			for(j = 0; j < edgeOption[i].length; j++)
				for(k = 0; k < edgeOption[i][j].length; k++){
					edgeOption[i][j][k] = graph.edgeOption[i][j][k];
					@SuppressWarnings("unchecked")LinkedList<int[]>[] usedEdgeLabels = new LinkedList[KK];
					this.usedEdgeLabels = usedEdgeLabels;
					for(n = 0; n < usedEdgeLabels.length; n++)
						usedEdgeLabels[n] = new LinkedList<int[]>(graph.usedEdgeLabels[n]);
				}
	}
	
	public void swap(int index1, int index2){
		Node temp = que[index1];
		que[index1] = que[index2];
		que[index2] = temp;
		
		que[index1].heapIndex = index1;
		que[index2].heapIndex = index2;
	}
	
	public void off(int index, int[] label){
		que[index].off(label);
		if(index < U)
			while(que[index].free < que[index/2].free){
				swap(index, index/2);
				index = index/2;
			}
	}
	
	public boolean setHumanLabel(int humanIndex, int[] label){
		for(int i = 0; i < que.length; i++)
			if(que[i].humanIndex == humanIndex)
				 return setLabel(que[i].heapIndex, label);
		return false;
	}
	
	public boolean setLabel(int index, int[] label){
		if(label[0] < K && label[1] < KK){
			int i;
			int[] edgeLabel;
			
			que[index].label[0] = label[0];
			que[index].label[1] = label[1];
			U--;
			swap(index, U);
			for(i = 0; i < U; i++)
				off(i, label);

			//Get newly acquired edge labels
			LinkedList<int[]> newEdgeList = new LinkedList<int[]>();
			for(i = 0; i < que[U].downStream.length; i++)
				if(que[U].downStream[i].label[0] >= 0){
					edgeLabel = getEdgeLabel(que[U].downStream[i].label, label);
					edgeOption[edgeLabel[0]][edgeLabel[1]][edgeLabel[2]]--;
					if(edgeOption[edgeLabel[0]][edgeLabel[1]][edgeLabel[2]] < 0)
						return false;
					else if(edgeOption[edgeLabel[0]][edgeLabel[1]][edgeLabel[2]] == 0)
						newEdgeList.add(edgeLabel);
				}
			//Turn off 
			Object[] objects;
			if(!usedEdgeLabels[label[1]].isEmpty()){
				objects = usedEdgeLabels[que[U].label[1]].toArray();
				int[][] edgeLabels = new int[objects.length][];
				for(i = 0; i < objects.length; i++)
					edgeLabels[i] = (int[])objects[i];
				edgeOff(U, edgeLabels);
			}
			
			//Turn off
			if(!newEdgeList.isEmpty()){
				objects = newEdgeList.toArray();
				int[][] newEdgeLabels = new int[objects.length][];
				for(i = 0; i < objects.length; i++)
					newEdgeLabels[i] = (int[])objects[i];
				for(i = U; i < que.length-F; i++)
					if(edgeOff(i, newEdgeLabels))
						i--;
			}
			//Add new edge labels
			while(!newEdgeList.isEmpty()){
				edgeLabel = newEdgeList.poll();
				usedEdgeLabels[edgeLabel[1]].add(edgeLabel);
				usedEdgeLabels[edgeLabel[2]].add(edgeLabel);
			}
			return true;
		}
		return false;
	}
	
	public boolean edgeOff(int index, int[][] edgeLabel){
		int i, j;
		LinkedList<int[]> offLimitNodeList = new LinkedList<int[]>();
		for(i = 0; i < edgeLabel.length; i++)
			if(que[index].label[1] == edgeLabel[i][1] || que[index].label[1] == edgeLabel[i][2])
				if(edgeLabel[i][1] == edgeLabel[i][2]){
					offLimitNodeList.add(getClockWise(que[index].label, edgeLabel[i]));
					offLimitNodeList.add(getCounterClockWise(que[index].label, edgeLabel[i]));
				}
				else
					offLimitNodeList.add(getWise(que[index].label, edgeLabel[i]));
		Object[] objects = offLimitNodeList.toArray();
		int[][] offLimitNodeLabels = new int[objects.length][];
		for(i = 0; i < objects.length; i++)
			offLimitNodeLabels[i] = (int[])objects[i];
		objects = null;
		boolean finished = true;
		Node node;
		for(i = 0; i < que[index].downStream.length; i++){
			node = que[index].downStream[i];
			if(node.label[0] < 0){
				finished = false;
				for(j = 0; j < offLimitNodeLabels.length; j++)
					off(node.heapIndex, offLimitNodeLabels[j]);
			}
		}
		node = null;
		offLimitNodeList= null;
		offLimitNodeLabels = null;
		if(finished){
			F++;
			swap(index, que.length-F);
			return true;
		}
		return false;
	}
	
	public int[] getEdgeLabel(int[] start, int[] destination){
		int[] def = new int[3];
		if(start[1] == destination[1]){ //def[0] is rho labeling
			def[0] = Math.abs(start[0] - destination[0]);
			if(K - def[0] < def[0])		def[0] = K - def[0];
			def[1] = start[1];
			def[2] = start[1];
		}
		else{
			if(start[1] > destination[1]){
				def[0] = start[0] - destination[0];
				def[1] = destination[1];
				def[2] = start[1];
			}
			else{
				def[0] = destination[0] - start[0];
				def[1] = start[1];
				def[2] = destination[1];
			}
			if(def[0] < 0)
				def[0] = def[0] + K;
		}
		
		return def;
	}
	
	public int[] getWise(int[] start, int[] edgeLength){
		int[] dest = new int[2];
		if(start[1] == edgeLength[1]){
			dest[1] = edgeLength[2];
			dest[0] = start[0] + edgeLength[0];
			if(dest[0] >= K)
				dest[0] = dest[0] - K;
		}
		else{
			dest[1] = edgeLength[1];
			dest[0] = start[0] - edgeLength[0];
			if(dest[0] < 0)
				dest[0] = dest[0] + K;
		}
		return dest;
	}
	

	public int[] getClockWise(int[] start, int[] edgeLength){
		int[] dest = new int[2];
		dest[1] = edgeLength[2];
		dest[0] = start[0] + edgeLength[0];
		if(dest[0] >= K)
			dest[0] = dest[0] - K;
		return dest;
	}
	
	public int[] getCounterClockWise(int[] start, int[] edgeLength){
		int[] dest = new int[2];
		dest[1] = edgeLength[2];
		dest[0] = start[0] - edgeLength[0];
		if(dest[0] < 0)
			dest[0] = dest[0] + K;
		return dest;
	}
	
	public void killStandardEdges(){
		int[] label;
		for(int i = 0; i < edgeOption.length; i++){
			for(int j = 0; j < edgeOption[i].length; j++){
				label = new int[3];
				label[0] = i;
				label[1] = j;
				label[2] = j;
				edgeOption[i][j][j] = 0;
				usedEdgeLabels[j].add(label);
			}
		}
	}
	
	private void sortByHumanIndex(){
		int i;
		int heapSize = que.length;
		
		//Make max heap
		for(i = heapSize/2; i >= 0; i--)
			maxHeapifyByHumanIndex(i, heapSize);
		
		
		for(i = 0; i < que.length; i++){
			heapSize--;
			swap(0, heapSize);
			maxHeapifyByHumanIndex(0, heapSize);
		}
	}
	
	private void maxHeapifyByHumanIndex(int index, int heapSize){
		int childIndex = index * 2;
		if(childIndex < heapSize){
			if(childIndex + 1 < heapSize){
				if( que[childIndex].humanIndex >= que[childIndex+1].humanIndex ){
					if( que[childIndex].humanIndex > que[index].humanIndex ){
						swap(index, childIndex);
						maxHeapifyByHumanIndex(childIndex, heapSize);
					}
				}
				else if( que[childIndex+1].humanIndex > que[index].humanIndex ){
					swap(index, childIndex+1);
					maxHeapifyByHumanIndex(childIndex+1, heapSize);
				}
			}
			else if( que[childIndex].humanIndex > que[index].humanIndex ){
				swap(index, childIndex);
				maxHeapifyByHumanIndex(childIndex, heapSize);
			}
		}
	}
	
	public String toString(){
		Graph graph = new Graph(this);
		graph.sortByHumanIndex();
		int i, j;

		String string = "\n";
		for(i = 0; i < que.length; i++){
			string = string + "\n  Node " + i + ": \tLabel: (" + graph.que[i].label[0] + ", " + graph.que[i].label[1] + ")\tNeighbors: ";
			for(j = 0; j < graph.que[i].downStream.length; j++)
				string = string + graph.que[i].downStream[j].humanIndex + " ";
		}
		return string;
	}
	
	public boolean solve(int index){
		Graph graph = GM.getNext(index);
		if(que[0].label[0] >= 0){

			if(graph == null){
				System.out.println("Block " + index + "\n" + this);
				return true;
			}
			graph.copyEdgeStuff(this);
			if(graph.solve(index+1)){
				System.out.println("Block " + index + "\n" + this);
				return true;
			}
			return false;
		}
		if(que[0].free == 0)
			return false;
		while(que[0].minOption[0] < K){
			if(que[0].free == 0)
				return false;
			graph = new Graph(this);
			if(graph.setLabel(0, graph.que[0].minOption)){
				if(graph.solve(index))	return true;
				else					off(0, que[0].minOption);
			}
			else						off(0, que[0].minOption);
		}
		return false;
	}
}

