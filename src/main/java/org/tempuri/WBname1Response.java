
package org.tempuri;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="WBname1Result" type="{http://tempuri.org/}ResultsKind"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "wBname1Result"
})
@XmlRootElement(name = "WBname1Response")
public class WBname1Response {

    @XmlElement(name = "WBname1Result", required = true)
    @XmlSchemaType(name = "string")
    protected ResultsKind wBname1Result;

    /**
     * 获取wBname1Result属性的值。
     * 
     * @return
     *     possible object is
     *     {@link ResultsKind }
     *     
     */
    public ResultsKind getWBname1Result() {
        return wBname1Result;
    }

    /**
     * 设置wBname1Result属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link ResultsKind }
     *     
     */
    public void setWBname1Result(ResultsKind value) {
        this.wBname1Result = value;
    }

}
