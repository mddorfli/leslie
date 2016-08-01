package org.leslie.server.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.common.code.ICode;

import leslie.org.leslie.shared.appointment.AppointmentCodeType;

@Entity
@Table(name = "appointment")
public class StoredAppointment {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "appointment_type_uid")
	private long appointmentTypeUid;

	@Column(name = "from_date")
	@Temporal(TemporalType.DATE)
	private Date fromDate;

	@Column(name = "to_date")
	@Temporal(TemporalType.DATE)
	private Date toDate;

	@Column(name = "description")
	private String description;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAppointmentTypeUid() {
		return appointmentTypeUid;
	}

	public void setAppointmentTypeUid(long appointmentTypeUid) {
		this.appointmentTypeUid = appointmentTypeUid;
	}

	public ICode<Long> getAppointmentCode() {
		return BEANS.get(AppointmentCodeType.class).getCode(appointmentTypeUid);
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
