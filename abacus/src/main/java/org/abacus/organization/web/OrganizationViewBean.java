package org.abacus.organization.web;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.abacus.common.shared.AbcBusinessException;
import org.abacus.common.web.JsfMessageHelper;
import org.abacus.common.web.SessionInfoHelper;
import org.abacus.definition.shared.constant.EnumList;
import org.abacus.organization.core.handler.OrganizationHandler;
import org.abacus.organization.shared.entity.OrganizationEntity;

@ManagedBean
@ViewScoped
@SuppressWarnings("serial")
public class OrganizationViewBean implements Serializable {

	private OrganizationEntity selOrganization;
	private List<OrganizationEntity> organizationList;

	@ManagedProperty(value = "#{organizationHandler}")
	private OrganizationHandler organizationHandler;

	@ManagedProperty(value = "#{sessionInfoHelper}")
	private SessionInfoHelper sessionInfoHelper;

	@ManagedProperty(value = "#{jsfMessageHelper}")
	private JsfMessageHelper jsfMessageHelper;
	
	@ManagedProperty(value = "#{orgDepartmentViewBean}")
	private OrgDepartmentViewBean orgDepartmentViewBean;

	@ManagedProperty(value = "#{orgFiscalViewBean}")
	private OrgFiscalViewBean orgFiscalViewBean;

	private EnumList.OrgOrganizationLevelEnum level;
	
	@PostConstruct
	public void init() {
		findOrganizationList();
	}
	
	public void groupChangeListener(){
		clearOrganization();
	}

	public void organizationRowSelectListener() {
		orgDepartmentViewBean.setSelOrganization(selOrganization);
		orgFiscalViewBean.setSelOrganization(selOrganization);
	}

	public void saveOrganization() {
		try{
			selOrganization = organizationHandler.saveOrganizationEntity(selOrganization);
			jsfMessageHelper.addInfo("organizationKayitIslemiBasarili");
//			jsfMessageHelper.addInfo("organizationGuncellemeIslemiBasarili");
			findOrganizationList();
		} catch (AbcBusinessException e) {
			jsfMessageHelper.addError(e);
		}
	}

	public void deleteOrganization() {
		if (!selOrganization.isNew()) {
			organizationHandler.deleteOrganizationEntity(selOrganization);
			jsfMessageHelper.addInfo("organizationSilmeIslemiBasarili");
		}
		findOrganizationList();
	}

	public void clearOrganization() {
		selOrganization = new OrganizationEntity();
		orgDepartmentViewBean.setSelOrganization(null);
	}

	public void findOrganizationList() {
		clearOrganization();
		organizationList = null;
		organizationList = organizationHandler.findByOrganization(sessionInfoHelper.currentOrganizationId());
	}

	public SessionInfoHelper getSessionInfoHelper() {
		return sessionInfoHelper;
	}

	public void setSessionInfoHelper(SessionInfoHelper sessionInfoHelper) {
		this.sessionInfoHelper = sessionInfoHelper;
	}

	public JsfMessageHelper getJsfMessageHelper() {
		return jsfMessageHelper;
	}

	public void setJsfMessageHelper(JsfMessageHelper jsfMessageHelper) {
		this.jsfMessageHelper = jsfMessageHelper;
	}

	public OrganizationEntity getSelOrganization() {
		return selOrganization;
	}

	public void setSelOrganization(OrganizationEntity selOrganization) {
		if (selOrganization!=null){
			this.selOrganization = selOrganization;
		}
	}

	public List<OrganizationEntity> getOrganizationList() {
		return organizationList;
	}

	public void setOrganizationList(List<OrganizationEntity> organizationList) {
		this.organizationList = organizationList;
	}

	public OrganizationHandler getOrganizationHandler() {
		return organizationHandler;
	}

	public void setOrganizationHandler(OrganizationHandler organizationHandler) {
		this.organizationHandler = organizationHandler;
	}

	public OrgDepartmentViewBean getOrgDepartmentViewBean() {
		return orgDepartmentViewBean;
	}

	public void setOrgDepartmentViewBean(OrgDepartmentViewBean orgDepartmentViewBean) {
		this.orgDepartmentViewBean = orgDepartmentViewBean;
	}

	public EnumList.OrgOrganizationLevelEnum getLevel() {
		return level;
	}

	public void setLevel(EnumList.OrgOrganizationLevelEnum level) {
		this.level = level;
	}

	public OrgFiscalViewBean getOrgFiscalViewBean() {
		return orgFiscalViewBean;
	}

	public void setOrgFiscalViewBean(OrgFiscalViewBean orgFiscalViewBean) {
		this.orgFiscalViewBean = orgFiscalViewBean;
	}

}
