package org.leslie.server.skill;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.leslie.server.ServerSession;
import org.leslie.shared.skill.ISkillService;
import org.leslie.shared.skill.SkillTablePageData;

@RunWithSubject("mdo")
@RunWith(ServerTestRunner.class)
@RunWithServerSession(ServerSession.class)
public class SkillServiceTest {

	@Test
	public void testGetSkillTableData() {
		SkillTablePageData tableData = BEANS.get(ISkillService.class).getSkillTableData();
		Assert.assertEquals(71, tableData.getRowCount());
	}

	@Test
	public void testGetPersonalSkillTableData() {
		SkillTablePageData tableData = BEANS.get(ISkillService.class).getPersonalSkillTableData();
		Assert.assertEquals(71, tableData.getRowCount());
	}

}
