package com.leeigle.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.leeigle.runner.LogHandle;
import com.leeigle.util.CountDownLatchUtils;

public class ProgressView implements Runnable {

	private JProgressBar progressBar;
	private List<String> filePathList;
	private JPanel panel;
	private ExecutorService threadPool = Executors.newFixedThreadPool(3);
	
	public ProgressView(JProgressBar progressBar, List<String> filePathList, JPanel panel)	{
		this.progressBar = progressBar;
		this.filePathList = filePathList;
		this.panel = panel;
	}
	@Override
	public void run() {
		//首先计算总的行数
		int count = 0;
		for (String string : filePathList) {
			File file = new File(string);
			LineNumberReader reader = null;
			try {
				reader = new LineNumberReader(new FileReader(file));
				reader.skip(Long.MAX_VALUE);  
				int lineNum = reader.getLineNumber();
				count += lineNum;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		CountDownLatchUtils cdl = new CountDownLatchUtils(count);
		for (String filePath : filePathList) {
			LogHandle handle = new LogHandle(filePath, cdl, progressBar, panel);
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
