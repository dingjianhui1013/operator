package com.itrus.ca.common.utils;

import org.hibernate.dialect.Oracle10gDialect;

public class Oracle12Dialect extends Oracle10gDialect {
	public Oracle12Dialect() {  
		super();
		registerHibernateType(1, "string");
		registerHibernateType(-9, "string");
		registerHibernateType(-16, "string");
		registerHibernateType(3, "double");
    }  


}
