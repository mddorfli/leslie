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
import org.leslie.server.jpa.mapping.impl.ProjectActivityMapping;

@Entity
@Table(name = "activity_project")
@DiscriminatorValue(ProjectActivity.TYPE_UID)
@PrimaryKeyJoinColumn(name = "activity_id", referencedColumnName = "id")
@ClassDataMapping(ProjectActivityMapping.class)
@NamedQueries({
	@NamedQuery(name = ProjectActivity.QUERY_BY_PROJECT_ID, query = ""
		+ "SELECT pa "
		+ "  FROM ProjectActivity pa "
		+ "  JOIN FETCH pa.user "
		+ "  JOIN FETCH pa.project "
		+ " WHERE pa.project.id = :projectId ")
})
public class ProjectActivity extends Activity {

    protected static final String TYPE_UID = "1";

    public static final String QUERY_BY_PROJECT_ID = "ProjectActivity.byProjectId";

    public ProjectActivity() {
	super.setActivityTypeUid(Integer.parseInt(TYPE_UID));
    }

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    private double percentage;

    public Project getProject() {
	return project;
    }

    public void setProject(Project project) {
	this.project = project;
    }

    public double getPercentage() {
	return percentage;
    }

    public void setPercentage(double percentage) {
	this.percentage = percentage;
    }

}
