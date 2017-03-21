package org.leslie.server.user;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.leslie.server.ServerSession;
import org.leslie.shared.user.IUserLookupService;
import org.leslie.shared.user.UserLookupCall;

@RunWithSubject("admin")
@RunWith(ServerTestRunner.class)
@RunWithServerSession(ServerSession.class)
public class UserLookupCallTest {

	@Test
	public void testTextLookupCall() {
		UserLookupCall call = new UserLookupCall();
		call.setText("Maroc");
		List<? extends ILookupRow<Long>> results = BEANS.get(IUserLookupService.class).getDataByText(call);
		Assert.assertEquals(1, results.size());
		ILookupRow<Long> row = results.get(0);
		Assert.assertEquals("Maroc Villager", row.getText());
	}

	@Test
	public void testKeyLookupCall() {
		UserLookupCall call = new UserLookupCall();
		call.setKey(Long.valueOf(2L));
		List<? extends ILookupRow<Long>> results = BEANS.get(IUserLookupService.class).getDataByKey(call);
		Assert.assertEquals(1, results.size());
		ILookupRow<Long> row = results.get(0);
		Assert.assertEquals("Maroc Villager", row.getText());
	}

	@Test
	public void testProjectLookupCall() {
		UserLookupCall call = new UserLookupCall();
		call.setProjectId(Long.valueOf(1L));
		List<? extends ILookupRow<Long>> results = BEANS.get(IUserLookupService.class).getDataByAll(call);
		Assert.assertEquals(4, results.size());
		for (ILookupRow<Long> row : results) {
			Assert.assertTrue(row.isEnabled());
		}
	}

	@Test
	public void testDisabledProjectLookupCall() {
		UserLookupCall call = new UserLookupCall();
		call.setDisabledProjectId(Long.valueOf(1L));
		List<? extends ILookupRow<Long>> results = BEANS.get(IUserLookupService.class).getDataByAll(call);
		Assert.assertEquals(5, results.size());
		Set<Long> memberIds = new HashSet<>(Arrays.asList(2l, 3l, 4l, 5l));
		for (ILookupRow<Long> row : results) {
			boolean member = memberIds.contains(row.getKey());
			Assert.assertTrue("User with id " + row.getKey() + " should " + (member ? "not " : "") + "be enabled",
					!member || !row.isEnabled());
		}
	}
}
