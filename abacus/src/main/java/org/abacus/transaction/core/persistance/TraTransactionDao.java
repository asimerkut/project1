package org.abacus.transaction.core.persistance;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.abacus.transaction.shared.entity.TraDetailEntity;
import org.abacus.transaction.shared.entity.TraDocumentEntity;
import org.abacus.transaction.shared.holder.TraDocumentSearchCriteria;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.util.StringUtils;




public abstract class TraTransactionDao<T extends TraDocumentEntity, D extends TraDetailEntity> {

	@PersistenceContext
	private EntityManager em;
	
	public abstract Class<T> getDocumentClass();

	public abstract Class<D> getDetailClass();

	public T documentSave(T document) {
		Session currentSession = em.unwrap(Session.class);
		currentSession.save(document);
		return document;
	}

	public Boolean documentDelete(T document) {
		Session currentSession = em.unwrap(Session.class);
		currentSession.delete(document);
		return true;
	}

	public D detailSave(D detail) {
		Session currentSession = em.unwrap(Session.class);
		currentSession.save(detail);
		detail.savePoint();
		return detail;		
	}

	public Boolean detailDelete(D detail) {
		Session currentSession = em.unwrap(Session.class);
		currentSession.delete(detail);
		return true;		
	}

	public List<T> readDocument(TraDocumentSearchCriteria documentSearchCriteria, String organization, String fiscalYearId) {
		Session currentSession = em.unwrap(Session.class);
		Criteria criteria = currentSession.createCriteria(getDocumentClass(),"s");
		criteria.createAlias("s.fiscalPeriod", "fp", JoinType.INNER_JOIN);
		criteria.createAlias("s.item", "itm", JoinType.LEFT_OUTER_JOIN);

		if(documentSearchCriteria.getDocumentId() != null){
			criteria.add(Restrictions.eq("s.id", documentSearchCriteria.getDocumentId()));
		}
		
		if(StringUtils.hasText(organization)){
			criteria.add(Restrictions.like("s.organization.id", organization+"%"));
		}
		
		if(StringUtils.hasText(fiscalYearId)){
			criteria.add(Restrictions.eq("fp.fiscalYear.id", fiscalYearId));
		}
		
		if(StringUtils.hasText(documentSearchCriteria.getDocNo())){
			criteria.add(Restrictions.like("s.docNo","%"+documentSearchCriteria.getDocNo()+"%"));
		}

		if(documentSearchCriteria.getDocStartDate()!=null ){
			criteria.add(Restrictions.ge("s.docDate", documentSearchCriteria.getDocStartDate()));
		}

		if(documentSearchCriteria.getDocEndDate()!=null ){
			criteria.add(Restrictions.le("s.docDate", documentSearchCriteria.getDocEndDate()));
		}

		criteria.addOrder(Order.desc("s.docDate"));
		
		List<T> result = criteria.list();
		
		return result;
	}
	
}