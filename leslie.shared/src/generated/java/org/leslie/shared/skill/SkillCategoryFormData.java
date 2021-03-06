package org.leslie.shared.skill;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications
 * recommended.
 */
@Generated(value = "org.leslie.client.skill.SkillCategoryForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class SkillCategoryFormData extends AbstractFormData {

	private static final long serialVersionUID = 1L;

	/**
	 * access method for property CategoryId.
	 */
	public Long getCategoryId() {
		return getCategoryIdProperty().getValue();
	}

	/**
	 * access method for property CategoryId.
	 */
	public void setCategoryId(Long categoryId) {
		getCategoryIdProperty().setValue(categoryId);
	}

	public CategoryIdProperty getCategoryIdProperty() {
		return getPropertyByClass(CategoryIdProperty.class);
	}

	public Name getName() {
		return getFieldByClass(Name.class);
	}

	public static class CategoryIdProperty extends AbstractPropertyData<Long> {

		private static final long serialVersionUID = 1L;
	}

	public static class Name extends AbstractValueFieldData<String> {

		private static final long serialVersionUID = 1L;
	}
}
