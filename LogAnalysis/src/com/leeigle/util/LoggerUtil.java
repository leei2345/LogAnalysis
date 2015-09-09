package com.leeigle.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggerUtil {

  private static Logger httpLogger = LoggerFactory.getLogger("HttpLogger");


  public static void HttpInfoLog(String log) {
    httpLogger.info(log);
  }

}
