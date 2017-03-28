package org.leslie.server.entity.mapping;

import org.leslie.server.entity.User;
import org.leslie.server.entity.VacationActivity;
import org.leslie.server.jpa.JPA;
import org.leslie.server.mapping.CustomDataMapping;
import org.leslie.shared.vacation.VacationFormData;
import org.leslie.shared.vacation.VacationTablePageData.VacationTableRowData;

public class VacationActivityMapping implements
	CustomDataMapping<VacationActivity, VacationFormData, VacationTableRowData> {

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
    public void read(VacationActivity fromEntity, VacationFormData toForm) {
	if (fromEntity.getApprovedBy() != null) {
	    toForm.getApprovedBy().setValue(fromEntity.getApprovedBy().getId());
	}
	toForm.getRequestedBy().setValue(fromEntity.getUser().getId());
    }

    @Override
    public void write(VacationFormData fromForm, VacationActivity toEntity) {
	toEntity.setUser(JPA.find(User.class, fromForm.getRequestedBy().getValue()));
	if (fromForm.getApprovedBy().getValue() != null) {
	    toEntity.setApprovedBy(JPA.find(User.class, fromForm.getApprovedBy().getValue()));
	}
    }

}
