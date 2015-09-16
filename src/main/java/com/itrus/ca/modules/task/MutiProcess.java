package com.itrus.ca.modules.task;

import java.util.Date;
import java.util.List;

import com.itrus.ca.modules.task.entity.BasicInfoScca;

public class MutiProcess implements Runnable {

	private List<BasicInfoScca> sccaList;
	private String no;
	private List<Integer> in;
	
	public MutiProcess(){
	}
	
//	public MutiProcess(List<BasicInfoScca> sccaList){
//		this.sccaList = sccaList;
//	}
	
	public MutiProcess(List<Integer> in , String no){
		this.in = in ;
		this.no = no ;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Date date = new Date();
		System.out.println(date.getTime());
		for (int i = 0; i < in.size(); i++) {
			System.out.println(in.get(i)+"|||||||||||"+no);
		}
	}

}
