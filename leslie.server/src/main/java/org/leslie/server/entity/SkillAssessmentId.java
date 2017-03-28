package org.leslie.server.entity;

import java.io.Serializable;
import java.util.Arrays;

public class SkillAssessmentId implements Serializable {

	private Long user;

	private Long skill;

	public SkillAssessmentId() {
	}

	public SkillAssessmentId(Long user, Long skill) {
		this.user = user;
		this.skill = skill;
	}

	public Long getUser() {
		return user;
	}

	public void setUser(Long user) {
		this.user = user;
	}

	public Long getSkill() {
		return skill;
	}

	public void setSkill(Long skill) {
		this.skill = skill;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj != null && obj instanceof SkillAssessmentId) {
			SkillAssessmentId saObj = (SkillAssessmentId) obj;
			result = saObj.getSkill() == getSkill() && saObj.getUser() == getUser();
		}
		return result;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(new long[] { user, skill });
	}
}
