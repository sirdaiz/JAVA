package com.scor.main;

import com.scor.implementations.FileServiceImpl;
import com.scor.interfaces.FileService;
import com.scor.utils.LOGUtils;


public class Init {
	private static final String nameInterviewXML = "jasper/informeTUW_70901_19112015_112137.xml";
	private static final String savePath = "/home/pepe/test.pdf";
	private static final String mainJasper = "jasper/datosGenerales.jasper";
	private static final String xmlHeader = "/tuw";
	private static final String locale = "es";
	
	public static void main(String[] args) {
		try {
			FileService fileService = new FileServiceImpl();		
			fileService.savePDF(nameInterviewXML, xmlHeader, mainJasper, locale, savePath);
		} catch (Exception e) {
			LOGUtils.setInfo("Failed to create the PDF, check the log file: " + e.toString());
		}
		
		LOGUtils.setInfo("The file is created in : " + savePath);
	}

}
