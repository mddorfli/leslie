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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "activity_type_uid", discriminatorType = DiscriminatorType.INTEGER)
public abstract class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "activity_type_uid")
    private int activityTypeUid;

    @Column(name = "from_date")
    @Temporal(TemporalType.DATE)
    private Date from;

    @Column(name = "to_date")
    @Temporal(TemporalType.DATE)
    private Date to;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public int getId() {
	return id;
    }

    public void setId(int id) {
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
