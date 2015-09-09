package com.leeigle.runner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JProgressBar;

import org.apache.commons.lang3.StringUtils;

import com.leeigle.http.TaobaoApiHandler;
import com.leeigle.util.CountDownLatchUtils;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class LogHandle implements Runnable {
	
	private String filePath;
	private CountDownLatchUtils cdl;
	private JProgressBar progress;
	private File file;
	private static final Pattern pattern = Pattern.compile("account=\"(\\d{11})\".*dstIp=\"(.*?)\".*accessTime=\"(.*?)\".*url=\"(.*?)\"");
	private static final Pattern wordPattern = Pattern.compile("[word|wd|q]{1}=(.*?)[\\&]{0,1}?");
	
	public LogHandle(String filePath, CountDownLatchUtils cdl, JProgressBar progress) {
		this.filePath = filePath;
		this.cdl = cdl;
		this.progress = progress;
		this.file = new File(filePath);
	}

	@Override
	public void run() {
		OutputStream out = null;
		WritableSheet sheet = null;
		BufferedReader reader = null;
		WritableWorkbook workbook = null;
		try {
			out = new FileOutputStream(new File(filePath.replaceAll("\\..*?$", ".xls")));
			workbook = Workbook.createWorkbook(out);
			sheet = workbook.createSheet("sheet1", 0);
			reader = new BufferedReader(new FileReader(file));
			String line;
			try {
				Label no1 = new Label(0,0,"手机");
				sheet.addCell(no1);
				Label no2 = new Label(1,0,"地区");
				sheet.addCell(no2);
				Label no3 = new Label(2,0,"时间");
				sheet.addCell(no3);
				Label no4 = new Label(3,0,"关键词");
				sheet.addCell(no4);
			} catch (RowsExceededException e) {
				e.printStackTrace();
			} catch (WriteException e) {
				e.printStackTrace();
			}
			int index = 1;
			while ((line = reader.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.find()) {
					String phone = matcher.group(1);
					String ip = matcher.group(2);
					TaobaoApiHandler http = new TaobaoApiHandler(ip);
					String area = "";
					for (int retry = 0; retry < 3; retry++) {
						area = http.getArea();
						if (!StringUtils.isBlank(area)) {
							break;
						}
					}
					if (StringUtils.isBlank(area)) {
						area = ip;
					}
					String time = matcher.group(3);
					String url = matcher.group(4);
					String word = null;
					if (!StringUtils.isBlank(url)) {
						url = URLDecoder.decode(url, "UTF-8");
						Matcher wordm = wordPattern.matcher(url);
						if (wordm.find()) {
							word = wordm.group(1);
						}
					}
					Label inner1 = new Label(0, index, phone);
					Label inner2 = new Label(1, index, area);
					Label inner3 = new Label(2, index, time);
					Label inner4 = new Label(3, index, word);
					sheet.addCell(inner1);
					sheet.addCell(inner2);
					sheet.addCell(inner3);
					sheet.addCell(inner4);
					index++;
				} 
				cdl.countDown();
				int precent = cdl.getPrecent();
				progress.setValue(precent);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.write();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        try {
				workbook.close();
			} catch (WriteException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Pattern wordPattern = Pattern.compile("[q|word|wd]{1}\\=(.*?)\\&?.*");
		Matcher m = wordPattern.matcher("http://www.baidu.com/s?word=site:m.ladyyu.com&sdsd=sds");
		if (m.find()) {
			System.out.println(m.group(1));
		}
	}
	
}
