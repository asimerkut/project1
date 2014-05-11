package org.abacus.definition.core.persistance;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.abacus.definition.shared.entity.DefItemEntity;
import org.abacus.definition.shared.holder.ItemSearchCriteria;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@SuppressWarnings("serial")
@Service
public class DefItemDao implements Serializable {

	@PersistenceContext
	private EntityManager em;

	public List<DefItemEntity> requestItems(ItemSearchCriteria searchCriteria) {

		Criteria criteria = this.createRequestItemsCriteria(searchCriteria);
		
		if(searchCriteria.getFirst() != null && searchCriteria.getPageSize() != null){
			criteria.setFirstResult(searchCriteria.getFirst());
			criteria.setMaxResults(searchCriteria.getFirst() + searchCriteria.getPageSize());
		}
		
		criteria.addOrder(Order.asc("i.code"));
		
		List<DefItemEntity> resultList = criteria.list();
		
		return resultList;
	}
	
	public Criteria createRequestItemsCriteria(ItemSearchCriteria searchCriteria){
		Session currentSession = em.unwrap(Session.class);
		Criteria criteria  = currentSession.createCriteria(DefItemEntity.class,"i");
		
		if(StringUtils.hasText(searchCriteria.getOrganization())){
			criteria.add(Restrictions.eq("i.organization.id", searchCriteria.getOrganization()));
		}
		if(searchCriteria.getItemType() != null){
			criteria.add(Restrictions.eq("i.type.id", searchCriteria.getItemType().name()));
		}
		if(searchCriteria.getItemClass() != null){
			criteria.add(Restrictions.eq("i.itemClass", searchCriteria.getItemClass()));
		}
		if(StringUtils.hasText(searchCriteria.getCodeLike())){
			criteria.add(Restrictions.ilike("i.code", "%" + searchCriteria.getCodeLike().toLowerCase() + "%"));
		}
		if(StringUtils.hasText(searchCriteria.getNameLike())){
			criteria.add(Restrictions.ilike("i.name", "%" + searchCriteria.getNameLike() + "%"));
		}
		if(StringUtils.hasText(searchCriteria.getCategoryCodeLike())){
			criteria.createAlias("i.category", "c");
			criteria.add(Restrictions.ilike("c.name", "%" + searchCriteria.getCategoryCodeLike() + "%"));
		}
		if(searchCriteria.getStatus() != null){
			criteria.add(Restrictions.eq("i.active", searchCriteria.getStatus()));
		}
		
		return criteria;
	}
	
	public Integer itemCount(ItemSearchCriteria searchCriteria){
		Criteria criteria = this.createRequestItemsCriteria(searchCriteria);
		criteria.setProjection(Projections.rowCount());
		Long count = (Long) criteria.uniqueResult();
		return count.intValue();
	}

	
	
}
