package org.abacus.transaction.shared;

import org.abacus.common.shared.AbcBusinessException;

@SuppressWarnings("serial")
public class UnableToOutputDetail extends AbcBusinessException {
	
	public UnableToOutputDetail(String name) {
		super("notEnoughItemInStock",name);
	}

}
