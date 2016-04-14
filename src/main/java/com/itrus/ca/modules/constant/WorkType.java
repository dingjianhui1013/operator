package com.itrus.ca.modules.constant;

/**
 * 计费策略业务类型
 * 不是work_deal_info的类型！
 * @author ZhangJingtao
 *
 */
public class WorkType {
	public static Integer TYPE_ADD = 0;//新增
	public static Integer TYPE_UPDATE = 1;//更新
	public static Integer TYPE_REISSUE = 2;//遗失补办
	public static Integer TYPE_CHANGE = 3;//变更
	public static Integer TYPE_OPEN = 4;//开户费
	public static Integer TYPE_TRUST = 5;//可信设备
	public static Integer TYPE_DAMAGE = 6;//损坏更换
	public static Integer TYPE_RENEW = 7;//续费
}
