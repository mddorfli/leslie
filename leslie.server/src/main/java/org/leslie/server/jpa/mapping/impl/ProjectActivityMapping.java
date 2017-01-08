package org.leslie.server.jpa.mapping.impl;

import java.math.BigDecimal;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.leslie.server.jpa.entity.ProjectActivity;
import org.leslie.server.jpa.mapping.CustomDataMapping;
import org.leslie.shared.activity.ProjectActivityTablePageData.ProjectActivityTableRowData;

public class ProjectActivityMapping
	implements CustomDataMapping<ProjectActivity, AbstractFormData, ProjectActivityTableRowData> {

    @Override
    public void read(ProjectActivity fromEntity, AbstractFormData toForm) {
	// n/a
    }

    @Override
    public void read(ProjectActivity fromEntity, ProjectActivityTableRowData toRow) {
	toRow.setProjectId(fromEntity.getProject().getId());
	toRow.setProject(fromEntity.getProject().getName());
	toRow.setUserId(fromEntity.getUser().getId());
	toRow.setUser(fromEntity.getUser().getDisplayName());
	toRow.setPercentage(BigDecimal.valueOf(fromEntity.getPercentage()));
    }

    @Override
    public void write(AbstractFormData fromForm, ProjectActivity toEntity) {
	// n/a
    }
}
