package com.itrus.ca.modules.self.utils;

/**
 * 申请表的状态信息
 * 
 *
 */
public class selfApplicationStatus {
	// 申请表状态
    public static final String examinations = "0";// 等待审核
    public static final String downApply = "1";// 审核完成 下载
    public static final String payApply = "2";// 下载完成等待缴费
    public static final String handApply = "3";// 等待办理
    public static final String finishApply = "4";// 业务完成
    public static final String makeApply = "5";// 制证
    public static final String DEL_FLAG = "delFlag";//删除申请表
    public static final String denyApply = "6";//审核未通过
    
    // 申请表类型
    public static final String addApply = "0";//新增
    public static final String updateApply = "1";//未审核时修改
    public static final String modifyApply = "2";//变更
    public static final String renovateApply = "3";//更新

}
