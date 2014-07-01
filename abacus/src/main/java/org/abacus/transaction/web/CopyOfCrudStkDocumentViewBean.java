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
import org.abacus.transaction.shared.entity.StkDetailEntity;
import org.abacus.transaction.shared.entity.StkDocumentEntity;
import org.abacus.transaction.shared.entity.TraDocumentEntity;
import org.abacus.transaction.shared.event.ReadDetailEvent;
import org.abacus.transaction.shared.event.ReadDocumentEvent;
import org.abacus.transaction.shared.event.RequestReadDetailEvent;
import org.abacus.transaction.shared.event.RequestReadDocumentEvent;
import org.abacus.transaction.shared.event.TraBulkUpdateEvent;
import org.abacus.transaction.shared.event.TraBulkUpdatedEvent;
import org.abacus.transaction.shared.holder.TraDocumentSearchCriteria;
import org.springframework.util.CollectionUtils;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class CopyOfCrudStkDocumentViewBean implements Serializable {

	@ManagedProperty(value = "#{jsfMessageHelper}")
	private JsfMessageHelper jsfMessageHelper;

	@ManagedProperty(value = "#{sessionInfoHelper}")
	private SessionInfoHelper sessionInfoHelper;

	@ManagedProperty(value = "#{jsfDialogHelper}")
	private JsfDialogHelper jsfDialogHelper;

	@ManagedProperty(value = "#{stkTransactionHandler}")
	private TraTransactionHandler<StkDocumentEntity, StkDetailEntity> transactionHandler;

	@ManagedProperty(value = "#{defTaskRepository}")
	private DefTaskRepository taskRepository;

	private StkDocumentEntity document;

	private List<DefTaskEntity> stkTaskList;

	private Boolean showDocument = true;

	private List<StkDetailEntity> detailList;

	private StkDetailEntity selectedDetail;

	private EnumList.DefTypeGroupEnum selectedGroupEnum;

	private EnumList.DefTypeEnum selectedDetailServiceType;

	@PostConstruct
	private void init() {
		try {
			String grp = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("grp");
			selectedGroupEnum = EnumList.DefTypeGroupEnum.valueOf(grp.toUpperCase());
		} catch (Exception e) {
			jsfMessageHelper.addWarn("noDocumentGroupDefined");
			this.showDocument = false;
		}
		if (sessionInfoHelper.currentUser().getSelectedFiscalYear() == null) {
			jsfMessageHelper.addWarn("noFiscalYearDefined");
			this.showDocument = false;
		}

		String operation = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("operation");
		this.initSelections();
		if (operation.equals("create")) {
			this.initNewDocument();
		} else if (operation.equals("detail") || operation.equals("update")) {
			String documentId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("document");
			this.findStkDocument(Long.valueOf(documentId));
			if (document == null) {
				jsfMessageHelper.addError("noDocumentFind", selectedGroupEnum.getDescription());
			}
		}
	}

	private void initSelections() {
		stkTaskList = taskRepository.getTaskList(sessionInfoHelper.currentRootOrganizationId(), EnumList.DefTypeGroupEnum.STK.name());
		selectedDetailServiceType = EnumList.DefTypeEnum.ITM_SR_ST;
	}

	private void initNewDocument() {
		document = new StkDocumentEntity();
		document.setEntityStatus(EnumList.EntityStatus.NEW);
	}

	public void saveDetail() {
		//abcCmdContext.getCmds().add(new CreateStkDetailCmd(transactionHandler, selectedDetail));
		selectedDetail.setEntityStatus(EnumList.EntityStatus.NEW);
		detailList.add(selectedDetail);
		selectedDetail = null;
		jsfMessageHelper.addInfo("createSuccessful", "Fiş Detay");
	}

	public void updateDetail() {
		//abcCmdContext.getCmds().add(new UpdateStkDetailCmd(transactionHandler, selectedDetail));
		selectedDetail.setEntityStatus(EnumList.EntityStatus.UPDATE);
		this.selectedDetail = null;
		jsfMessageHelper.addInfo("updateSuccessful", "Fiş Detay");
	}
	
	public void deleteDetail(StkDetailEntity detail) {
		//abcCmdContext.getCmds().add(new DeleteStkDetailCmd(transactionHandler, selectedDetail));
		selectedDetail.setEntityStatus(EnumList.EntityStatus.DELETE);
		jsfMessageHelper.addInfo("deleteSuccessful", "Fiş Detay");
	}
	
	public void save(){
		String user = sessionInfoHelper.currentUserName();
		String organization = sessionInfoHelper.currentOrganizationId();
		String fiscalYear = sessionInfoHelper.selectedFiscalYearId();
		try{
			TraBulkUpdatedEvent<StkDocumentEntity, StkDetailEntity> updatedEvent = transactionHandler.bulkUpdate(new TraBulkUpdateEvent<StkDocumentEntity, StkDetailEntity>(document, detailList, user, organization, fiscalYear));
			this.document = updatedEvent.getDocument(); 
			selectedDetail = null;
			this.findStkDocument(document.getId());
			jsfMessageHelper.addInfo("createSuccessful", "Fiş Detay");
		}catch(AbcBusinessException e){
			jsfMessageHelper.addError(e);
		}
	}

	private void findStkDocument(Long documentId) {
		TraDocumentSearchCriteria traDocumentSearchCriteria = new TraDocumentSearchCriteria(documentId);

		ReadDocumentEvent<StkDocumentEntity> readDocumentEvent = transactionHandler.readDocumentList(new RequestReadDocumentEvent<StkDocumentEntity>(traDocumentSearchCriteria, sessionInfoHelper.currentOrganizationId(), sessionInfoHelper.selectedFiscalYearId()));
		if (CollectionUtils.isEmpty(readDocumentEvent.getDocumentList())) {
			document = null;
		} else {
			document = readDocumentEvent.getDocumentList().get(0);
			ReadDetailEvent<StkDetailEntity> readDetailEvent = transactionHandler.readDetailList(new RequestReadDetailEvent<StkDetailEntity>(document.getId()));
			detailList = readDetailEvent.getDetails();
		}
	}

	public void updateDetailSelected(StkDetailEntity detail) {
		this.selectedDetail = detail;
	}

	public Boolean isWBTaskSelected() {
		if (document == null || document.getTask() == null) {
			return false;
		}
		boolean isWBTaskSelected = this.document.getTask().getType().getId().startsWith(EnumList.DefTypeEnum.STK_WB.name());
		return isWBTaskSelected;
	}

	public void initNewDetail() {
		selectedDetail = new StkDetailEntity();
		selectedDetail.setDocument(document);
		selectedDetailServiceType = EnumList.DefTypeEnum.ITM_SR_ST;
	}

	public void selectedDetailServiceTypeChanged() {
		selectedDetail = new StkDetailEntity();
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

	public TraTransactionHandler<StkDocumentEntity, StkDetailEntity> getTransactionHandler() {
		return transactionHandler;
	}

	public void setTransactionHandler(TraTransactionHandler<StkDocumentEntity, StkDetailEntity> transactionHandler) {
		this.transactionHandler = transactionHandler;
	}

	public DefTaskRepository getTaskRepository() {
		return taskRepository;
	}

	public void setTaskRepository(DefTaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public TraDocumentEntity getDocument() {
		return document;
	}

	public void setDocument(StkDocumentEntity document) {
		this.document = document;
	}

	public List<DefTaskEntity> getStkTaskList() {
		return stkTaskList;
	}

	public void setStkTaskList(List<DefTaskEntity> stkTaskList) {
		this.stkTaskList = stkTaskList;
	}

	public Boolean getShowDocument() {
		return showDocument;
	}

	public void setShowDocument(Boolean showDocument) {
		this.showDocument = showDocument;
	}

	public List<StkDetailEntity> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<StkDetailEntity> detailList) {
		this.detailList = detailList;
	}

	public StkDetailEntity getSelectedDetail() {
		return selectedDetail;
	}

	public void setSelectedDetail(StkDetailEntity selectedDetail) {
		this.selectedDetail = selectedDetail;
	}

	public EnumList.DefTypeGroupEnum getSelectedGroupEnum() {
		return selectedGroupEnum;
	}

	public void setSelectedGroupEnum(EnumList.DefTypeGroupEnum selectedGroupEnum) {
		this.selectedGroupEnum = selectedGroupEnum;
	}

	public EnumList.DefTypeEnum getSelectedDetailServiceType() {
		return selectedDetailServiceType;
	}

	public void setSelectedDetailServiceType(EnumList.DefTypeEnum selectedDetailServiceType) {
		this.selectedDetailServiceType = selectedDetailServiceType;
	}

}