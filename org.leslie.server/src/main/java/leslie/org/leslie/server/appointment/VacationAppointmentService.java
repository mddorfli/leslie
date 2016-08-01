package leslie.org.leslie.server.appointment;

import java.util.Date;
import java.util.Optional;

import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.jpa.StoredAppointment;
import org.leslie.server.jpa.StoredUser;
import org.leslie.server.jpa.StoredVacationAppointment;

import leslie.org.leslie.server.JPA;
import leslie.org.leslie.server.ServerSession;
import leslie.org.leslie.shared.appointment.AppointmentCodeType.VacationCode;
import leslie.org.leslie.shared.appointment.IVacationAppointmentService;
import leslie.org.leslie.shared.security.CreateVacationPermission;
import leslie.org.leslie.shared.security.ReadVacationPermission;
import leslie.org.leslie.shared.security.UpdateVacationPermission;
import leslie.org.leslie.shared.work.VacationAppointmentFormData;
import leslie.org.leslie.shared.work.VacationAppointmentTablePageData;
import leslie.org.leslie.shared.work.VacationAppointmentTablePageData.VacationAppointmentTableRowData;

public class VacationAppointmentService implements IVacationAppointmentService {

	@Override
	public VacationAppointmentTablePageData getVacationAppointmentTableData(SearchFilter filter) {
		final VacationAppointmentTablePageData pageData = new VacationAppointmentTablePageData();
		JPA.createQuery(""
				+ "SELECT v "
				+ "  FROM " + StoredVacationAppointment.class.getSimpleName() + " v "
				+ " WHERE v.requestedBy.id = :userNr ",
				StoredVacationAppointment.class)
				.setParameter("userNr", ServerSession.get().getUserNr())
				.getResultList()
				.forEach((vacation) -> loadTableData(vacation, pageData));;
		return pageData;
	}

	@Override
	public VacationAppointmentFormData prepareCreate(VacationAppointmentFormData formData) {
		if (!ACCESS.check(new CreateVacationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		return formData;
	}

	@Override
	public VacationAppointmentFormData create(VacationAppointmentFormData formData) {
		if (!ACCESS.check(new CreateVacationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		verifyDateOverlap(formData.getFrom().getValue(), formData.getTo().getValue(), null);
		StoredVacationAppointment vacation = new StoredVacationAppointment();
		vacation.setAppointment(new StoredAppointment());
		exportFormData(formData, vacation);
		vacation.setRequestedBy(ServerSession.get().getUser());
		vacation.getAppointment().setAppointmentTypeUid(VacationCode.ID);
		JPA.persist(vacation.getAppointment());
		JPA.persist(vacation);
		return formData;
	}

	@Override
	public VacationAppointmentFormData load(VacationAppointmentFormData formData) {
		if (!ACCESS.check(new ReadVacationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredVacationAppointment vacation = JPA.find(StoredVacationAppointment.class, formData.getId());
		importFormData(formData, vacation);
		return formData;
	}

	@Override
	public VacationAppointmentFormData store(VacationAppointmentFormData formData) {
		if (!ACCESS.check(new UpdateVacationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		verifyDateOverlap(formData.getFrom().getValue(), formData.getTo().getValue(), formData.getId());
		StoredVacationAppointment vacation = JPA.find(StoredVacationAppointment.class, formData.getId());
		exportFormData(formData, vacation);
		JPA.merge(vacation);
		return formData;
	}

	@Override
	public void delete(Long selectedValue) {
		if (!ACCESS.check(new UpdateVacationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredVacationAppointment vacation = JPA.find(StoredVacationAppointment.class, selectedValue);
		JPA.remove(vacation);
		JPA.remove(vacation.getAppointment());
	}

	private static void verifyDateOverlap(Date fromDate, Date toDate, Long excludedAppointmentId) {
		TypedQuery<StoredVacationAppointment> query = JPA.createQuery(""
				+ "SELECT v "
				+ "  FROM " + StoredVacationAppointment.class.getSimpleName() + " v "
				+ "  JOIN FETCH v.appointment a "
				+ " WHERE v.requestedBy.id = :userNr "
				+ (excludedAppointmentId == null ? "" : "	  AND a.id != :excludedAppointmentId ")
				+ "   AND (:toDate >= a.fromDate AND :fromDate <= a.toDate OR "
				+ "        :fromDate <= a.toDate AND :toDate >= a.fromDate ) ",
				StoredVacationAppointment.class);
		query.setParameter("userNr", ServerSession.get().getUserNr());
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		if (excludedAppointmentId != null) {
			query.setParameter("excludedAppointmentId", excludedAppointmentId);
		}
		Optional<StoredVacationAppointment> conflict = query.getResultList().stream().findAny();
		if (conflict.isPresent()) {
			throw new VetoException(TEXTS.get("VacationDatesOverlapValidationError"));
		}
	}

	private static void loadTableData(StoredVacationAppointment vacation, VacationAppointmentTablePageData pageData) {
		VacationAppointmentTableRowData row = pageData.addRow();
		row.setId(vacation.getAppointment().getId());
		row.setFrom(vacation.getAppointment().getFromDate());
		row.setTo(vacation.getAppointment().getToDate());
		row.setApproved(vacation.getApprovedBy() != null);
		row.setApprovedBy(vacation.getApprovedBy() == null ? null : vacation.getApprovedBy().getDisplayName());
		row.setDays(DateUtility.getDaysBetween(
				vacation.getAppointment().getFromDate(), vacation.getAppointment().getToDate()) + 1);
	}

	private static void importFormData(VacationAppointmentFormData formData, StoredVacationAppointment vacation) {
		formData.getFrom().setValue(vacation.getAppointment().getFromDate());
		formData.getTo().setValue(vacation.getAppointment().getToDate());
		formData.getApproved().setValue(vacation.getApprovedBy() != null);
		formData.getApprovedBy().setValue(vacation.getApprovedBy() == null ? null : vacation.getApprovedBy().getId());
	}

	private static void exportFormData(VacationAppointmentFormData formData, StoredVacationAppointment vacation) {
		vacation.getAppointment().setFromDate(formData.getFrom().getValue());
		vacation.getAppointment().setToDate(formData.getTo().getValue());
		Long approverId = formData.getApprovedBy().getValue();
		vacation.setApprovedBy(approverId == null ? null : JPA.find(StoredUser.class, approverId));
	}
}
