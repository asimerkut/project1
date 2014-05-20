package org.abacus.catering.shared.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.abacus.common.shared.entity.DynamicEntity;
import org.abacus.definition.shared.entity.DefValueEntity;

@Entity
@SuppressWarnings("serial")
@Table(name = "cat_menu_item")
public class CatMenuItemEntity extends DynamicEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", nullable = false)
	private CatMenuEntity menu;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "item_id", nullable = false)
	private DefValueEntity item;

	public CatMenuEntity getMenu() {
		return menu;
	}

	public void setMenu(CatMenuEntity menu) {
		this.menu = menu;
	}

	public DefValueEntity getItem() {
		return item;
	}

	public void setItem(DefValueEntity item) {
		this.item = item;
	}

}