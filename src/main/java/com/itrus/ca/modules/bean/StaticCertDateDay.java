package com.itrus.ca.modules.bean;

import java.util.List;

import com.itrus.ca.modules.profile.entity.ConfigApp;

public class StaticCertDateDay {
	private ConfigApp configApp;
	private List<StaticCertDay> zCertDays;//自费
	private List<StaticCertDay> hCertDays;//合同
	private List<StaticCertDay> zfCertDays;//政府
	
	public List<StaticCertDay> getCertDays() {
		return certDays;
	}
	public void setCertDays(List<StaticCertDay> certDays) {
		this.certDays = certDays;
	}
	private List<StaticCertDay> certDays;
	
	public ConfigApp getConfigApp() {
		return configApp;
	}
	public void setConfigApp(ConfigApp configApp) {
		this.configApp = configApp;
	}
	public List<StaticCertDay> getzCertDays() {
		return zCertDays;
	}
	public void setzCertDays(List<StaticCertDay> zCertDays) {
		this.zCertDays = zCertDays;
	}
	public List<StaticCertDay> gethCertDays() {
		return hCertDays;
	}
	public void sethCertDays(List<StaticCertDay> hCertDays) {
		this.hCertDays = hCertDays;
	}
	public List<StaticCertDay> getZfCertDays() {
		return zfCertDays;
	}
	public void setZfCertDays(List<StaticCertDay> zfCertDays) {
		this.zfCertDays = zfCertDays;
	}
}
