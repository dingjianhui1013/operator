package com.itrus.ca.modules.work.vo;



import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReceivedPaymentsVo {
	
	private Map<String, List<String>> officePayMethod;
	
	private Map<String, Set<String>> officeDistrict;
	private Map<String, List<String>> districtPayMethod;
	private Map<String, List<Double>> districtMoneys;

	
	
	private List<Workoffice_MoneyVo>  officeMoneyVo;
	
	private List<WorkDate_MoneVo> dateMoneyVo;
	
	private List<Workoffice_district_MoneyVo> officeDistrictMoneyVo;
	
	private Object[] dates;
	
	private String appName;
	
	private List<Double> payMethodMoneys;
	
	private Double moneys;

	public Map<String, List<String>> getOfficePayMethod() {
		return officePayMethod;
	}

	public void setOfficePayMethod(Map<String, List<String>> officePayMethod) {
		this.officePayMethod = officePayMethod;
	}

	public List<Workoffice_MoneyVo> getOfficeMoneyVo() {
		return officeMoneyVo;
	}

	public void setOfficeMoneyVo(List<Workoffice_MoneyVo> officeMoneyVo) {
		this.officeMoneyVo = officeMoneyVo;
	}

	public List<WorkDate_MoneVo> getDateMoneyVo() {
		return dateMoneyVo;
	}

	public void setDateMoneyVo(List<WorkDate_MoneVo> dateMoneyVo) {
		this.dateMoneyVo = dateMoneyVo;
	}

	public Object[] getDates() {
		return dates;
	}

	public void setDates(Object[] dates) {
		this.dates = dates;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public List<Double> getPayMethodMoneys() {
		return payMethodMoneys;
	}

	public void setPayMethodMoneys(List<Double> payMethodMoneys) {
		this.payMethodMoneys = payMethodMoneys;
	}

	public List<Workoffice_district_MoneyVo> getOfficeDistrictMoneyVo() {
		return officeDistrictMoneyVo;
	}

	public void setOfficeDistrictMoneyVo(List<Workoffice_district_MoneyVo> officeDistrictMoneyVo) {
		this.officeDistrictMoneyVo = officeDistrictMoneyVo;
	}

	public Map<String, Set<String>> getOfficeDistrict() {
		return officeDistrict;
	}

	public void setOfficeDistrict(Map<String, Set<String>> officeDistrict) {
		this.officeDistrict = officeDistrict;
	}

	public Map<String, List<String>> getDistrictPayMethod() {
		return districtPayMethod;
	}

	public void setDistrictPayMethod(Map<String, List<String>> districtPayMethod) {
		this.districtPayMethod = districtPayMethod;
	}

	public Map<String, List<Double>> getDistrictMoneys() {
		return districtMoneys;
	}

	public void setDistrictMoneys(Map<String, List<Double>> districtMoneys) {
		this.districtMoneys = districtMoneys;
	}

	public Double getMoneys() {
		return moneys;
	}

	public void setMoneys(Double moneys) {
		this.moneys = moneys;
	}
	
	
	
	
}
