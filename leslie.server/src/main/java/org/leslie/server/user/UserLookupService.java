package org.leslie.server.user;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.leslie.server.jpa.AbstractJpaLookupService;
import org.leslie.server.jpa.entity.User;
import org.leslie.shared.user.IUserLookupService;

public class UserLookupService extends AbstractJpaLookupService<Long, User> implements IUserLookupService {

    @Override
    protected Class<User> getConfiguredEntityType() {
	return User.class;
    }

    @Override
    protected String getConfiguredSqlSelect(ILookupCall<Long> call) {
	return ""
		+ "SELECT u "
		+ "  FROM " + User.class.getSimpleName() + " u ";
    }

    @Override
    protected Object[] execGenerateDataRow(User user, ILookupCall<Long> call) {
	// final UserLookupCall lookup = (UserLookupCall) call;

	// FIXME
	boolean enabled = true;
	// boolean enabled = lookup.getDisabledProjectId() == null ||
	// user.getProjectAssignments().stream()
	// .map(UserProjectAssignment::getProject)
	// .noneMatch(project -> project.getId() ==
	// lookup.getDisabledProjectId().longValue());

	return new Object[] { user.getId(), user.getDisplayName(), null, null, null, null, null, enabled };
    }

}
