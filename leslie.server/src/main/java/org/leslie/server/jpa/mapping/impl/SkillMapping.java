package org.leslie.server.jpa.mapping.impl;

import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.Skill;
import org.leslie.server.jpa.entity.SkillCategory;
import org.leslie.server.jpa.mapping.CustomDataMapping;
import org.leslie.shared.skill.SkillFormData;
import org.leslie.shared.skill.SkillTablePageData.SkillTableRowData;

public class SkillMapping implements CustomDataMapping<Skill, SkillFormData, SkillTableRowData> {

	@Override
	public void read(Skill fromEntity, SkillTableRowData toRow) {
		toRow.setCategoryId(fromEntity.getCategory().getId());
		toRow.setCategory(fromEntity.getCategory().getName());
	}

	@Override
	public void read(Skill fromEntity, SkillFormData toForm) {
		toForm.getCategory().setValue(fromEntity.getCategory() == null ? null : fromEntity.getCategory().getId());
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
