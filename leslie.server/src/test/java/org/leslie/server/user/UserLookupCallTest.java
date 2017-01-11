package org.leslie.server.user;

import java.util.List;

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
	call.setText("Marco");
	List<? extends ILookupRow<Long>> results = BEANS.get(IUserLookupService.class).getDataByText(call);
	Assert.assertEquals(1, results.size());
	ILookupRow<Long> row = results.get(0);
	Assert.assertEquals("Marco Dörfliger", row.getText());
    }

    @Test
    public void testKeyLookupCall() {
	UserLookupCall call = new UserLookupCall();
	call.setKey(Long.valueOf(1L));
	List<? extends ILookupRow<Long>> results = BEANS.get(IUserLookupService.class).getDataByKey(call);
	Assert.assertEquals(2, results.size());
	ILookupRow<Long> row = results.get(0);
	Assert.assertEquals("Marco Dörfliger", row.getText());
    }

}
