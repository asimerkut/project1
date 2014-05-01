package org.abacus.definition.core.persistance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.abacus.common.web.AbcUtility;
import org.abacus.definition.core.persistance.repository.DefLevelRepository;
import org.abacus.definition.core.persistance.repository.DefValueRepository;
import org.abacus.definition.shared.entity.DefLevelEntity;
import org.abacus.definition.shared.entity.DefValueEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@SuppressWarnings("serial")
@Service
public class DefValueDao implements Serializable {

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private DefValueRepository valueRepository;

	@Autowired
	private DefLevelRepository levelRepository;

	public DefValueEntity save(DefValueEntity entity) {
		if (entity.isNew() && entity.getParent().getId().equals(0L)){
			DefValueEntity root = valueRepository.findOne(0L);
			entity.setParent(root);
		}
		entity = valueRepository.save(entity);
		em.flush();
		insertLevelTree(entity);
		return entity;
	}

	public void delete(DefValueEntity entity) {
		levelRepository.deleteLevelNode(entity.getId());
		valueRepository.delete(entity);
	}

	private void insertLevelTree(DefValueEntity value){
//		List<DefValueEntity> resultList = getValueChildren(value.getId());//Full Tree Level Insert, Node degistirme ozelligi olursa aktiflestirilecek
		List<DefValueEntity> resultList = new ArrayList<DefValueEntity>();
		resultList.add(value);

		for (DefValueEntity val : resultList) {
			levelRepository.deleteLevelNode(val.getId());
			insertLevelNode(val);
		}
	}

	private void insertLevelNode(DefValueEntity value){
		DefValueEntity hierarchy = value; 
		List<DefLevelEntity> levelList = new ArrayList<DefLevelEntity>();
		for (Integer descOrder = 1; hierarchy.getParent()!=null ; descOrder++) {
			DefLevelEntity lvl = new DefLevelEntity();
			lvl.setType(value.getType());
			lvl.setValue(value);
			lvl.setParent(hierarchy);
			lvl.setLevel_desc(descOrder);
			levelList.add(lvl);
			hierarchy = hierarchy.getParent(); 
		}
		for (DefLevelEntity lvl : levelList) {
			lvl.setLevel_asc(1+levelList.size()-lvl.getLevel_desc());
			lvl.setId(AbcUtility.LPad(value.getId().toString(),10,'0')+"_"+AbcUtility.LPad(lvl.getLevel_asc().toString(),2,'0'));
			levelRepository.save(lvl);
		}
	}
	
	private List<DefValueEntity> getValueChildren(Long valueId){
		
		StringBuilder sb = new StringBuilder();
		sb.append("select v.* from def_value v, ");
		sb.append("	(with recursive r (id) as ( "); //Oracle'da recursive silinecek "with r (id)"
		sb.append("	select root.id from def_value root where root.id = :valueId ");
		sb.append("	union all ");
		sb.append("	select child.id from def_value child join r on child.parent_id =r.id ");
		sb.append(") select * from r) tree ");
		sb.append("where v.id = tree.id ");

		Query query = em.createNativeQuery(sb.toString(), DefValueEntity.class);
		query.setParameter("valueId", valueId);
		List<DefValueEntity> resultList = query.getResultList();
		for (DefValueEntity relation : resultList) {
		  System.out.println(relation.toString());
		}
		return resultList;
	}
	
}