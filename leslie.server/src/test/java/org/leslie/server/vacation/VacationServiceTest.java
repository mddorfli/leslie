package org.leslie.server.vacation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.leslie.server.ServerSession;
import org.leslie.shared.vacation.IVacationService;
import org.leslie.shared.vacation.VacationTablePageData;
import org.leslie.shared.vacation.VacationTablePageData.VacationTableRowData;

@RunWithSubject("mdo")
@RunWith(ServerTestRunner.class)
@RunWithServerSession(ServerSession.class)
public class VacationServiceTest {

    @Test
    public void testGetVacationTableData() {
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	VacationTablePageData result = BEANS.get(IVacationService.class).getVacationTableData(null);
	assertEquals(1L, result.getRowCount());
	VacationTableRowData row = result.getRows()[0];
	assertNotNull(row);
	assertEquals("Annual leave", row.getDescription());
	assertEquals("2017-04-01", df.format(row.getFrom()));
	assertEquals("2017-04-07", df.format(row.getTo()));
    }
}
