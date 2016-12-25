package org.leslie.server.jpa.mapping;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;

public class NoMapping implements ICustomMapping {

    @Override
    public void write(AbstractFormData fromFormData, Object toEntity) {
	// does nothing
    }

    @Override
    public void write(AbstractTableRowData fromRow, Object toEntity) {
	// does nothing
    }

    @Override
    public void read(Object fromEntity, AbstractFormData toForm) {
	// does nothing
    }

    @Override
    public void read(Object fromEntity, AbstractTableRowData toRow) {
	// does nothing
    }
}
