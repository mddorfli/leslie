package org.leslie.server.user;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.Project;
import org.leslie.server.jpa.entity.User;
import org.leslie.server.jpa.lookup.AbstractJpaEntityLookupService;
import org.leslie.shared.user.IUserLookupService;
import org.leslie.shared.user.UserLookupCall;

public class UserLookupService extends AbstractJpaEntityLookupService<Long, User> implements IUserLookupService {

    @Override
    protected Class<User> getConfiguredEntityType() {
	return User.class;
    }

    @Override
    protected String getConfiguredJpqlSelect() {
	return ""
		+ "SELECT u "
		+ "  FROM " + User.class.getSimpleName() + " u "
		+ "WHERE 1=1 "
		+ "<key>AND u.id = :key</key> "
		+ "<text>AND UPPER(u.name) LIKE UPPER(CONCAT(:text, '%'))</text> "
		+ "<all></all> ";
    }

    @Override
    protected List<ILookupRow<Long>> execGenerateLookupRowData(ILookupCall<Long> call, List<User> resultList) {
	final UserLookupCall lookup = (UserLookupCall) call;
	final List<?> disabledUserIds;
	if (lookup.getDisabledProjectId() != null) {
	    disabledUserIds = JPA.createQuery(""
		    + "SELECT DISTINCT KEY(pa).id "
		    + "  FROM " + Project.class.getSimpleName() + " p "
		    + "  JOIN p.userAssignments pa "
		    + " WHERE p.id = :disabledProjectId ")
		    .setParameter("disabledProjectId", lookup.getDisabledProjectId())
		    .getResultList();
	} else {
	    disabledUserIds = Collections.EMPTY_LIST;
	}
	final Project project = lookup.getProjectId() == null ? null : JPA.find(Project.class, lookup.getProjectId());

	List<ILookupRow<Long>> rowData = resultList.stream()
		.filter(user -> project == null || project.getUserAssignments().keySet().contains(user))
		.map(user -> UserLookupService.generateRow(user, disabledUserIds))
		.collect(Collectors.toList());
	return rowData;
    }

    private static ILookupRow<Long> generateRow(User user, List<?> disabledUserIds) {
	boolean enabled = !disabledUserIds.contains(Long.valueOf(user.getId()));
	Object[] row = new Object[] {
		Long.valueOf(user.getId()),
		user.getDisplayName(),
		null,
		null,
		null,
		null,
		null,
		Boolean.valueOf(enabled)
	};
	return new LookupRow<>(row, row.length - 1, Long.class);
    }
}
