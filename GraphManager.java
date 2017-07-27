import java.util.Hashtable;

public class GraphManager {
	Graph[] startBlocks;
	
	public GraphManager(int numStarters, int[][] edgeAdjacency, int K, int KK, int O){
		int i;
		startBlocks = new Graph[numStarters];
		for(i = 0; i < startBlocks.length; i++)
			startBlocks[i] = new Graph(edgeAdjacency, K, KK, O, this);
	}
	
	public void killStandardEdges(){
		for(int i = 0; i < startBlocks.length; i++)
			startBlocks[i].killStandardEdges();
	}
	
	public void solve(){
		getNext(-1).solve(0);
		System.out.println("\n\n  https://www.linkedin.com/in/danigannon/");
	}
	
	public Graph getNext(int index){
		index++;
		if(index < startBlocks.length)
			return new Graph(startBlocks[index]);
		return null;
	}
	
	public void setHumanIndex(int blockNum, int humanIndex, int[] label){
		startBlocks[blockNum].setHumanLabel(humanIndex, label);
		for(int i = 0; i < startBlocks.length; i++){
			if(i != blockNum)
				startBlocks[i].copyEdgeStuff(startBlocks[blockNum]);
		}
	}
	
	public void findRepeatEdges(){
		Hashtable<String, String> edgetable = new Hashtable<String, String>();
		int[] edge;
		for(int i = 0; i < startBlocks.length; i++){
			for(int j = 0; j < startBlocks[i].que.length; j++){
				if(startBlocks[i].que[j].label[0] >= 0){
					for(int k = 0; k < startBlocks[i].que[j].downStream.length; k++){
						if(startBlocks[i].que[j].downStream[k].label[0] >= 0 && startBlocks[i].que[j].downStream[k].humanIndex > startBlocks[i].que[j].humanIndex){
							edge = startBlocks[i].getEdgeLabel(startBlocks[i].que[j].downStream[k].label, startBlocks[i].que[j].label);
							String string1 = edge[0] + " " + edge[1] + " " + edge[2];
							String string2 = "{" + i + ", " + startBlocks[i].que[j].humanIndex + ", " + startBlocks[i].que[j].downStream[k].humanIndex + "}";
							if(edgetable.containsKey(string1))
								System.out.println(edgetable.get(string1) + " :: " + string2);
							else
								edgetable.put(string1, string2);
						}
					}
				}
			}
		}
		System.out.println("Done");
	}
	
	public String toString(){
		String string = "";
		for(int i = 0; i < startBlocks.length; i++)
			string = string + "\nBlock " + i + "\n" + startBlocks[i].toString();
		return string;
	}
}

