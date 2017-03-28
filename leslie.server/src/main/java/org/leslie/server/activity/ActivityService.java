package org.leslie.server.activity;

import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.scout.rt.platform.Bean;
import org.leslie.server.entity.Activity;
import org.leslie.server.jpa.JPA;

@Bean
public class ActivityService {

	public static void remove(List<Long> activityIds) {
		List<Activity> activities = JPA.createNamedQuery(Activity.QUERY_BY_IDS, Activity.class)
				.setParameter("activityIds", activityIds).getResultList();

		for (Activity activity : activities) {
			JPA.remove(activity);
		}
	}

	/**
	 * Performs the equivalent select:<br>
	 * <code>
	 * 	 TypedQuery<Activity> query = JPA.createQuery(""<br>
	 * 	 + "SELECT a "<br>
	 * 	 + " FROM Activity a "<br>
	 * 	 + " WHERE (:toDate >= a.from AND :fromDate <= a.to OR "<br>
	 * 	 + " :fromDate <= a.to AND :toDate >= a.from) "<br>
	 * 	 + (userNr != null ? " AND a.user.id = :userNr " : "")<br>
	 * 	 + (excludedActivityId != null ?<br>
	 * 	 " AND a.id != :excludedActivityId " : "")<br>
	 * 	 + (entityType != null ? " AND TYPE(a) = :entityType " : ""),<br>
	 * 	 Activity.class);<br>
	 * </code>
	 * 
	 * @param clazz
	 * @param fromDate
	 * @param toDate
	 * @param userNr
	 * @param excludedActivityId
	 * @param whereConstraint
	 * @return
	 */
	public static <T extends Activity> List<T> getCollisions(Class<T> clazz, Date fromDate, Date toDate, Long userNr,
			Long excludedActivityId) {
		assert fromDate != null;
		assert toDate != null;

		CriteriaBuilder cb = JPA.getCriteriaBuilder();
		CriteriaQuery<T> cQuery = cb.createQuery(clazz);
		Root<T> a = cQuery.from(clazz);
		Predicate p1 = cb.greaterThanOrEqualTo(cb.parameter(Date.class, "toDate"), a.get("from"));
		Predicate p2 = cb.lessThanOrEqualTo(cb.parameter(Date.class, "fromDate"), a.get("to"));
		Predicate p3 = cb.lessThanOrEqualTo(cb.parameter(Date.class, "fromDate"), a.get("to"));
		Predicate p4 = cb.greaterThanOrEqualTo(cb.parameter(Date.class, "toDate"), a.get("from"));
		Predicate where = cb.conjunction();
		where = cb.and(where, cb.or(cb.and(p1, p2), cb.and(p3, p4)));

		if (userNr != null) {
			where = cb.and(where, cb.equal(a.get("user").get("id"), cb.parameter(Long.class, "userNr")));
		}
		if (excludedActivityId != null) {
			where = cb.and(where, cb.notEqual(a.get("id"), cb.parameter(Long.class, "excludedActivityId")));
		}
		cQuery.where(where);
		TypedQuery<T> query = JPA.createQuery(cQuery);

		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		if (userNr != null) {
			query.setParameter("userNr", userNr);
		}
		if (excludedActivityId != null) {
			query.setParameter("excludedActivityId", excludedActivityId);
		}
		return query.getResultList();
	}
}
