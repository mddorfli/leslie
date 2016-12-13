package leslie.org.leslie.shared.admin;

import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.page.AbstractTablePageData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications
 * recommended.
 */
@Generated(value = "leslie.org.leslie.client.user.UserTablePage", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class UserPageData extends AbstractTablePageData {

    private static final long serialVersionUID = 1L;

    @Override
    public UserRowData addRow() {
	return (UserRowData) super.addRow();
    }

    @Override
    public UserRowData addRow(int rowState) {
	return (UserRowData) super.addRow(rowState);
    }

    @Override
    public UserRowData createRow() {
	return new UserRowData();
    }

    @Override
    public Class<? extends AbstractTableRowData> getRowType() {
	return UserRowData.class;
    }

    @Override
    public UserRowData[] getRows() {
	return (UserRowData[]) super.getRows();
    }

    @Override
    public UserRowData rowAt(int index) {
	return (UserRowData) super.rowAt(index);
    }

    public void setRows(UserRowData[] rows) {
	super.setRows(rows);
    }

    public static class UserRowData extends AbstractTableRowData {

	private static final long serialVersionUID = 1L;
	public static final String id = "id";
	public static final String username = "username";
	public static final String firstName = "firstName";
	public static final String lastName = "lastName";
	public static final String displayName = "displayName";
	public static final String email = "email";
	public static final String lastLogin = "lastLogin";
	public static final String loginAttempts = "loginAttempts";
	public static final String blocked = "blocked";
	private Long m_id;
	private String m_username;
	private String m_firstName;
	private String m_lastName;
	private String m_displayName;
	private String m_email;
	private Date m_lastLogin;
	private Integer m_loginAttempts;
	private Boolean m_blocked;

	public Long getId() {
	    return m_id;
	}

	public void setId(Long newId) {
	    m_id = newId;
	}

	public String getUsername() {
	    return m_username;
	}

	public void setUsername(String newUsername) {
	    m_username = newUsername;
	}

	public String getFirstName() {
	    return m_firstName;
	}

	public void setFirstName(String newFirstName) {
	    m_firstName = newFirstName;
	}

	public String getLastName() {
	    return m_lastName;
	}

	public void setLastName(String newLastName) {
	    m_lastName = newLastName;
	}

	public String getDisplayName() {
	    return m_displayName;
	}

	public void setDisplayName(String newDisplayName) {
	    m_displayName = newDisplayName;
	}

	public String getEmail() {
	    return m_email;
	}

	public void setEmail(String newEmail) {
	    m_email = newEmail;
	}

	public Date getLastLogin() {
	    return m_lastLogin;
	}

	public void setLastLogin(Date newLastLogin) {
	    m_lastLogin = newLastLogin;
	}

	public Integer getLoginAttempts() {
	    return m_loginAttempts;
	}

	public void setLoginAttempts(Integer newLoginAttempts) {
	    m_loginAttempts = newLoginAttempts;
	}

	public Boolean getBlocked() {
	    return m_blocked;
	}

	public void setBlocked(Boolean newBlocked) {
	    m_blocked = newBlocked;
	}
    }
}
