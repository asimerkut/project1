package org.abacus.user.shared;

import org.abacus.common.shared.AbcBusinessException;

public class GroupNameInUseException extends AbcBusinessException {

	public GroupNameInUseException(String... params){
		super(params);
	}
	
} 
