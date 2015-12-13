
package org.tempuri;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>GetKind的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="GetKind"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="inname"/&gt;
 *     &lt;enumeration value="outname"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "GetKind")
@XmlEnum
public enum GetKind {

    @XmlEnumValue("inname")
    INNAME("inname"),
    @XmlEnumValue("outname")
    OUTNAME("outname");
    private final String value;

    GetKind(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GetKind fromValue(String v) {
        for (GetKind c: GetKind.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
