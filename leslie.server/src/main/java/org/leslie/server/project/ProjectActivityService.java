package org.leslie.server.project;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.activity.AbstractActivityService;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.ProjectActivity;
import org.leslie.server.jpa.mapping.FieldMappingUtility;
import org.leslie.shared.activity.ProjectActivityTablePageData;
import org.leslie.shared.project.IProjectActivityService;
import org.leslie.shared.project.ProjectActivityFormData;
import org.leslie.shared.security.permission.ManageProjectPermission;
import org.leslie.shared.security.permission.ReadProjectPermission;

public class ProjectActivityService extends AbstractActivityService<ProjectActivity> implements IProjectActivityService {

    @Override
    protected Class<ProjectActivity> getEntityType() {
	return ProjectActivity.class;
    }

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
	FieldMappingUtility.importTablePageData(resultList, pageData);

	return pageData;
    }

    @Override
    public ProjectActivityFormData prepareCreate(ProjectActivityFormData formData) {
	if (!ACCESS.check(new ManageProjectPermission(formData.getProjectId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	return formData;
    }

    @Override
    public ProjectActivityFormData create(ProjectActivityFormData formData) {
	if (!ACCESS.check(new ManageProjectPermission(formData.getProjectId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	ProjectActivity pa = new ProjectActivity();
	FieldMappingUtility.exportFormData(formData, pa);
	JPA.persist(pa);

	return formData;
    }

    @Override
    public ProjectActivityFormData load(ProjectActivityFormData formData) {
	ProjectActivity pa = JPA.find(ProjectActivity.class, formData.getActivityId());
	if (!ACCESS.check(new ReadProjectPermission(pa.getProject().getId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	FieldMappingUtility.importFormData(pa, formData);
	return formData;
    }

    @Override
    public ProjectActivityFormData store(ProjectActivityFormData formData) {
	if (!ACCESS.check(new ManageProjectPermission(formData.getProjectId()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	ProjectActivity pa = JPA.find(ProjectActivity.class, formData.getActivityId());
	FieldMappingUtility.exportFormData(formData, pa);
	return formData;
    }

    @Override
    public void remove(List<Long> activityIds) {
	super.remove(activityIds);
    }
}
