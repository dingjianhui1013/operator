/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.ixin.entity.Vo;


/**
 * 短信配置Entity
 * 
 * @author qt
 * @version 2015-11-27
 */
public class IxinDataVo {

    
    private String appName;//应用名称
    private long certNumber;//再用证书 个数
    private long survivalNumber;//存活证书个数
    private String rate;//存活率
    
    public IxinDataVo() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public IxinDataVo(String appName, long certNumber, long survivalNumber,String rate) {
        super();
        this.appName = appName;
        this.certNumber = certNumber;
        this.survivalNumber = survivalNumber;
        this.rate = rate;
    }
   
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public long getCertNumber() {
        return certNumber;
    }
    public void setCertNumber(long certNumber) {
        this.certNumber = certNumber;
    }
    public long getSurvivalNumber() {
        return survivalNumber;
    }
    public void setSurvivalNumber(long survivalNumber) {
        this.survivalNumber = survivalNumber;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
	
}
