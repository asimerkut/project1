package org.abacus.transaction.core.persistance.repository;

import java.util.List;

import org.abacus.transaction.shared.entity.FinDetailEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface FinDetailRepository extends CrudRepository<FinDetailEntity, Long> {

	@Query("select d from FinDetailEntity d inner join fetch d.item i where d.document.id = :documentId")
	List<FinDetailEntity> findByDocumentId(@Param("documentId")Long documentId);

	@Query("select d from FinDetailEntity d inner join fetch d.item i where d.bsDocument.id = :documentId")
	List<FinDetailEntity> findPRDetailList(@Param("documentId")Long documentId);

	@Modifying
	@Transactional
	@Query("update FinDetailEntity t set t.document=null where t.document.id = :finDocId and (resource != 'FIN' and resource != 'ACC')")
	void updateRefFinInfo(@Param("finDocId") Long id);
	
}
 