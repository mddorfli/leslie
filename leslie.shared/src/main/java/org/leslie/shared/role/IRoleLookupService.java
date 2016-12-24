package org.leslie.shared.role;

import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

@TunnelToServer
public interface IRoleLookupService extends ILookupService<Long> {
}
