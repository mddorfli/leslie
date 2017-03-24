package org.leslie.server.role;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.leslie.server.jpa.entity.Role;
import org.leslie.server.jpa.lookup.AbstractJpaEntityLookupService;
import org.leslie.shared.role.IRoleLookupService;

public class RoleLookupService extends AbstractJpaEntityLookupService<Long, Role> implements IRoleLookupService {

	@Override
	protected Class<Role> getConfiguredEntityType() {
		return Role.class;
	}

	@Override
	protected String getConfiguredJpqlSelect(ILookupCall<Long> call) {
		return ""
				+ "SELECT r "
				+ "FROM " + Role.class.getSimpleName() + " r "
				+ "WHERE 1=1 "
				+ "<key>AND r.id = :key</key> "
				+ "<text>AND UPPER(r.name) LIKE UPPER(CONCAT(:text, '%'))</text> "
				+ "<all></all> ";
	}

	@Override
	protected List<ILookupRow<Long>> execGenerateLookupRowData(ILookupCall<Long> call, List<Role> resultList) {
		return resultList.stream()
				.map(role -> new LookupRow<Long>(Long.valueOf(role.getId()), role.getName()))
				.collect(Collectors.toList());
	}
}
