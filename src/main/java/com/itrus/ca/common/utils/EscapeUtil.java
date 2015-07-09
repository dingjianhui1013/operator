package com.itrus.ca.common.utils;

/***************************************************************
 **
 ** @author Zhang Jingtao
 **
 ** date:2014年9月10日 下午3:35:49
 ** 
 ** @describe:转义_
 **
 **************************************************************
 */
public class EscapeUtil {
	/**
	 * 转义sql中的% _
	 * @date:2014年9月10日
	 * @user:Zhang Jingtao
	 * @return_type:String
	 */
	public static String escapeLike(String likeParam){
		if (likeParam==null) {
			return "";
		}else {
			return likeParam.replace("%", "\\%").replace("_", "\\_");
		}
	}
	
	/**
	 * 转义sql中的% _
	 * @date:2014年9月10日
	 * @user:Zhang Jingtao
	 * @return_type:String
	 */
	public static String escapeInput(String likeParam){
		if (likeParam==null) {
			return "";
		}else {
			return likeParam.replace("&lsquo;", "'").replace("&rsquo;", "'")
					.replace("&ldquo;", "“").replace("&rdquo;", "”").replace("&amp;","&")
					.replace("&quot;","“").replace("&hellip;","......");
		}
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		System.out.println(escapeLike("%%"));
		System.out.println(escapeLike("%ds%21"));
		System.out.println(escapeLike("__"));
		System.out.println(escapeLike("89_89"));
		
	}
}
