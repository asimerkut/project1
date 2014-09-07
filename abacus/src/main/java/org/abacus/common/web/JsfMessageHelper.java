package org.abacus.common.web;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.abacus.common.shared.AbcBusinessException;
import org.springframework.stereotype.Component;

@Component
public class JsfMessageHelper implements Serializable {


	public void addError(AbcBusinessException e) {
		this.addError(e.getMessage(), e.getParameters());
	}

	public void addSimple(String caption, String message) {
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, caption, message));
	}

	public void addInfo(String message, String... params) {
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Bilgi", label(message, params)));
	}

	public void addWarn(String message, String... params) {
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_WARN, "Dikkat", label(message, params)));
	}

	public void addError(String message, String... params) {
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Hata", label(message, params)));
	}

	public void addFatal(String message, String... params) {
		FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_FATAL, "Fatal", label(message, params)));
	}

	public String label(String key, String... params) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			ResourceBundle bundle = context.getApplication().getResourceBundle(context, "lbl");
			String message = bundle.getString(key);
			if (params != null && params.length > 0) {
				message = MessageFormat.format(message, params);
			}
			return message;
		} catch (Exception e) {
			return "Label: " + key;
		}
	}

}
