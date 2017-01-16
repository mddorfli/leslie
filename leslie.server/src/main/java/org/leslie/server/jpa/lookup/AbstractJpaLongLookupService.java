package org.leslie.server.jpa.lookup;

import java.util.List;

import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.leslie.server.jpa.JPA;

/**
 * @author Marco Dörfliger
 */
public abstract class AbstractJpaLongLookupService extends AbstractJpaLookupService<Long> {

    /**
     * Must generate {@link LongLookupRow} objects using the NEW keyword.
     * e.g.<br>
     * <code>SELECT NEW org.leslie.server.jpa.LongLookupRow(a.id, a.name)</code>...
     * 
     * @param call
     *            TODO
     * 
     * @return JP QL select statement.
     */
    protected abstract String getConfiguredJpqlSelect(ILookupCall<Long> call);

    /**
     * Additional parameter setting etc can be done here.
     * 
     * @param query
     * @param call
     *            TODO
     */
    protected void execPrepareQuery(TypedQuery<LongLookupRow> query, ILookupCall<Long> call) {
	// can be overridden
    }

    @Override
    protected final List<LongLookupRow> execGenerateRowData(ILookupCall<Long> call, CALL_TYPE callType) {
	String queryString = getConfiguredJpqlSelect(call);
	TypedQuery<LongLookupRow> query = JPA.createQuery(
		filterSqlByCallType(queryString, callType), LongLookupRow.class);

	setCallQueryBinds(query, call, callType);
	execPrepareQuery(query, call);

	query.setMaxResults(call.getMaxRowCount());
	return query.getResultList();
    }

}
