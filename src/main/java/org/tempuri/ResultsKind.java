
package org.tempuri;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ResultsKind的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="ResultsKind"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Ok"/&gt;
 *     &lt;enumeration value="wrong"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "ResultsKind")
@XmlEnum
public enum ResultsKind {

    @XmlEnumValue("Ok")
    OK("Ok"),
    @XmlEnumValue("wrong")
    WRONG("wrong");
    private final String value;

    ResultsKind(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ResultsKind fromValue(String v) {
        for (ResultsKind c: ResultsKind.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
