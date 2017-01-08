package org.leslie.server.activity;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.ProjectActivity;
import org.leslie.server.jpa.mapping.FieldMapper;
import org.leslie.shared.activity.IActivityService;
import org.leslie.shared.activity.ProjectActivityTablePageData;
import org.leslie.shared.activity.ProjectActivityTablePageData.ProjectActivityTableRowData;
import org.leslie.shared.security.permission.ReadProjectPermission;

public class AbstractActivityService implements IActivityService {

    @Override
    public ProjectActivityTablePageData getProjectActivityTableData(SearchFilter filter, Long projectId) {
	if (!ACCESS.check(new ReadProjectPermission(projectId))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}

	List<ProjectActivity> resultList = JPA.createQuery(""
		+ "SELECT pa "
		+ "  FROM ProjectActivity pa "
		+ "  JOIN FETCH pa.user "
		+ "  JOIN FETCH pa.project "
		+ " WHERE pa.project.id = :projectId ",
		ProjectActivity.class)
		.setParameter("projectId", projectId)
		.getResultList();

	ProjectActivityTablePageData pageData = new ProjectActivityTablePageData();
	FieldMapper.importTablePageData(resultList, pageData, (pa, row) -> {
	    ProjectActivityTableRowData resRow = (ProjectActivityTableRowData) row;
	    resRow.setProject(pa.getProject().getName());
	});

	return pageData;
    }
}
