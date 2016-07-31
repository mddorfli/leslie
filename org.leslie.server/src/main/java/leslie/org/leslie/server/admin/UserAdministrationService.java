package leslie.org.leslie.server.admin;

import java.util.stream.Collectors;

import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.security.SecurityUtility;
import org.eclipse.scout.rt.platform.util.Base64Utility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;
import org.leslie.server.jpa.StoredRole;
import org.leslie.server.jpa.StoredUser;

import leslie.org.leslie.server.JPA;
import leslie.org.leslie.shared.admin.IUserAdministrationService;
import leslie.org.leslie.shared.admin.UserAdministrationFormData;
import leslie.org.leslie.shared.security.ReadAdministrationPermission;
import leslie.org.leslie.shared.security.UpdateAdministrationPermission;

@Bean
public class UserAdministrationService implements IUserAdministrationService {

	@Override
	public UserAdministrationFormData create(UserAdministrationFormData formData) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredUser user = new StoredUser();
		exportFormData(formData, user);
		applyPassword(user, formData.getPassword().getValue());

		JPA.persist(user);
		return formData;
	}

	@Override
	public UserAdministrationFormData load(UserAdministrationFormData formData) throws ProcessingException {
		if (!ACCESS.check(new ReadAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredUser user = JPA.find(StoredUser.class, formData.getUserNr());
		importUser(user, formData);
		return formData;
	}

	@Override
	public UserAdministrationFormData store(UserAdministrationFormData formData) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredUser user = JPA.find(StoredUser.class, formData.getUserNr());
		exportFormData(formData, user);
		if (!StringUtility.isNullOrEmpty(formData.getPassword().getValue())) {
			applyPassword(user, formData.getPassword().getValue());
		}
		JPA.merge(user);
		return formData;
	}

	@Override
	public void delete(Long selectedValue) throws ProcessingException {
		if (!ACCESS.check(new UpdateAdministrationPermission())) {
			throw new VetoException(TEXTS.get("AuthorizationFailed"));
		}
		StoredUser user = JPA.find(StoredUser.class, selectedValue);
		JPA.remove(user);
	}

	private static void exportFormData(UserAdministrationFormData formData, StoredUser user) {
		user.setUsername(formData.getUsername().getValue());
		user.setFirstName(formData.getFirstName().getValue());
		user.setLastName(formData.getLastName().getValue());
		user.setEmail(formData.getEmail().getValue());
		user.setBlocked(formData.getBlocked().getValue());

		user.setRoles(formData.getRoles().getValue().stream()
				.map(id -> JPA.find(StoredRole.class, id))
				.collect(Collectors.toList()));
	}

	private static void importUser(StoredUser user, UserAdministrationFormData formData) {
		formData.getUsername().setValue(user.getUsername());
		formData.getFirstName().setValue(user.getFirstName());
		formData.getLastName().setValue(user.getLastName());
		formData.getEmail().setValue(user.getEmail());
		formData.getBlocked().setValue(user.isBlocked());

		formData.getRoles().setValue(user.getRoles().stream()
				.map(StoredRole::getId)
				.collect(Collectors.toSet()));
	}

	private static void applyPassword(StoredUser user, String password) {
		byte[] salt = SecurityUtility.createRandomBytes();
		byte[] hash = SecurityUtility.hash(Base64Utility.decode(password), salt);
		user.setPasswordSalt(Base64Utility.encode(salt));
		user.setPasswordHash(Base64Utility.encode(hash));
	}
}
