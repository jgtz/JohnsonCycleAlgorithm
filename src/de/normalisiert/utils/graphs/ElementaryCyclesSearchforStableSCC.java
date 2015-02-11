package de.normalisiert.utils.graphs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;



/**
 * Searchs all elementary cycles in a given directed graph. The implementation
 * is independent from the concrete objects that represent the graphnodes, it
 * just needs an array of the objects representing the nodes the graph
 * and an adjacency-matrix of type boolean, representing the edges of the
 * graph. It then calculates based on the adjacency-matrix the elementary
 * cycles and returns a list, which contains lists itself with the objects of the 
 * concrete graphnodes-implementation. Each of these lists represents an
 * elementary cycle.<br><br>
 *
 * The implementation uses the algorithm of Donald B. Johnson for the search of
 * the elementary cycles. For a description of the algorithm see:<br>
 * Donald B. Johnson: Finding All the Elementary Circuits of a Directed Graph.
 * SIAM Journal on Computing. Volumne 4, Nr. 1 (1975), pp. 77-84.<br><br>
 *
 * The algorithm of Johnson is based on the search for strong connected
 * components in a graph. For a description of this part see:<br>
 * Robert Tarjan: Depth-first search and linear graph algorithms. In: SIAM
 * Journal on Computing. Volume 1, Nr. 2 (1972), pp. 146-160.<br>
 * 
 * @author Frank Meyer, web_at_normalisiert_dot_de
 * @version 1.2, 22.03.2009
 *
 */

/**
 * The original program has been modified to adapt it to the needs of finding stable motifs
 * in the expanded network representation discussed in 	arXiv:1304.3467 [q-bio.MN]
 * 
 * @author JGTZ
 * @version 26.04.2013
 */


public class ElementaryCyclesSearchforStableSCC {
	/** List of cycles */
	private ArrayList<ArrayList<String>> cycles = null;

	/** Adjacency-list of graph */
	private int[][] adjList = null;
        
	/** self-loops in the graph */
	private ArrayList selfLoopsSCC = new ArrayList();  

	/** Graphnodes */
	private String[] graphNodes = null;
        
        /** Graphnodes */
	private ArrayList<String> graphNodesList;

	/** Blocked nodes, used by the algorithm of Johnson */
	private boolean[] blocked = null;

	/** B-Lists, used by the algorithm of Johnson */
	private ArrayList[] B = null;

	/** Stack for nodes, used by the algorithm of Johnson */
	private ArrayList stack = null;

        private ArrayList allSelfLoopsSCC = new ArrayList();
        
        private ArrayList<Integer> allSelfLoopsSCCComple = new ArrayList();
        
        private ArrayList<Integer> allSelfLoopsSCCCompos = new ArrayList();
        
        
        
	/**
	 * Constructor.
	 *
	 * @param matrix adjacency-matrix of the graph
	 * @param graphNodes array of the graphnodes of the graph; this is used to
	 * build sets of the elementary cycles containing the objects of the original
	 * graph-representation
	 */
	public ElementaryCyclesSearchforStableSCC(boolean[][] matrix, String[] graphNodes) {
		this.graphNodes = graphNodes;
                this.graphNodesList=new ArrayList(Arrays.asList(graphNodes));
		this.adjList = AdjacencyList.getAdjacencyList(matrix);
	}
        
        public ElementaryCyclesSearchforStableSCC(int[][] adjList, String[] graphNodes) {
		this.graphNodes = graphNodes;
                this.graphNodesList=new ArrayList(Arrays.asList(graphNodes));
		this.adjList = adjList;
                int index,index2;
                Integer Index;
                String dummy;
                for(int i=0;i<adjList.length;i++){
                    Arrays.sort(adjList[i]);
                    if(Arrays.binarySearch(adjList[i], i)>-1){
                        selfLoopsSCC.add(i);                       
                        allSelfLoopsSCC.add(i);
                        if (graphNodes[i].startsWith("-")){
                            Index=new Integer(graphNodesList.indexOf(graphNodes[i].split("-")[1]));
                        }
                        else{
                            Index=new Integer(graphNodesList.indexOf("-"+Integer.valueOf(graphNodes[i])));
                        }
                        allSelfLoopsSCCComple.add(Index); //this is the index of the complement of the nodes with selfloops
                        
//                        if (graphNodes[i].startsWith("-")){
//                            index2=graphNodesList.indexOf(graphNodes[i].split("-")[1]);
//                            allSelfLoopsSCC.add(index2);
//                            for(int j=0;j<adjList[index2].length;j++){
//                                index=adjList[index2][j];
//                                if(Math.rint(Double.parseDouble(graphNodes[index]))!=Double.parseDouble(graphNodes[index])){
//                                    if(!allSelfLoopsSCC.contains(index)){allSelfLoopsSCC.add(index);}                                  
//                                }
//                            }
//                        }
//                        else{
//                            index2=graphNodesList.indexOf("-"+Integer.valueOf(graphNodes[i]));
//                            allSelfLoopsSCC.add(index2);
//                            for(int j=0;j<adjList[index2].length;j++){
//                                index=adjList[index2][j];
//                                if(Math.rint(Double.parseDouble(graphNodes[index]))!=Double.parseDouble(graphNodes[index])){
//                                    if(!allSelfLoopsSCC.contains(index)){allSelfLoopsSCC.add(index);}                                  
//                                }
//                            }
//                        }
                    }
                }
                for(int i1=0;i1<allSelfLoopsSCCComple.size();i1++){
                    for(int i=0;i<adjList[allSelfLoopsSCCComple.get(i1)].length;i++){                    
                        dummy=graphNodes[adjList[allSelfLoopsSCCComple.get(i1)][i]];
                        if(Math.rint(Double.parseDouble(dummy))!=Double.parseDouble(dummy)){
                            if(!allSelfLoopsSCCCompos.contains(new Integer(adjList[allSelfLoopsSCCComple.get(i1)][i]))){
                                allSelfLoopsSCCCompos.add(new Integer(adjList[allSelfLoopsSCCComple.get(i1)][i]));
                            }                            
                        }
                    }
                }

        
        }

        public ElementaryCyclesSearchforStableSCC(int[][] adjList, String[] graphNodes, int attractor) {
		
            this.graphNodes = graphNodes;
                this.graphNodesList=new ArrayList(Arrays.asList(graphNodes));
		this.adjList = adjList;
                int index,index2;
                Integer Index;
                String dummy;
                for(int i=0;i<adjList.length;i++){
                    Arrays.sort(adjList[i]);
                    if(Arrays.binarySearch(adjList[i], i)>-1){
                        selfLoopsSCC.add(i);                       
                        allSelfLoopsSCC.add(i);

//                        if (graphNodes[i].startsWith("-")){
//                            index2=graphNodesList.indexOf(graphNodes[i].split("-")[1]);
//                            allSelfLoopsSCC.add(index2);
//                            for(int j=0;j<adjList[index2].length;j++){
//                                index=adjList[index2][j];
//                                if(Math.rint(Double.parseDouble(graphNodes[index]))!=Double.parseDouble(graphNodes[index])){
//                                    if(!allSelfLoopsSCC.contains(index)){allSelfLoopsSCC.add(index);}                                  
//                                }
//                            }
//                        }
//                        else{
//                            index2=graphNodesList.indexOf("-"+Integer.valueOf(graphNodes[i]));
//                            allSelfLoopsSCC.add(index2);
//                            for(int j=0;j<adjList[index2].length;j++){
//                                index=adjList[index2][j];
//                                if(Math.rint(Double.parseDouble(graphNodes[index]))!=Double.parseDouble(graphNodes[index])){
//                                    if(!allSelfLoopsSCC.contains(index)){allSelfLoopsSCC.add(index);}                                  
//                                }
//                            }
//                        }
                    }
                }
                for(int i1=0;i1<allSelfLoopsSCCComple.size();i1++){
                    for(int i=0;i<adjList[allSelfLoopsSCCComple.get(i1)].length;i++){                    
                        dummy=graphNodes[adjList[allSelfLoopsSCCComple.get(i1)][i]];
                        if(Math.rint(Double.parseDouble(dummy))!=Double.parseDouble(dummy)){
                            if(!allSelfLoopsSCCCompos.contains(new Integer(adjList[allSelfLoopsSCCComple.get(i1)][i]))){
                                allSelfLoopsSCCCompos.add(new Integer(adjList[allSelfLoopsSCCComple.get(i1)][i]));
                            }                            
                        }
                    }
                }

        
        }
              
 
        public ElementaryCyclesSearchforStableSCC(int[][] adjList, String[] graphNodes, ArrayList<String> sources) {
		this.graphNodes = graphNodes;
                this.graphNodesList=new ArrayList(Arrays.asList(graphNodes));
		this.adjList = adjList;
                int index,index2;
                Integer Index;
                String dummy;
                for(int i=0;i<adjList.length;i++){
                    Arrays.sort(adjList[i]);
                    if(Arrays.binarySearch(adjList[i], i)>-1){
                        selfLoopsSCC.add(i);
                        dummy=graphNodes[i];
                        if(dummy.startsWith("-")){dummy=dummy.split("-")[1];}
                        if(!sources.contains(dummy)){
                            allSelfLoopsSCC.add(i);
                            if (graphNodes[i].startsWith("-")){
                                Index=new Integer(graphNodesList.indexOf(graphNodes[i].split("-")[1]));
                            }
                            else{
                                Index=new Integer(graphNodesList.indexOf("-"+Integer.valueOf(graphNodes[i])));
                            }
                            allSelfLoopsSCCComple.add(Index); //this is the index of the complement of the nodes with selfloops
                        }
//                        if (graphNodes[i].startsWith("-")){
//                            index2=graphNodesList.indexOf(graphNodes[i].split("-")[1]);
//                            allSelfLoopsSCC.add(index2);
//                            for(int j=0;j<adjList[index2].length;j++){
//                                index=adjList[index2][j];
//                                if(Math.rint(Double.parseDouble(graphNodes[index]))!=Double.parseDouble(graphNodes[index])){
//                                    if(!allSelfLoopsSCC.contains(index)){allSelfLoopsSCC.add(index);}                                  
//                                }
//                            }
//                        }
//                        else{
//                            index2=graphNodesList.indexOf("-"+Integer.valueOf(graphNodes[i]));
//                            allSelfLoopsSCC.add(index2);
//                            for(int j=0;j<adjList[index2].length;j++){
//                                index=adjList[index2][j];
//                                if(Math.rint(Double.parseDouble(graphNodes[index]))!=Double.parseDouble(graphNodes[index])){
//                                    if(!allSelfLoopsSCC.contains(index)){allSelfLoopsSCC.add(index);}                                  
//                                }
//                            }
//                        }
                    }
                }
                for(int i1=0;i1<allSelfLoopsSCCComple.size();i1++){
                    for(int i=0;i<adjList[allSelfLoopsSCCComple.get(i1)].length;i++){                    
                        dummy=graphNodes[adjList[allSelfLoopsSCCComple.get(i1)][i]];
                        if(Math.rint(Double.parseDouble(dummy))!=Double.parseDouble(dummy)){
                            if(!allSelfLoopsSCCCompos.contains(new Integer(adjList[allSelfLoopsSCCComple.get(i1)][i]))){
                                allSelfLoopsSCCCompos.add(new Integer(adjList[allSelfLoopsSCCComple.get(i1)][i]));
                            }                            
                        }
                    }
                }

        
        }        
        
        public ArrayList<ArrayList<String>> getSelfLoops() {		
		return this.allSelfLoopsSCC;
	}

        
	/**
	 * Returns List::List::Object with the Lists of nodes of all elementary
	 * cycles in the graph.
	 *
	 * @return List::List::Object with the Lists of the elementary cycles.
	 */
	public ArrayList<ArrayList<String>> getElementaryCycles() {
		this.cycles = new ArrayList();
		this.blocked = new boolean[this.adjList.length];
		this.B = new ArrayList[this.adjList.length];
		this.stack = new ArrayList();
		StrongConnectedComponents sccs = new StrongConnectedComponents(this.adjList);
		int s = 0;

		while (true) {
			SCCResult sccResult = sccs.getAdjacencyList(s);
			if (sccResult != null && sccResult.getAdjList() != null) {
				ArrayList[] scc = sccResult.getAdjList();
				s = sccResult.getLowestNodeId();
				for (int j = 0; j < scc.length; j++) {
					if ((scc[j] != null) && (scc[j].size() > 0)) {
						this.blocked[j] = false;
						this.B[j] = new ArrayList();
					}
				}

				this.findCycles(s, s, scc);
                                
				s++;
			} else {
				break;
			}
		}
                    for(int i=0;i<selfLoopsSCC.size();i++){
                        ArrayList cycle = new ArrayList();
                        cycle.add(this.graphNodes[((Integer) selfLoopsSCC.get(i))]);
                        if(!cycles.contains(cycle)){this.cycles.add(cycle);}
                    }
                
		return this.cycles;
	}

	/**
	 * Calculates the cycles containing a given node in a strongly connected
	 * component. The method calls itself recursively.
	 *
	 * @param v
	 * @param s
	 * @param adjList adjacency-list with the subgraph of the strongly
	 * connected component s is part of.
	 * @return true, if cycle found; false otherwise
	 */
	private boolean findCycles(int v, int s, ArrayList[] adjList) {
		boolean f = false;
                Integer Index;
                String[] cycleNames;
		this.stack.add(new Integer(v));
		this.blocked[v] = true;
                if (graphNodes[v].startsWith("-")){
                    Index=new Integer(graphNodesList.indexOf(graphNodes[v].split("-")[1]));
                    if (stack.contains(Index)){
                        f=true;
                    }
                }
                else if (Math.rint(Double.parseDouble(graphNodes[v]))==Double.parseDouble(graphNodes[v])){
                    Index=new Integer(graphNodesList.indexOf("-"+Integer.valueOf(graphNodes[v])));
                    if (stack.contains(Index)){
                        f=true;
                    }
                }
                                
                
                if(!f){
                    for (int i = 0; i < adjList[v].size(); i++) {
                            int w = ((Integer) adjList[v].get(i)).intValue();
                            // found cycle
                            if (w == s) {
                                    ArrayList<String> cycle = new ArrayList<String>();
                                    for (int j = 0; j < this.stack.size(); j++) {
                                            int index = ((Integer) this.stack.get(j)).intValue();
                                            cycle.add(this.graphNodes[index]);
                                    }
                                    cycleNames=new String[cycle.size()];
                                    cycleNames=cycle.toArray(cycleNames);
                                    Arrays.sort(cycleNames);
                                    cycle = new ArrayList(Arrays.asList(cycleNames));                                   
                                    this.cycles.add(cycle);
                                    f = true;
                                                                     
                            } else if (!this.blocked[w]) {
                                    if (this.findCycles(w, s, adjList)) {
                                            f = true;
                                    }
                            }
                    }
                }
		if (f) {
			this.unblock(v);
		} else {
			for (int i = 0; i < adjList[v].size(); i++) {
				int w = ((Integer) adjList[v].get(i)).intValue();
				if (!this.B[w].contains(new Integer(v))) {
					this.B[w].add(new Integer(v));
				}
			}
		}

		this.stack.remove(new Integer(v));
		return f;
	}

	/**
	 * Unblocks recursively all blocked nodes, starting with a given node.
	 *
	 * @param node node to unblock
	 */
	private void unblock(int node) {
		this.blocked[node] = false;
		ArrayList Bnode = this.B[node];
		while (Bnode.size() > 0) {
			Integer w = (Integer) Bnode.get(0);
			Bnode.remove(0);
			if (this.blocked[w.intValue()]) {
				this.unblock(w.intValue());
			}
		}
	}
                                       
	
	/**
	 * Returns List::List::Object with the Lists of nodes of all elementary
	 * cycles in the graph.
	 * 
         * @param maxCycleLength The method will only look for cycles of less
         * or equal than this length
	 * @return List::List::Object with the Lists of the elementary cycles.
	 */
	public ArrayList<ArrayList<String>> getElementaryCycles(int maxCycleLength) {
		this.cycles = new ArrayList();
		this.blocked = new boolean[this.adjList.length];
		this.B = new ArrayList[this.adjList.length];
		this.stack = new ArrayList();
		StrongConnectedComponents sccs = new StrongConnectedComponents(this.adjList);
		int s = 0;

		while (true) {
			SCCResult sccResult = sccs.getAdjacencyList(s);
			if (sccResult != null && sccResult.getAdjList() != null) {
				ArrayList[] scc = sccResult.getAdjList();
				s = sccResult.getLowestNodeId();
				for (int j = 0; j < scc.length; j++) {
					if ((scc[j] != null) && (scc[j].size() > 0)) {
						this.blocked[j] = false;
						this.B[j] = new ArrayList();
					}
				}

				this.findCycles(s, s, scc,maxCycleLength);
                                
				s++;
			} else {
				break;
			}
		}
                    for(int i=0;i<selfLoopsSCC.size();i++){
                        ArrayList cycle = new ArrayList();
                        cycle.add(this.graphNodes[((Integer) selfLoopsSCC.get(i))]);
                        if(!cycles.contains(cycle)){this.cycles.add(cycle);}
                    }
                
		return this.cycles;
	}

        
       	private boolean findCyclesNSL(int v, int s, ArrayList[] adjList) {
		Integer Index;
                boolean f = false;
		this.stack.add(new Integer(v));
		this.blocked[v] = true;
                //This is th cutoff of the max stack size, so that we do not run out of memory
                if(allSelfLoopsSCCComple.contains(v) || allSelfLoopsSCCCompos.contains(v)){
                    f=true;
                }
                else{
                    if (graphNodes[v].startsWith("-")){
                        Index=new Integer(graphNodesList.indexOf(graphNodes[v].split("-")[1]));
                        if (stack.contains(Index)){
                            f=true;
                        }
                    }
                    else if (Math.rint(Double.parseDouble(graphNodes[v]))==Double.parseDouble(graphNodes[v])){
                        Index=new Integer(graphNodesList.indexOf("-"+Integer.valueOf(graphNodes[v])));
                        if (stack.contains(Index)){
                            f=true;
                        }
                    }
                }
                if (!f) {
                    for (int i = 0; i < adjList[v].size(); i++) {
                            int w = ((Integer) adjList[v].get(i)).intValue();
                            // found cycle
                            if (w == s) {
                                    ArrayList cycle = new ArrayList();
                                    for (int j = 0; j < this.stack.size(); j++) {
                                            int index = ((Integer) this.stack.get(j)).intValue();
                                            cycle.add(this.graphNodes[index]);
                                    }
                                    this.cycles.add(cycle);
                                    f = true;
                            } else if (!this.blocked[w]) {
                                    if (this.findCyclesNSL(w, s, adjList)) {
                                            f = true;
                                    }
                            }
                    }
                }
                
		if (f) {
			this.unblock(v);
		} else {
			for (int i = 0; i < adjList[v].size(); i++) {
				int w = ((Integer) adjList[v].get(i)).intValue();
				if (!this.B[w].contains(new Integer(v))) {
					this.B[w].add(new Integer(v));
				}
			}
		}

		this.stack.remove(new Integer(v));
		return f;
	}
 
       	private boolean findCyclesNSL(int v, int s, ArrayList[] adjList,int maxLength) {
		boolean f = false;
		this.stack.add(new Integer(v));
		this.blocked[v] = true;
                Integer Index;
                //This is th cutoff of the max stack size, so that we do not run out of memory
                if(allSelfLoopsSCC.contains(v) || this.stack.size()>maxLength){
                    f=true;
                }
                else{
                    if (graphNodes[v].startsWith("-")){
                        Index=new Integer(graphNodesList.indexOf(graphNodes[v].split("-")[1]));
                        if (stack.contains(Index)){
                            f=true;
                        }
                    }
                    else if (Math.rint(Double.parseDouble(graphNodes[v]))==Double.parseDouble(graphNodes[v])){
                        Index=new Integer(graphNodesList.indexOf("-"+Integer.valueOf(graphNodes[v])));
                        if (stack.contains(Index)){
                            f=true;
                        }
                    }
                }
                if (!f) {
                    for (int i = 0; i < adjList[v].size(); i++) {
                            int w = ((Integer) adjList[v].get(i)).intValue();
                            // found cycle
                            if (w == s) {
                                    ArrayList cycle = new ArrayList();
                                    for (int j = 0; j < this.stack.size(); j++) {
                                            int index = ((Integer) this.stack.get(j)).intValue();
                                            cycle.add(this.graphNodes[index]);
                                    }
                                    this.cycles.add(cycle);
                                    f = true;
                            } else if (!this.blocked[w]) {
                                    if (this.findCyclesNSL(w, s, adjList,maxLength)) {
                                            f = true;
                                    }
                            }
                    }
                }
                
		if (f) {
			this.unblock(v);
		} else {
			for (int i = 0; i < adjList[v].size(); i++) {
				int w = ((Integer) adjList[v].get(i)).intValue();
				if (!this.B[w].contains(new Integer(v))) {
					this.B[w].add(new Integer(v));
				}
			}
		}

		this.stack.remove(new Integer(v));
		return f;
	}
 
    
        
        
        public ArrayList getElementaryCyclesNSL() {
		this.cycles = new ArrayList();
		this.blocked = new boolean[this.adjList.length];
		this.B = new ArrayList[this.adjList.length];
		this.stack = new ArrayList();
		StrongConnectedComponents sccs = new StrongConnectedComponents(this.adjList);
		int s = 0;

		while (true) {
			SCCResult sccResult = sccs.getAdjacencyList(s);
			if (sccResult != null && sccResult.getAdjList() != null) {
				ArrayList[] scc = sccResult.getAdjList();
				s = sccResult.getLowestNodeId();
				for (int j = 0; j < scc.length; j++) {
					if ((scc[j] != null) && (scc[j].size() > 0)) {
						this.blocked[j] = false;
						this.B[j] = new ArrayList();
                                                if(selfLoopsSCC.contains(j)){
                                                    selfLoopsSCC.remove(selfLoopsSCC.indexOf(j));
                                                }
					}
				}

				this.findCyclesNSL(s, s, scc);
                                
				s++;
			} else {
				break;
			}
		}

		return this.cycles;
	}
        
        
        
        public ArrayList getElementaryCyclesNSL(int maxLength) {
		this.cycles = new ArrayList();
		this.blocked = new boolean[this.adjList.length];
		this.B = new ArrayList[this.adjList.length];
		this.stack = new ArrayList();
		StrongConnectedComponents sccs = new StrongConnectedComponents(this.adjList);
		int s = 0;

		while (true) {
			SCCResult sccResult = sccs.getAdjacencyList(s);
			if (sccResult != null && sccResult.getAdjList() != null) {
				ArrayList[] scc = sccResult.getAdjList();
				s = sccResult.getLowestNodeId();
				for (int j = 0; j < scc.length; j++) {
					if ((scc[j] != null) && (scc[j].size() > 0)) {
						this.blocked[j] = false;
						this.B[j] = new ArrayList();
                                                if(selfLoopsSCC.contains(j)){
                                                    selfLoopsSCC.remove(selfLoopsSCC.indexOf(j));
                                                }
					}
				}

				this.findCyclesNSL(s, s, scc,maxLength);
                                
				s++;
			} else {
				break;
			}
		}

		return this.cycles;
	}
	
        
        
	/**
	 * Calculates the cycles containing a given node in a strongly connected
	 * component. The method calls itself recursively.
	 *
	 * @param v
	 * @param s
	 * @param adjList adjacency-list with the subgraph of the strongly
	 * connected component s is part of.
         * @param maxCycleLength The method will only look for cycles of less 
         * or equal than this length
	 * @return true, if cycle found; false otherwise
	 */
	private boolean findCycles(int v, int s, ArrayList[] adjList, int maxCycleLength) {
		boolean f = false;
                Integer Index;
                String[] cycleNames;
		this.stack.add(new Integer(v));
		this.blocked[v] = true;
                //This is th cutoff of the max stack size, so that we do not run out of memory
                if(stack.size()>maxCycleLength){
                    f=true;
                }
                else{
                    if (graphNodes[v].startsWith("-")){
                        Index=new Integer(graphNodesList.indexOf(graphNodes[v].split("-")[1]));
                        if (stack.contains(Index)){
                            f=true;
                        }
                    }
                    else if (Math.rint(Double.parseDouble(graphNodes[v]))==Double.parseDouble(graphNodes[v])){
                        Index=new Integer(graphNodesList.indexOf("-"+Integer.valueOf(graphNodes[v])));
                        if (stack.contains(Index)){
                            f=true;
                        }
                    }
                }
                
                
                if(!f){
                    for (int i = 0; i < adjList[v].size(); i++) {
                            int w = ((Integer) adjList[v].get(i)).intValue();
                            // found cycle
                            if (w == s) {
                                    ArrayList<String> cycle = new ArrayList<String>();
                                    for (int j = 0; j < this.stack.size(); j++) {
                                            int index = ((Integer) this.stack.get(j)).intValue();
                                            cycle.add(this.graphNodes[index]);
                                    }
                                    cycleNames=new String[cycle.size()];
                                    cycleNames=cycle.toArray(cycleNames);
                                    Arrays.sort(cycleNames);
                                    cycle = new ArrayList(Arrays.asList(cycleNames));                                   
                                    this.cycles.add(cycle);
                                    f = true;
                                                                     
                            } else if (!this.blocked[w]) {
                                    if (this.findCycles(w, s, adjList,maxCycleLength)) {
                                            f = true;
                                    }
                            }
                    }
                }
		if (f) {
			this.unblock(v);
		} else {
			for (int i = 0; i < adjList[v].size(); i++) {
				int w = ((Integer) adjList[v].get(i)).intValue();
				if (!this.B[w].contains(new Integer(v))) {
					this.B[w].add(new Integer(v));
				}
			}
		}

		this.stack.remove(new Integer(v));
		return f;
	}



}

