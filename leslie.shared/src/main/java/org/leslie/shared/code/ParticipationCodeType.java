package org.leslie.shared.code;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;
import org.leslie.shared.code.ParticipationCodeType.ParticipationLevel;

public class ParticipationCodeType extends AbstractCodeType<Long, ParticipationLevel> {

    /**
     * Defines the project participation levels.
     * 
     * @author Marco Dörfliger
     *
     */
    public enum ParticipationLevel {
	NONE,
	VIEWER,
	MEMBER,
	MANAGER;
    }

    private static final long serialVersionUID = 1L;
    public static final long ID = 100L;

    @Override
    public Long getId() {
	return ID;
    }

    @Order(1000)
    public static class ViewerCode extends AbstractCode<ParticipationLevel> {
	private static final long serialVersionUID = 1L;
	public static final ParticipationLevel ID = ParticipationLevel.VIEWER;

	@Override
	protected String getConfiguredText() {
	    return TEXTS.get("Viewer");
	}

	@Override
	public ParticipationCodeType.ParticipationLevel getId() {
	    return ID;
	}
    }

    @Order(2000)
    public static class MemberCode extends AbstractCode<ParticipationLevel> {
	private static final long serialVersionUID = 1L;
	public static final ParticipationLevel ID = ParticipationLevel.MEMBER;

	@Override
	protected String getConfiguredText() {
	    return TEXTS.get("Member");
	}

	@Override
	public ParticipationLevel getId() {
	    return ID;
	}
    }

    @Order(3000)
    public static class ManagerCode extends AbstractCode<ParticipationLevel> {
	private static final long serialVersionUID = 1L;
	public static final ParticipationLevel ID = ParticipationLevel.MANAGER;

	@Override
	protected String getConfiguredText() {
	    return TEXTS.get("Manager");
	}

	@Override
	public ParticipationLevel getId() {
	    return ID;
	}
    }

}
