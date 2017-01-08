package org.leslie.server.jpa.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "activity_project")
@DiscriminatorValue("1")
@PrimaryKeyJoinColumn(name = "activity_id", referencedColumnName = "id")
public class ProjectActivity extends Activity {

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
