package org.abacus.definition.core.handler;

import java.util.List;

import org.abacus.definition.core.persistance.DefValueDao;
import org.abacus.definition.core.persistance.repository.DefValueRepository;
import org.abacus.definition.shared.entity.DefValueEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("serial")
@Service("defValueHandler")
public class DefValueHandlerImpl implements DefValueHandler {

	@Autowired
	private DefValueDao defValueDao;

	@Autowired
	private DefValueRepository defValueRepo;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly=true)
	public List<DefValueEntity> getValueList(String organizationId, String typ){
		return defValueRepo.getValueList(organizationId, typ); 
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly=false)
	public DefValueEntity saveValueEntity(DefValueEntity entity) {
		entity = defValueDao.saveValueEntity(entity);
		return entity;	
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly=false)
	public void deleteValueEntity(DefValueEntity entity) {
		defValueDao.deleteValueEntity(entity);
	}

}
