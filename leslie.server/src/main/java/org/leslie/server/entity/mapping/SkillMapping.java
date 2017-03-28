package org.leslie.server.entity.mapping;

import javax.persistence.Persistence;

import org.leslie.server.entity.Skill;
import org.leslie.server.entity.SkillCategory;
import org.leslie.server.jpa.JPA;
import org.leslie.server.mapping.CustomDataMapping;
import org.leslie.shared.skill.SkillFormData;
import org.leslie.shared.skill.SkillTablePageData.SkillTableRowData;

public class SkillMapping implements CustomDataMapping<Skill, SkillFormData, SkillTableRowData> {

	@Override
	public void read(Skill fromEntity, SkillTableRowData toRow) {
		toRow.setCategoryId(fromEntity.getCategory() == null ? null : fromEntity.getCategory().getId());
		toRow.setCategory(fromEntity.getCategory() == null ? null : fromEntity.getCategory().getName());
	}

	@Override
	public void read(Skill fromEntity, SkillFormData toForm) {
		// don't force lazy loading
		if (Persistence.getPersistenceUtil().isLoaded(fromEntity, "category")) {
			toForm.getCategory().setValue(fromEntity.getCategory() == null ? null : fromEntity.getCategory().getId());
		} else {
			toForm.getCategory().setValue(null);
		}
	}

	@Override
	public void write(SkillFormData fromForm, Skill toEntity) {
		SkillCategory category = null;
		if (fromForm.getCategory() != null) {
			category = JPA.find(SkillCategory.class, fromForm.getCategory().getValue());
		}
		toEntity.setCategory(category);
	}
}
