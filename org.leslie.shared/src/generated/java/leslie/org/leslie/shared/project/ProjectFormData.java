package leslie.org.leslie.shared.project;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications
 * recommended.
 */
@Generated(value = "leslie.org.leslie.client.project.ProjectForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class ProjectFormData extends AbstractFormData {

    private static final long serialVersionUID = 1L;

    /**
     * access method for property Id.
     */
    public long getId() {
	return getIdProperty().getValue() == null ? 0L : getIdProperty().getValue();
    }

    /**
     * access method for property Id.
     */
    public void setId(long id) {
	getIdProperty().setValue(id);
    }

    public IdProperty getIdProperty() {
	return getPropertyByClass(IdProperty.class);
    }

    public Name getName() {
	return getFieldByClass(Name.class);
    }

    public Version getVersion() {
	return getFieldByClass(Version.class);
    }

    public static class IdProperty extends AbstractPropertyData<Long> {

	private static final long serialVersionUID = 1L;
    }

    public static class Name extends AbstractValueFieldData<String> {

	private static final long serialVersionUID = 1L;
    }

    public static class Version extends AbstractValueFieldData<String> {

	private static final long serialVersionUID = 1L;
    }
}
