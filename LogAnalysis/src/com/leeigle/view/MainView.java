package com.leeigle.view;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;


/**
 * 主界面
 * @author leei
 *
 */
public class MainView implements ActionListener {
	
	private JFrame frame = new JFrame("日志解析工具");
	private JTabbedPane tabPane = new JTabbedPane();
	private Container con = new Container();
	private JPanel panel1 = new JPanel();
	private JPanel panel2 = new JPanel();
	private JPanel panel3 = new JPanel();
	private JLabel label1 = new JLabel("选择文件");
	private JTextField text1 = new JTextField(20);
	private JButton button1 = new JButton("...");
	private JFileChooser jfc = new JFileChooser();
	private JButton button3 = new JButton("解析");
	private JProgressBar progressBar = new JProgressBar();
	private List<String> filePathList = new ArrayList<String>();
	
	public MainView() {
		jfc.setCurrentDirectory(new File("d://"));// 文件选择器的初始目录定为d盘
		double lx = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double ly = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		frame.setLocation(new Point((int) (lx / 2) - 250, (int) (ly / 2) - 250));// 设定窗口出现位置
		frame.setSize(470, 450);// 设定窗口大小
		frame.setContentPane(tabPane);// 设置布局
		panel1.setLayout(new  FlowLayout(FlowLayout.LEFT,20,0));
		panel1.add(label1);
		panel1.add(text1);
		panel1.add(button1);
		panel1.setBounds(10, 10, 440, 30);
		
		panel2.setLayout(new  FlowLayout(FlowLayout.LEFT,10,0));
		panel2.setBounds(10, 50, 440, 240);
		
		button3.setBounds(360, 300, 60, 20);
		
		button1.addActionListener(this); // 添加事件处理
		button3.addActionListener(this); // 添加事件处理
		
		panel3.setBounds(10, 350, 440, 30);
		progressBar.setStringPainted(true);  //显示提示信息
		progressBar.setIndeterminate(false); 
		progressBar.setPreferredSize(new Dimension(260, 20));
		panel3.add(progressBar);
		 
		con.add(panel1);
		con.add(panel2);
		con.add(panel3);
		con.add(button3);
		frame.setVisible(true);// 窗口可见
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 使能关闭窗口，结束程序
		tabPane.add("添加文件并解析", con);// 添加布局1
	}
	/**
	 * 时间监听的方法
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource().equals(button1)) {// 判断触发方法的按钮是哪个
			jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);// 设定只能选择到文件
			jfc.setMultiSelectionEnabled(true);
			int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
			if (state == 1) {
				return;
			} else {
				File[] fs = jfc.getSelectedFiles();
				String dirPath = null;
				for (File file : fs) {
					String fileName = file.getName();
					String filePath = file.getAbsolutePath();
					if (StringUtils.isBlank(dirPath)) {
						dirPath = file.getParentFile().getPath();
					}
					JLabel innerLabel = new JLabel(fileName);
					panel2.add(innerLabel);
					if (fileName.contains(".txt") || fileName.contains(".log")) {
						filePathList.add(filePath);
					}
				}
				panel2.validate();
				text1.setText(dirPath);
			}
		}
		// 绑定到选择文件，先择文件事件
		if (e.getSource().equals(button3)) {
			if (filePathList.size() == 0) {
				JOptionPane.showMessageDialog(null, "没有可解析日志文件.", "警告",JOptionPane.ERROR_MESSAGE);  
			} else {
				new Thread(new ProgressView(progressBar, filePathList)).start();
			}
		}
	}
	public static void main(String[] args) {
		new MainView();
	}
}
