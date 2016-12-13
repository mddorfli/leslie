package leslie.org.leslie.shared.security;

import org.eclipse.scout.rt.shared.security.BasicHierarchyPermission;

public enum PermissionLevel {
    LEVEL_UNDEFINED(BasicHierarchyPermission.LEVEL_UNDEFINED, "Undefined"),
    LEVEL_NONE(BasicHierarchyPermission.LEVEL_NONE, "None"),
    LEVEL_OWN(10, "Own"),
    LEVEL_PROJECT(20, "Project"),
    LEVEL_ALL(BasicHierarchyPermission.LEVEL_ALL, "All");

    private int value;
    private String nameLK;

    private PermissionLevel(int value, String name) {
	this.value = value;
	this.nameLK = name;
    }

    public int getValue() {
	return value;
    }

    public String getNameLK() {
	return nameLK;
    }

    public static PermissionLevel getInstance(int value) {
	PermissionLevel result = null;
	for (PermissionLevel entry : PermissionLevel.values()) {
	    if (entry.getValue() == value) {
		result = entry;
		break;
	    }
	}
	return result;
    }
}
