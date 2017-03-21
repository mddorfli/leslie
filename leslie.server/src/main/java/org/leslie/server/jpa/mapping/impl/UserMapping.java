package org.leslie.server.jpa.mapping.impl;

import java.util.stream.Collectors;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.leslie.client.user.UserFormData;
import org.leslie.server.jpa.JPA;
import org.leslie.server.jpa.entity.Role;
import org.leslie.server.jpa.entity.User;
import org.leslie.server.jpa.mapping.CustomDataMapping;

public class UserMapping implements CustomDataMapping<User, UserFormData, AbstractTableRowData> {

    @Override
    public void read(User user, UserFormData toForm) {
	if (user.getRoles() != null) {
	    toForm.getRoles().setValue(user.getRoles().stream()
		    .map(Role::getId)
		    .collect(Collectors.toSet()));
	}
    }

    @Override
    public void read(User fromEntity, AbstractTableRowData toRow) {
	// N/A
    }

    @Override
    public void write(UserFormData formData, User toEntity) {
	if (formData.getRoles().getValue() != null) {
	    toEntity.setRoles(formData.getRoles().getValue().stream()
		    .map(id -> JPA.find(Role.class, id))
		    .collect(Collectors.toList()));
	}
    }
}
