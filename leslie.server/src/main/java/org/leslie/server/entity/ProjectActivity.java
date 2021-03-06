package org.leslie.server.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.leslie.server.entity.mapping.ProjectActivityMapping;
import org.leslie.server.mapping.MappedClass;

@Entity
@Table(name = "activity_project")
@DiscriminatorValue(ProjectActivity.TYPE_UID)
@PrimaryKeyJoinColumn(name = "activity_id", referencedColumnName = "id")
@MappedClass(ProjectActivityMapping.class)
@NamedQueries({
	@NamedQuery(name = ProjectActivity.QUERY_BY_PROJECTID_FETCH_USER_PROJECT, query = ""
		+ "SELECT pa "
		+ "  FROM ProjectActivity pa "
		+ "  JOIN FETCH pa.user "
		+ "  JOIN FETCH pa.project "
		+ " WHERE pa.project.id = :projectId "),
	@NamedQuery(name = ProjectActivity.QUERY_BY_PROJECTID_FETCH_USER_SORTED, query = ""
		+ "SELECT pa "
		+ "  FROM ProjectActivity pa "
		+ "  JOIN FETCH pa.user "
		+ " WHERE pa.project.id = :projectId "
		+ " ORDER BY pa.from, pa.user.id "),
})
public class ProjectActivity extends Activity {

    protected static final String TYPE_UID = "1";

    public static final String QUERY_BY_PROJECTID_FETCH_USER_PROJECT = "ProjectActivity.byProjectIdFetchUserProject";
    public static final String QUERY_BY_PROJECTID_FETCH_USER_SORTED = "ProjectActivity.byProjectIdFetchUserSorted";

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
