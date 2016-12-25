package org.leslie.server.jpa.mapping;

import java.util.stream.Collectors;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.leslie.client.user.UserFormData;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.Role;
import org.leslie.server.jpa.entity.User;

public class UserRoleMapping implements ICustomMapping {

    @Override
    public void read(Object fromEntity, AbstractFormData toForm) {
	User user = (User) fromEntity;
	UserFormData formData = (UserFormData) toForm;
	if (user.getRoles() != null) {
	    formData.getRoles().setValue(user.getRoles().stream()
		    .map(Role::getId)
		    .collect(Collectors.toSet()));
	}
    }

    @Override
    public void read(Object fromEntity, AbstractTableRowData toRow) {
	// N/A
    }

    @Override
    public void write(AbstractFormData fromFormData, Object toEntity) {
	UserFormData formData = (UserFormData) fromFormData;
	User user = (User) toEntity;
	if (formData.getRoles().getValue() != null) {
	    user.setRoles(formData.getRoles().getValue().stream()
		    .map(id -> JPA.find(Role.class, id))
		    .collect(Collectors.toList()));
	}
    }

    @Override
    public void write(AbstractTableRowData fromRow, Object toEntity) {
	// N/A
    }
}
