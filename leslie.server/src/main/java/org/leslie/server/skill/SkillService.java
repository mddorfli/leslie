package org.leslie.server.skill;

import java.util.Arrays;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.ServerSession;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.Skill;
import org.leslie.server.jpa.mapping.MappingUtility;
import org.leslie.shared.security.permission.CreateSkillPermission;
import org.leslie.shared.security.permission.ReadSkillPermission;
import org.leslie.shared.security.permission.UpdateSkillPermission;
import org.leslie.shared.skill.ISkillService;
import org.leslie.shared.skill.SkillFormData;
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
		List<Long> categoryIds = Arrays.asList(categoryId);
		List<Skill> skills = JPA.createNamedQuery(Skill.QUERY_IN_CATEGORY_IDS, Skill.class)
				.setParameter("categoryIds", categoryIds)
				.getResultList();

		SkillTablePageData pageData = new SkillTablePageData();
		MappingUtility.importTablePageData(skills, pageData);

		return pageData;
	}

	@Override
	public SkillFormData load(SkillFormData formData) throws ProcessingException {
		if (!ACCESS.check(new ReadSkillPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}

		Skill skill = JPA.find(Skill.class, formData.getSkillId());
		MappingUtility.importFormData(skill, formData);

		return formData;
	}

	@Override
	public SkillFormData create(SkillFormData formData) throws ProcessingException {
		if (!ACCESS.check(new CreateSkillPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}

		Skill skill = new Skill();
		MappingUtility.exportFormData(formData, skill);
		JPA.persist(skill);
		formData.setSkillId(skill.getId());

		return formData;
	}

	@Override
	public SkillFormData store(SkillFormData formData) throws ProcessingException {
		if (!ACCESS.check(new UpdateSkillPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		Skill skill = JPA.find(Skill.class, formData.getSkillId());
		MappingUtility.exportFormData(formData, skill);

		return formData;
	}

	@Override
	public void delete(List<Long> skillIds) throws ProcessingException {
		if (!ACCESS.check(new UpdateSkillPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}

		for (Long id : skillIds) {
			Skill skill = JPA.find(Skill.class, id);
			JPA.remove(skill);
		}
	}

	@Override
	public boolean currentUserHasSkill(long skillId) {
		return ServerSession.get().getUser().getSkills().stream()
				.anyMatch(assessment -> assessment.getSkill().getId() == skillId);
	}
}
