package org.leslie.server.jpa.mapping;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;

public class NoMapping implements CustomDataMapping<Object, AbstractFormData, AbstractTableRowData> {

    @Override
    public void read(Object fromEntity, AbstractFormData toForm) {
	// does nothing
    }

    @Override
    public void read(Object fromEntity, AbstractTableRowData toRow) {
	// does nothing
    }

    @Override
    public void write(AbstractFormData fromForm, Object toEntity) {
	// does nothing
    }

}
