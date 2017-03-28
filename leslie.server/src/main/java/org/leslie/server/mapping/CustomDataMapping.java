package org.leslie.server.mapping;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;

public interface CustomDataMapping<E, F extends AbstractFormData, R extends AbstractTableRowData> {

    void read(E fromEntity, R toRow);

    void read(E fromEntity, F toForm);

    void write(F fromForm, E toEntity);
}
