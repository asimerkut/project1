package org.abacus.transaction.shared.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.abacus.definition.shared.entity.DefTypeEntity;

@Entity
@Table(name = "tra_detail")
@SuppressWarnings("serial")
public class FinDetailEntity extends TraDetailEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "document_fin_id", nullable = true)
	private FinDocumentEntity document;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "glc_id", nullable = true)
	private DefTypeEntity glc;

//  TODO:ACC
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "account_id", nullable = true)
//	private DefValueEntity account;
	
	public FinDetailEntity() {
	}

	@Override
	public FinDocumentEntity getDocument() {
		return document;
	} 

	@Override
	public void setDocument(TraDocumentEntity document) {
		this.document = (FinDocumentEntity) document;
	}

	public DefTypeEntity getGlc() {
		return glc;
	}

	public void setGlc(DefTypeEntity glc) {
		this.glc = glc;
	}

}
