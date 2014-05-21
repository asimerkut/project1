package org.abacus.organization.core.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.abacus.definition.shared.constant.EnumList;
import org.abacus.organization.core.persistance.DepartmentDao;
import org.abacus.organization.core.persistance.repository.DepartmentRepository;
import org.abacus.organization.shared.entity.DepartmentEntity;
import org.abacus.user.core.persistance.repository.UserDepartmentRepository;
import org.abacus.user.shared.entity.SecUserDepartmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("serial")
@Service("departmentHandler")
public class DepartmentHandlerImpl implements DepartmentHandler {

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private UserDepartmentRepository userDepartmentRepository;

	@Autowired
	private DepartmentDao departmentDao;

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly=true)
	public List<DepartmentEntity> findByOrganizationAndGroup(String organizationId, EnumList.OrgDepartmentGroupEnum groupEnum) {
		List<DepartmentEntity> list = departmentRepository.findByOrganizationAndGroup(organizationId, groupEnum);
		return list;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly=false)
	public DepartmentEntity saveDepartmentEntity(DepartmentEntity entity) {
		List<SecUserDepartmentEntity> list = new ArrayList<>();
		for (SecUserDepartmentEntity user : entity.getDepartmentUserList()) {
			user.setId(null);
			list.add(user);
		}
		entity = departmentRepository.save(entity);
		userDepartmentRepository.deleteDepartmentUsers(entity.getId());
		userDepartmentRepository.save(list);
		return entity;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly=false)
	public void deleteDepartmentEntity(DepartmentEntity entity) {
		departmentRepository.delete(entity);
	}
	
	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly=true)
	public DepartmentEntity getDepartmentEntity(Long id){
		return departmentDao.findDepartment(id); 
	}

}
