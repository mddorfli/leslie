package org.leslie.server.jpa;

import java.util.List;

import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;

public abstract class AbstractJpaEntityLookupService<K, E> extends AbstractJpaLookupService<K> {

    protected abstract Class<E> getConfiguredEntityType();

    protected abstract String getConfiguredJpqlSelect();

    protected abstract List<ILookupRow<K>> execGenerateLookupRowData(ILookupCall<K> call, List<E> resultList);

    /**
     * Additional parameter setting etc can be done here.
     * 
     * @param query
     */
    protected void execPrepareQuery(TypedQuery<E> query) {
	// can be overridden
    }

    @Override
    public List<ILookupRow<K>> getRowData(ILookupCall<K> call, CALL_TYPE callType) {
	String queryString = getConfiguredJpqlSelect();
	TypedQuery<E> query = JPA.createQuery(filterSqlByCallType(queryString, callType), getConfiguredEntityType());

	setCallParameters(call, callType, query);
	execPrepareQuery(query);

	query.setMaxResults(call.getMaxRowCount());
	List<E> resultList = query.getResultList();

	List<ILookupRow<K>> rowData = execGenerateLookupRowData(call, resultList);
	return rowData;
    }
}
