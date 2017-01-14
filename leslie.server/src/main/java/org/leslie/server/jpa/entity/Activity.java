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
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.leslie.server.jpa.mapping.ClassDataMapping;
import org.leslie.server.jpa.mapping.FieldDataMapping;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "activity_type_uid", discriminatorType = DiscriminatorType.INTEGER)
@NamedQuery(name = Activity.QUERY_BY_IDS, query = "SELECT a FROM Activity a WHERE a.id IN :activityIds ")
@ClassDataMapping
public abstract class Activity {

    public static final String QUERY_BY_IDS = "Activity.byIds";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @FieldDataMapping(readOnly = true, pageFieldName = "activityId", formFieldName = "activityId")
    private long id;

    @Column(name = "activity_type_uid")
    private int activityTypeUid;

    @Column(name = "from_date")
    @Temporal(TemporalType.DATE)
    @FieldDataMapping
    private Date from;

    @Column(name = "to_date")
    @Temporal(TemporalType.DATE)
    @FieldDataMapping
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
