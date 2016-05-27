package com.scor.utils;

import java.io.InputStream;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LOGUtils {

	private Logger log;
	private static LOGUtils instance = null;

	public LOGUtils() {
		this.log = Logger.getLogger(LOGUtils.class);
		InputStream ist = this.getClass().getResourceAsStream("log4j.properties");
		PropertyConfigurator.configure(ist);
	}

	public void reportInfo(String message) {
		log.info(message);
	}

	public void reportError(String message) {

		log.error(message);
	}
	
	public void reportError(Exception e) {
		log.error(e.getMessage());
		e.printStackTrace();
	}


	public void reportErrorCustomMessaje(String error, Exception e) {
		log.error(error, new Throwable(e.getMessage()));
	}

	/**
	 * Static methods
	 */

	public static LOGUtils getInstance() {
		if (instance == null) {
			instance = new LOGUtils();
		}
		return instance;
	}

	public static void setError(String e) {
		getInstance().reportError(e);
	}
	
	public static void setInfo(String e) {
		getInstance().reportInfo(e);
	}

	public static void setError(Exception e) {
		getInstance().reportError(e);
	}
}
