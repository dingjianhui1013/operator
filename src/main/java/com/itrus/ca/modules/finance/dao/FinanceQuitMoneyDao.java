package com.itrus.ca.modules.finance.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.itrus.ca.common.persistence.BaseDao;
import com.itrus.ca.common.persistence.BaseDaoImpl;
import com.itrus.ca.modules.finance.entity.FinanceQuitMoney;

public interface FinanceQuitMoneyDao extends FinanceQuitMoneyDaoCustom, CrudRepository<FinanceQuitMoney, Long> {

}

interface FinanceQuitMoneyDaoCustom extends BaseDao<FinanceQuitMoney> {}

@Repository
class FinanceQuitMoneyDaoImpl extends BaseDaoImpl<FinanceQuitMoney> implements FinanceQuitMoneyDaoCustom {

}
