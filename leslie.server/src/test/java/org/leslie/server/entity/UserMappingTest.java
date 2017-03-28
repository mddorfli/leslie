package org.leslie.server.entity;

import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.leslie.client.user.UserFormData;
import org.leslie.client.user.UserPageData;
import org.leslie.client.user.UserPageData.UserRowData;
import org.leslie.server.ServerSession;
import org.leslie.server.entity.User;
import org.leslie.server.mapping.MappingUtility;

@RunWithSubject("admin")
@RunWith(ServerTestRunner.class)
@RunWithServerSession(ServerSession.class)
public class UserMappingTest {

    @Test
    public void testExportFormData() {
	UserFormData formData = new UserFormData();
	formData.getUsername().setValue("username");
	formData.getFirstName().setValue("firstName");
	formData.getLastName().setValue("lastName");
	formData.getEmail().setValue("email");
	formData.getBlocked().setValue(Boolean.TRUE);

	User user = new User();
	user.setId(25);
	MappingUtility.exportFormData(formData, user);

	// because the id field is read only, it should not be overwritten
	assertEquals(25L, user.getId());
	assertEquals("username", user.getUsername());
	assertEquals("firstName", user.getFirstName());
	assertEquals("lastName", user.getLastName());
	assertEquals("email", user.getEmail());
	assertEquals(true, user.isBlocked());
    }

    @Test
    public void testImportFormData() {
	User user = new User();
	user.setId(5);
	user.setUsername("username");
	user.setBlocked(true);
	user.setEmail("email");
	user.setFirstName("firstName");
	user.setLastName("lastName");

	UserFormData formData = new UserFormData();
	MappingUtility.importFormData(user, formData);

	assertEquals(Long.valueOf(5L), formData.getUserId());
	assertEquals("username", formData.getUsername().getValue());
	assertEquals(Boolean.TRUE, formData.getBlocked().getValue());
	assertEquals("email", formData.getEmail().getValue());
	assertEquals("firstName", formData.getFirstName().getValue());
	assertEquals("lastName", formData.getLastName().getValue());
    }

    @Test
    public void testImportPageData() {
	User user = new User();
	user.setId(5);
	user.setUsername("username");
	user.setBlocked(true);
	user.setEmail("email");
	user.setFirstName("firstName");
	user.setLastName("lastName");

	UserPageData pageData = new UserPageData();
	MappingUtility.importTablePageData(Collections.singletonList(user), pageData);

	assertEquals(1, pageData.getRowCount());
	UserRowData row = pageData.getRows()[0];

	assertEquals(Long.valueOf(5L), row.getId());
	assertEquals("username", row.getUsername());
	assertEquals("firstName", row.getFirstName());
	assertEquals("lastName", row.getLastName());
	assertEquals("email", row.getEmail());
	assertEquals(Boolean.TRUE, row.getBlocked());
    }

}
