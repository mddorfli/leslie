package org.leslie.server.jpa.mapping.impl;

import javax.persistence.Persistence;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.leslie.server.jpa.entity.Skill;
import org.leslie.server.jpa.mapping.CustomDataMapping;
import org.leslie.shared.skill.SkillTablePageData.SkillTableRowData;

public class SkillMapping implements CustomDataMapping<Skill, AbstractFormData, SkillTableRowData> {

	@Override
	public void read(Skill fromEntity, SkillTableRowData toRow) {
		// this is to prevent database access for lazy-loaded fields. The data must be pre-fetched by the query.
		if (Persistence.getPersistenceUtil().isLoaded(fromEntity, "category")) {
			toRow.setCategoryId(fromEntity.getCategory().getId());
			toRow.setCategory(fromEntity.getCategory().getName());
		}
	}

	@Override
	public void read(Skill fromEntity, AbstractFormData toForm) {
		// TODO Auto-generated method stub
	}

	@Override
	public void write(AbstractFormData fromForm, Skill toEntity) {
		// TODO Auto-generated method stub
	}

}
