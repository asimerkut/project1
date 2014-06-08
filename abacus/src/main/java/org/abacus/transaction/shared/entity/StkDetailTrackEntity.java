package org.abacus.transaction.shared.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.abacus.common.shared.entity.DynamicEntity;

@Entity
@Table(name = "stk_detail_track")
@SuppressWarnings("serial")
public class StkDetailTrackEntity extends DynamicEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "detail_id", nullable = false)
	private StkDetailEntity detail;
	
	@Column(name = "parent_track_id", nullable = false)
	private Long parentTrackId;

	@Column(name = "root_track_id", nullable = false)
	private Long rootTrackId;

	@Column(name = "root_detail_id", nullable = false)
	private Long rootDetailId;
	
	@Column(name = "base_track_count", nullable = false, precision = 10, scale = 3)
	private BigDecimal baseTrackCount;

	@Column(name = "base_used_count", nullable = false, precision = 10, scale = 3)
	private BigDecimal baseUsedCount;

	@Column(name = "unit_track_price", nullable = false, precision = 12, scale = 2)
	private BigDecimal unitOrderPrice;

	@Column(name = "unit_cost_price", nullable = false, precision = 12, scale = 2)
	private BigDecimal unitCostPrice;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "lot_track_date", nullable = false)
	private Date lotTrackDate;

	@Column(name = "batch_track_no", nullable = true)
	private String batchTrackNo;

	public StkDetailTrackEntity() {
	}

	public StkDetailEntity getDetail() {
		return detail;
	}

	public void setDetail(StkDetailEntity detail) {
		this.detail = detail;
	}

	public Long getParentTrackId() {
		return parentTrackId;
	}

	public void setParentTrackId(Long parentTrackId) {
		this.parentTrackId = parentTrackId;
	}

	public Long getRootTrackId() {
		return rootTrackId;
	}

	public void setRootTrackId(Long rootTrackId) {
		this.rootTrackId = rootTrackId;
	}

	public Long getRootDetailId() {
		return rootDetailId;
	}

	public void setRootDetailId(Long rootDetailId) {
		this.rootDetailId = rootDetailId;
	}

	public BigDecimal getBaseTrackCount() {
		return baseTrackCount;
	}

	public void setBaseTrackCount(BigDecimal baseTrackCount) {
		this.baseTrackCount = baseTrackCount;
	}

	public BigDecimal getBaseUsedCount() {
		return baseUsedCount;
	}

	public void setBaseUsedCount(BigDecimal baseUsedCount) {
		this.baseUsedCount = baseUsedCount;
	}

	public BigDecimal getUnitOrderPrice() {
		return unitOrderPrice;
	}

	public void setUnitOrderPrice(BigDecimal unitOrderPrice) {
		this.unitOrderPrice = unitOrderPrice;
	}

	public BigDecimal getUnitCostPrice() {
		return unitCostPrice;
	}

	public void setUnitCostPrice(BigDecimal unitCostPrice) {
		this.unitCostPrice = unitCostPrice;
	}

	public Date getLotTrackDate() {
		return lotTrackDate;
	}

	public void setLotTrackDate(Date lotTrackDate) {
		this.lotTrackDate = lotTrackDate;
	}

	public String getBatchTrackNo() {
		return batchTrackNo;
	}

	public void setBatchTrackNo(String batchTrackNo) {
		this.batchTrackNo = batchTrackNo;
	}


}
