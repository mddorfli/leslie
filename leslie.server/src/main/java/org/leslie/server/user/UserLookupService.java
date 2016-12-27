package org.leslie.server.user;

import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.services.lookup.AbstractLookupService;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.Project;
import org.leslie.server.jpa.entity.User;
import org.leslie.shared.code.ParticipationCodeType.Participation;
import org.leslie.shared.user.IUserLookupService;
import org.leslie.shared.user.UserLookupCall;

public class UserLookupService extends AbstractLookupService<Long> implements IUserLookupService {

    private enum CALL_TYPE {
	KEY, TEXT, ALL, REC
    }

    public List<ILookupRow<Long>> getRowData(ILookupCall<Long> call, CALL_TYPE callType) {
	final UserLookupCall lookup = (UserLookupCall) call;

	String queryString = filterSqlByCallType(""
		+ "SELECT u "
		+ "  FROM " + User.class.getSimpleName() + " u "
		+ "WHERE 1=1 "
		+ "<key>AND u.id = :key</key> "
		+ "<text>AND UPPER(u.name) LIKE UPPER(:text || '%')</text> "
		+ "<all></all> ", callType);

	TypedQuery<User> query = JPA.createQuery(queryString, User.class);

	if (callType == CALL_TYPE.KEY) {
	    query.setParameter("key", call.getKey());

	} else if (callType == CALL_TYPE.TEXT) {
	    query.setParameter("text", call.getText());
	}
	query.setMaxResults(call.getMaxRowCount());

	return query.getResultList().stream()
		.map(user -> UserLookupService.generateRow(lookup, user))
		.collect(Collectors.toList());
    }

    private String filterSqlByCallType(String sqlSelect, CALL_TYPE callType) {
	String modifiedQueryString;
	switch (callType) {
	default:
	case ALL:
	    modifiedQueryString = filterSqlByAll(sqlSelect);
	    break;
	case KEY:
	    modifiedQueryString = filterSqlByKey(sqlSelect);
	    break;
	case REC:
	    modifiedQueryString = filterSqlByRec(sqlSelect);
	    break;
	case TEXT:
	    modifiedQueryString = filterSqlByText(sqlSelect);
	    break;
	}
	return modifiedQueryString;
    }

    private static ILookupRow<Long> generateRow(UserLookupCall lookup, User user) {
	boolean enabled = true;
	if (lookup.getDisabledProjectId() != null) {
	    enabled = user.getProjectAssignments().keySet().stream()
		    .noneMatch(project -> project.getId() == lookup.getDisabledProjectId().longValue());
	    System.out.println("Projects for user " + user.getDisplayName() + ": ");
	    for (Entry<Project, Participation> entry : user.getProjectAssignments().entrySet()) {
		System.out.println("Project " + entry.getKey().getName() + " with role " + entry.getValue().name());
	    }
	    System.out.println();
	}
	Object[] row = new Object[] {
		Long.valueOf(user.getId()),
		user.getDisplayName(),
		null,
		null,
		null,
		null,
		null,
		Boolean.valueOf(enabled) };
	return new LookupRow<>(row, row.length - 1, Long.class);
    }

    @Override
    public List<? extends ILookupRow<Long>> getDataByKey(ILookupCall<Long> call) {
	return getRowData(call, CALL_TYPE.KEY);
    }

    @Override
    public List<? extends ILookupRow<Long>> getDataByText(ILookupCall<Long> call) {
	return getRowData(call, CALL_TYPE.TEXT);
    }

    @Override
    public List<? extends ILookupRow<Long>> getDataByAll(ILookupCall<Long> call) {
	return getRowData(call, CALL_TYPE.ALL);
    }

    @Override
    public List<? extends ILookupRow<Long>> getDataByRec(ILookupCall<Long> call) {
	return getRowData(call, CALL_TYPE.REC);
    }

    /**
     * Process xml tags.<br>
     * Keep content of "key" tag.<br>
     * Remove text,all,rec tags.
     */
    protected String filterSqlByKey(String sqlSelect) {
	return StringUtility.removeTagBounds(StringUtility.removeTags(sqlSelect, new String[] { "text", "all", "rec" }),
		"key");
    }

    /**
     * Process xml tags.<br>
     * Keep content of "text" tag.<br>
     * Remove key,all,rec tags.
     */
    protected String filterSqlByText(String sqlSelect) {
	return StringUtility.removeTagBounds(StringUtility.removeTags(sqlSelect, new String[] { "key", "all", "rec" }),
		"text");
    }

    /**
     * Process xml tags.<br>
     * Keep content of "all" tag.<br>
     * Remove key,text,rec tags.
     */
    protected String filterSqlByAll(String sqlSelect) {
	return StringUtility.removeTagBounds(StringUtility.removeTags(sqlSelect, new String[] { "key", "text", "rec" }),
		"all");
    }

    /**
     * Process xml tags.<br>
     * Keep content of "rec" tag.<br>
     * Remove key,text,all tags.
     */
    protected String filterSqlByRec(String sqlSelect) {
	return StringUtility.removeTagBounds(StringUtility.removeTags(sqlSelect, new String[] { "key", "text", "all" }),
		"rec");
    }
}
