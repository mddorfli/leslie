package org.leslie.server.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.User;
import org.leslie.server.jpa.lookup.AbstractJpaLookupService;
import org.leslie.shared.user.IUserLookupService;
import org.leslie.shared.user.UserLookupCall;

public class UserLookupService extends AbstractJpaLookupService<Long> implements IUserLookupService {

    /**
     * Performs the equivalent lookup: <code>
     "SELECT DISTINCT u "<br>
     + " FROM User u "<br>
     + (lookup.getProjectId() == null && lookup.getDisabledProjectId() == null ? "" : " JOIN FETCH u.projectAssignments pa ")<br>
     + "WHERE 1=1 "<br>
     + (lookup.getProjectId() == null ? "" : " AND pa.project.id = :projectId ")<br>
     + "&lt;key&gt;AND u.id = :key&lt;/key&gt; "<br>
     + "&lt;text&gt;AND UPPER(CONCAT(u.firstName, ' ', u.lastName)) LIKE UPPER(CONCAT(:text, '%'))&lt;/text&gt; "<br>
     + "&lt;all&gt;&lt;/all&gt; ";<br>
     </code>
     */
    @Override
    protected List<? extends ILookupRow<Long>> execGenerateRowData(ILookupCall<Long> call, CALL_TYPE callType) {
	UserLookupCall lookup = (UserLookupCall) call;
	CriteriaBuilder cb = JPA.getCriteriaBuilder();
	CriteriaQuery<User> q = cb.createQuery(User.class);
	Root<User> u = q.from(User.class);
	q.select(u).distinct(true);

	if (lookup.getProjectId() != null || lookup.getDisabledProjectId() != null) {
	    u.fetch("projectAssignments");
	}

	Predicate where = cb.conjunction();
	ParameterExpression<Long> projectId = cb.parameter(Long.class, "projectId");
	if (lookup.getProjectId() != null) {
	    where = cb.and(where, cb.equal(u.get("projectAssignments").get("project").get("id"), projectId));
	}
	switch (callType) {
	case KEY:
	    where = cb.and(where, cb.equal(u.get("id"), cb.parameter(Long.class, "key")));
	    break;
	case TEXT:
	    Expression<String> lhs = cb
		    .upper(cb.concat(cb.concat(u.get("firstName"), cb.literal(" ")), u.get("lastName")));
	    Expression<String> rhs = cb.upper(cb.concat(cb.parameter(String.class, "text"), "%"));
	    where = cb.and(where, cb.like(lhs, rhs));
	    break;
	default:
	    break;
	}
	q.where(where);

	TypedQuery<User> query = JPA.createQuery(q);
	setCallQueryBinds(query, call, callType);
	if (lookup.getProjectId() != null) {
	    query.setParameter(projectId, lookup.getProjectId());
	}

	return query.getResultList().stream()
		.map(user -> {
		    boolean enabled = true;
		    if (lookup.getDisabledProjectId() != null) {
			enabled = user.getProjectAssignments().stream()
				.noneMatch(pa -> pa.getProject().getId() == lookup.getDisabledProjectId().longValue());
		    }
		    return new LookupRow<>(user.getId(), user.getDisplayName()).withEnabled(enabled);
		})
		.collect(Collectors.toList());
    }

    // @Override
    // protected String getConfiguredJpqlSelect(ILookupCall<Long> call) {
    // UserLookupCall lookup = (UserLookupCall) call;
    // return ""
    // + "SELECT DISTINCT u "
    // + " FROM User u "
    // + (lookup.getProjectId() == null && lookup.getDisabledProjectId() == null
    // ? "" : " JOIN FETCH u.projectAssignments pa ")
    // + "WHERE 1=1 "
    // + (lookup.getProjectId() == null ? "" : " AND pa.project.id = :projectId
    // ")
    // + "<key>AND u.id = :key</key> "
    // + "<text>AND UPPER(CONCAT(u.firstName, ' ', u.lastName)) LIKE
    // UPPER(CONCAT(:text, '%'))</text> "
    // + "<all></all> ";
    // }

    // @Override
    // protected void execPrepareQuery(TypedQuery<LongLookupRow> query,
    // ILookupCall<Long> call) {
    // UserLookupCall lookup = (UserLookupCall) call;
    // if (lookup.getProjectId() != null) {
    // query.setParameter("projectId", lookup.getProjectId());
    // }
    // }

    // @Override
    // protected List<ILookupRow<Long>>
    // execGenerateLookupRowData(ILookupCall<Long> call, List<User> resultList)
    // {
    // final UserLookupCall lookup = (UserLookupCall) call;
    // final List<?> disabledUserIds;
    // if (lookup.getDisabledProjectId() != null) {
    // disabledUserIds = JPA.createQuery(""
    // + "SELECT DISTINCT KEY(pa).id "
    // + " FROM Project p "
    // + " JOIN p.userAssignments pa "
    // + " WHERE p.id = :disabledProjectId ")
    // .setParameter("disabledProjectId", lookup.getDisabledProjectId())
    // .getResultList();
    // } else {
    // disabledUserIds = Collections.EMPTY_LIST;
    // }
    // final Project project = lookup.getProjectId() == null ? null :
    // JPA.find(Project.class, lookup.getProjectId());

    // List<ILookupRow<Long>> rowData = resultList.stream()
    // .filter(user -> project == null ||
    // project.getUserAssignments().keySet().contains(user))
    // .map(user -> UserLookupService.generateRow(user, disabledUserIds))
    // .collect(Collectors.toList());

    // return rowData;
    // }
    //
    // private static ILookupRow<Long> generateRow(User user, List<?>
    // disabledUserIds) {
    // boolean enabled = !disabledUserIds.contains(Long.valueOf(user.getId()));
    // Object[] row = new Object[] {
    // Long.valueOf(user.getId()),
    // user.getDisplayName(),
    // null,
    // null,
    // null,
    // null,
    // null,
    // Boolean.valueOf(enabled)
    // };
    // return new LookupRow<>(row, row.length - 1, Long.class);
    // }
}
