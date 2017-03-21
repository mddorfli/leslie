package org.leslie.shared.skill;

import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface ISkillCategoryService extends IService {

	SkillCategoryTablePageData getSkillCategoryTableData();
}
