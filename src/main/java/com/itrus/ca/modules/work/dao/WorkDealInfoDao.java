/**
 * There are <a href="https://github.com/thinkgem/jeesite">JeeSite</a> code generation
 */
package com.itrus.ca.modules.work.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.profile.entity.ConfigProduct;
import com.itrus.ca.modules.work.entity.WorkCertInfo;
import com.itrus.ca.modules.work.entity.WorkDealInfo;
import com.itrus.ca.modules.work.entity.WorkPayInfo;

/**
 * 业务办理DAO接口
 * 
 * @author ZhangShaoBo
 * @version 2014-06-13
 */
public interface WorkDealInfoDao extends WorkDealInfoDaoCustom,
		CrudRepository<WorkDealInfo, Long> {
	@Modifying
	@Query("update WorkDealInfo set delFlag='" + WorkDealInfo.DEL_FLAG_DELETE
			+ "' where id = ?1")
	public void deleteById(Long id);

	@Modifying
	@Query("delete WorkDealInfo where id = ?1")
	public void delete(Long id);

	@Modifying
	@Query("From WorkDealInfo w where w.userSn like ?1 ")
	public List<WorkDealInfo> findNum(String timeStr);

	@Modifying
	@Query("From WorkDealInfo w where w.svn like ?1  order by w.createDate desc")
	public List<WorkDealInfo> findSvn(String svn);

	@Modifying
	@Query("From WorkDealInfo w where w.keySn=?1 order by w.creactDate DESC ")
	public List<WorkDealInfo> findktydsb1(String keySn);

	@Modifying
	@Query("From WorkDealInfo w where w.firstCertSN=?1 order by w.createDate DESC ")
	public List<WorkDealInfo> findByFirstCertSN(String firstCertSN);
	
	@Modifying
	@Query("From WorkDealInfo w where w.firstCertSN=?1 and w.dealInfoType=?2 order by w.createDate DESC ")
	public List<WorkDealInfo> findByFirstCertSNAndDealInfoType(String firstCertSN,Integer dealInfoType);

	@Modifying
	@Query("From WorkDealInfo w where w.keySn=?1 and w.certSn =?2 and delFlag = "
			+ WorkDealInfo.DEL_FLAG_NORMAL)
	public List<WorkDealInfo> findgxfw1(String keySn, String certSn);

	@Modifying
	@Query("From WorkDealInfo w where w.keySn=?1 and w.workCertInfo.id=?2")
	public List<WorkDealInfo> findgx(String keySn, Long id);

	@Modifying
	@Query("update WorkDealInfo set delFlag='" + WorkDealInfo.DEL_FLAG_NORMAL
			+ "' where id = ?1")
	public void deleteReturnById(Long id);

	@Modifying
	@Query("select DISTINCT(w.configProduct) From WorkDealInfo w where w.id  in ?1")
	public List<ConfigProduct> findByDistinkIds(List<Long> ids);

	
	@Modifying
	@Query("select DISTINCT(w.configProduct) From WorkDealInfo w where w.id  in (select scca.workDealInfoId from BatchUpdateInfoScca scca where scca.remark like ?1 and scca.statusCode = 1)")
	public List<ConfigProduct> findByDistinkIds(String remarkInfo);
	
	
	@Modifying
	@Query("update WorkDealInfo set firstCertSN=?2 where id = ?1")
	public void modifyFirstCertSN(Long id, String firstCertSn);

}

/**
 * DAO自定义接口
 * 
 * @author ZhangShaoBo
 */
interface WorkDealInfoDaoCustom extends BaseDao<WorkDealInfo> {

}

/**
 * DAO自定义接口实现
 * 
 * @author ZhangShaoBo
 */
@Component
class WorkDealInfoDaoImpl extends BaseDaoImpl<WorkDealInfo> implements
		WorkDealInfoDaoCustom {

}
