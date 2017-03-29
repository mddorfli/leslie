package org.leslie.shared.security.permission;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;
import org.leslie.shared.skill.ISkillService;

/**
 * Determines whether users can make skill assessments.<br>
 * Level {@link #LEVEL_OWN} enables self-assessment only.<br>
 * Level {@link BasicHierarchyPermission#LEVEL_ALL} can assess everyone.<br>
 * 
 * @author mddorfli
 *
 */
public class AssessSkillPermission extends BasicHierarchyPermission {

	private static final long serialVersionUID = 1L;

	public static final int LEVEL_OWN = 10;

	private long skillAssessmentId;

	public AssessSkillPermission() {
		super(AssessSkillPermission.class.getSimpleName() + ".*", LEVEL_OWN);
	}

	public AssessSkillPermission(long skillAssessmentId) {
		super(AssessSkillPermission.class.getSimpleName() + "." + skillAssessmentId, LEVEL_UNDEFINED);
		this.skillAssessmentId = skillAssessmentId;
	}

	public long getSkillAssessmentId() {
		return skillAssessmentId;
	}

	@Override
	protected int execCalculateLevel(BasicHierarchyPermission other) {
		// by default, only the maximum level can guarantee access
		int result = LEVEL_ALL;
		if (other instanceof AssessSkillPermission) {
			AssessSkillPermission otherPermission = (AssessSkillPermission) other;
			ISkillService service = BEANS.get(ISkillService.class);
			if (service.checkCurrentUserHasSkillAssessment(otherPermission.getSkillAssessmentId())) {
				// if the user is the owner, LEVEL_OWN is sufficient.
				result = LEVEL_OWN;
			}
		}
		return result;
	}

}
