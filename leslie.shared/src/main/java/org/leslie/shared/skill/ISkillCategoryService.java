package org.leslie.shared.skill;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface ISkillCategoryService extends IService {

	SkillCategoryTablePageData getSkillCategoryTableData();

	SkillCategoryFormData load(SkillCategoryFormData formData) throws ProcessingException;

	SkillCategoryFormData create(SkillCategoryFormData formData) throws ProcessingException;

	SkillCategoryFormData store(SkillCategoryFormData formData) throws ProcessingException;

	void delete(List<Long> categoryIds) throws ProcessingException;

}
