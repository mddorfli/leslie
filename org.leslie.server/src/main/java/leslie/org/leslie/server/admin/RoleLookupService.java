package leslie.org.leslie.server.admin;

import org.leslie.server.jpa.StoredRole;

import leslie.org.leslie.server.AbstractJpaLookupService;
import leslie.org.leslie.shared.admin.lookup.IRoleLookupService;

public class RoleLookupService extends AbstractJpaLookupService<Long, StoredRole> implements IRoleLookupService {

	@Override
	protected String getConfiguredSqlSelect() {
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
	protected Object[] execGenerateDataRow(StoredRole obj) {
		return new Object[]{obj.getId(), obj.getName()};
	}
}
