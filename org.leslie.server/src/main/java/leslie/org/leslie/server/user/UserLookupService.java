package leslie.org.leslie.server.user;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.leslie.server.jpa.User;
import org.leslie.server.jpa.UserProjectAssignment;

import leslie.org.leslie.server.AbstractJpaLookupService;
import leslie.org.leslie.shared.user.IUserLookupService;
import leslie.org.leslie.shared.user.UserLookupCall;

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
	final UserLookupCall lookup = (UserLookupCall) call;

	boolean enabled = lookup.getDisabledProjectId() == null ||
		user.getProjectAssignments().stream()
			.map(UserProjectAssignment::getProject)
			.noneMatch(project -> project.getId() == lookup.getDisabledProjectId().longValue());

	return new Object[] { user.getId(), user.getDisplayName(), null, null, null, null, null, enabled };
    }

}
