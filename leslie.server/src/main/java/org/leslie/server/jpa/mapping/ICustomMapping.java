package org.leslie.server.jpa.mapping;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;

public interface ICustomMapping {

    void read(Object fromEntity, AbstractFormData toForm);

    void read(Object fromEntity, AbstractTableRowData toRow);

    void write(AbstractFormData fromForm, Object toEntity);

    void write(AbstractTableRowData fromRow, Object toEntity);

}
