package org.abacus.transaction.shared.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.abacus.common.shared.entity.DynamicEntity;
import org.abacus.definition.shared.entity.DefItemEntity;
import org.abacus.definition.shared.entity.DefUnitCodeEntity;
import org.abacus.organization.shared.entity.DepartmentEntity;
import org.abacus.organization.shared.entity.FiscalYearEntity;
import org.hibernate.validator.constraints.Range;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class TraDetailEntity extends DynamicEntity {

	@Temporal(TemporalType.DATE)
	@Column(name = "lot_detail_date", nullable = false)
	private Date lotDetailDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fiscal_year_id", nullable = false)
	private FiscalYearEntity fiscalYear;
	
	@Column(name = "tr_state_detail", nullable = false)
	@Range(min=-1, max=+1)
	private Integer trStateDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private DefItemEntity item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_unit_id", nullable = false)
	private DefUnitCodeEntity itemUnit;
	
	@Column(name = "item_detail_count", nullable = false, precision = 10, scale = 3)
	private BigDecimal itemDetailCount;

	@Column(name = "base_detail_count", nullable = false, precision = 10, scale = 3)
	private BigDecimal baseDetailCount;
	
	@Column(name = "batch_detail_no", nullable = true)
	private String batchDetailNo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id", nullable = false)
	private DepartmentEntity department;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_opp_id", nullable = true)
	private DepartmentEntity departmentOpp;
	
	@Column(name = "base_detail_amount", nullable = false, precision = 12, scale = 2)
	private BigDecimal baseDetailAmount;
	
	public TraDetailEntity() {
	}

	public abstract TraDocumentEntity getDocument();
	public abstract void setDocument(TraDocumentEntity document);


	public Date getLotDetailDate() {
		return lotDetailDate;
	}

	public void setLotDetailDate(Date lotDetailDate) {
		this.lotDetailDate = lotDetailDate;
	}

	public FiscalYearEntity getFiscalYear() {
		return fiscalYear;
	}

	public void setFiscalYear(FiscalYearEntity fiscalYear) {
		this.fiscalYear = fiscalYear;
	}

	public DefItemEntity getItem() {
		return item;
	}

	public void setItem(DefItemEntity item) {
		this.item = item;
	}

	public DefUnitCodeEntity getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(DefUnitCodeEntity itemUnit) {
		this.itemUnit = itemUnit;
	}

	public BigDecimal getItemDetailCount() {
		return itemDetailCount;
	}

	public void setItemDetailCount(BigDecimal itemDetailCount) {
		this.itemDetailCount = itemDetailCount;
	}

	public BigDecimal getBaseDetailCount() {
		return baseDetailCount;
	}

	public void setBaseDetailCount(BigDecimal baseDetailCount) {
		this.baseDetailCount = baseDetailCount;
	}

	public String getBatchDetailNo() {
		return batchDetailNo;
	}

	public void setBatchDetailNo(String batchDetailNo) {
		this.batchDetailNo = batchDetailNo;
	}

	public DepartmentEntity getDepartment() {
		return department;
	}

	public void setDepartment(DepartmentEntity department) {
		this.department = department;
	}

	public DepartmentEntity getDepartmentOpp() {
		return departmentOpp;
	}

	public void setDepartmentOpp(DepartmentEntity departmentOpp) {
		this.departmentOpp = departmentOpp;
	}

	public BigDecimal getBaseDetailAmount() {
		return baseDetailAmount;
	}

	public void setBaseDetailAmount(BigDecimal baseDetailAmount) {
		this.baseDetailAmount = baseDetailAmount;
	}

	public Integer getTrStateDetail() {
		return trStateDetail;
	}

	public void setTrStateDetail(Integer trStateDetail) {
		this.trStateDetail = trStateDetail;
	}

}