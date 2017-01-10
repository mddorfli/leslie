package org.leslie.server.jpa.mapping.impl;

import java.math.BigDecimal;

import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.Project;
import org.leslie.server.jpa.entity.ProjectActivity;
import org.leslie.server.jpa.entity.User;
import org.leslie.server.jpa.mapping.CustomDataMapping;
import org.leslie.shared.activity.ProjectActivityTablePageData.ProjectActivityTableRowData;
import org.leslie.shared.project.ProjectActivityFormData;

public class ProjectActivityMapping
	implements CustomDataMapping<ProjectActivity, ProjectActivityFormData, ProjectActivityTableRowData> {

    @Override
    public void read(ProjectActivity fromEntity, ProjectActivityFormData toForm) {
	toForm.getUser().setValue(fromEntity.getUser().getId());
	toForm.getPercentage().setValue(BigDecimal.valueOf(fromEntity.getPercentage()));
    }

    @Override
    public void read(ProjectActivity fromEntity, ProjectActivityTableRowData toRow) {
	toRow.setUserId(fromEntity.getUser().getId());
	toRow.setUser(fromEntity.getUser().getDisplayName());
	toRow.setPercentage(BigDecimal.valueOf(fromEntity.getPercentage()));
    }

    @Override
    public void write(ProjectActivityFormData fromForm, ProjectActivity toEntity) {
	toEntity.setProject(JPA.find(Project.class, fromForm.getProjectId()));
	toEntity.setUser(JPA.find(User.class, fromForm.getUser().getValue()));
	toEntity.setPercentage(fromForm.getPercentage().getValue().doubleValue());
    }
}
