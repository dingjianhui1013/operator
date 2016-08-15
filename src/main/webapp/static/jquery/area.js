//得到省
	function getPrivince(){
		$("#s_province").html("");
		$.ajax({
			url : ctx+"/selfArea/findAllPrivince",
			type : "GET",
			dataType : "JSON",
			success : function(d) {
				
				
				var opt = document.createElement("OPTION");
				opt.innerText='省份';
				opt.value="";
				
				$("#s_province").append(opt);
				$.each(d.list,function(idx, ele) {
					
					var var_Option = document.createElement("OPTION");
					var_Option.innerText=ele.area_name;
					var_Option.value=ele.area_name;
					var_Option.id=ele.area_id;
					if(ele.area_name == province){
						
						var_Option.selected=true;
					}
					
					$("#s_province").append(var_Option);
	
				});
				
			}
		});
	}
	
	
	
	
	//得到第二级
	function getCity(privinceId){
		$("#s_city").html("");
		$("#s_county").html("");
		if(privinceId!= "0"){
			$.ajax({
				url : ctx+"/selfArea/findLower?higher="+ privinceId,
				type : "GET",
				dataType : "JSON",
				success : function(d) {
					
					var opt = document.createElement("OPTION");
					opt.innerText='地级市';
					opt.value="";
					
					$("#s_city").append(opt);
					$.each(d.list,function(idx, ele) {
						var var_Option = document.createElement("OPTION");
						var_Option.innerText=ele.area_name;
						var_Option.value=ele.area_name;
						var_Option.id=ele.area_id;
						if(ele.area_name ==city){
							
							var_Option.selected=true;
						}
						
						$("#s_city").append(var_Option);
					});
					
				}
			});
		}else{
			$("#s_city").html("<option value = ''>地级市</option>");
			$("#s_county").html("<option value = ''>市、县级市</option>");
		}
	}
	
	
	//得到第三级
	function getTown(cityId){
		$("#s_county").html("");
		if(cityId!= "0"){
			$.ajax({
				url : ctx+"/selfArea/findLower?higher="+ cityId,
				type : "GET",
				dataType : "JSON",
				success : function(d) {
					var opt = document.createElement("OPTION");
					opt.innerText='市、县级市';
					opt.value="";
					$("#s_county").append(opt);
					$.each(d.list,function(idx, ele) {
						var var_Option = document.createElement("OPTION");
						var_Option.innerText=ele.area_name;
						var_Option.value=ele.area_name;
						var_Option.id=ele.area_id;
						if(ele.area_name ==district){
							
							var_Option.selected=true;
						}
						
						$("#s_county").append(var_Option);
					});
					
				}
			});
		}else{
			$("#s_county").html("<option value = ''>市、县级市</option>");
		}
	}