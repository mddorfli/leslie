package org.leslie.server.jpa.mapping.impl;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.leslie.server.jpa.entity.VacationActivity;
import org.leslie.server.jpa.mapping.CustomDataMapping;
import org.leslie.shared.vacation.VacationTablePageData.VacationTableRowData;

public class VacationActivityMapping implements
	CustomDataMapping<VacationActivity, AbstractFormData, VacationTableRowData> {

    @Override
    public void read(VacationActivity fromEntity, AbstractFormData toForm) {
    }

    @Override
    public void read(VacationActivity fromEntity, VacationTableRowData toRow) {
	if (fromEntity.getApprovedBy() != null) {
	    toRow.setApprovedById(fromEntity.getApprovedBy().getId());
	    toRow.setApprovedBy(fromEntity.getApprovedBy().getDisplayName());
	}
	toRow.setUserId(fromEntity.getUser().getId());
	toRow.setUser(fromEntity.getUser().getDisplayName());
    }

    @Override
    public void write(AbstractFormData fromForm, VacationActivity toEntity) {
    }

}
