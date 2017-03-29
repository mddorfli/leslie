package org.leslie.server.skill;

import java.sql.Date;
import java.time.Instant;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
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

@Bean
public class SkillAssessmentService implements ISkillAssessmentService {

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
		JPA.persist(assessment);

		user.getSkillAssessments().add(assessment);
		skill.getAssessments().add(assessment);

		return formData;
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
