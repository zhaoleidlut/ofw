package com.htong.gzzd.service;

import com.htong.dao.GzzdHistoryDao;

public class GzzdThread extends Thread {
	private GzzdHistoryDao gzzdHistoryDao;
	
	public GzzdThread(GzzdHistoryDao gzzdHistoryDao) {
		this.gzzdHistoryDao = gzzdHistoryDao;
	}

	@Override
	public void run() {
		
	}
	
	

}
