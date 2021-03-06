package org.leslie.shared.user;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

public class UserLookupCall extends LookupCall<Long> {

    private static final long serialVersionUID = 1L;

    private Long projectId;

    private Long disabledProjectId;

    public Long getDisabledProjectId() {
	return disabledProjectId;
    }

    public void setDisabledProjectId(Long disabledProjectId) {
	this.disabledProjectId = disabledProjectId;
    }

    public Long getProjectId() {
	return projectId;
    }

    public void setProjectId(Long projectId) {
	this.projectId = projectId;
    }

    @Override
    protected Class<? extends ILookupService<Long>> getConfiguredService() {
	return IUserLookupService.class;
    }
}
