package org.leslie.client.role;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.leslie.client.role.RoleForm.MainBox.CancelButton;
import org.leslie.client.role.RoleForm.MainBox.GroupBox;
import org.leslie.client.role.RoleForm.MainBox.GroupBox.NameField;
import org.leslie.client.role.RoleForm.MainBox.OkButton;
import org.leslie.shared.role.IRoleService;
import org.leslie.shared.security.permission.UpdateAdministrationPermission;

@FormData(value = RoleFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class RoleForm extends AbstractForm {

    private Long roleNr;

    public RoleForm() throws ProcessingException {
	super();
    }

    public CancelButton getCancelButton() {
	return getFieldByClass(CancelButton.class);
    }

    @Override
    protected String getConfiguredTitle() {
	return TEXTS.get("Role");
    }

    public GroupBox getGroupBox() {
	return getFieldByClass(GroupBox.class);
    }

    public MainBox getMainBox() {
	return getFieldByClass(MainBox.class);
    }

    public NameField getNameField() {
	return getFieldByClass(NameField.class);
    }

    public OkButton getOkButton() {
	return getFieldByClass(OkButton.class);
    }

    @FormData
    public Long getRoleNr() {
	return roleNr;
    }

    @FormData
    public void setRoleNr(Long roleNr) {
	this.roleNr = roleNr;
    }

    public void startModify() throws ProcessingException {
	startInternal(new ModifyHandler());
    }

    public void startNew() throws ProcessingException {
	startInternal(new NewHandler());
    }

    @Order(10.0)
    public class MainBox extends AbstractGroupBox {

	@Override
	protected int getConfiguredGridColumnCount() {
	    return 1;
	}

	@Order(30.0)
	public class CancelButton extends AbstractCancelButton {
	}

	@Order(10.0)
	public class GroupBox extends AbstractGroupBox {

	    @Order(10.0)
	    public class NameField extends AbstractStringField {

		@Override
		protected String getConfiguredLabel() {
		    return TEXTS.get("Name");
		}
	    }
	}

	@Order(20.0)
	public class OkButton extends AbstractOkButton {
	}
    }

    public class ModifyHandler extends AbstractFormHandler {

	@Override
	public void execLoad() throws ProcessingException {
	    RoleFormData formData = new RoleFormData();
	    importFormData(formData);
	    setEnabledPermission(new UpdateAdministrationPermission());
	}

	@Override
	public void execStore() throws ProcessingException {
	    IRoleService service = BEANS.get(IRoleService.class);
	    RoleFormData formData = new RoleFormData();
	    exportFormData(formData);
	    formData = service.store(formData);
	}
    }

    public class NewHandler extends AbstractFormHandler {

	@Override
	public void execStore() throws ProcessingException {
	    IRoleService service = BEANS.get(IRoleService.class);
	    RoleFormData formData = new RoleFormData();
	    exportFormData(formData);
	    formData = service.create(formData);
	}
    }
}
