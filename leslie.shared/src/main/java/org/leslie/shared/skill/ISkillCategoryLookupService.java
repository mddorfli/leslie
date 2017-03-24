package org.leslie.shared.skill;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

@TunnelToServer
public interface ISkillCategoryLookupService extends ILookupService<Long> {
}
