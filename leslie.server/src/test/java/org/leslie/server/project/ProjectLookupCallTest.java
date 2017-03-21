package org.leslie.server.project;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.leslie.server.ServerSession;
import org.leslie.shared.project.ProjectLookupCall;

@RunWithSubject("admin")
@RunWith(ServerTestRunner.class)
@RunWithServerSession(ServerSession.class)
public class ProjectLookupCallTest {

	protected ProjectLookupCall createLookupCall() {
		return new ProjectLookupCall();
	}

	@Test
	public void testLookupByAll() {
		ProjectLookupCall call = createLookupCall();
		List<? extends ILookupRow<Long>> data = call.getDataByAll();
		assertArrayEquals(new String[] { "iPOSIC v2.5", "MUTS v4.x", "Project X v0.1" },
				data.stream().map(ILookupRow::getText).collect(Collectors.toList()).toArray());
	}

	@Test
	public void testLookupByKey() {
		ProjectLookupCall call = createLookupCall();
		call.setKey(1L);
		List<? extends ILookupRow<Long>> data = call.getDataByKey();
		assertEquals(1, data.size());
		assertEquals("iPOSIC v2.5", data.get(0).getText());
	}

	@Test
	public void testLookupByText() {
		ProjectLookupCall call = createLookupCall();
		call.setText("MUTS");
		List<? extends ILookupRow<Long>> data = call.getDataByText();
		assertEquals(1, data.size());
		assertEquals("MUTS v4.x", data.get(0).getText());
	}
}
