package com.leeigle.runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;

import javax.swing.JProgressBar;

import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class LogHandle implements Runnable {
	
	private String filePath;
	private CountDownLatch cdl;
	private JProgressBar progress;
	private File file;
	private static final Pattern pattern = Pattern.compile("account=\"(\\d{11})\".*srcIp=\"(.*?)\".*accessTime=\"(.*?)\"");
	
	public LogHandle(String filePath, CountDownLatch cdl, JProgressBar progress) {
		this.filePath = filePath;
		this.cdl = cdl;
		this.progress = progress;
		this.file = new File(filePath);
	}

	@Override
	public void run() {
		OutputStream out;
		WritableSheet sheet = null;
		BufferedReader reader = null;
		try {
			out = new FileOutputStream(new File(filePath.replaceAll("\\..*?$", ".xls")));
			WritableWorkbook workbook = Workbook.createWorkbook(out);
			sheet = workbook.createSheet("sheet1", 0);
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
}
