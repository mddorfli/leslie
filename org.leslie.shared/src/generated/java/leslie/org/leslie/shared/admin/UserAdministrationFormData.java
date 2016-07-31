package leslie.org.leslie.shared.admin;

import java.util.Set;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications
 * recommended.
 */
@Generated(value = "leslie.org.leslie.client.admin.UserAdministrationForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class UserAdministrationFormData extends AbstractFormData {

	private static final long serialVersionUID = 1L;

	public Blocked getBlocked() {
		return getFieldByClass(Blocked.class);
	}
	public Email getEmail() {
		return getFieldByClass(Email.class);
	}
	public FirstName getFirstName() {
		return getFieldByClass(FirstName.class);
	}
	public LastName getLastName() {
		return getFieldByClass(LastName.class);
	}
	public Password getPassword() {
		return getFieldByClass(Password.class);
	}
	public Roles getRoles() {
		return getFieldByClass(Roles.class);
	}
	/**
	 * access method for property UserNr.
	 */
	public Long getUserNr() {
		return getUserNrProperty().getValue();
	}
	/**
	 * access method for property UserNr.
	 */
	public void setUserNr(Long userNr) {
		getUserNrProperty().setValue(userNr);
	}
	public UserNrProperty getUserNrProperty() {
		return getPropertyByClass(UserNrProperty.class);
	}
	public Username getUsername() {
		return getFieldByClass(Username.class);
	}

	public static class Blocked extends AbstractValueFieldData<Boolean> {

		private static final long serialVersionUID = 1L;
	}
	public static class Email extends AbstractValueFieldData<String> {

		private static final long serialVersionUID = 1L;
	}
	public static class FirstName extends AbstractValueFieldData<String> {

		private static final long serialVersionUID = 1L;
	}
	public static class LastName extends AbstractValueFieldData<String> {

		private static final long serialVersionUID = 1L;
	}
	public static class Password extends AbstractValueFieldData<String> {

		private static final long serialVersionUID = 1L;
	}
	public static class Roles extends AbstractValueFieldData<Set<Long>> {

		private static final long serialVersionUID = 1L;
	}
	public static class UserNrProperty extends AbstractPropertyData<Long> {

		private static final long serialVersionUID = 1L;
	}
	public static class Username extends AbstractValueFieldData<String> {

		private static final long serialVersionUID = 1L;
	}
}
