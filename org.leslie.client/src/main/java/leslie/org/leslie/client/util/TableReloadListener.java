/**
 *
 */
package leslie.org.leslie.client.util;

import org.eclipse.scout.rt.client.ui.DataChangeListener;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

/**
 * @author Kiwi
 */
public class TableReloadListener implements DataChangeListener {

	private AbstractPageWithTable<?> m_table;

	public TableReloadListener(AbstractPageWithTable<?> table) {
		m_table = table;
	}

	@Override
	public void dataChanged(Object... dataTypes) throws ProcessingException {
		m_table.reloadPage();
	}

}
