package com.scor.implementations;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.FontKey;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.PdfFont;
import net.sf.jasperreports.engine.util.JRLoader;

import com.scor.exceptions.FileException;
import com.scor.interfaces.FileService;
import com.scor.utils.LOGUtils;

@SuppressWarnings("deprecation")
public class FileServiceImpl implements FileService {
	
	/*
	 * (non-Javadoc)
	 * @see com.scor.interfaces.FileService#savePDF(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	public void savePDF(String nameInterviewXML, String xmlHeader, String mainJasper, String locale, String savePath) {
		String method = "savePDF";	
		
		try{	 
			JasperPrint print = makePDF(nameInterviewXML, xmlHeader, mainJasper, locale);
			FontKey keyArialBold = new FontKey("Helvetica-bold", true, false);  
			PdfFont fontArialBold = new PdfFont("Helvetica-Bold","Cp1252",false); 
			 
			Map fontMap = new HashMap();
			fontMap.put(keyArialBold,fontArialBold);
			 
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, savePath);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
			exporter.setParameter(JRExporterParameter.FONT_MAP, fontMap);
			exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8"); 
			
			exporter.exportReport();
		}catch (JRException e){
			LOGUtils.setError(e);
		}catch (Exception e){
			FileException exc = new FileException(this.getClass(), method, e);
			
			LOGUtils.setError(exc.toString());
		}
	}
	
	/*
	 * 
	 */
	private JasperPrint makePDF(String nameInterviewXML, String xmlHeader, String mainJasper, String locale){
		String method = "makePDF";
		JasperPrint print = null;
		
		try {
			InputStream is = findFileInProject(nameInterviewXML);
			
			if (is != null) {				
				Map <String, Object> hm = new HashMap <String, Object> ();			
				
				Locale localePDF = new Locale(locale);
				String dateToString = new SimpleDateFormat("EEEEEEEEE, dd MMMMMMMMM yyyy", localePDF).format(new Date()).toUpperCase();
				ResourceBundle myResources = ResourceBundle.getBundle("i18n/iReport",localePDF);
				
				hm.put(JRParameter.REPORT_LOCALE, localePDF);
				hm.put(JRParameter.REPORT_RESOURCE_BUNDLE, myResources);
				hm.put("fecha", dateToString);				
				hm.put("logoScor", new FileInputStream("jasper/logo.png"));
				hm.put("calculatorData", obtainCriticalData());
				
				hm.put("datosIniciales", new FileInputStream("jasper/datosIniciales.jasper"));
				hm.put("paramsTarificador", new FileInputStream("jasper/paramsTarificador.jasper"));
				hm.put("tarificacionCalculador", new FileInputStream("jasper/tarificacionCalculador.jasper"));
				hm.put("tarificacionGT", new FileInputStream("jasper/tarificacionGT.jasper"));
				hm.put("cuestionario", new FileInputStream("jasper/cuestionario.jasper"));
				hm.put("cuestionarioSeccion", new FileInputStream("jasper/cuestionarioSeccion.jasper"));
				hm.put("cuestionarioSeccionModificacion", new FileInputStream("jasper/cuestionarioSeccionModificacion.jasper"));
				hm.put("coberturasGeneral", new FileInputStream("jasper/coberturasGeneral.jasper"));
				hm.put("coberturasContratadas", new FileInputStream("jasper/coberturasContratadas.jasper"));
				hm.put("coberturasGeneralGT", new FileInputStream("jasper/coberturasGeneral.jasper"));
				hm.put("coberturasCuestionario", new FileInputStream("jasper/coberturasCuestionario.jasper"));
				hm.put("coberturasGeneralCalculador", new FileInputStream("jasper/coberturasGeneral.jasper"));
				
				InputStream in = new FileInputStream(mainJasper);
				JRXmlDataSource jrxmlds = new JRXmlDataSource(is, xmlHeader);
				JasperReport reporte = (JasperReport) JRLoader.loadObject(in); 
				print = JasperFillManager.fillReport(reporte, hm,jrxmlds);
			}
		} catch (JRException e){
			LOGUtils.setError(e);
		} catch (Exception e) {
			FileException exc = new FileException(this.getClass(), method, e);
			
			LOGUtils.setError(exc.toString());
		}

		
		return print;
	}
	
	/*
	 * 
	 */
	@SuppressWarnings("resource")
	private InputStream findFileInProject (String nameInterviewXML) throws IOException {
		InputStream is = null;
		String method = "findFileInProject";
		
		try {
			StringBuilder sbXML = new StringBuilder();
			BufferedReader reader= new BufferedReader( new FileReader (nameInterviewXML));
		    String  line = null;	  
		    String  ls = System.getProperty("line.separator");
	
			while( ( line = reader.readLine() ) != null ) {
				sbXML.append( line );
				sbXML.append( ls );
			}
			
			if (sbXML.toString()!= "") {
				is = new ByteArrayInputStream(sbXML.toString().getBytes());
			}
			
		} catch (FileNotFoundException e) {
			LOGUtils.setError("The file " + nameInterviewXML + " not found. Exception : " + e.toString());
		} catch (Exception e) {
			FileException exc = new FileException(this.getClass(), method, e);
			
			LOGUtils.setError(exc.toString());
		}
		
		return is;
	}
	
	/*
	 * 
	 */
	private List<String> obtainCriticalData() {
		List <String> result = new ArrayList <String> ();
		result.add("Age : 44 years");
		result.add("Sexe : Man");
		result.add("Système métrique : International");
		result.add("Unité de cholestérol : mg/dl");
		
		return result;
	}
}
