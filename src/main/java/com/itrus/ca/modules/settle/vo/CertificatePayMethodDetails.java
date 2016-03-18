package com.itrus.ca.modules.settle.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 证书结算实体
 * 
 * @author liulipeng
 *
 */
public class CertificatePayMethodDetails {

	private int totalCount;
	private Map<String,String> methods;
	private int payMethodcount;
	private int methodAlipayCount;
	private int methodBankCount;
	private int methodMoneyCount;
	private int methodPosCount;
	private int methodAlipayAndBankCount;
	private int methodAlipayAndMoneyCount;
	private int methodAlipayAndPosCount;
	private int methodBankAndMoneyCount;
	private int methodBankAndPosCount;
	private int methodMoneyAndPosCount;
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPayMethodcount() {
		return payMethodcount;
	}
	public void setPayMethodcount(int payMethodcount) {
		this.payMethodcount = payMethodcount;
	}
	public int getMethodAlipayCount() {
		return methodAlipayCount;
	}
	public void setMethodAlipayCount(int methodAlipayCount) {
		this.methodAlipayCount = methodAlipayCount;
	}
	public int getMethodBankCount() {
		return methodBankCount;
	}
	public void setMethodBankCount(int methodBankCount) {
		this.methodBankCount = methodBankCount;
	}
	public int getMethodMoneyCount() {
		return methodMoneyCount;
	}
	public void setMethodMoneyCount(int methodMoneyCount) {
		this.methodMoneyCount = methodMoneyCount;
	}
	public int getMethodPosCount() {
		return methodPosCount;
	}
	public void setMethodPosCount(int methodPosCount) {
		this.methodPosCount = methodPosCount;
	}
	public int getMethodAlipayAndBankCount() {
		return methodAlipayAndBankCount;
	}
	public void setMethodAlipayAndBankCount(int methodAlipayAndBankCount) {
		this.methodAlipayAndBankCount = methodAlipayAndBankCount;
	}
	public int getMethodAlipayAndMoneyCount() {
		return methodAlipayAndMoneyCount;
	}
	public void setMethodAlipayAndMoneyCount(int methodAlipayAndMoneyCount) {
		this.methodAlipayAndMoneyCount = methodAlipayAndMoneyCount;
	}
	public int getMethodAlipayAndPosCount() {
		return methodAlipayAndPosCount;
	}
	public void setMethodAlipayAndPosCount(int methodAlipayAndPosCount) {
		this.methodAlipayAndPosCount = methodAlipayAndPosCount;
	}
	public int getMethodBankAndMoneyCount() {
		return methodBankAndMoneyCount;
	}
	public void setMethodBankAndMoneyCount(int methodBankAndMoneyCount) {
		this.methodBankAndMoneyCount = methodBankAndMoneyCount;
	}
	public int getMethodBankAndPosCount() {
		return methodBankAndPosCount;
	}
	public void setMethodBankAndPosCount(int methodBankAndPosCount) {
		this.methodBankAndPosCount = methodBankAndPosCount;
	}
	public int getMethodMoneyAndPosCount() {
		return methodMoneyAndPosCount;
	}
	public void setMethodMoneyAndPosCount(int methodMoneyAndPosCount) {
		this.methodMoneyAndPosCount = methodMoneyAndPosCount;
	}
	public Map<String,String> getMethods() {
		if(this.methods==null){
			this.methods = new HashMap<String,String>();
		}
		return this.methods;
	}
	public void setMethods(Map<String,String> methods) {
		this.methods = methods;
	}
	
	public void addMethod(String method,String desc) {
		if(this.methods==null){
			this.methods = new HashMap<String,String>();
		}
		this.methods.put(method,desc);
	}
	
	
	
	
	
}