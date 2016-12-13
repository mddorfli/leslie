package leslie.org.leslie.server.role;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.leslie.server.jpa.Role;

import leslie.org.leslie.server.AbstractJpaLookupService;
import leslie.org.leslie.shared.role.IRoleLookupService;

public class RoleLookupService extends AbstractJpaLookupService<Long, Role> implements IRoleLookupService {

	@Override
	protected String getConfiguredSqlSelect(ILookupCall<Long> call) {
		return "SELECT r "
				+ " FROM " + Role.class.getSimpleName() + " r "
				+ "WHERE 1=1 "
				+ "<key>AND ROLE_NR = :key</key> "
				+ "<text>AND UPPER(NAME) LIKE UPPER(:text || '%')</text> "
				+ "<all></all> ";
	}

	@Override
	protected Class<Role> getConfiguredEntityType() {
		return Role.class;
	}

	@Override
	protected Object[] execGenerateDataRow(Role obj, ILookupCall<Long> call) {
		return new Object[]{obj.getId(), obj.getName()};
	}
}
