package com.itrus.ca.modules.sys.utils;

import javax.servlet.http.HttpServletResponse;

public class JsonUtils {
	/**
	 * 该方法作废  更正为writeJson
	 * @param response
	 * @param json
	 * @param callback
	 * @throws Exception
	 */
	public static void writeJsonCommon(HttpServletResponse response ,String json,String callback) throws Exception{
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		System.out.println(json.toString());
		response.getWriter().write(json.toString());
	}
	
	public static void writeJson(HttpServletResponse response ,String json) throws Exception{
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		System.out.println(json.toString());
		response.getWriter().write(json.toString());
	}
	
	public static void writeJson4AjaxSubmit(HttpServletResponse response ,String json) throws Exception{
		response.setContentType("text/html;charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		System.out.println(json.toString());
		response.getWriter().write(json.toString());
	}
}
