package com.leeigle.view;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.*;
import javax.swing.*;
public class JProgressBarTest  extends JFrame{

    public JProgressBarTest() {
        super();
        setTitle("表格");
        setBounds(100,100,350,150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JButton button = new JButton("           ");
        final JButton button2 = new JButton("完成");
        button2.setEnabled(false); //初始化时不可用
        button.setBounds(100,100,100,100);
        
        final JProgressBar progressBar = new JProgressBar();
        progressBar.setStringPainted(true);  //显示提示信息
        progressBar.setIndeterminate(false);   //确定进度的进度条
        //progressBar.setIndeterminate(true);   //不确定进度的进度条
        //progressBar.setString("升级中...");    //确定信息时加上此条，则提示升级中，没有%比，如是不加上这个，则会提示%
        setLayout(new FlowLayout(2,10,10));
        getContentPane().add(button);  //布局处理
        getContentPane().add(button2);  //布局处理
        getContentPane().add(progressBar);  //布局处理
        new Progress(progressBar,button2).start();   //自定义类progress
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        JProgressBarTest jProgressBarTest = new JProgressBarTest();
        jProgressBarTest.setVisible(true);
    }

}
class Progress extends Thread{//自定义类progress
    private final int []progressValue = {6,18,27,39,51,66,81,100};
    private JProgressBar progressBar;
    private JButton button;
    public Progress(JProgressBar progressBar,JButton button)
    {
        this.progressBar = progressBar;
        this.button =button;
    }
    public void run()
    {
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
        button.setEnabled(true);  //按钮可用
    }
}