package de.normalisiert.utils.graphs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class SCCResult {
	private Set nodeIDsOfSCC = null;
	private ArrayList[] adjList = null;
	private int lowestNodeId = -1;
	
	public SCCResult(ArrayList[] adjList, int lowestNodeId) {
		this.adjList = adjList;
		this.lowestNodeId = lowestNodeId;
		this.nodeIDsOfSCC = new HashSet();
		if (this.adjList != null) {
			for (int i = this.lowestNodeId; i < this.adjList.length; i++) {
				if (this.adjList[i].size() > 0) {
					this.nodeIDsOfSCC.add(new Integer(i));
				}
			}
		}
	}

	public ArrayList[] getAdjList() {
		return adjList;
	}

	public int getLowestNodeId() {
		return lowestNodeId;
	}
        
        public int getNodes() {
		return lowestNodeId;
	}
}
