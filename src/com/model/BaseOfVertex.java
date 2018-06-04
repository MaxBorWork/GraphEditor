package com.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BaseOfVertex {
	ArrayList<ArrayList<Integer>> base = new ArrayList<ArrayList<Integer>>();

	public void add(ArrayList<Integer> vv) {
		// TODO Auto-generated method stub
		base.add(vv);
	}
	
	public ArrayList<Integer> giveWeight(int i) {
		// TODO Auto-generated method stub
		return base.get(i);
	}

	public void set(int index1, ArrayList<Integer> zhach) {
		base.set(index1, zhach);
		
	}

	public int size() {
		// TODO Auto-generated method stub
		return base.size();
	}

	public int findEstr(int i) {
		ArrayList<Integer> vertex= new ArrayList();
		ArrayList<Integer> ekstr= new ArrayList();
		for(int ind=0; ind<20; ind++) {
			ekstr.add(ind, null);
		}
		vertex=  this.giveWeight(i);
		for(int in=0; in<vertex.size(); in++) {
			if(vertex.get(in)!=null) {
				ekstr.set(in, vertex.get(in));
				ekstr =this.helpEkstr(ekstr, in, in);
			}
		}
		int max=0;
		for (int k=0; k<ekstr.size(); k++) {
			if(ekstr.get(k)!=null) {if(ekstr.get(k)>max){max=ekstr.get(k);}}
		}
		return max;
		
	}

	private ArrayList<Integer> helpEkstr(ArrayList<Integer> ekstr, int in, int in2) {
		ArrayList<Integer> vertex= new ArrayList();
		vertex=  this.giveWeight(in2);
		for(int i=0; i<vertex.size(); i++) {
			if(vertex.get(i)!=null) {
				int plus = ekstr.get(in);
				int result = plus+  vertex.get(i);
				ekstr.set(in, result);
				ekstr =this.helpEkstr(ekstr, in, i);
			}		
	}	return ekstr;
		}


}
