package org.leslie.server.jpa;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

import org.eclipse.scout.rt.platform.BEANS;

public final class JPA {

    private static EntityManager getEntityManager() {
	return BEANS.get(EntityManagerService.class).getEntityManager();
    }

    /**
     * @param entity
     * @see javax.persistence.EntityManager#persist(java.lang.Object)
     */
    public static void persist(Object entity) {
	getEntityManager().persist(entity);
    }

    /**
     * @param entity
     * @return
     * @see javax.persistence.EntityManager#merge(java.lang.Object)
     */
    public static <T> T merge(T entity) {
	return getEntityManager().merge(entity);
    }

    /**
     * @param entity
     * @see javax.persistence.EntityManager#remove(java.lang.Object)
     */
    public static void remove(Object entity) {
	getEntityManager().remove(entity);
    }

    /**
     * @param entityClass
     * @param primaryKey
     * @return
     * @see javax.persistence.EntityManager#find(java.lang.Class,
     *      java.lang.Object)
     */
    public static <T> T find(Class<T> entityClass, Object primaryKey) {
	return getEntityManager().find(entityClass, primaryKey);
    }

    /**
     * @param entityClass
     * @param primaryKey
     * @param properties
     * @return
     * @see javax.persistence.EntityManager#find(java.lang.Class,
     *      java.lang.Object, java.util.Map)
     */
    public static <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
	return getEntityManager().find(entityClass, primaryKey, properties);
    }

    /**
     * @param entityClass
     * @param primaryKey
     * @param lockMode
     * @return
     * @see javax.persistence.EntityManager#find(java.lang.Class,
     *      java.lang.Object, javax.persistence.LockModeType)
     */
    public static <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
	return getEntityManager().find(entityClass, primaryKey, lockMode);
    }

    /**
     * @param entityClass
     * @param primaryKey
     * @param lockMode
     * @param properties
     * @return
     * @see javax.persistence.EntityManager#find(java.lang.Class,
     *      java.lang.Object, javax.persistence.LockModeType, java.util.Map)
     */
    public static <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode,
	    Map<String, Object> properties) {
	return getEntityManager().find(entityClass, primaryKey, lockMode, properties);
    }

    /**
     * @param entityClass
     * @param primaryKey
     * @return
     * @see javax.persistence.EntityManager#getReference(java.lang.Class,
     *      java.lang.Object)
     */
    public static <T> T getReference(Class<T> entityClass, Object primaryKey) {
	return getEntityManager().getReference(entityClass, primaryKey);
    }

    /**
     * 
     * @see javax.persistence.EntityManager#flush()
     */
    public static void flush() {
	getEntityManager().flush();
    }

    /**
     * @param flushMode
     * @see javax.persistence.EntityManager#setFlushMode(javax.persistence.FlushModeType)
     */
    public static void setFlushMode(FlushModeType flushMode) {
	getEntityManager().setFlushMode(flushMode);
    }

    /**
     * @return
     * @see javax.persistence.EntityManager#getFlushMode()
     */
    public static FlushModeType getFlushMode() {
	return getEntityManager().getFlushMode();
    }

    /**
     * @param entity
     * @param lockMode
     * @see javax.persistence.EntityManager#lock(java.lang.Object,
     *      javax.persistence.LockModeType)
     */
    public static void lock(Object entity, LockModeType lockMode) {
	getEntityManager().lock(entity, lockMode);
    }

    /**
     * @param entity
     * @param lockMode
     * @param properties
     * @see javax.persistence.EntityManager#lock(java.lang.Object,
     *      javax.persistence.LockModeType, java.util.Map)
     */
    public static void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
	getEntityManager().lock(entity, lockMode, properties);
    }

    /**
     * @param entity
     * @see javax.persistence.EntityManager#refresh(java.lang.Object)
     */
    public static void refresh(Object entity) {
	getEntityManager().refresh(entity);
    }

    /**
     * @param entity
     * @param properties
     * @see javax.persistence.EntityManager#refresh(java.lang.Object,
     *      java.util.Map)
     */
    public static void refresh(Object entity, Map<String, Object> properties) {
	getEntityManager().refresh(entity, properties);
    }

    /**
     * @param entity
     * @param lockMode
     * @see javax.persistence.EntityManager#refresh(java.lang.Object,
     *      javax.persistence.LockModeType)
     */
    public static void refresh(Object entity, LockModeType lockMode) {
	getEntityManager().refresh(entity, lockMode);
    }

    /**
     * @param entity
     * @param lockMode
     * @param properties
     * @see javax.persistence.EntityManager#refresh(java.lang.Object,
     *      javax.persistence.LockModeType, java.util.Map)
     */
    public static void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
	getEntityManager().refresh(entity, lockMode, properties);
    }

    /**
     * 
     * @see javax.persistence.EntityManager#clear()
     */
    public static void clear() {
	getEntityManager().clear();
    }

    /**
     * @param entity
     * @see javax.persistence.EntityManager#detach(java.lang.Object)
     */
    public static void detach(Object entity) {
	getEntityManager().detach(entity);
    }

    /**
     * @param entity
     * @return
     * @see javax.persistence.EntityManager#contains(java.lang.Object)
     */
    public static boolean contains(Object entity) {
	return getEntityManager().contains(entity);
    }

    /**
     * @param entity
     * @return
     * @see javax.persistence.EntityManager#getLockMode(java.lang.Object)
     */
    public static LockModeType getLockMode(Object entity) {
	return getEntityManager().getLockMode(entity);
    }

    /**
     * @param propertyName
     * @param value
     * @see javax.persistence.EntityManager#setProperty(java.lang.String,
     *      java.lang.Object)
     */
    public static void setProperty(String propertyName, Object value) {
	getEntityManager().setProperty(propertyName, value);
    }

    /**
     * @return
     * @see javax.persistence.EntityManager#getProperties()
     */
    public static Map<String, Object> getProperties() {
	return getEntityManager().getProperties();
    }

    /**
     * @param qlString
     * @return
     * @see javax.persistence.EntityManager#createQuery(java.lang.String)
     */
    public static Query createQuery(String qlString) {
	return getEntityManager().createQuery(qlString);
    }

    /**
     * @param criteriaQuery
     * @return
     * @see javax.persistence.EntityManager#createQuery(javax.persistence.criteria.CriteriaQuery)
     */
    public static <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
	return getEntityManager().createQuery(criteriaQuery);
    }

    /**
     * @param updateQuery
     * @return
     * @see javax.persistence.EntityManager#createQuery(javax.persistence.criteria.CriteriaUpdate)
     */
    public static Query createQuery(CriteriaUpdate<?> updateQuery) {
	return getEntityManager().createQuery(updateQuery);
    }

    /**
     * @param deleteQuery
     * @return
     * @see javax.persistence.EntityManager#createQuery(javax.persistence.criteria.CriteriaDelete)
     */
    public static Query createQuery(CriteriaDelete<?> deleteQuery) {
	return getEntityManager().createQuery(deleteQuery);
    }

    /**
     * @param qlString
     * @param resultClass
     * @return
     * @see javax.persistence.EntityManager#createQuery(java.lang.String,
     *      java.lang.Class)
     */
    public static <T> TypedQuery<T> createQuery(String qlString, Class<T> resultClass) {
	return getEntityManager().createQuery(qlString, resultClass);
    }

    /**
     * @param name
     * @return
     * @see javax.persistence.EntityManager#createNamedQuery(java.lang.String)
     */
    public static Query createNamedQuery(String name) {
	return getEntityManager().createNamedQuery(name);
    }

    /**
     * @param name
     * @param resultClass
     * @return
     * @see javax.persistence.EntityManager#createNamedQuery(java.lang.String,
     *      java.lang.Class)
     */
    public static <T> TypedQuery<T> createNamedQuery(String name, Class<T> resultClass) {
	return getEntityManager().createNamedQuery(name, resultClass);
    }

    /**
     * @param sqlString
     * @return
     * @see javax.persistence.EntityManager#createNativeQuery(java.lang.String)
     */
    public static Query createNativeQuery(String sqlString) {
	return getEntityManager().createNativeQuery(sqlString);
    }

    /**
     * @param sqlString
     * @param resultClass
     * @return
     * @see javax.persistence.EntityManager#createNativeQuery(java.lang.String,
     *      java.lang.Class)
     */
    public static Query createNativeQuery(String sqlString, Class<?> resultClass) {
	return getEntityManager().createNativeQuery(sqlString, resultClass);
    }

    /**
     * @param sqlString
     * @param resultSetMapping
     * @return
     * @see javax.persistence.EntityManager#createNativeQuery(java.lang.String,
     *      java.lang.String)
     */
    public static Query createNativeQuery(String sqlString, String resultSetMapping) {
	return getEntityManager().createNativeQuery(sqlString, resultSetMapping);
    }

    /**
     * @param name
     * @return
     * @see javax.persistence.EntityManager#createNamedStoredProcedureQuery(java.lang.String)
     */
    public static StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
	return getEntityManager().createNamedStoredProcedureQuery(name);
    }

    /**
     * @param procedureName
     * @return
     * @see javax.persistence.EntityManager#createStoredProcedureQuery(java.lang.String)
     */
    public static StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
	return getEntityManager().createStoredProcedureQuery(procedureName);
    }

    /**
     * @param procedureName
     * @param resultClasses
     * @return
     * @see javax.persistence.EntityManager#createStoredProcedureQuery(java.lang.String,
     *      java.lang.Class[])
     */
    public static StoredProcedureQuery createStoredProcedureQuery(String procedureName, Class<?>... resultClasses) {
	return getEntityManager().createStoredProcedureQuery(procedureName, resultClasses);
    }

    /**
     * @param procedureName
     * @param resultSetMappings
     * @return
     * @see javax.persistence.EntityManager#createStoredProcedureQuery(java.lang.String,
     *      java.lang.String[])
     */
    public static StoredProcedureQuery createStoredProcedureQuery(String procedureName, String... resultSetMappings) {
	return getEntityManager().createStoredProcedureQuery(procedureName, resultSetMappings);
    }

    /**
     * 
     * @see javax.persistence.EntityManager#joinTransaction()
     */
    public static void joinTransaction() {
	getEntityManager().joinTransaction();
    }

    /**
     * @return
     * @see javax.persistence.EntityManager#isJoinedToTransaction()
     */
    public static boolean isJoinedToTransaction() {
	return getEntityManager().isJoinedToTransaction();
    }

    /**
     * @param cls
     * @return
     * @see javax.persistence.EntityManager#unwrap(java.lang.Class)
     */
    public static <T> T unwrap(Class<T> cls) {
	return getEntityManager().unwrap(cls);
    }

    /**
     * @return
     * @see javax.persistence.EntityManager#getDelegate()
     */
    public static Object getDelegate() {
	return getEntityManager().getDelegate();
    }

    /**
     * 
     * @see javax.persistence.EntityManager#close()
     */
    public static void close() {
	getEntityManager().close();
    }

    /**
     * @return
     * @see javax.persistence.EntityManager#isOpen()
     */
    public static boolean isOpen() {
	return getEntityManager().isOpen();
    }

    /**
     * @return
     * @see javax.persistence.EntityManager#getTransaction()
     */
    public static EntityTransaction getTransaction() {
	return getEntityManager().getTransaction();
    }

    /**
     * @return
     * @see javax.persistence.EntityManager#getEntityManagerFactory()
     */
    public static EntityManagerFactory getEntityManagerFactory() {
	return getEntityManager().getEntityManagerFactory();
    }

    /**
     * @return
     * @see javax.persistence.EntityManager#getCriteriaBuilder()
     */
    public static CriteriaBuilder getCriteriaBuilder() {
	return getEntityManager().getCriteriaBuilder();
    }

    /**
     * @return
     * @see javax.persistence.EntityManager#getMetamodel()
     */
    public static Metamodel getMetamodel() {
	return getEntityManager().getMetamodel();
    }

    /**
     * @param rootType
     * @return
     * @see javax.persistence.EntityManager#createEntityGraph(java.lang.Class)
     */
    public static <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
	return getEntityManager().createEntityGraph(rootType);
    }

    /**
     * @param graphName
     * @return
     * @see javax.persistence.EntityManager#createEntityGraph(java.lang.String)
     */
    public static EntityGraph<?> createEntityGraph(String graphName) {
	return getEntityManager().createEntityGraph(graphName);
    }

    /**
     * @param graphName
     * @return
     * @see javax.persistence.EntityManager#getEntityGraph(java.lang.String)
     */
    public static EntityGraph<?> getEntityGraph(String graphName) {
	return getEntityManager().getEntityGraph(graphName);
    }

    /**
     * @param entityClass
     * @return
     * @see javax.persistence.EntityManager#getEntityGraphs(java.lang.Class)
     */
    public static <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
	return getEntityManager().getEntityGraphs(entityClass);
    }

}
