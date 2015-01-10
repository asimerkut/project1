package org.abacus.transaction.web;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.abacus.definition.core.handler.DefTaskHandler;
import org.abacus.definition.shared.constant.EnumList;
import org.abacus.definition.shared.entity.DefTaskEntity;
import org.abacus.transaction.core.handler.TraTransactionHandler;
import org.abacus.transaction.shared.entity.FinDetailEntity;
import org.abacus.transaction.shared.entity.FinDocumentEntity;
import org.abacus.transaction.shared.entity.TraDocumentEntity;
import org.abacus.transaction.shared.event.CreateDetailEvent;
import org.abacus.transaction.shared.event.CreateDocumentEvent;
import org.abacus.transaction.shared.event.DeleteDetailEvent;
import org.abacus.transaction.shared.event.DetailCreatedEvent;
import org.abacus.transaction.shared.event.DetailDeletedEvent;
import org.abacus.transaction.shared.event.DetailUpdatedEvent;
import org.abacus.transaction.shared.event.DocumentCreatedEvent;
import org.abacus.transaction.shared.event.DocumentUpdatedEvent;
import org.abacus.transaction.shared.event.ReadDetailEvent;
import org.abacus.transaction.shared.event.ReadDocumentEvent;
import org.abacus.transaction.shared.event.RequestReadDetailEvent;
import org.abacus.transaction.shared.event.RequestReadDocumentEvent;
import org.abacus.transaction.shared.event.UpdateDetailEvent;
import org.abacus.transaction.shared.event.UpdateDocumentEvent;
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

	@ManagedProperty(value = "#{defTaskHandler}")
	private DefTaskHandler taskRepository;

	private FinDocumentEntity document;

	private List<DefTaskEntity> finTaskList;

	private Boolean showDocument = true;

	private List<FinDetailEntity> detailList;
	private List<FinDetailEntity> prDetailList;

	private FinDetailEntity selectedDetail;

	private EnumList.DefTypeGroupEnum selectedGroupEnum;
	private EnumList.DefTypeEnum selectedTypeEnum;

	private FinDocumentEntity bsDocumentEntity;
	
	@PostConstruct
	private void init() {
		try {
			String grp = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("grp");
			String typ = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("typ");
			selectedGroupEnum = EnumList.DefTypeGroupEnum.valueOf(grp.toUpperCase());
			if (typ!=null){
				selectedTypeEnum = EnumList.DefTypeEnum.valueOf(typ.toUpperCase());
			}
		} catch (Exception e) {
			jsfMessageHelper.addWarn("noDocumentGroupDefined");
			this.showDocument = false;
		}
		if (sessionInfoHelper.currentUser().getSelectedFiscalYear() == null) {
			jsfMessageHelper.addWarn("noFiscalYearDefined");
			this.showDocument = false;
		}

		String operation = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("operation");
		if (operation.equals("create")) {
			this.initSelections();
			this.initNewDocument();
		} else if (operation.equals("detail") || operation.equals("update")) {
			String documentId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("document");
			this.findFinDocument(Long.valueOf(documentId));
			if (document == null) {
				jsfMessageHelper.addError("noDocumentFind", selectedGroupEnum.getDescription());
				this.showDocument = false;
			} else {
				selectedTypeEnum = document.getTypeEnum(); 
				this.initSelections();
			}
		}
	}

	private void initSelections() {
		finTaskList = taskRepository.getTaskList(sessionInfoHelper.currentOrganization(), selectedTypeEnum);
	}

	public EnumList.DefTypeEnum getDocumentItemType(){
		if (document==null || document.getTask()==null){
			return null;
		}
		return document.getTask().getItemTypeDocument();
	}

	public EnumList.DefTypeEnum getBSItemType(){
		if (selectedTypeEnum==null){
			return null;
		}
		EnumList.DefTypeEnum bsType = null;
		if (selectedTypeEnum.equals(EnumList.DefTypeEnum.FIN_P)){
			bsType = EnumList.DefTypeEnum.FIN_B;
		} else if (selectedTypeEnum.equals(EnumList.DefTypeEnum.FIN_R)){
			bsType = EnumList.DefTypeEnum.FIN_S;
		}
		return bsType;
	}

	public EnumList.DefTypeEnum getDetailItemType(){
		if (document==null || document.getTask()==null){
			return null;
		}
		return document.getTask().getItemTypeDetail();
	}
	
	private void initNewDocument() {
		document = new FinDocumentEntity();
	}

	public void saveDocument() {
		try {
			DocumentCreatedEvent<FinDocumentEntity> documentCreatedEvent = transactionHandler.newDocument(new CreateDocumentEvent<FinDocumentEntity>(document, sessionInfoHelper.currentUserName(), sessionInfoHelper.currentOrganization(), sessionInfoHelper.currentFiscalYear()));
			document = (FinDocumentEntity) documentCreatedEvent.getDocument();
			this.findFinDocument(document.getId());
			jsfMessageHelper.addInfo("createSuccessful", "Fiş");
		} catch (AbcBusinessException e) {
			jsfMessageHelper.addException(e);
		}

	}

	public void updateDocument() {
		try {
			DocumentUpdatedEvent<FinDocumentEntity> documentUpdatedEvent = transactionHandler.updateDocument(new UpdateDocumentEvent<FinDocumentEntity>(document, sessionInfoHelper.currentUserName()));
			this.findFinDocument(document.getId());
			jsfMessageHelper.addInfo("updateSuccessful", "Fiş");
		} catch (AbcBusinessException e) {
			jsfMessageHelper.addException(e);
		}
	}

	public void saveDetail() {
		try {
			DetailCreatedEvent<FinDetailEntity> event = transactionHandler.newDetail(new CreateDetailEvent<FinDetailEntity>(selectedDetail, true));
			this.findFinDocument(document.getId());
			selectedDetail = null;
			jsfMessageHelper.addInfo("createSuccessful", "Fiş Detay");
		} catch (AbcBusinessException e) {
			jsfMessageHelper.addException(e);
		}
	}

	private void findFinDocument(Long documentId) {
		detailList = new ArrayList<FinDetailEntity>();
		prDetailList = new ArrayList<FinDetailEntity>();
		
		TraDocumentSearchCriteria traDocumentSearchCriteria = new TraDocumentSearchCriteria(documentId);

		ReadDocumentEvent<FinDocumentEntity> readDocumentEvent = transactionHandler.readDocumentList(new RequestReadDocumentEvent<FinDocumentEntity>(traDocumentSearchCriteria, sessionInfoHelper.currentOrganization(), sessionInfoHelper.currentFiscalYear()));
		if (CollectionUtils.isEmpty(readDocumentEvent.getDocumentList())) {
			document = null;
		} else {
			document = readDocumentEvent.getDocumentList().get(0);
			ReadDetailEvent<FinDetailEntity> readDetailEvent = transactionHandler.readDetailList(new RequestReadDetailEvent<FinDetailEntity>(document.getId()));
			detailList = readDetailEvent.getDetails();
			
			if (document.getFinInfo().getPrAmount().compareTo(BigDecimal.ZERO)>0){//Varsa Odeme/Tahsilat Detayi
				ReadDetailEvent<FinDetailEntity> readPRDetailEvent = transactionHandler.readPRDetailList(new RequestReadDetailEvent<FinDetailEntity>(document.getId()));
				prDetailList = readPRDetailEvent.getDetails();
			}
			
		}
	}
	
	public void updateDetailSelected(FinDetailEntity detail){
		this.selectedDetail = detail;
	}

	public void updateDetail() {
		try {
			DetailUpdatedEvent<FinDetailEntity> detailUpdatedEvent = transactionHandler.updateDetail(new UpdateDetailEvent<FinDetailEntity>(selectedDetail, true));
			this.findFinDocument(document.getId());
			this.selectedDetail = null;
			jsfMessageHelper.addInfo("updateSuccessful", "Fiş Detay");
		} catch (AbcBusinessException e) {
			jsfMessageHelper.addException(e);
		}
	}

	public void deleteDetail(FinDetailEntity detail) {
		try {
			DetailDeletedEvent<FinDetailEntity> deleteDetailEvent = transactionHandler.deleteDetail(new DeleteDetailEvent<FinDetailEntity>(detail, true));
			this.findFinDocument(document.getId());
			jsfMessageHelper.addInfo("deleteSuccessful", "Fiş Detay");
		} catch (AbcBusinessException e) {
			jsfMessageHelper.addException(e);
		}
	}

	public Boolean isTaskSelected(EnumList.DefTypeEnum taskEnum) {
		if (this.document == null || this.document.getTask() == null) {
			return false;
		}
		boolean result = this.document.getTask().getType().getId().startsWith(taskEnum.name());
		return result;
	}

	public Boolean isTaskSelectedState(Integer trState) {
		if (this.document == null || this.document.getTask() == null) {
			return false;
		}
		boolean result = this.document.getTask().getType().getTrStateType().compareTo(trState)==0;
		return result;
	}
	
	public void initNewDetail() {
		selectedDetail = new FinDetailEntity();
		selectedDetail.setDocument(document);
	}

	public void selectedDetailServiceTypeChanged() {
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

	public DefTaskHandler getTaskRepository() {
		return taskRepository;
	}

	public void setTaskRepository(DefTaskHandler taskRepository) {
		this.taskRepository = taskRepository;
	}

	public TraDocumentEntity getDocument() {
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

	public FinDetailEntity getSelectedDetail() {
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

	public EnumList.DefTypeEnum getSelectedTypeEnum() {
		return selectedTypeEnum;
	}

	public void setSelectedTypeEnum(EnumList.DefTypeEnum selectedTypeEnum) {
		this.selectedTypeEnum = selectedTypeEnum;
	}

	public FinDocumentEntity getBsDocumentEntity() {
		return bsDocumentEntity;
	}

	public void setBsDocumentEntity(FinDocumentEntity bsDocumentEntity) {
		this.bsDocumentEntity = bsDocumentEntity;
	}

	public List<FinDetailEntity> getPrDetailList() {
		return prDetailList;
	}

	public void setPrDetailList(List<FinDetailEntity> prDetailList) {
		this.prDetailList = prDetailList;
	}

}
