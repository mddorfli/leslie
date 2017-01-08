package org.leslie.server.jpa.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "activity_vacation")
@DiscriminatorValue("2")
@PrimaryKeyJoinColumn(name = "activity_id", referencedColumnName = "id")
public class VacationActivity extends Activity {

    @ManyToOne
    @JoinColumn(name = "approved_by_user_id")
    private User approvedBy;

    private String description;

    public User getApprovedBy() {
	return approvedBy;
    }

    public void setApprovedBy(User approvedBy) {
	this.approvedBy = approvedBy;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

}
