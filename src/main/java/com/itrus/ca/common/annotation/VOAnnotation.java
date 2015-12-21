/**
 *2015年5月22日 上午10:45:02
 */
package com.itrus.ca.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author: liubin<br>
 *          用来帮助反射到VO对象用
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface VOAnnotation {
	String varName();
}
