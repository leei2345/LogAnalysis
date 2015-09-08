package com.leeigle.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JProgressBar;

import com.leeigle.runner.LogHandle;

public class ProgressView implements Runnable {

	private JProgressBar progressBar;
	private List<String> filePathList;
	private ExecutorService threadPool = Executors.newFixedThreadPool(3);
	
	public ProgressView(JProgressBar progressBar, List<String> filePathList)	{
		this.progressBar = progressBar;
		this.filePathList = filePathList;
	}
	@Override
	public void run() {
		//首先计算总的行数
		int count = 0;
		for (String string : filePathList) {
			File file = new File(string);
			LineNumberReader reader;
			try {
				reader = new LineNumberReader(new FileReader(file));
				count += reader.getLineNumber();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		CountDownLatch cdl = new CountDownLatch(count);
		for (String filePath : filePathList) {
			LogHandle handle = new LogHandle(filePath, cdl, progressBar);
			threadPool.execute(handle);
		}
        progressBar.setIndeterminate(false);  //采用确定的进度条
        try {
			cdl.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        progressBar.setString("解析完成.");  //提示信息
	}

}
