package org.leslie.server.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "appointment_ext_vacation")
public class StoredVacationAppointment {

	@Id
	@OneToOne
	@JoinColumn(name = "appointment_id")
	private StoredAppointment appointment;

	@ManyToOne
	@JoinColumn(name = "requested_by_user_id", referencedColumnName = "id")
	private StoredUser requestedBy;

	@ManyToOne
	@JoinColumn(name = "approved_by_user_id", referencedColumnName = "id")
	private StoredUser approvedBy;

	public StoredAppointment getAppointment() {
		return appointment;
	}

	public void setAppointment(StoredAppointment appointment) {
		this.appointment = appointment;
	}

	public StoredUser getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(StoredUser requestedBy) {
		this.requestedBy = requestedBy;
	}

	public StoredUser getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(StoredUser approvedBy) {
		this.approvedBy = approvedBy;
	}

}
