package org.leslie.shared.skill;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface ISkillService extends IService {

	enum SkillPresentation {
		/**
		 * Administration view.
		 */
		ADMIN,

		/**
		 * Also administration view, shown under a parent category.
		 */
		ADMIN_CATEGORY,

		/**
		 * Personal view. Self-evaluation mode.
		 */
		PERSONAL,
	}

	SkillTablePageData getSkillTableData();

	SkillTablePageData getCategorySkillTableData(Long categoryId);

	/**
	 * @return skill table data for the currently logged in user
	 */
	SkillTablePageData getPersonalSkillTableData();

	SkillFormData load(SkillFormData formData) throws ProcessingException;

	SkillFormData create(SkillFormData formData) throws ProcessingException;

	SkillFormData store(SkillFormData formData) throws ProcessingException;

	void delete(List<Long> categoryIds) throws ProcessingException;

}
