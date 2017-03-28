package org.leslie.server.skill;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.entity.Skill;
import org.leslie.server.entity.SkillCategory;
import org.leslie.server.jpa.JPA;
import org.leslie.server.mapping.MappingUtility;
import org.leslie.shared.security.permission.CreateSkillPermission;
import org.leslie.shared.security.permission.ReadSkillPermission;
import org.leslie.shared.security.permission.UpdateSkillPermission;
import org.leslie.shared.skill.ISkillCategoryService;
import org.leslie.shared.skill.SkillCategoryFormData;
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

	@Override
	public SkillCategoryFormData load(SkillCategoryFormData formData) throws ProcessingException {
		if (!ACCESS.check(new ReadSkillPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}

		SkillCategory category = JPA.find(SkillCategory.class, formData.getCategoryId());
		MappingUtility.importFormData(category, formData);

		return formData;
	}

	@Override
	public SkillCategoryFormData create(SkillCategoryFormData formData) throws ProcessingException {
		if (ACCESS.getLevel(new CreateSkillPermission()) < CreateSkillPermission.LEVEL_ALL) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		SkillCategory category = new SkillCategory();
		MappingUtility.exportFormData(formData, category);
		JPA.persist(category);
		formData.setCategoryId(category.getId());

		return formData;
	}

	@Override
	public SkillCategoryFormData store(SkillCategoryFormData formData) throws ProcessingException {
		if (ACCESS.getLevel(new UpdateSkillPermission()) < UpdateSkillPermission.LEVEL_ALL) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}

		SkillCategory category = JPA.find(SkillCategory.class, formData.getCategoryId());
		MappingUtility.exportFormData(formData, category);

		return formData;
	}

	@Override
	public void delete(List<Long> categoryIds) throws ProcessingException {
		if (ACCESS.getLevel(new UpdateSkillPermission()) < UpdateSkillPermission.LEVEL_ALL) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}

		// remove the category from any skills using it
		for (Skill skill : JPA.createNamedQuery(Skill.QUERY_IN_CATEGORY_IDS, Skill.class)
				.setParameter("categoryIds", categoryIds)
				.getResultList()) {
			skill.setCategory(null);
		}

		for (Long id : categoryIds) {
			SkillCategory category = JPA.find(SkillCategory.class, id);
			JPA.remove(category);
		}
	}

}
