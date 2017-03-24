package org.leslie.server.jpa.lookup;

import java.util.List;

import javax.persistence.Query;

import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.server.services.lookup.AbstractLookupService;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

/**
 * Sql SELECT statement for getting {@link LookupRow}s.<br>
 * <p>
 * The expected columns are
 * <ul>
 * <li>Object key
 * <li>String text
 * <li>String iconId
 * <li>String tooltip
 * <li>String background color
 * <li>String foreground color
 * <li>String font
 * <li>Boolean enabled
 * <li>Object parentKey used in hierarchical structures to point to the parents
 * primary key
 * <li>{@link TriState} active (0,1,null,...) see {@link TriState#parse(Object)}
 * </ul>
 * <p>
 * Valid bind names are: Object key, String text, String all, Object rec,
 * {@link TriState} active<br>
 * Valid xml tags are: &lt;key&gt;, &lt;text&gt;, &lt;all&gt;, &lt;rec&gt;
 */
public abstract class AbstractJpaLookupService<K> extends AbstractLookupService<K> {

	public static enum LookupCallType {
		/**
		 * Lookup by id (primary key).
		 */
		KEY,

		/**
		 * Lookup by text.
		 */
		TEXT,

		/**
		 * Select all.
		 */
		ALL,

		/**
		 * Recursive lookup (by parent).
		 */
		REC
	}

	protected abstract List<? extends ILookupRow<K>> execGenerateRowData(ILookupCall<K> call, LookupCallType callType);

	/**
	 * Process xml tags.<br>
	 * Keep content of "key" tag.<br>
	 * Remove text,all,rec tags.
	 */
	protected String filterSqlByKey(String sqlSelect) {
		return StringUtility.removeTagBounds(StringUtility.removeTags(sqlSelect, new String[] { "text", "all", "rec" }),
				"key");
	}

	/**
	 * Process xml tags.<br>
	 * Keep content of "text" tag.<br>
	 * Remove key,all,rec tags.
	 */
	protected String filterSqlByText(String sqlSelect) {
		return StringUtility.removeTagBounds(StringUtility.removeTags(sqlSelect, new String[] { "key", "all", "rec" }),
				"text");
	}

	/**
	 * Process xml tags.<br>
	 * Keep content of "all" tag.<br>
	 * Remove key,text,rec tags.
	 */
	protected String filterSqlByAll(String sqlSelect) {
		return StringUtility.removeTagBounds(StringUtility.removeTags(sqlSelect, new String[] { "key", "text", "rec" }),
				"all");
	}

	/**
	 * Process xml tags.<br>
	 * Keep content of "rec" tag.<br>
	 * Remove key,text,all tags.
	 */
	protected String filterSqlByRec(String sqlSelect) {
		return StringUtility.removeTagBounds(StringUtility.removeTags(sqlSelect, new String[] { "key", "text", "all" }),
				"rec");
	}

	@Override
	public List<? extends ILookupRow<K>> getDataByKey(ILookupCall<K> call) {
		return execGenerateRowData(call, LookupCallType.KEY);
	}

	@Override
	public List<? extends ILookupRow<K>> getDataByText(ILookupCall<K> call) {
		return execGenerateRowData(call, LookupCallType.TEXT);
	}

	@Override
	public List<? extends ILookupRow<K>> getDataByAll(ILookupCall<K> call) {
		return execGenerateRowData(call, LookupCallType.ALL);
	}

	@Override
	public List<? extends ILookupRow<K>> getDataByRec(ILookupCall<K> call) {
		return execGenerateRowData(call, LookupCallType.REC);
	}

	protected String filterSqlByCallType(String sqlSelect, LookupCallType callType) {
		String modifiedQueryString;
		switch (callType) {
		default:
		case ALL:
			modifiedQueryString = filterSqlByAll(sqlSelect);
			break;
		case KEY:
			modifiedQueryString = filterSqlByKey(sqlSelect);
			break;
		case REC:
			modifiedQueryString = filterSqlByRec(sqlSelect);
			break;
		case TEXT:
			modifiedQueryString = filterSqlByText(sqlSelect);
			break;
		}
		return modifiedQueryString;
	}

	protected void setCallQueryBinds(Query query, ILookupCall<K> call, LookupCallType callType) {
		switch (callType) {
		case KEY:
			query.setParameter("key", call.getKey());
			break;
		case TEXT:
			query.setParameter("text", call.getText());
			break;
		case REC:
			query.setParameter("rec", call.getRec());
			break;
		case ALL:
		default:
			break;
		}
	}
}
