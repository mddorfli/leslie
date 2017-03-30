package org.leslie.shared.skill;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface ISkillAssessmentService extends IService {

	SkillAssessmentFormData load(SkillAssessmentFormData formData) throws ProcessingException;

	SkillAssessmentFormData store(SkillAssessmentFormData formData) throws ProcessingException;

	/**
	 * @param skillId
	 * @return true if the current user has this skill assessment
	 */
	boolean checkCurrentUserHasSkillAssessment(long skillAssessmentId);

	SkillAssessmentHistoryFormData loadHistory(SkillAssessmentHistoryFormData formData);
}
