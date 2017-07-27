public class GridLabelR {
	//Input a graph as {{Node 0 neighbors}, {Node 1 neighbors}, ...},
	public static final int[][]	PUT_YOUR_GRAPH_HERE = 	{{1, 4, 5}, {0, 2, 8}, {1, 3, 6}, {2, 4, 9}, {0, 3, 7},
            											 {0, 6, 9}, {2, 5, 7}, {4, 6, 8}, {1, 7, 9}, {3, 5, 8}};
	public static final int		NUMBER_OF_STARTERS	=	3;
	
	
	//{node#, block#, label[0], label[1]}
	//{{}} => no labels set
	public static final int[][] Labels = 	{{0, 0, 3, 0}, {1, 0, 10, 2}, {2, 0, 7, 0}, {3, 0, 9, 1}, {4, 0, 14, 2}, {5, 0, 4, 1}, {6, 0, 8, 2}, {7, 0, 14, 1}, {8, 0, 6, 0}, {9, 0, 5, 2},
											 {0, 1, 0, 1}, {1, 1, 2, 2}, {2, 1, 3, 1}, {3, 1, 0, 2}, {4, 1, 0, 0}, {5, 1, 3, 2}, {6, 1, 6, 0}, {7, 1, 10, 1}, {8, 1, 11, 0}, {9, 1, 5, 1},
											 {0, 2, 0, 1}, {1, 2, 6, 2}, {2, 2, 1, 0}};
	
	public static final int GRID_WIDTH = 15;
	public static final int GRID_DEPTH = 3;
	public static final boolean ERASE_STANDARD_EDGES = true;		//Erase edges between nodes (a, x) and (b, x)

	public static final int MULTI_ORDER = 1;
	
	
	
	
	
	//------------- Real Code -------------------
	
	public static void main(String[] args) {
		GraphManager GM = new GraphManager(NUMBER_OF_STARTERS, PUT_YOUR_GRAPH_HERE, GRID_WIDTH, GRID_DEPTH, MULTI_ORDER);
		int[] nodeLabel = new int[2];
		for(int i = 0; i < Labels.length; i++){
			if(Labels[i].length > 0){
				nodeLabel[0] = Labels[i][2];
				nodeLabel[1] = Labels[i][3];
				GM.setHumanIndex(Labels[i][1], Labels[i][0], nodeLabel);
			}
			else{
				nodeLabel[0] = 0;
				nodeLabel[1] = 0;
				boolean swapped = false;
				int j = 0;
				while(!swapped){
					if(GM.startBlocks[0].que[0].downStream.length > 0){
						GM.startBlocks[0].swap(GM.startBlocks[0].que[0].downStream[0].heapIndex, GM.startBlocks[0].que.length-1);
						swapped = true;
					}
					j++;
				}
				GM.setHumanIndex(0, 0, nodeLabel);
			}
		}
		if(ERASE_STANDARD_EDGES)
			GM.killStandardEdges();

		
		System.out.println(GM);
		System.out.println("\n-------------Solving...\n");
		GM.solve();
	}

}

