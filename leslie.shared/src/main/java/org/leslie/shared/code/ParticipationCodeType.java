package org.leslie.shared.code;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;
import org.leslie.shared.code.ParticipationCodeType.Participation;

public class ParticipationCodeType extends AbstractCodeType<Long, Participation> {

    public enum Participation {
	NONE,
	VIEWER,
	MEMBER,
	MANAGER
    }

    private static final long serialVersionUID = 1L;
    public static final long ID = 100L;

    @Override
    public Long getId() {
	return ID;
    }

    @Order(1000)
    public static class ViewerCode extends AbstractCode<Participation> {
	private static final long serialVersionUID = 1L;
	public static final Participation ID = Participation.VIEWER;

	@Override
	protected String getConfiguredText() {
	    return TEXTS.get("Viewer");
	}

	@Override
	public ParticipationCodeType.Participation getId() {
	    return ID;
	}
    }

    @Order(2000)
    public static class MemberCode extends AbstractCode<Participation> {
	private static final long serialVersionUID = 1L;
	public static final Participation ID = Participation.MEMBER;

	@Override
	protected String getConfiguredText() {
	    return TEXTS.get("Member");
	}

	@Override
	public Participation getId() {
	    return ID;
	}
    }

    @Order(3000)
    public static class ManagerCode extends AbstractCode<Participation> {
	private static final long serialVersionUID = 1L;
	public static final Participation ID = Participation.MANAGER;

	@Override
	protected String getConfiguredText() {
	    return TEXTS.get("Manager");
	}

	@Override
	public Participation getId() {
	    return ID;
	}
    }

}
