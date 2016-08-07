package leslie.org.leslie.server.role;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.leslie.server.jpa.StoredRole;

import leslie.org.leslie.server.AbstractJpaLookupService;
import leslie.org.leslie.shared.role.IRoleLookupService;

public class RoleLookupService extends AbstractJpaLookupService<Long, StoredRole> implements IRoleLookupService {

	@Override
	protected String getConfiguredSqlSelect(ILookupCall<Long> call) {
		return "SELECT r "
				+ " FROM " + StoredRole.class.getSimpleName() + " r "
				+ "WHERE 1=1 "
				+ "<key>AND ROLE_NR = :key</key> "
				+ "<text>AND UPPER(NAME) LIKE UPPER(:text || '%')</text> "
				+ "<all></all> ";
	}

	@Override
	protected Class<StoredRole> getConfiguredEntityType() {
		return StoredRole.class;
	}

	@Override
	protected Object[] execGenerateDataRow(StoredRole obj, ILookupCall<Long> call) {
		return new Object[]{obj.getId(), obj.getName()};
	}
}
