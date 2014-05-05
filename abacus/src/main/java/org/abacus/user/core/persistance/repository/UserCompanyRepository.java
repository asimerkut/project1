package org.abacus.user.core.persistance.repository;

import org.abacus.user.shared.entity.SecUserOrganizationEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserCompanyRepository  extends CrudRepository<SecUserOrganizationEntity, Long>{


	@Modifying
	@Transactional
	@Query("delete from SecUserCompanyEntity e where e.user.id = :username")
	void delete(@Param("username")String username);

}
