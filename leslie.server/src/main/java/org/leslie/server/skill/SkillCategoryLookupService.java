package org.leslie.server.skill;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.leslie.server.entity.SkillCategory;
import org.leslie.server.jpa.AbstractJpaLookupService;
import org.leslie.server.jpa.JPA;
import org.leslie.shared.lookup.LongLookupRow;
import org.leslie.shared.skill.ISkillCategoryLookupService;

public class SkillCategoryLookupService extends AbstractJpaLookupService<Long> implements ISkillCategoryLookupService {

	@Override
	protected List<LongLookupRow> execGenerateRowData(ILookupCall<Long> call, LookupCallType callType) {
		CriteriaBuilder cb = JPA.getCriteriaBuilder();
		CriteriaQuery<LongLookupRow> cq = cb.createQuery(LongLookupRow.class);
		Root<SkillCategory> s = cq.from(SkillCategory.class);
		cq.select(cb.construct(LongLookupRow.class, s.get("id"), s.get("name")));

		if (callType == LookupCallType.KEY) {
			cq.where(cb.equal(s.get("id"), cb.parameter(Long.class, "key")));

		} else if (callType == LookupCallType.TEXT) {
			Expression<String> lhs = cb.upper(s.get("name"));
			Expression<String> rhs = cb.upper(cb.concat(cb.parameter(String.class, "text"), "%"));
			cq.where(cb.like(lhs, rhs));
		}

		TypedQuery<LongLookupRow> query = JPA.createQuery(cq);
		setCallQueryBinds(query, call, callType);

		return query.getResultList();
	}
}
