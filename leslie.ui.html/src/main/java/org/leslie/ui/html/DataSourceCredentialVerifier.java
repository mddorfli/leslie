package org.leslie.ui.html;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Bean;
import org.eclipse.scout.rt.platform.config.AbstractPositiveIntegerConfigProperty;
import org.eclipse.scout.rt.platform.config.AbstractStringConfigProperty;
import org.eclipse.scout.rt.platform.security.ICredentialVerifier;
import org.eclipse.scout.rt.platform.security.SecurityUtility;
import org.eclipse.scout.rt.platform.util.Base64Utility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Bean
public class DataSourceCredentialVerifier implements ICredentialVerifier {

    private static final String SELECT_USER_SQL = ""
	    + "SELECT pw_hash, pw_salt"
	    + "  FROM users "
	    + " WHERE username = ? "
	    + "   AND (blocked IS NULL OR blocked = false) "
	    + "   AND (failed_login_attempts < ? OR id = 1) ";

    private static final String UPDATE_RETRY_COUNT_SQL = ""
	    + "UPDATE users "
	    + "   SET failed_login_attempts = COALESCE(failed_login_attempts, 0) + 1 "
	    + " WHERE username = ? "
	    + "   AND id != 1 ";

    private static final int DEFAULT_MAX_RETRIES = 5;

    private static Logger logger = LoggerFactory.getLogger(DataSourceCredentialVerifier.class);

    private String m_dbUrl;
    private String m_dbUsername;
    private String m_dbPassword;
    private Integer m_maxRetries;

    private Connection m_connection;

    @PostConstruct
    private void init() {
	m_dbUrl = BEANS.get(JdbcUrlProperty.class).getValue();
	m_dbUsername = BEANS.get(JdbcUsernameProperty.class).getValue();
	m_dbPassword = BEANS.get(JdbcPasswordProperty.class).getValue();
	m_maxRetries = BEANS.get(MaxRetriesProperty.class).getValue();

	try {
	    m_connection = DriverManager.getConnection(m_dbUrl, m_dbUsername, m_dbPassword);
	} catch (SQLException e) {
	    logger.error("Unable to connect to database", e);
	}
    }

    @PreDestroy
    private void destroy() {
	if (m_connection != null) {
	    try {
		m_connection.close();
	    } catch (SQLException e) {
		// NOP
	    }
	}

    }

    @Override
    public int verify(String username, char[] password) throws IOException {
	int result = AUTH_FORBIDDEN;
	try (PreparedStatement stmt = m_connection.prepareStatement(SELECT_USER_SQL)) {
	    stmt.setString(1, username);
	    stmt.setInt(2, m_maxRetries != null ? m_maxRetries.intValue() : DEFAULT_MAX_RETRIES);

	    logger.debug("{}", stmt);

	    stmt.execute();
	    ResultSet rs = stmt.getResultSet();
	    if (rs != null && rs.next()) {
		String pwHash = rs.getString(1);
		String pwSalt = rs.getString(2);

		if (!StringUtility.isNullOrEmpty(pwHash) && !StringUtility.isNullOrEmpty(pwSalt)) {
		    byte[] hash = SecurityUtility.hash(Base64Utility.decode(String.valueOf(password)),
			    Base64Utility.decode(pwSalt));
		    if (Arrays.equals(hash, Base64Utility.decode(pwHash))) {
			result = AUTH_OK;
		    }
		}

	    }

	} catch (SQLException e) {
	    logger.error("Unable to fetch authentication info from db", e);
	    result = AUTH_FAILED;
	}

	if (result == AUTH_FAILED && !StringUtility.isNullOrEmpty(username)) {
	    // attempt to increment the failed retry count
	    try (PreparedStatement stmt = m_connection.prepareStatement(UPDATE_RETRY_COUNT_SQL)) {
		stmt.setString(1, username);
		int updates = stmt.executeUpdate();
		if (updates > 0) {
		    logger.info("retry count for {} incremented. {} row updated.", username, updates);
		}
	    } catch (SQLException e) {
		logger.warn("Unable to update retry count for user " + username, e);
	    }
	}

	return result;
    }

    public static class JdbcUrlProperty extends AbstractStringConfigProperty {

	@Override
	public String getKey() {
	    return "auth.jdbc.url";
	}
    }

    public static class JdbcUsernameProperty extends AbstractStringConfigProperty {

	@Override
	public String getKey() {
	    return "auth.jdbc.user";
	}
    }

    public static class JdbcPasswordProperty extends AbstractStringConfigProperty {

	@Override
	public String getKey() {
	    return "auth.jdbc.password";
	}
    }

    public static class MaxRetriesProperty extends AbstractPositiveIntegerConfigProperty {

	@Override
	public String getKey() {
	    return "auth.maxretries";
	}
    }

    public static void main(String[] args) {
	String username = "admin";
	String password = "manager";
	byte[] salt = SecurityUtility.createRandomBytes();
	byte[] hash = SecurityUtility.hash(Base64Utility.decode(password), salt);

	System.out.printf("The salt is: [%s]\n", Base64Utility.encode(salt));
	System.out.printf("The hash for %s/%s is: [%s]\n", username, password, Base64Utility.encode(hash));
    }
}
