package com.scor.interfaces;

public interface FileService {
	
	/**
	 * 
	 * @param ficheroXML
	 * @param recordPath
	 * @param plantilla
	 * @param locale
	 * @param nombreArchivo
	 */
	public void savePDF(String ficheroXML, String recordPath, String plantilla, String locale, String nombreArchivo); 
}
