package org.leslie.server.skill;

import java.util.List;

import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.SkillCategory;
import org.leslie.server.jpa.mapping.MappingUtility;
import org.leslie.shared.skill.ISkillCategoryService;
import org.leslie.shared.skill.SkillCategoryTablePageData;

public class SkillCategoryService implements ISkillCategoryService {

	@Override
	public SkillCategoryTablePageData getSkillCategoryTableData() {
		SkillCategoryTablePageData pageData = new SkillCategoryTablePageData();
		List<SkillCategory> categories = JPA.createNamedQuery(SkillCategory.QUERY_ALL, SkillCategory.class)
				.getResultList();
		MappingUtility.importTablePageData(categories, pageData);
		return pageData;
	}
}
