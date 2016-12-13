package leslie.org.leslie.shared.admin;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications
 * recommended.
 */
@Generated(value = "leslie.org.leslie.client.role.RoleTablePage", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class RolePageData extends AbstractTablePageData {

    private static final long serialVersionUID = 1L;

    @Override
    public RoleRowData addRow() {
	return (RoleRowData) super.addRow();
    }

    @Override
    public RoleRowData addRow(int rowState) {
	return (RoleRowData) super.addRow(rowState);
    }

    @Override
    public RoleRowData createRow() {
	return new RoleRowData();
    }

    @Override
    public Class<? extends AbstractTableRowData> getRowType() {
	return RoleRowData.class;
    }

    @Override
    public RoleRowData[] getRows() {
	return (RoleRowData[]) super.getRows();
    }

    @Override
    public RoleRowData rowAt(int index) {
	return (RoleRowData) super.rowAt(index);
    }

    public void setRows(RoleRowData[] rows) {
	super.setRows(rows);
    }

    public static class RoleRowData extends AbstractTableRowData {

	private static final long serialVersionUID = 1L;
	public static final String roleNr = "roleNr";
	public static final String roleName = "roleName";
	private Long m_roleNr;
	private String m_roleName;

	public Long getRoleNr() {
	    return m_roleNr;
	}

	public void setRoleNr(Long newRoleNr) {
	    m_roleNr = newRoleNr;
	}

	public String getRoleName() {
	    return m_roleName;
	}

	public void setRoleName(String newRoleName) {
	    m_roleName = newRoleName;
	}
    }
}
