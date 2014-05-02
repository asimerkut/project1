package org.abacus.organization.core.handler;

import java.io.Serializable;
import java.util.List;

import org.abacus.organization.shared.entity.CompanyEntity;

public interface CompanyHandler extends Serializable{

	List<CompanyEntity> findAllOrderById();

	CompanyEntity saveCompanyEntity(CompanyEntity entity);
	
	void deleteCompanyEntity(CompanyEntity entity);
}

