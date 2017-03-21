package org.leslie.client.permission;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.leslie.shared.security.PermissionLevel;

public class PermissionLevelLookupCall extends LocalLookupCall<Integer> {
    private static final long serialVersionUID = 1L;

    @Override
    protected List<LookupRow<Integer>> execCreateLookupRows() throws ProcessingException {
	ArrayList<LookupRow<Integer>> rows = new ArrayList<LookupRow<Integer>>();

	for (PermissionLevel level : PermissionLevel.values()) {
	    rows.add(new LookupRow<Integer>(level.getValue(), TEXTS.get(level.getNameLK())));
	}
	return rows;
    }
}
