package org.leslie.server.jpa.entity;

import static org.junit.Assert.assertTrue;

import java.util.stream.Collectors;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.leslie.server.ServerSession;
import org.leslie.server.jpa.JPA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWithSubject("admin")
@RunWith(ServerTestRunner.class)
@RunWithServerSession(ServerSession.class)
public class PersistenceTest {

    private static final Logger LOG = LoggerFactory.getLogger(PersistenceTest.class);

    @Test
    public void testUpdateUserProjectRelation() {
	Project project = JPA.createQuery("SELECT p FROM Project p WHERE p.id = 2", Project.class).getSingleResult();
	LOG.info("Before removal: Project (via select query) {} has users {}.", project.getName(),
		project.getUserAssignments().stream()
			.map(ProjectAssignment::getUser)
			.map(User::getUsername)
			.collect(Collectors.toList()));

	assertTrue("User mdo does not exist",
		project.getUserAssignments().stream()
			.map(ProjectAssignment::getUser)
			.map(User::getUsername)
			.anyMatch(username -> "mdo".equals(username)));

	User user = JPA.find(User.class, 2L);
	project = JPA.find(Project.class, 2L);

	LOG.info("Removing user {} from project {}", user.getUsername(), project.getName());
	project.getUserAssignments().remove(user);

	LOG.info("Project {} now has users {}.", project.getName(),
		project.getUserAssignments().stream()
			.map(ProjectAssignment::getUser)
			.map(User::getUsername)
			.collect(Collectors.toList()));

	project = JPA.createQuery("SELECT p FROM Project p WHERE p.id = 2", Project.class).getSingleResult();
	LOG.info("After removal: Project (via select query) {} has users {}.", project.getName(),
		project.getUserAssignments().stream()
			.map(ProjectAssignment::getUser)
			.map(User::getUsername)
			.collect(Collectors.toList()));

	assertTrue("User mdo was not removed",
		project.getUserAssignments().stream()
			.map(ProjectAssignment::getUser)
			.map(User::getUsername)
			.noneMatch(username -> "mdo".equals(username)));

	JPA.getTransaction().setRollbackOnly();
    }
}
