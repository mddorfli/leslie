package leslie.org.leslie.server;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.platform.BEANS;
import org.leslie.server.jpa.EntityManagerService;

public final class JPA {

	private static EntityManager getEntityManager() {
		return BEANS.get(EntityManagerService.class).getEntityManager();
	}

	public static <T> TypedQuery<T> createQuery(String sqlString, Class<T> resultClass) {
		return getEntityManager().createQuery(sqlString, resultClass);
	}

	public static <T> T merge(T entity) {
		return getEntityManager().merge(entity);
	}

	public static void persist(Object entity) {
		getEntityManager().persist(entity);
	}

	public static <T> T find(Class<T> entityClass, long primaryKey) {
		return getEntityManager().find(entityClass, primaryKey);
	}

	public static void remove(Object entity) {
		getEntityManager().remove(entity);
	}
}
