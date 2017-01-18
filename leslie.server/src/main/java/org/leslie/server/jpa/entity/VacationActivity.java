package org.leslie.server.jpa.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.leslie.server.jpa.mapping.ClassDataMapping;
import org.leslie.server.jpa.mapping.FieldDataMapping;
import org.leslie.server.jpa.mapping.impl.VacationActivityMapping;

@Entity
@Table(name = "activity_vacation")
@DiscriminatorValue(VacationActivity.TYPE_UID)
@PrimaryKeyJoinColumn(name = "activity_id", referencedColumnName = "id")
@NamedQueries({
	@NamedQuery(name = VacationActivity.QUERY_ALL, query = ""
		+ "SELECT va "
		+ "  FROM VacationActivity va "),
	@NamedQuery(name = VacationActivity.QUERY_BY_USER, query = ""
		+ "SELECT va "
		+ "  FROM VacationActivity va "
		+ " WHERE va.user = :user "),
})
@ClassDataMapping(VacationActivityMapping.class)
public class VacationActivity extends Activity {

    public static final String TYPE_UID = "2";

    public static final String QUERY_ALL = "VacationActivity.all";
    public static final String QUERY_BY_USER = "VacationActivity.byUser";

    @ManyToOne
    @JoinColumn(name = "approved_by_user_id")
    private User approvedBy;

    @FieldDataMapping
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
