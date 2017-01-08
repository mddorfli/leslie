package org.leslie.server.jpa.lookup;

import java.util.List;

import javax.persistence.TypedQuery;

import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.leslie.server.jpa.JPA;

public abstract class AbstractJpaLongLookupService extends AbstractJpaLookupService<Long> {

    /**
     * Must generate {@link LongLookupRow} objects using the NEW keyword. e.g.
     * <code>SELECT NEW org.leslie.server.jpa.LongLookupRow(a.id, a.name)</code>...
     * 
     * @return JP QL select statement.
     */
    protected abstract String getConfiguredJpqlSelect();

    /**
     * Additional parameter setting etc can be done here.
     * 
     * @param query
     */
    protected void execPrepareQuery(TypedQuery<LongLookupRow> query) {
	// can be overridden
    }

    @Override
    protected List<LongLookupRow> execGenerateRowData(ILookupCall<Long> call, CALL_TYPE callType) {
	String queryString = getConfiguredJpqlSelect();
	TypedQuery<LongLookupRow> query = JPA.createQuery(
		filterSqlByCallType(queryString, callType), LongLookupRow.class);

	setCallQueryBinds(query, call, callType);
	execPrepareQuery(query);

	query.setMaxResults(call.getMaxRowCount());
	return query.getResultList();
    }

}
