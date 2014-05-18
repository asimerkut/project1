package org.abacus.definition.web.shared.event;

import org.abacus.common.shared.event.UpdatedEvent;
import org.abacus.definition.shared.entity.DefItemProductEntity;

public class ItemProductUptatedEvent extends UpdatedEvent {

	private DefItemProductEntity product;
	
	public ItemProductUptatedEvent(DefItemProductEntity product){
		this.product = product;
	}

	public DefItemProductEntity getProduct() {
		return product;
	}

	public void setProduct(DefItemProductEntity product) {
		this.product = product;
	}

}
