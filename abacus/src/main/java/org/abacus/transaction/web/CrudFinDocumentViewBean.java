package org.abacus.transaction.web;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.abacus.common.shared.AbcBusinessException;
import org.abacus.common.web.JsfDialogHelper;
import org.abacus.common.web.JsfMessageHelper;
import org.abacus.common.web.SessionInfoHelper;
import org.abacus.definition.core.persistance.repository.DefTaskRepository;
import org.abacus.definition.shared.constant.EnumList;
import org.abacus.definition.shared.entity.DefTaskEntity;
import org.abacus.transaction.core.handler.TraTransactionHandler;
import org.abacus.transaction.shared.entity.FinDetailEntity;
import org.abacus.transaction.shared.entity.FinDocumentEntity;
import org.abacus.transaction.shared.entity.TraDetailEntity;
import org.abacus.transaction.shared.event.CreateDetailEvent;
import org.abacus.transaction.shared.event.CreateDocumentEvent;
import org.abacus.transaction.shared.event.DetailCreatedEvent;
import org.abacus.transaction.shared.event.DocumentCreatedEvent;
import org.abacus.transaction.shared.event.ReadDetailEvent;
import org.abacus.transaction.shared.event.ReadDocumentEvent;
import org.abacus.transaction.shared.event.RequestReadDetailEvent;
import org.abacus.transaction.shared.event.RequestReadDocumentEvent;
import org.abacus.transaction.shared.holder.TraDocumentSearchCriteria;
import org.springframework.util.CollectionUtils;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class CrudFinDocumentViewBean implements Serializable {

	@ManagedProperty(value = "#{jsfMessageHelper}")
	private JsfMessageHelper jsfMessageHelper;

	@ManagedProperty(value = "#{sessionInfoHelper}")
	private SessionInfoHelper sessionInfoHelper;

	@ManagedProperty(value = "#{jsfDialogHelper}")
	private JsfDialogHelper jsfDialogHelper;

	@ManagedProperty(value = "#{finTransactionHandler}")
	private TraTransactionHandler<FinDocumentEntity, FinDetailEntity> transactionHandler;

	@ManagedProperty(value = "#{defTaskRepository}")
	private DefTaskRepository taskRepository;

	private FinDocumentEntity document;

	private List<DefTaskEntity> finTaskList;

	private Boolean showDocument = true;

	private List<FinDetailEntity> detailList;

	private FinDetailEntity selectedDetail;

	private EnumList.DefTypeGroupEnum selectedGroupEnum;
	
	@PostConstruct
	private void init() {
		try{
			String grp = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("grp");
			selectedGroupEnum = EnumList.DefTypeGroupEnum.valueOf(grp.toUpperCase());
		}catch(Exception e){
			jsfMessageHelper.addWarn("noDocumentGroupDefined");
			this.showDocument = false;
		}
		if (sessionInfoHelper.currentUser().getSelectedFiscalYear() == null){
			jsfMessageHelper.addWarn("noFiscalYearDefined");
			this.showDocument = false;
		}

		String operation = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("operation");
		this.initSelections();
		if (operation.equals("create")) {
			this.initNewDocument();
		} else if (operation.equals("detail") || operation.equals("update")) {
			String documentId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("document");
			this.findFinDocument(Long.valueOf(documentId));
			if (document == null) {
				jsfMessageHelper.addError("noDocumentFind", selectedGroupEnum.getDescription());
			}
		}
	}

	private void initSelections() {
		finTaskList = taskRepository.getTaskList(sessionInfoHelper.currentRootOrganizationId(), EnumList.DefTypeGroupEnum.FIN.name());
	}

	private void initNewDocument() {
		document = new FinDocumentEntity();
	}

	public void saveDocument() {
		try {
			DocumentCreatedEvent<FinDocumentEntity> documentCreatedEvent = transactionHandler.newDocument(new CreateDocumentEvent<FinDocumentEntity>(document, sessionInfoHelper.currentUserName(), sessionInfoHelper.currentOrganizationId(), sessionInfoHelper.selectedFiscalYearId()));
			document = (FinDocumentEntity) documentCreatedEvent.getDocument();
			this.findFinDocument(document.getId());
			jsfMessageHelper.addInfo("createSuccessfull", "Fiş");
		} catch (AbcBusinessException e) {
			jsfMessageHelper.addError(e);
		}

	}

	public void saveDetail() {
		try {
			DetailCreatedEvent<FinDetailEntity> event = transactionHandler.newDetail(new CreateDetailEvent<FinDetailEntity>(selectedDetail, sessionInfoHelper.currentUserName()));
			this.findFinDocument(document.getId());
			selectedDetail = null;
			jsfMessageHelper.addInfo("createSuccessfull", "Fiş Detay");
		} catch (AbcBusinessException e) {
			jsfMessageHelper.addError(e);
		}
	}

	private void findFinDocument(Long documentId) {
		TraDocumentSearchCriteria traDocumentSearchCriteria = new TraDocumentSearchCriteria(documentId);
		traDocumentSearchCriteria.setDocumentGroupEnum(selectedGroupEnum);
		
		ReadDocumentEvent<FinDocumentEntity> readDocumentEvent = transactionHandler.readDocument(new RequestReadDocumentEvent<FinDocumentEntity>(traDocumentSearchCriteria, sessionInfoHelper.currentOrganizationId(), sessionInfoHelper.selectedFiscalYearId()));
		if (CollectionUtils.isEmpty(readDocumentEvent.getDocumentList())) {
			document = null;
		} else {
			document = readDocumentEvent.getDocumentList().get(0);
			ReadDetailEvent<FinDetailEntity> readDetailEvent = transactionHandler.readDetail(new RequestReadDetailEvent<FinDetailEntity>(document.getId()));
			detailList = readDetailEvent.getDetails();
		}
	}

	public void initNewDetail() {
		selectedDetail = new FinDetailEntity();
		selectedDetail.setDocument(document);
	}

	public JsfMessageHelper getJsfMessageHelper() {
		return jsfMessageHelper;
	}

	public void setJsfMessageHelper(JsfMessageHelper jsfMessageHelper) {
		this.jsfMessageHelper = jsfMessageHelper;
	}

	public SessionInfoHelper getSessionInfoHelper() {
		return sessionInfoHelper;
	}

	public void setSessionInfoHelper(SessionInfoHelper sessionInfoHelper) {
		this.sessionInfoHelper = sessionInfoHelper;
	}

	public JsfDialogHelper getJsfDialogHelper() {
		return jsfDialogHelper;
	}

	public void setJsfDialogHelper(JsfDialogHelper jsfDialogHelper) {
		this.jsfDialogHelper = jsfDialogHelper;
	}

	public TraTransactionHandler<FinDocumentEntity, FinDetailEntity> getTransactionHandler() {
		return transactionHandler;
	}

	public void setTransactionHandler(TraTransactionHandler<FinDocumentEntity, FinDetailEntity> transactionHandler) {
		this.transactionHandler = transactionHandler;
	}

	public DefTaskRepository getTaskRepository() {
		return taskRepository;
	}

	public void setTaskRepository(DefTaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public FinDocumentEntity getDocument() {
		return document;
	}

	public void setDocument(FinDocumentEntity document) {
		this.document = document;
	}

	public List<DefTaskEntity> getFinTaskList() {
		return finTaskList;
	}

	public void setFinTaskList(List<DefTaskEntity> finTaskList) {
		this.finTaskList = finTaskList;
	}

	public Boolean getShowDocument() {
		return showDocument;
	}

	public void setShowDocument(Boolean showDocument) {
		this.showDocument = showDocument;
	}

	public List<FinDetailEntity> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<FinDetailEntity> detailList) {
		this.detailList = detailList;
	}

	public TraDetailEntity getSelectedDetail() {
		return selectedDetail;
	}

	public void setSelectedDetail(FinDetailEntity selectedDetail) {
		this.selectedDetail = selectedDetail;
	}

	public EnumList.DefTypeGroupEnum getSelectedGroupEnum() {
		return selectedGroupEnum;
	}

	public void setSelectedGroupEnum(EnumList.DefTypeGroupEnum selectedGroupEnum) {
		this.selectedGroupEnum = selectedGroupEnum;
	}

}
