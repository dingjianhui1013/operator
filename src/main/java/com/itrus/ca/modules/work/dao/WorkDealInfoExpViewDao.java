/**
 *2016年7月19日 下午1:23:03
 */
package com.itrus.ca.modules.work.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfoExpView;

/**
 * @author: liubin
 *
 */
public interface WorkDealInfoExpViewDao extends WorkDealInfoExpViewDaoCustom,
		CrudRepository<WorkDealInfoExpView, Long> {

}

/**
 * DAO自定义接口
 * 
 * @author ZhangShaoBo
 */
interface WorkDealInfoExpViewDaoCustom extends BaseDao<WorkDealInfoExpView> {

}

/**
 * DAO自定义接口实现
 * 
 * @author ZhangShaoBo
 */
@Component
class WorkDealInfoExpViewDaoImpl extends BaseDaoImpl<WorkDealInfoExpView>
		implements WorkDealInfoExpViewDaoCustom {

}