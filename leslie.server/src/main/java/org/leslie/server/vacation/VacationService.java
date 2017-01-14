package org.leslie.server.vacation;

import java.util.List;
import java.util.Optional;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.ServerSession;
import org.leslie.server.activity.AbstractActivityService;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.VacationActivity;
import org.leslie.server.jpa.mapping.FieldMappingUtility;
import org.leslie.shared.security.permission.ApproveVacationPermission;
import org.leslie.shared.security.permission.RequestVacationPermission;
import org.leslie.shared.vacation.IVacationService;
import org.leslie.shared.vacation.VacationFormData;
import org.leslie.shared.vacation.VacationTablePageData;

public class VacationService extends AbstractActivityService<VacationActivity> implements IVacationService {

    @Override
    protected Class<VacationActivity> getEntityType() {
	return VacationActivity.class;
    }

    @Override
    public VacationTablePageData getVacationTableData(SearchFilter filter) {
	VacationTablePageData pageData = new VacationTablePageData();
	final List<VacationActivity> vacations;
	if (ACCESS.check(new ApproveVacationPermission())) {
	    vacations = JPA.createNamedQuery(VacationActivity.QUERY_ALL, VacationActivity.class)
		    .getResultList();
	} else {
	    vacations = JPA.createNamedQuery(VacationActivity.QUERY_BY_USER, VacationActivity.class)
		    .setParameter("user", ServerSession.get().getUser())
		    .getResultList();
	}
	FieldMappingUtility.importTablePageData(vacations, pageData);
	return pageData;
    }

    @Override
    public VacationFormData prepareCreate(VacationFormData formData) {
	if (!ACCESS.check(new RequestVacationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	formData.getRequestedBy().setValue(ServerSession.get().getUserNr());
	return formData;
    }

    @Override
    public VacationFormData create(VacationFormData formData) {
	if (!ACCESS.check(new RequestVacationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	List<VacationActivity> collisions = super.getCollisions(formData.getFrom().getValue(),
		formData.getTo().getValue(), Optional.of(formData.getRequestedBy().getValue()),
		Optional.of(VacationActivity.class), Optional.empty(), Optional.empty());
	if (!collisions.isEmpty()) {
	    throw new VetoException(TEXTS.get("VacationOverlaps"));
	}
	VacationActivity va = new VacationActivity();
	FieldMappingUtility.exportFormData(formData, va);
	JPA.persist(va);
	return formData;
    }

    @Override
    public VacationFormData load(VacationFormData formData) {
	VacationActivity va = JPA.find(VacationActivity.class, formData.getActivityId());
	// you can only update your own vacations, unless you have the approve
	// vacation permission
	if (!(va.getUser().getId() == ServerSession.get().getUserNr().longValue()
		|| ACCESS.check(new ApproveVacationPermission()))) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	FieldMappingUtility.importFormData(va, formData);
	return formData;
    }

    @Override
    public VacationFormData store(VacationFormData formData) {
	if ((formData.getApprovedBy().getValue() != null
		|| !ServerSession.get().getUserNr().equals(formData.getRequestedBy().getValue()))
		&& !ACCESS.check(new ApproveVacationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	List<VacationActivity> collisions = super.getCollisions(formData.getFrom().getValue(),
		formData.getTo().getValue(), Optional.of(formData.getRequestedBy().getValue()),
		Optional.of(VacationActivity.class), Optional.of(formData.getActivityId()), Optional.empty());
	if (!collisions.isEmpty()) {
	    throw new VetoException(TEXTS.get("VacationOverlaps"));
	}
	VacationActivity va = JPA.find(VacationActivity.class, formData.getActivityId());
	FieldMappingUtility.exportFormData(formData, va);
	return formData;
    }

    @Override
    public void approveVacation(Long vacationId) {
	if (!ACCESS.check(new ApproveVacationPermission())) {
	    throw new VetoException(TEXTS.get("AuthorizationFailed"));
	}
	JPA.find(VacationActivity.class, vacationId)
		.setApprovedBy(ServerSession.get().getUser());
    }

    @Override
    public void remove(List<Long> activityIds) {
	super.remove(activityIds);
    }
}
