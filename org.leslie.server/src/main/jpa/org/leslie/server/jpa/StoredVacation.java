package org.leslie.server.jpa;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "vacation")
public class StoredVacation {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "requested_by_user_id", referencedColumnName = "id")
	private StoredUser requestedBy;

	@Column(name = "from_date")
	@Temporal(TemporalType.DATE)
	private Date fromDate;

	@Column(name = "to_date")
	@Temporal(TemporalType.DATE)
	private Date toDate;

	@ManyToOne
	@JoinColumn(name = "approved_by_user_id", referencedColumnName = "id")
	private StoredUser approvedBy;

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the requestedBy
	 */
	public StoredUser getRequestedBy() {
		return requestedBy;
	}

	/**
	 * @param requestedBy
	 *            the requestedBy to set
	 */
	public void setRequestedBy(StoredUser requestedBy) {
		this.requestedBy = requestedBy;
	}

	/**
	 * @return the fromDate
	 */
	public Date getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate
	 *            the fromDate to set
	 */
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public Date getToDate() {
		return toDate;
	}

	/**
	 * @param toDate
	 *            the toDate to set
	 */
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	/**
	 * @return the approvedBy
	 */
	public StoredUser getApprovedBy() {
		return approvedBy;
	}

	/**
	 * @param approvedBy
	 *            the approvedBy to set
	 */
	public void setApprovedBy(StoredUser approvedBy) {
		this.approvedBy = approvedBy;
	}

}
