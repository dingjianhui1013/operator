
<%@ taglib prefix="shiro" uri="/WEB-INF/tlds/shiros.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="fnc" uri="/WEB-INF/tlds/fnc.tld" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%
	//String rootPath = com.itrus.ca.common.config.Global.getConfig("server.root");
    String path = request.getContextPath();  
    String rootPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";  
    String imgPath = com.itrus.ca.common.config.Global.getConfig("imgFileUpload.path");
    
	request.setAttribute("server.root", rootPath);
	request.setAttribute("imgFileUpload.path", imgPath);
%>
<c:set var="imgPath" value="<%=imgPath %>"/>
<c:set var="rootPath" value="<%=rootPath %>"/>
<c:set var="ctx" scope="session" value="${rootPath}${fns:getAdminPath()}"/>
<c:set var="ctxStatic" value="${rootPath}/static"/>
<c:set var="js" value="${rootPath}/js"/>
<c:set var="ctxILetter" value="${rootPath}"/>
