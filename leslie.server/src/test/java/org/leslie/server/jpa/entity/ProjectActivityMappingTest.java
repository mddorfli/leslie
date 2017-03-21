package org.leslie.server.jpa.entity;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.leslie.server.ServerSession;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.mapping.MappingUtility;
import org.leslie.shared.activity.ProjectResourcesTablePageData.ProjectResourcesTableRowData;

@RunWithSubject("admin")
@RunWith(ServerTestRunner.class)
@RunWithServerSession(ServerSession.class)
public class ProjectActivityMappingTest {

    @Test
    public void testImportPageData() {
	java.util.Date fromDate = Date
		.from(LocalDate.of(2017, 01, 01).atStartOfDay(ZoneId.systemDefault()).toInstant());
	java.util.Date toDate = Date.from(LocalDate.of(2017, 01, 31).atStartOfDay(ZoneId.systemDefault()).toInstant());

	ProjectActivity pa = new ProjectActivity();
	pa.setId(1);
	pa.setPercentage(80.0);
	pa.setFrom(fromDate);
	pa.setTo(toDate);
	pa.setProject(JPA.find(Project.class, 1L));
	pa.setUser(JPA.find(User.class, 2L));

	ProjectResourcesTableRowData rowData = new ProjectResourcesTableRowData();
	MappingUtility.importTableRowData(pa, rowData);

	assertEquals(Long.valueOf(2L), rowData.getUserId());
	assertEquals("Maroc Villager", rowData.getUser());
	assertEquals(BigDecimal.valueOf(80.0), rowData.getPercentage());
	assertEquals(fromDate, rowData.getFrom());
	assertEquals(toDate, rowData.getTo());
    }

}
