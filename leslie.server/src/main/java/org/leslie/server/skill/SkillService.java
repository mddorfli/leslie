package org.leslie.server.skill;

import java.util.List;

import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.Skill;
import org.leslie.server.jpa.mapping.MappingUtility;
import org.leslie.shared.skill.ISkillService;
import org.leslie.shared.skill.SkillTablePageData;

public class SkillService implements ISkillService {

	@Override
	public SkillTablePageData getSkillTableData() {
		List<Skill> allSkills = JPA.createNamedQuery(Skill.QUERY_ALL_FETCH_CATEGORY, Skill.class).getResultList();

		SkillTablePageData pageData = new SkillTablePageData();
		MappingUtility.importTablePageData(allSkills, pageData);
		return pageData;
	}

	@Override
	public SkillTablePageData getSkillTableData(Long categoryId) {
		List<Skill> skills = JPA.createNamedQuery(Skill.QUERY_BY_CATEGORY_ID, Skill.class)
				.setParameter("categoryId", categoryId).getResultList();

		SkillTablePageData pageData = new SkillTablePageData();
		MappingUtility.importTablePageData(skills, pageData);

		return pageData;
	}
}
