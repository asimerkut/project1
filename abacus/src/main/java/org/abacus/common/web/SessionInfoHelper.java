package org.abacus.common.web;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.abacus.common.security.SecUser;
import org.abacus.organization.core.persistance.repository.FiscalPeriodRepository;
import org.abacus.organization.core.persistance.repository.OrganizationRepository;
import org.abacus.organization.core.util.OrganizationUtils;
import org.abacus.organization.shared.entity.FiscalPeriodEntity;
import org.abacus.organization.shared.entity.FiscalYearEntity;
import org.abacus.organization.shared.entity.OrganizationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@SuppressWarnings("serial")
@Component
public class SessionInfoHelper implements Serializable {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private OrganizationRepository organizationRepository;

	@Autowired
	private FiscalPeriodRepository fiscalPeriodRepository;

	@Autowired
	private OrganizationUtils organizationUtils;

	public SecUser currentUser(){
		SecUser secUser = (SecUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return secUser;
	} 
	
	public String currentUserName(){
		return currentUser().getUsername();
	} 
	
	public boolean isAuthenticated(){
		Authentication authentucation = SecurityContextHolder.getContext().getAuthentication();
		if(authentucation != null && authentucation.getPrincipal() instanceof SecUser){
			return true;
		}
		
		return false;
	}

	public void companyChanged(){
		OrganizationEntity selectedOrganization = organizationRepository.fetchOrganization(currentOrganization().getId());
		Set<FiscalYearEntity> fiscalYearSet = organizationUtils.findFiscalYearSet(selectedOrganization);
		FiscalYearEntity selectedFiscalYear = organizationUtils.findDefaultFiscalYear(fiscalYearSet);
		currentUser().setCompanyFiscalYearSet(fiscalYearSet);
		currentUser().setSelectedFiscalYear(selectedFiscalYear);
		this.redirect();
	}
	
	public void fiscalYearChanged(){
		this.redirect();
	}
	
	private void redirect(){
		FacesContext context = FacesContext.getCurrentInstance();
	    NavigationHandler navigationHandler = context.getApplication().getNavigationHandler();
	    navigationHandler.handleNavigation(context, null, "/app/index.abc?faces-redirect=true");
	}
	
	public Long getNewId(){
		StringBuilder sb = new StringBuilder();
		sb.append("select nextval('seq_id') newId");
		Query query = em.createNativeQuery(sb.toString());
		List<BigInteger> resultList = query.getResultList();
		return Long.parseLong(resultList.get(0).toString());				
	}
	
	public OrganizationEntity currentOrganization() {
		return currentUser().getSelectedOrganization();
	}

	public FiscalYearEntity currentFiscalYear() {
		return currentUser().getSelectedFiscalYear();
	}

	public FiscalPeriodEntity getFiscalPeriod(Date docDate) {
		FiscalYearEntity fisYear = currentUser().getSelectedFiscalYear();
		return fiscalPeriodRepository.findFiscalPeriod(fisYear.getId(), docDate);
	}

}
