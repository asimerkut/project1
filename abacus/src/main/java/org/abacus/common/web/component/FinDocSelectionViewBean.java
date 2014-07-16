package org.abacus.common.web.component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.abacus.common.web.SessionInfoHelper;
import org.abacus.definition.shared.constant.EnumList;
import org.abacus.transaction.core.handler.TraTransactionHandler;
import org.abacus.transaction.shared.entity.FinDetailEntity;
import org.abacus.transaction.shared.entity.FinDocumentEntity;
import org.abacus.transaction.shared.event.ReadDocumentEvent;
import org.abacus.transaction.shared.event.RequestReadDocumentEvent;
import org.abacus.transaction.shared.holder.TraDocumentSearchCriteria;

@SuppressWarnings("serial")
@ManagedBean
@ViewScoped
public class FinDocSelectionViewBean implements Serializable {

	@ManagedProperty(value = "#{finTransactionHandler}")
	private TraTransactionHandler<FinDocumentEntity, FinDetailEntity> finTransactionHandler;
	
	@ManagedProperty(value = "#{sessionInfoHelper}")
	private SessionInfoHelper sessionInfoHelper;
	
	private Map<String, List<FinDocumentEntity>> resultMap = new HashMap<>();
	
	@PostConstruct
	public void init() {
	}

	public List<FinDocumentEntity> getDocumentList(EnumList.DefTypeEnum typeEnum) {
		String organization = sessionInfoHelper.currentOrganization().getId();
		String key = organization+"-"+typeEnum.getName();
		if (resultMap.containsKey(key)) {
			return resultMap.get(key);
		} else {
			TraDocumentSearchCriteria documentSearchCriteria = new TraDocumentSearchCriteria();
			documentSearchCriteria.setDocType(typeEnum);

			ReadDocumentEvent<FinDocumentEntity> readDocumentEvent = finTransactionHandler.readDocumentList(new RequestReadDocumentEvent<FinDocumentEntity>(documentSearchCriteria, sessionInfoHelper.currentOrganization(), sessionInfoHelper.currentFiscalYear()));
			List<FinDocumentEntity> documentSearchResultList = readDocumentEvent.getDocumentList();
			resultMap.put(key, documentSearchResultList);
			return documentSearchResultList;
		}
	}

	public TraTransactionHandler<FinDocumentEntity, FinDetailEntity> getFinTransactionHandler() {
		return finTransactionHandler;
	}

	public void setFinTransactionHandler(TraTransactionHandler<FinDocumentEntity, FinDetailEntity> finTransactionHandler) {
		this.finTransactionHandler = finTransactionHandler;
	}

	public SessionInfoHelper getSessionInfoHelper() {
		return sessionInfoHelper;
	}

	public void setSessionInfoHelper(SessionInfoHelper sessionInfoHelper) {
		this.sessionInfoHelper = sessionInfoHelper;
	}

}