package leslie.org.leslie.server.user;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.leslie.server.jpa.StoredUser;

import leslie.org.leslie.server.AbstractJpaLookupService;
import leslie.org.leslie.shared.user.IUserLookupService;
import leslie.org.leslie.shared.user.UserLookupCall;

public class UserLookupService extends AbstractJpaLookupService<Long, StoredUser> implements IUserLookupService {

	@Override
	protected Class<StoredUser> getConfiguredEntityType() {
		return StoredUser.class;
	}

	@Override
	protected String getConfiguredSqlSelect(ILookupCall<Long> call) {
		return ""
				+ "SELECT u "
				+ "  FROM " + StoredUser.class.getSimpleName() + " u ";
	}

	@Override
	protected Object[] execGenerateDataRow(StoredUser user, ILookupCall<Long> call) {
		final UserLookupCall lookup = (UserLookupCall) call;

		boolean enabled = lookup.getDisabledProjectId() == null ||
				user.getProjects().stream()
						.noneMatch(project -> project.getId() == lookup.getDisabledProjectId().longValue());

		return new Object[]{user.getId(), user.getDisplayName(), null, null, null, null, null, enabled};
	}

}
