package org.leslie.server.skill;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.leslie.server.ServerSession;
import org.leslie.shared.skill.SkillCategoryLookupCall;

@RunWithSubject("admin")
@RunWith(ServerTestRunner.class)
@RunWithServerSession(ServerSession.class)
public class SkillCategoryLookupCallTest {

	protected SkillCategoryLookupCall createLookupCall() {
		return new SkillCategoryLookupCall();
	}

	@Test
	public void testLookupByAll() {
		SkillCategoryLookupCall call = createLookupCall();
		List<? extends ILookupRow<Long>> data = call.getDataByAll();
		assertEquals(6, data.size());
	}

	@Test
	public void testLookupByKey() {
		SkillCategoryLookupCall call = createLookupCall();
		call.setKey(Long.valueOf(1L));
		List<? extends ILookupRow<Long>> data = call.getDataByKey();
		assertEquals(1, data.size());
		assertEquals("Generic skills", data.get(0).getText());
	}

	@Test
	public void testLookupByText() {
		SkillCategoryLookupCall call = createLookupCall();
		call.setText("D");
		List<? extends ILookupRow<Long>> data = call.getDataByText();
		assertEquals(2, data.size());
		List<String> expected = Arrays.asList("Database", "Domain knowledge");
		for (ILookupRow<Long> row : data) {
			assertTrue(expected.contains(row.getText()));
			expected.remove(row.getText());
		}
		assertTrue(expected.isEmpty());
	}
}
