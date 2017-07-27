public class Node {
	int[] label;
	boolean[][] optionTable;
	int K;
	int free;
	Node[] downStream;
	int upIndex;
	int[] minOption;
	int humanIndex;
	int heapIndex;
	
	public Node(int optionLength, int optionWidth, int humanIndex, int heapIndex, int numDownStream, int numUpStream){
		K = optionLength;
		label = new int[2];
		label[0] = -1;
		label[1] = -1;
		optionTable = new boolean[optionLength][optionWidth];
		for(int i = 0; i < optionLength; i++){
			for(int j = 0; j < optionWidth; j++){
				optionTable[i][j] = true;
			}
		}
		minOption = new int[2];
		minOption[0] = 0;
		minOption[1] = 0;
		upIndex = 0;
		this.humanIndex = humanIndex;
		this.heapIndex = heapIndex;
		downStream = new Node[numDownStream];
		free = optionLength * optionWidth;
	}
	
	public Node(Node node){
		int i, j;
		label = new int[node.label.length];
		for(i = 0 ; i < label.length; i++)
			label[i] = node.label[i];
		optionTable = new boolean[node.optionTable.length][node.optionTable[0].length];
		for(i = 0; i < optionTable.length; i++)
			for(j = 0; j < optionTable[i].length; j++)
				optionTable[i][j] = node.optionTable[i][j];
		K = node.K;
		free = node.free;
		downStream = new Node[node.downStream.length];
		minOption = new int[node.minOption.length];
		for(i = 0; i < minOption.length; i++)
			minOption[i] = node.minOption[i];
		humanIndex = node.humanIndex;
		heapIndex = node.heapIndex;
		upIndex = 0;
	}
	
	
	public void off(int[] label){
		if(optionTable[label[0]][label[1]]){
			optionTable[label[0]][label[1]] = false;
			free--;
			while(minOption[1] < optionTable[minOption[0]].length && !optionTable[minOption[0]][minOption[1]]){
				while(minOption[0] < optionTable.length && !optionTable[minOption[0]][minOption[1]])
					minOption[0]++;
				if(minOption[0] == optionTable.length){
					minOption[1]++;
					minOption[0] = 0;
				}
			}
		}
	}
	
	public String toString(){
		int i, j;
		String string = "";
		for(i = 0; i < optionTable.length; i++)
			for(j = 0; j < optionTable[i].length; j++)
				if(optionTable[i][j])
					string = string + "{" + i + ", " + j + "}";
		return string;
	}
}

