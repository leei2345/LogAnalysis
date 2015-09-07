package com.leeigle.view;

import javax.swing.JProgressBar;

public class ProgressView implements Runnable {

	private final int []progressValue = {6,18,27,39,51,66,81,100};
	private JProgressBar progressBar;
	
	public ProgressView(JProgressBar progressBar)
	{
		this.progressBar = progressBar;
	}
	@Override
	public void run() {
	        for(int i=0;i<progressValue.length;i++)
	        {
	            try
	            {
	                Thread.sleep(1000);
	            }catch(Exception e)
	            {
	                e.printStackTrace();
	            }
	            progressBar.setValue(progressValue[i]);  //进度值
	        }
	        progressBar.setIndeterminate(false);  //采用确定的进度条
	        //progressBar.setIndeterminate(true);   //不确定进度的进度条
	        progressBar.setString("升级完成.");  //提示信息
	}

}
