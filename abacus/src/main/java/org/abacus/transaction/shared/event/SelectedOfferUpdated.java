package org.abacus.transaction.shared.event;

import org.abacus.transaction.shared.entity.ReqDetailOfferEntity;

public class SelectedOfferUpdated {

	private ReqDetailOfferEntity offer;

	public SelectedOfferUpdated(ReqDetailOfferEntity offer) {
		this.offer = offer;
	}

	public ReqDetailOfferEntity getOffer() {
		return offer;
	}

	public void setOffer(ReqDetailOfferEntity offer) {
		this.offer = offer;
	}

}
