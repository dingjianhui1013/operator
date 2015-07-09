package com.itrus.ca.modules.profile.entity;

import javax.persistence.Transient;

public class ProductTypeObj {
		Integer id;
		String name;
		
		public ProductTypeObj(Integer id,String name){
			this.id = id;
			this.name = name;
		}
		@Transient
		public Integer getId() {
			return id;
		}
		@Transient
		public String getName() {
			return name;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public void setName(String name) {
			this.name = name;
		}

}
