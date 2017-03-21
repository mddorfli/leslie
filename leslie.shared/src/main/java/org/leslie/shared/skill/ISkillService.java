package org.leslie.shared.skill;

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
	}
	
	SkillTablePageData getSkillTableData();
	
	SkillTablePageData getSkillTableData(Long categoryId);
}
