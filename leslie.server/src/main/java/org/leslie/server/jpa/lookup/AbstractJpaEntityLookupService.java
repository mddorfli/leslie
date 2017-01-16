package org.leslie.server.jpa.lookup;

import java.util.List;

import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.leslie.server.jpa.JPA;

public abstract class AbstractJpaEntityLookupService<K, E> extends AbstractJpaLookupService<K> {

    protected abstract Class<E> getConfiguredEntityType();

    protected abstract String getConfiguredJpqlSelect(ILookupCall<K> call);

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
    public List<ILookupRow<K>> execGenerateRowData(ILookupCall<K> call, CALL_TYPE callType) {
	String queryString = getConfiguredJpqlSelect(call);
	TypedQuery<E> query = JPA.createQuery(filterSqlByCallType(queryString, callType), getConfiguredEntityType());

	setCallQueryBinds(query, call, callType);
	execPrepareQuery(query);

	query.setMaxResults(call.getMaxRowCount());
	List<E> resultList = query.getResultList();

	List<ILookupRow<K>> rowData = execGenerateLookupRowData(call, resultList);
	return rowData;
    }
}
