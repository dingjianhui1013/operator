package com.itrus.ca.modules.self.dao;



import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.self.entity.SelfArea;
import com.itrus.ca.modules.work.entity.WorkDealInfo;

/**
 * 申请人DAO接口
 * 
 * @author HuHao
 * @version 2016-02-18
 */
public interface SelfAreaDao extends SelfAreaDaoCustom, CrudRepository<SelfArea, Long> {

	@Modifying
	@Query("from SelfArea where areaName = ?1")
	public List<SelfArea> findByAreaName(String areaName);

	
	
	
	
	@Modifying
	@Query("from SelfArea where areaName = ?1 and parentId = ?2")
	public List<SelfArea> findByProvinceName(String areaName,String provincedId);
}

/**
 * DAO自定义接口
 * 
 * @author HuHao
 */
interface SelfAreaDaoCustom extends BaseDao<SelfArea> {

}

/**
 * DAO自定义接口实现
 * 
 * @author HuHao
 */
@Component
class SelfAreaDaoImpl extends BaseDaoImpl<SelfArea> implements SelfAreaDaoCustom {


}
