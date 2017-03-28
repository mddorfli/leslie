package org.leslie.server.project;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.leslie.server.entity.Project;
import org.leslie.server.jpa.AbstractJpaLongLookupService;
import org.leslie.shared.lookup.LongLookupRow;
import org.leslie.shared.project.IProjectLookupService;

public class ProjectLookupService extends AbstractJpaLongLookupService implements IProjectLookupService {

	@Override
	protected String getConfiguredJpqlSelect(ILookupCall<Long> call) {
		return ""
				+ "SELECT NEW " + LongLookupRow.class.getName() + "(p.id, p.name) "
				+ "  FROM " + Project.class.getSimpleName() + " p "
				+ " WHERE 1=1 "
				+ "<key>AND p.id = :key</key> "
				+ "<text>AND UPPER(p.name) LIKE UPPER(CONCAT(:text, '%'))</text> "
				+ "<all></all> ";
	}
}
