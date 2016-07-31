package leslie.org.leslie.server.work;

import java.util.Date;
import java.util.Optional;

import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.jpa.StoredUser;
import org.leslie.server.jpa.StoredVacation;

import leslie.org.leslie.server.JPA;
import leslie.org.leslie.server.ServerSession;
import leslie.org.leslie.shared.security.CreateVacationPermission;
import leslie.org.leslie.shared.security.ReadVacationPermission;
import leslie.org.leslie.shared.security.UpdateVacationPermission;
import leslie.org.leslie.shared.work.IVacationService;
import leslie.org.leslie.shared.work.VacationFormData;
import leslie.org.leslie.shared.work.VacationsTablePageData;
import leslie.org.leslie.shared.work.VacationsTablePageData.VacationsTableRowData;

public class VacationService implements IVacationService {

	@Override
	public VacationsTablePageData getVacationsTableData(SearchFilter filter) {
		final VacationsTablePageData pageData = new VacationsTablePageData();
		JPA.createQuery(""
				+ "SELECT v "
				+ "  FROM " + StoredVacation.class.getSimpleName() + " v "
				+ " WHERE v.requestedBy.id = :userNr ",
				StoredVacation.class)
				.setParameter("userNr", ServerSession.get().getUserNr())
				.getResultList()
				.forEach((vacation) -> loadTableData(vacation, pageData));;
		return pageData;
	}

	@Override
	public VacationFormData prepareCreate(VacationFormData formData) {
		if (!ACCESS.check(new CreateVacationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		return formData;
	}

	@Override
	public VacationFormData create(VacationFormData formData) {
		if (!ACCESS.check(new CreateVacationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		verifyDateOverlap(formData.getFrom().getValue(), formData.getTo().getValue());
		StoredVacation vacation = new StoredVacation();
		exportFormData(formData, vacation);
		vacation.setRequestedBy(ServerSession.get().getUser());
		JPA.persist(vacation);
		return formData;
	}

	@Override
	public VacationFormData load(VacationFormData formData) {
		if (!ACCESS.check(new ReadVacationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredVacation vacation = JPA.find(StoredVacation.class, formData.getId());
		importFormData(formData, vacation);
		return formData;
	}

	@Override
	public VacationFormData store(VacationFormData formData) {
		if (!ACCESS.check(new UpdateVacationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		verifyDateOverlap(formData.getFrom().getValue(), formData.getTo().getValue());
		StoredVacation vacation = JPA.find(StoredVacation.class, formData.getId());
		exportFormData(formData, vacation);
		JPA.merge(vacation);
		return formData;
	}

	@Override
	public void delete(Long selectedValue) {
		if (!ACCESS.check(new UpdateVacationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredVacation vacation = JPA.find(StoredVacation.class, selectedValue);
		JPA.remove(vacation);
	}

	private static void verifyDateOverlap(Date fromDate, Date toDate) {
		TypedQuery<StoredVacation> query = JPA.createQuery(""
				+ "SELECT v "
				+ "  FROM " + StoredVacation.class.getSimpleName() + " v "
				+ " WHERE v.requestedBy.id = :userNr "
				+ "   AND (:toDate >= v.fromDate AND :fromDate <= v.toDate OR "
				+ "        :fromDate <= v.toDate AND :toDate >= v.fromDate ) ",
				StoredVacation.class);
		query.setParameter("userNr", ServerSession.get().getUserNr());
		query.setParameter("fromDate", fromDate);
		query.setParameter("toDate", toDate);
		Optional<StoredVacation> conflict = query.getResultList().stream().findAny();
		if (conflict.isPresent()) {
			throw new VetoException(TEXTS.get("VacationDatesOverlapValidationError"));
		}
	}

	private static void loadTableData(StoredVacation vacation, VacationsTablePageData pageData) {
		VacationsTableRowData row = pageData.addRow();
		row.setId(vacation.getId());
		row.setFrom(vacation.getFromDate());
		row.setTo(vacation.getToDate());
		row.setApproved(vacation.getApprovedBy() != null);
		row.setApprovedBy(vacation.getApprovedBy() == null ? null : vacation.getApprovedBy().getDisplayName());
		row.setDays(DateUtility.getDaysBetween(vacation.getFromDate(), vacation.getToDate()) + 1);
	}

	private static void importFormData(VacationFormData formData, StoredVacation vacation) {
		formData.getFrom().setValue(vacation.getFromDate());
		formData.getTo().setValue(vacation.getToDate());
		formData.getApproved().setValue(vacation.getApprovedBy() != null);
		formData.getApprovedBy().setValue(vacation.getApprovedBy() == null ? null : vacation.getApprovedBy().getId());
	}

	private static void exportFormData(VacationFormData formData, StoredVacation vacation) {
		vacation.setFromDate(formData.getFrom().getValue());
		vacation.setToDate(formData.getTo().getValue());
		Long approverId = formData.getApprovedBy().getValue();
		vacation.setApprovedBy(approverId == null ? null : JPA.find(StoredUser.class, approverId));
	}
}
