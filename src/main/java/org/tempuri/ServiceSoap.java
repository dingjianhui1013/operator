package org.tempuri;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 3.1.4
 * 2016-02-19T16:03:10.359+08:00
 * Generated source version: 3.1.4
 * 
 */
@WebService(targetNamespace = "http://tempuri.org/", name = "ServiceSoap")
@XmlSeeAlso({ObjectFactory.class})
public interface ServiceSoap {

    /**
     * 获取所有新的上行短信
     */
    @WebResult(name = "getMoMessageResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "getMoMessage", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetMoMessage")
    @WebMethod(action = "http://tempuri.org/getMoMessage")
    @ResponseWrapper(localName = "getMoMessageResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetMoMessageResponse")
    public org.tempuri.ArrayOfString getMoMessage(
        @WebParam(name = "uid", targetNamespace = "http://tempuri.org/")
        java.lang.String uid,
        @WebParam(name = "upass", targetNamespace = "http://tempuri.org/")
        java.lang.String upass
    );

    /**
     * 黑白名单管理单一
     */
    @WebResult(name = "WBnameResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "WBname", targetNamespace = "http://tempuri.org/", className = "org.tempuri.WBname")
    @WebMethod(operationName = "WBname", action = "http://tempuri.org/WBname")
    @ResponseWrapper(localName = "WBnameResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.WBnameResponse")
    public org.tempuri.ResultsKind wBname(
        @WebParam(name = "tel", targetNamespace = "http://tempuri.org/")
        java.lang.String tel,
        @WebParam(name = "getkind", targetNamespace = "http://tempuri.org/")
        org.tempuri.GetKind getkind,
        @WebParam(name = "uid", targetNamespace = "http://tempuri.org/")
        java.lang.String uid,
        @WebParam(name = "pass", targetNamespace = "http://tempuri.org/")
        java.lang.String pass
    );

    /**
     * 登陆密码修改
     */
    @WebResult(name = "EditpassResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "Editpass", targetNamespace = "http://tempuri.org/", className = "org.tempuri.Editpass")
    @WebMethod(operationName = "Editpass", action = "http://tempuri.org/Editpass")
    @ResponseWrapper(localName = "EditpassResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.EditpassResponse")
    public java.lang.String editpass(
        @WebParam(name = "oldpass", targetNamespace = "http://tempuri.org/")
        java.lang.String oldpass,
        @WebParam(name = "newpass", targetNamespace = "http://tempuri.org/")
        java.lang.String newpass,
        @WebParam(name = "scpid", targetNamespace = "http://tempuri.org/")
        java.lang.String scpid
    );

    /**
     * 下行群发
     */
    @WebResult(name = "mutilSendMessageResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "mutilSendMessage", targetNamespace = "http://tempuri.org/", className = "org.tempuri.MutilSendMessage")
    @WebMethod(action = "http://tempuri.org/mutilSendMessage")
    @ResponseWrapper(localName = "mutilSendMessageResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.MutilSendMessageResponse")
    public java.lang.String mutilSendMessage(
        @WebParam(name = "uid", targetNamespace = "http://tempuri.org/")
        java.lang.String uid,
        @WebParam(name = "upass", targetNamespace = "http://tempuri.org/")
        java.lang.String upass,
        @WebParam(name = "pid", targetNamespace = "http://tempuri.org/")
        int pid,
        @WebParam(name = "messageFile", targetNamespace = "http://tempuri.org/")
        org.tempuri.ArrayOfString messageFile
    );

    /**
     * 黑白名单管理群添加
     */
    @WebResult(name = "WBname1Result", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "WBname1", targetNamespace = "http://tempuri.org/", className = "org.tempuri.WBname1")
    @WebMethod(operationName = "WBname1", action = "http://tempuri.org/WBname1")
    @ResponseWrapper(localName = "WBname1Response", targetNamespace = "http://tempuri.org/", className = "org.tempuri.WBname1Response")
    public org.tempuri.ResultsKind wBname1(
        @WebParam(name = "tel", targetNamespace = "http://tempuri.org/")
        org.tempuri.ArrayOfString tel,
        @WebParam(name = "getkind", targetNamespace = "http://tempuri.org/")
        org.tempuri.GetKind getkind,
        @WebParam(name = "uid", targetNamespace = "http://tempuri.org/")
        java.lang.String uid,
        @WebParam(name = "pass", targetNamespace = "http://tempuri.org/")
        java.lang.String pass
    );

    /**
     * 下行单发
     */
    @WebResult(name = "sendMessageResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "sendMessage", targetNamespace = "http://tempuri.org/", className = "org.tempuri.SendMessage")
    @WebMethod(action = "http://tempuri.org/sendMessage")
    @ResponseWrapper(localName = "sendMessageResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.SendMessageResponse")
    public java.lang.String sendMessage(
        @WebParam(name = "uid", targetNamespace = "http://tempuri.org/")
        java.lang.String uid,
        @WebParam(name = "upass", targetNamespace = "http://tempuri.org/")
        java.lang.String upass,
        @WebParam(name = "messid", targetNamespace = "http://tempuri.org/")
        java.lang.String messid,
        @WebParam(name = "pid", targetNamespace = "http://tempuri.org/")
        int pid,
        @WebParam(name = "phone", targetNamespace = "http://tempuri.org/")
        java.lang.String phone,
        @WebParam(name = "message", targetNamespace = "http://tempuri.org/")
        java.lang.String message
    );

    /**
     * 通过子端口获取指定数量的上行短信
     */
    @WebResult(name = "getMoMessageSubportResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "getMoMessageSubport", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetMoMessageSubport")
    @WebMethod(action = "http://tempuri.org/getMoMessageSubport")
    @ResponseWrapper(localName = "getMoMessageSubportResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetMoMessageSubportResponse")
    public org.tempuri.ArrayOfString getMoMessageSubport(
        @WebParam(name = "uid", targetNamespace = "http://tempuri.org/")
        java.lang.String uid,
        @WebParam(name = "upass", targetNamespace = "http://tempuri.org/")
        java.lang.String upass,
        @WebParam(name = "count", targetNamespace = "http://tempuri.org/")
        int count
    );

    /**
     * 根据smId(唯一标识)，数量取上行短信
     */
    @WebResult(name = "getMoMessage1Result", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "getMoMessage1", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetMoMessage1")
    @WebMethod(action = "http://tempuri.org/getMoMessage1")
    @ResponseWrapper(localName = "getMoMessage1Response", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetMoMessage1Response")
    public org.tempuri.ArrayOfString getMoMessage1(
        @WebParam(name = "uid", targetNamespace = "http://tempuri.org/")
        java.lang.String uid,
        @WebParam(name = "upass", targetNamespace = "http://tempuri.org/")
        java.lang.String upass,
        @WebParam(name = "smId", targetNamespace = "http://tempuri.org/")
        java.lang.String smId,
        @WebParam(name = "count", targetNamespace = "http://tempuri.org/")
        int count
    );

    /**
     * 查询发送错误的短信信息
     */
    @WebResult(name = "GetSubmitErrorlistResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "GetSubmitErrorlist", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetSubmitErrorlist")
    @WebMethod(operationName = "GetSubmitErrorlist", action = "http://tempuri.org/GetSubmitErrorlist")
    @ResponseWrapper(localName = "GetSubmitErrorlistResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.GetSubmitErrorlistResponse")
    public org.tempuri.ArrayOfString getSubmitErrorlist(
        @WebParam(name = "uid", targetNamespace = "http://tempuri.org/")
        java.lang.String uid,
        @WebParam(name = "pass", targetNamespace = "http://tempuri.org/")
        java.lang.String pass,
        @WebParam(name = "begtime", targetNamespace = "http://tempuri.org/")
        java.lang.String begtime,
        @WebParam(name = "endtime", targetNamespace = "http://tempuri.org/")
        java.lang.String endtime
    );

    /**
     * 带子端口的短信发送
     */
    @WebResult(name = "SubPortSendMessageResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "SubPortSendMessage", targetNamespace = "http://tempuri.org/", className = "org.tempuri.SubPortSendMessage")
    @WebMethod(operationName = "SubPortSendMessage", action = "http://tempuri.org/SubPortSendMessage")
    @ResponseWrapper(localName = "SubPortSendMessageResponse", targetNamespace = "http://tempuri.org/", className = "org.tempuri.SubPortSendMessageResponse")
    public java.lang.String subPortSendMessage(
        @WebParam(name = "uid", targetNamespace = "http://tempuri.org/")
        java.lang.String uid,
        @WebParam(name = "upass", targetNamespace = "http://tempuri.org/")
        java.lang.String upass,
        @WebParam(name = "subport", targetNamespace = "http://tempuri.org/")
        java.lang.String subport,
        @WebParam(name = "messid", targetNamespace = "http://tempuri.org/")
        java.lang.String messid,
        @WebParam(name = "pid", targetNamespace = "http://tempuri.org/")
        int pid,
        @WebParam(name = "phone", targetNamespace = "http://tempuri.org/")
        java.lang.String phone,
        @WebParam(name = "message", targetNamespace = "http://tempuri.org/")
        java.lang.String message
    );
}
