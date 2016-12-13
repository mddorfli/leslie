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
	private User requestedBy;

	@ManyToOne
	@JoinColumn(name = "approved_by_user_id", referencedColumnName = "id")
	private User approvedBy;

	public StoredAppointment getAppointment() {
		return appointment;
	}

	public void setAppointment(StoredAppointment appointment) {
		this.appointment = appointment;
	}

	public User getRequestedBy() {
		return requestedBy;
	}

	public void setRequestedBy(User requestedBy) {
		this.requestedBy = requestedBy;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

}
