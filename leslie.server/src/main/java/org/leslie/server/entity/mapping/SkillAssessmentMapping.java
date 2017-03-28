package org.leslie.server.entity.mapping;

import javax.persistence.Persistence;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.leslie.server.entity.SkillAssessment;
import org.leslie.server.mapping.CustomDataMapping;
import org.leslie.server.mapping.MappingUtility;
import org.leslie.shared.skill.SkillTablePageData.SkillTableRowData;

public class SkillAssessmentMapping implements CustomDataMapping<SkillAssessment, AbstractFormData, SkillTableRowData> {

	@Override
	public void read(SkillAssessment fromEntity, SkillTableRowData toRow) {
		if (Persistence.getPersistenceUtil().isLoaded(fromEntity, "assessedBy") && fromEntity.getAssessedBy() != null) {
			toRow.setAssessedById(fromEntity.getAssessedBy().getId());
			toRow.setAssessedBy(fromEntity.getAssessedBy().getDisplayName());
		}
		// delegate to other mapping for skill-related columns
		if (Persistence.getPersistenceUtil().isLoaded(fromEntity, "skill")) {
			MappingUtility.importTableRowData(fromEntity.getSkill(), toRow);
		}
	}

	@Override
	public void read(SkillAssessment fromEntity, AbstractFormData toForm) {
		// TODO Auto-generated method stub
	}

	@Override
	public void write(AbstractFormData fromForm, SkillAssessment toEntity) {
		// TODO Auto-generated method stub

	}

}
