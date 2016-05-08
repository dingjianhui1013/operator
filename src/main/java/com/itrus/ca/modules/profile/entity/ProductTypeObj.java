package com.itrus.ca.modules.profile.entity;

import javax.persistence.Transient;

public class ProductTypeObj {
		Integer id;
		
		String lable;
		
		String name;
		
		public ProductTypeObj(Integer id,String name){
			this.id = id;
			this.name = name;
		}
		
		public ProductTypeObj(String lable,String name){
			this.lable = lable;
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
		@Transient
		public String getLable(){
			return lable;
		}
		
		public void setLable(String lable){
			this.lable = lable;
		}
		
		public void setId(Integer id) {
			this.id = id;
		}
		public void setName(String name) {
			this.name = name;
		}

}
