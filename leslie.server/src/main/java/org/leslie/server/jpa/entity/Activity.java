package org.leslie.server.jpa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.leslie.server.jpa.mapping.MappedClass;
import org.leslie.server.jpa.mapping.MappedField;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "activity_type_uid", discriminatorType = DiscriminatorType.INTEGER)
@NamedQueries({
		@NamedQuery(name = Activity.QUERY_BY_IDS, query = ""
				+ "SELECT a "
				+ "  FROM Activity a "
				+ " WHERE a.id IN :activityIds "),
		@NamedQuery(name = Activity.QUERY_BY_USERID, query = "SELECT a FROM Activity a WHERE a.user.id = :userId"),
		@NamedQuery(name = Activity.QUERY_BY_USERID_TYPE_FROM_TO, query = ""
				+ "SELECT a "
				+ "  FROM Activity a "
				+ " WHERE a.user.id = :userId "
				+ "   AND TYPE(a) = :type "
				+ "   AND (a.from BETWEEN :from AND :to OR "
				+ "        a.to BETWEEN :from AND :to OR "
				+ "        a.from <= :from AND a.to >= :to)"),
})
@MappedClass
public abstract class Activity {

	public static final String QUERY_BY_IDS = "Activity.byIds";
	public static final String QUERY_BY_USERID = "Activity.byUserId";
	public static final String QUERY_BY_USERID_TYPE_FROM_TO = "Activity.byUserIdTypeFromTo";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@MappedField(readOnly = true, pageFieldName = "activityId", formFieldName = "activityId")
	private long id;

	@Column(name = "activity_type_uid")
	private int activityTypeUid;

	@Column(name = "from_date")
	@Temporal(TemporalType.DATE)
	@MappedField
	private Date from;

	@Column(name = "to_date")
	@Temporal(TemporalType.DATE)
	@MappedField
	private Date to;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public long getId() {
		return id;
	}

	void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getActivityTypeUid() {
		return activityTypeUid;
	}

	public void setActivityTypeUid(int activityTypeUid) {
		this.activityTypeUid = activityTypeUid;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

}
