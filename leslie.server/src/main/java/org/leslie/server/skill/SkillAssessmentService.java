package org.leslie.server.skill;

import java.sql.Date;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.ServerSession;
import org.leslie.server.entity.Skill;
import org.leslie.server.entity.SkillAssessment;
import org.leslie.server.entity.User;
import org.leslie.server.jpa.JPA;
import org.leslie.shared.security.permission.AssessSkillPermission;
import org.leslie.shared.skill.ISkillAssessmentService;
import org.leslie.shared.skill.SkillAssessmentFormData;
import org.leslie.shared.skill.SkillAssessmentHistoryFormData;
import org.leslie.shared.skill.SkillAssessmentHistoryFormData.History;
import org.leslie.shared.skill.SkillAssessmentHistoryFormData.History.HistoryRowData;

@Bean
public class SkillAssessmentService implements ISkillAssessmentService {

	@Override
	public SkillAssessmentHistoryFormData loadHistory(SkillAssessmentHistoryFormData formData) {
		if (!ACCESS.check(new AssessSkillPermission(formData.getSkillAssessmentId()))) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		SkillAssessment current = JPA.find(SkillAssessment.class, formData.getSkillAssessmentId());
		formData.setUserId(current.getUser().getId());

		List<SkillAssessment> assessments = JPA.createNamedQuery(
				SkillAssessment.QUERY_HISTORY_BY_SKILL_ID_USER_ID_ORDERED, SkillAssessment.class)
				.setParameter("skillId", current.getSkill().getId())
				.setParameter("userId", current.getUser().getId())
				.getResultList();
		for (SkillAssessment assessment : assessments) {
			History history = formData.getHistory();
			HistoryRowData row = history.addRow();
			row.setAssessmentId(assessment.getId());
			row.setName(assessment.getSkill().getName());
			row.setSelfAssessment(assessment.getSelfAssessment());
			row.setAssessment(assessment.getAssessment());
			row.setAffinity(assessment.getSelfAffinity());
			row.setLastModified(assessment.getLastModified());
			row.setModifiedBy(assessment.getModifiedBy().getDisplayName());
			row.setModifiedById(assessment.getModifiedBy().getId());

			if (assessment.getSkill().getCategory() != null) {
				row.setCategory(assessment.getSkill().getCategory().getName());
			}

			if (assessment.getAssessedBy() != null) {
				row.setAssessedById(assessment.getAssessedBy().getId());
				row.setAssessedBy(assessment.getAssessedBy().getDisplayName());
			}
		}
		return formData;
	}

	@Override
	public SkillAssessmentFormData load(SkillAssessmentFormData formData) throws ProcessingException {
		Skill skill;
		if (formData.getSkillAssessmentId() != null) {
			if (!ACCESS.check(new AssessSkillPermission(formData.getSkillAssessmentId()))) {
				throw new VetoException(TEXTS.get("AuthorizationFailed"));
			}
			SkillAssessment assessment = JPA.find(SkillAssessment.class, formData.getSkillAssessmentId());
			formData.getSelfAssessment().setValue(assessment.getSelfAssessment());
			formData.getSelfAffinity().setValue(assessment.getSelfAffinity());

			skill = assessment.getSkill();
		} else {
			if (!ACCESS.check(new AssessSkillPermission())) {
				throw new VetoException(TEXTS.get("AuthorizationFailed"));
			}
			skill = JPA.find(Skill.class, formData.getSkillId());
		}

		formData.getSkillName().setValue(skill.getName());
		formData.getSkillDescription().setValue(skill.getDescription());

		return formData;
	}

	@Override
	public SkillAssessmentFormData store(SkillAssessmentFormData formData) throws ProcessingException {
		if (!ACCESS.check(new AssessSkillPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		User user = ServerSession.get().getUser();
		Skill skill = JPA.find(Skill.class, formData.getSkillId());

		// always inserts a new record
		SkillAssessment assessment = new SkillAssessment();
		assessment.setUser(user);
		assessment.setSkill(skill);
		assessment.setSelfAffinity(formData.getSelfAffinity().getValue());
		assessment.setSelfAssessment(formData.getSelfAssessment().getValue());
		assessment.setLastModified(Date.from(Instant.now()));
		assessment.setModifiedBy(user);

		// carry any existing assessment over to the new record
		List<SkillAssessment> history = JPA.createNamedQuery(
				SkillAssessment.QUERY_HISTORY_BY_SKILL_ID_USER_ID_ORDERED, SkillAssessment.class)
				.setParameter("skillId", skill.getId())
				.setParameter("userId", user.getId())
				.getResultList();
		SkillAssessment latestAssessment = CollectionUtility.firstElement(history);
		if (latestAssessment != null) {
			assessment.setAssessment(latestAssessment.getAssessment());
			assessment.setAssessedBy(latestAssessment.getAssessedBy());
		}

		JPA.persist(assessment);

		user.getSkillAssessments().add(assessment);
		skill.getAssessments().add(assessment);

		return formData;
	}

	@Override
	public void assessSkills(Long userId, List<Long> skillIds, Integer competencyLevel) {
		if (ACCESS.getLevel(new AssessSkillPermission()) != AssessSkillPermission.LEVEL_ALL) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}

		User assessedUser = JPA.find(User.class, userId);
		User currentUser = ServerSession.get().getUser();
		Map<Long, Skill> skills = JPA.createNamedQuery(Skill.QUERY_ALL, Skill.class).getResultList().stream()
				.collect(Collectors.toMap(Skill::getId, Function.identity()));

		for (Long skillId : skillIds) {
			Skill skill = skills.get(skillId);

			SkillAssessment assessment = new SkillAssessment();
			assessment.setUser(assessedUser);
			assessment.setSkill(skill);
			assessment.setAssessment(competencyLevel);
			assessment.setAssessedBy(currentUser);
			assessment.setLastModified(Date.from(Instant.now()));
			assessment.setModifiedBy(currentUser);

			// carry any existing self-assessment over to the new record
			List<SkillAssessment> history = JPA.createNamedQuery(
					SkillAssessment.QUERY_HISTORY_BY_SKILL_ID_USER_ID_ORDERED, SkillAssessment.class)
					.setParameter("skillId", skill.getId())
					.setParameter("userId", assessedUser.getId())
					.getResultList();
			SkillAssessment latestAssessment = CollectionUtility.firstElement(history);
			if (latestAssessment != null) {
				assessment.setSelfAffinity(latestAssessment.getSelfAffinity());
				assessment.setSelfAssessment(latestAssessment.getSelfAssessment());
			}

			JPA.persist(assessment);

			assessedUser.getSkillAssessments().add(assessment);
			skill.getAssessments().add(assessment);
		}
	}

	/**
	 * Runs without permission checks, as it is used by the permission checking
	 * mechanism.
	 * 
	 * @see org.leslie.shared.skill.ISkillService#checkCurrentUserHasSkillAssessment(long)
	 */
	@Override
	public boolean checkCurrentUserHasSkillAssessment(long skillAssessmentId) {
		User user = ServerSession.get().getUser();
		return user.getSkillAssessments().stream()
				.anyMatch(assessment -> assessment.getId() == skillAssessmentId);
	}

}
