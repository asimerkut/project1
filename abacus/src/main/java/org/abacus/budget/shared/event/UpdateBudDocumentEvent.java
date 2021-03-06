package org.abacus.budget.shared.event;

import org.abacus.budget.shared.entity.BudDocumentEntity;
import org.abacus.common.shared.event.UpdateEvent;

public class UpdateBudDocumentEvent extends UpdateEvent {

	private BudDocumentEntity document;
	private String user;

	public UpdateBudDocumentEvent(BudDocumentEntity document, String user) {
		this.document = document;
		this.user = user;
	}

	public BudDocumentEntity getDocument() {
		return document;
	}

	public void setDocument(BudDocumentEntity document) {
		this.document = document;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

}
