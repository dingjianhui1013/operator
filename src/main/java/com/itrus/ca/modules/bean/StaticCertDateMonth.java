package com.itrus.ca.modules.bean;

import java.util.List;

import com.itrus.ca.modules.profile.entity.ConfigApp;

public class StaticCertDateMonth {
	private ConfigApp configApp;
	private List<StaticCertMonth> certMonths;
	
	public ConfigApp getConfigApp() {
		return configApp;
	}
	public void setConfigApp(ConfigApp configApp) {
		this.configApp = configApp;
	}
	public List<StaticCertMonth> getCertMonths() {
		return certMonths;
	}
	public void setCertMonths(List<StaticCertMonth> certMonths) {
		this.certMonths = certMonths;
	}
}
