package com.leeigle.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import com.leeigle.util.LoggerUtil;


/**
 * 改写sina提供demo
 * @author leei
 *
 */
public class TaobaoApiHandler {

  private String urlStr;
  private static String apiUrl = "http://ip.taobao.com/service/getIpInfo.php?ip=";
  private static URLConnection conn = null;

  public TaobaoApiHandler(String ipStr) {
    try {
      String urStr = apiUrl + ipStr;
      URL url = new URL(urStr);
      this.urlStr = urStr;
      conn = url.openConnection();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    conn.setRequestProperty("accept", "*/*");
    conn.setRequestProperty("connection", "Keep-Alive");
    conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
    conn.setConnectTimeout(10000);
    conn.setReadTimeout(10000);
  }

  public void setConnTimeOut(int time) {
    conn.setConnectTimeout(time);
  }

  public void setReadTimeOut(int time) {
    conn.setReadTimeout(time);
  }

  /**
   * 向指定URL发送GET方法的请求
   * 
   * @param url
   *            发送请求的URL
   * @param param
   *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
   * @return URL 所代表远程资源的响应结果
   */
  public String sendGet() {
    String result = "";
    BufferedReader in = null;
    StringBuffer logStr = new StringBuffer("[GetRequest][" + urlStr + "]");
    try {
      // 建立实际的连接
      conn.connect();
      // 定义 BufferedReader输入流来读取URL的响应
      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        result += line;
      }
    }
    catch (SocketTimeoutException e) {
      logStr.append("[Exception][SocketTimeoutException]");
    }
    catch (UnknownHostException e) {
      logStr.append("[Exception][UnknownHostException]");
    }
    catch (IOException e) {
      logStr.append("[Exception][IOException]");
    }
    catch (Exception e) {
      logStr.append("[Exception][" + e.getMessage() + "]");
    }
    finally {
      try {
        if (in != null) {
          in.close();
        }
      }
      catch (Exception e2) {
        e2.printStackTrace();
      }
    }
    logStr.append("[Response][" + result + "]");
    LoggerUtil.HttpInfoLog(logStr.toString());
    return result;
  }

  /**
   * 向指定 URL 发送POST方法的请求
   * 
   * @param url
   *            发送请求的 URL
   * @param param
   *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
   * @return 所代表远程资源的响应结果
   */
  public String sendPost(String param) {
    PrintWriter out = null;
    BufferedReader in = null;
    String result = "";
    StringBuffer logStr = new StringBuffer("[PostRequest][" + urlStr + "][" + param + "]");
    try {
      conn.connect();
      // 获取URLConnection对象对应的输出流
      out = new PrintWriter(conn.getOutputStream());
      // 发送请求参数
      out.print(param);
      // flush输出流的缓冲
      out.flush();
      // 定义BufferedReader输入流来读取URL的响应
      in = new BufferedReader(
          new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        result += line;
      }
    }
    catch (SocketTimeoutException e) {
      logStr.append("[Exception][SocketTimeoutException]");
    }
    catch (UnknownHostException e) {
      logStr.append("[Exception][UnknownHostException]");
    }
    catch (IOException e) {
      logStr.append("[Exception][IOException]");
    }
    catch (Exception e) {
      logStr.append("[Exception][" + e.getMessage() + "]");
    }
    finally {
      try {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
    }
    logStr.append("/n[Response][" + result + "]");
    LoggerUtil.HttpInfoLog(logStr.toString());
    return result;
  }

  public static void main(String[] args) {
    TaobaoApiHandler s = new TaobaoApiHandler("http://www.ganji.com/pub/pub.php?act=pub&method=load&cid=6&mcid=14&domain=bj&h=2&qq-pf-to=pcqq.c2c");
    String html = s.sendGet();
    System.out.println(html);
  }
}