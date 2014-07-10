package org.abacus.report.jasper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

import org.abacus.common.web.JsfMessageHelper;
import org.abacus.definition.shared.constant.EnumList;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class JasperReportBean {

	@ManagedProperty(value = "#{jasperReportHandler}")
	private JasperReportHandler jasperReportHandler;
	
	@ManagedProperty(value = "#{jsfMessageHelper}")
	private JsfMessageHelper jsfMessageHelper;
	
	private EnumList.JasperReport jasperReport;

	public void reportListener(){
		System.out.println("ReportSelected:"+jasperReport.getName());
	}
	
	// Actions
	// ------------------------------------------------------------------------------------

	private String getPdfFilePath() {
		return "c:/temp/";
	}

	private String getPdfFileName(EnumList.JasperReport report) {
		return report.getName()+".pdf";
	}

	private String getJasperFile(EnumList.JasperReport report) {
		return "/jasper/"+report.getName()+".jasper";
	}

	private void generateReport(EnumList.JasperReport report) {
		try {
			Connection conn = jasperReportHandler.getConnection();
			File pdfFile = new File(getPdfFilePath(), getPdfFileName(report));
			pdfFile.delete();

			InputStream jasperStream = getClass().getResourceAsStream(getJasperFile(report));
			JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
			Map<String,Object> param = new HashMap<String, Object>();
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, param, conn);
			
			DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance(); 
			JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.awt.igno‌​re.missing.font","true"); 
			JasperExportManager.exportReportToPdfFile(jasperPrint, pdfFile.toString());
		} catch (Exception e) {
			jsfMessageHelper.addError("reportPrepareError", report.getName());
		}
	}

	public void downloadReport(EnumList.JasperReport report) throws IOException {
		generateReport(report);

		int DEFAULT_BUFFER_SIZE = 10240;
		// Prepare.
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

		File file = new File(getPdfFilePath(), getPdfFileName(report));
		BufferedInputStream input = null;
		BufferedOutputStream output = null;

		try {
			// Open file.
			input = new BufferedInputStream(new FileInputStream(file), DEFAULT_BUFFER_SIZE);

			// Init servlet response.
			response.reset();
			response.setHeader("Content-Type", "application/pdf");
			response.setHeader("Content-Length", String.valueOf(file.length()));
			response.setHeader("Content-Disposition", "inline; filename=\"" + getPdfFileName(report) + "\"");
			output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);

			// Write file contents to response.
			byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
			// Finalize task.
			output.flush();
		} catch (Exception e){
			jsfMessageHelper.addError("reportDownloadError", report.getName());
		} finally {
			// Gently close streams.
			close(output);
			close(input);
		}

		// Inform JSF that it doesn't need to handle response.
		// This is very important, otherwise you will get the following
		// exception in the logs:
		// java.lang.IllegalStateException: Cannot forward after response has
		// been committed.
		facesContext.responseComplete();
	}

	// Helpers (can be refactored to public utility class)
	// ----------------------------------------
	private static void close(Closeable resource) {
		if (resource != null) {
			try {
				resource.close();
			} catch (IOException e) {
				// Do your thing with the exception. Print it, log it or mail
				// it. It may be useful to
				// know that this will generally only be thrown when the client
				// aborted the download.
				e.printStackTrace();
			}
		}
	}

	public JasperReportHandler getJasperReportHandler() {
		return jasperReportHandler;
	}

	public void setJasperReportHandler(JasperReportHandler jasperReportHandler) {
		this.jasperReportHandler = jasperReportHandler;
	}

	public EnumList.JasperReport getJasperReport() {
		return jasperReport;
	}

	public void setJasperReport(EnumList.JasperReport jasperReport) {
		this.jasperReport = jasperReport;
	}

	public JsfMessageHelper getJsfMessageHelper() {
		return jsfMessageHelper;
	}

	public void setJsfMessageHelper(JsfMessageHelper jsfMessageHelper) {
		this.jsfMessageHelper = jsfMessageHelper;
	}

}