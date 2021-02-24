package com.aplication.preferences;

import java.util.prefs.Preferences;

/**
 * Con el fin de no dejar quemadas las credenciales en el .class
 * es mas facil controlarlas por aca
 * @author Jhon Eder MC
 * @version 1.2
 * @since 11-12-2020
 *
 */

public class PreferencesDBMS {
	
	Preferences preferences = Preferences.userNodeForPackage(PreferencesDBMS.class);
	
	public PreferencesDBMS() {
		String url = "jdbc:mysql://localhost:33065/db2";
		String user = "root";
		String password ="";	
		setCredentials(url, user, password);
	}
	
	
	private void setCredentials(String url, String user, String password) {
		preferences.put("db_url",url);
		preferences.put("db_user", user);
		preferences.put("db_password", password);		
	}
	
	public String getUrl() {
		return preferences.get("db_url", null);
	}
	
	public String getUser() {
		return preferences.get("db_user", null);
	}
	
	public String getPassword() {
		return preferences.get("deb_password", null);
	}

}

