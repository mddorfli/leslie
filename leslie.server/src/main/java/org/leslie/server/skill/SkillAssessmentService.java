package org.leslie.server.skill;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.entity.Skill;
import org.leslie.server.entity.SkillAssessment;
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
		// TODO Auto-generated method stub
		return null;
	}

}
