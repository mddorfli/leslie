package leslie.org.leslie.server.admin;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.leslie.server.jpa.EntityManagerService;
import org.leslie.server.jpa.StoredUser;

import leslie.org.leslie.shared.admin.IAdministrationOutlineService;
import leslie.org.leslie.shared.admin.PermissionTablePageData;
import leslie.org.leslie.shared.admin.UserPageData;
import leslie.org.leslie.shared.admin.UserPageData.UserRowData;

@Bean
public class AdministrationOutlineService implements IAdministrationOutlineService {

	@Override
	public PermissionTablePageData getPermissionTableData(Long roleNr) throws ProcessingException {
		return null;
	}

	@Override
	public Object[][] getRoleTableData() throws ProcessingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserPageData getUserTableData() throws ProcessingException {
		EntityManager em = BEANS.get(EntityManagerService.class).getEntityManager();
		TypedQuery<StoredUser> query = em.createQuery("SELECT u FROM " + StoredUser.class.getSimpleName() + " u ",
				StoredUser.class);

		UserPageData pageData = new UserPageData();
		for (StoredUser user : query.getResultList()) {
			UserRowData row = pageData.addRow();
			row.setId(user.getId());
			row.setUsername(user.getUsername());
			row.setFirstName(user.getFirstName());
			row.setLastName(user.getLastName());
			row.setEmail(user.getEmail());
			row.setLoginAttempts(user.getFailedLoginAttempts());
			row.setBlocked(user.isBlocked());
		}
		return pageData;
	}

}
