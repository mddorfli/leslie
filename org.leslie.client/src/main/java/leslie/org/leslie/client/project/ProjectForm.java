package leslie.org.leslie.client.project;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.TEXTS;

import leslie.org.leslie.client.project.ProjectForm.MainBox.CancelButton;
import leslie.org.leslie.client.project.ProjectForm.MainBox.GroupBox;
import leslie.org.leslie.client.project.ProjectForm.MainBox.GroupBox.NameField;
import leslie.org.leslie.client.project.ProjectForm.MainBox.GroupBox.VersionField;
import leslie.org.leslie.client.project.ProjectForm.MainBox.OkButton;
import leslie.org.leslie.shared.project.IProjectService;
import leslie.org.leslie.shared.project.ProjectFormData;
import leslie.org.leslie.shared.security.permission.CreateProjectPermission;
import leslie.org.leslie.shared.security.permission.UpdateProjectPermission;

@FormData(value = ProjectFormData.class, sdkCommand = FormData.SdkCommand.CREATE)
public class ProjectForm extends AbstractForm {

    private long id;

    @FormData
    public long getId() {
	return id;
    }

    @FormData
    public void setId(long id) {
	this.id = id;
    }

    @Override
    protected String getConfiguredTitle() {
	return TEXTS.get("Project");
    }

    public void startModify() {
	startInternalExclusive(new ModifyHandler());
    }

    public void startNew() {
	startInternal(new NewHandler());
    }

    public CancelButton getCancelButton() {
	return getFieldByClass(CancelButton.class);
    }

    public MainBox getMainBox() {
	return getFieldByClass(MainBox.class);
    }

    public VersionField getVersionField() {
	return getFieldByClass(VersionField.class);
    }

    public GroupBox getGroupBox() {
	return getFieldByClass(GroupBox.class);
    }

    public NameField getNameField() {
	return getFieldByClass(NameField.class);
    }

    public OkButton getOkButton() {
	return getFieldByClass(OkButton.class);
    }

    @Order(1000)
    public class MainBox extends AbstractGroupBox {

	@Override
	protected int getConfiguredGridColumnCount() {
	    return 1;
	}

	@Order(0)
	public class GroupBox extends AbstractGroupBox {

	    @Order(1000)
	    public class NameField extends AbstractStringField {

		@Override
		protected String getConfiguredLabel() {
		    return TEXTS.get("Name");
		}

		@Override
		protected int getConfiguredMaxLength() {
		    return 80;
		}
	    }

	    @Order(2000)
	    public class VersionField extends AbstractStringField {

		@Override
		protected String getConfiguredLabel() {
		    return TEXTS.get("Version");
		}

		@Override
		protected int getConfiguredMaxLength() {
		    return 80;
		}
	    }
	}

	@Order(100000)
	public class OkButton extends AbstractOkButton {
	}

	@Order(101000)
	public class CancelButton extends AbstractCancelButton {
	}
    }

    public class ModifyHandler extends AbstractFormHandler {

	@Override
	protected void execLoad() {
	    IProjectService service = BEANS.get(IProjectService.class);
	    ProjectFormData formData = new ProjectFormData();
	    exportFormData(formData);
	    formData = service.load(formData);
	    importFormData(formData);

	    setEnabledPermission(new UpdateProjectPermission(getId()));
	}

	@Override
	protected void execStore() {
	    IProjectService service = BEANS.get(IProjectService.class);
	    ProjectFormData formData = new ProjectFormData();
	    exportFormData(formData);
	    service.store(formData);
	}
    }

    public class NewHandler extends AbstractFormHandler {

	@Override
	protected void execLoad() {
	    IProjectService service = BEANS.get(IProjectService.class);
	    ProjectFormData formData = new ProjectFormData();
	    exportFormData(formData);
	    formData = service.prepareCreate(formData);
	    importFormData(formData);

	    setEnabledPermission(new CreateProjectPermission());
	}

	@Override
	protected void execStore() {
	    IProjectService service = BEANS.get(IProjectService.class);
	    ProjectFormData formData = new ProjectFormData();
	    exportFormData(formData);
	    service.create(formData);
	}
    }
}
