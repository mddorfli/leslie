package org.leslie.server.activity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.Activity;

public abstract class AbstractActivityService<T extends Activity> {

    abstract protected Class<T> getEntityType();

    protected void remove(List<Long> activityIds) {
	List<Activity> activities = JPA.createNamedQuery(Activity.QUERY_BY_IDS, Activity.class)
		.setParameter("activityIds", activityIds)
		.getResultList();

	for (Activity activity : activities) {
	    JPA.remove(activity);
	}
    }

    protected List<T> getCollisions(Date fromDate, Date toDate,
	    Optional<Long> userNr, Optional<Class<? extends Activity>> entityType, Optional<Long> excludedActivityId,
	    Optional<Predicate> whereConstraint) {
	return AbstractActivityService.getCollisions(getEntityType(), fromDate, toDate,
		userNr, entityType, excludedActivityId, whereConstraint);
    }

    /**
     * Performs the equivalent select:<br>
     * <code>
     * 	 TypedQuery<Activity> query = JPA.createQuery(""
     * 	 + "SELECT a "
     * 	 + " FROM Activity a "
     * 	 + " WHERE (:toDate >= a.from AND :fromDate <= a.to OR "
     * 	 + " :fromDate <= a.to AND :toDate >= a.from) "
     * 	 + (userNr.isPresent() ? " AND a.user.id = :userNr " : "")
     * 	 + (excludedActivityId.isPresent() ?
     * 	 " AND a.id != :excludedActivityId " : "")
     * 	 + (entityType.isPresent() ? " AND TYPE(a) = :entityType " : ""),
     * 	 Activity.class);
     * </code>
     * 
     * @param clazz
     * @param fromDate
     * @param toDate
     * @param userNr
     * @param entityType
     * @param excludedActivityId
     * @param whereConstraint
     * @return
     */
    static <T extends Activity> List<T> getCollisions(Class<T> clazz, Date fromDate, Date toDate,
	    Optional<Long> userNr, Optional<Class<? extends Activity>> entityType, Optional<Long> excludedActivityId,
	    Optional<Predicate> whereConstraint) {
	CriteriaBuilder cb = JPA.getCriteriaBuilder();
	CriteriaQuery<T> cQuery = cb.createQuery(clazz);
	Root<T> a = cQuery.from(clazz);
	Predicate p1 = cb.greaterThanOrEqualTo(cb.parameter(Date.class, "toDate"), a.get("from"));
	Predicate p2 = cb.lessThanOrEqualTo(cb.parameter(Date.class, "fromDate"), a.get("to"));
	Predicate p3 = cb.lessThanOrEqualTo(cb.parameter(Date.class, "fromDate"), a.get("to"));
	Predicate p4 = cb.greaterThanOrEqualTo(cb.parameter(Date.class, "toDate"), a.get("from"));
	Predicate where = cb.conjunction();
	where = cb.and(where, cb.or(cb.and(p1, p2), cb.and(p3, p4)));

	// optional (argument-based) parts
	if (userNr.isPresent()) {
	    where = cb.and(where, cb.equal(a.get("user").get("id"), cb.parameter(Long.class, "userNr")));
	}
	if (excludedActivityId.isPresent()) {
	    where = cb.and(where, cb.notEqual(a.get("id"), cb.parameter(Long.class, "excludedActivityId")));
	}
	if (entityType.isPresent()) {
	    where = cb.and(where, cb.equal(a.type(), cb.parameter(Class.class, "entityType")));
	}
	if (whereConstraint.isPresent()) {
	    where = cb.and(where, whereConstraint.get());
	}
	cQuery.where(where);
	TypedQuery<T> query = JPA.createQuery(cQuery);

	query.setParameter("fromDate", fromDate);
	query.setParameter("toDate", toDate);
	if (userNr.isPresent()) {
	    query.setParameter("userNr", userNr.get());
	}
	if (excludedActivityId.isPresent()) {
	    query.setParameter("excludedActivityId", excludedActivityId.get());
	}
	if (entityType.isPresent()) {
	    query.setParameter("entityType", entityType.get());
	}
	return query.getResultList();
    }
}
