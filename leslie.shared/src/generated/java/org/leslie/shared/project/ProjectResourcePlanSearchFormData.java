package org.leslie.shared.project;

import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications
 * recommended.
 */
@Generated(value = "org.leslie.client.project.ProjectResourcePlanSearchForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class ProjectResourcePlanSearchFormData extends AbstractFormData {

    private static final long serialVersionUID = 1L;

    public From getFrom() {
	return getFieldByClass(From.class);
    }

    public To getTo() {
	return getFieldByClass(To.class);
    }

    public static class From extends AbstractValueFieldData<Date> {

	private static final long serialVersionUID = 1L;
    }

    public static class To extends AbstractValueFieldData<Date> {

	private static final long serialVersionUID = 1L;
    }
}