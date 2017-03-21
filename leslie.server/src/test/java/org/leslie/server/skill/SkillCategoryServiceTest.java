package org.leslie.server.skill;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.leslie.server.ServerSession;
import org.leslie.shared.skill.ISkillCategoryService;
import org.leslie.shared.skill.SkillCategoryTablePageData;

@RunWithSubject("mdo")
@RunWith(ServerTestRunner.class)
@RunWithServerSession(ServerSession.class)
public class SkillCategoryServiceTest {

	@Test
	public void testGetSkillCategoryTableData() {
		SkillCategoryTablePageData dataTable = BEANS.get(ISkillCategoryService.class).getSkillCategoryTableData();
		Assert.assertEquals(6, dataTable.getRowCount());
	}
}
