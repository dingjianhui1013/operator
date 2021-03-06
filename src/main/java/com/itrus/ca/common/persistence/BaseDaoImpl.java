/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.itrus.ca.common.persistence;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.Version;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.filter.impl.CachingWrapperFilter;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.alibaba.druid.pool.DruidDataSource;
import com.itrus.ca.common.annotation.VOAnnotation;
import com.itrus.ca.common.utils.ReflectHelper;
import com.itrus.ca.common.utils.Reflections;
import com.itrus.ca.common.utils.StringHelper;
import com.itrus.ca.common.utils.StringUtils;

/**
 * DAO支持类实现
 * 
 * @author ThinkGem
 * @version 2013-05-15
 * @param <T>
 */
public class BaseDaoImpl<T> implements BaseDao<T> {

	/**
	 * 获取实体工厂管理对象
	 */
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private DataSource dataSource;

	/**
	 * 实体类类型(由构造方法自动赋值)
	 */
	private Class<?> entityClass;

	/**
	 * 构造方法，根据实例类自动获取实体类类型
	 */
	public BaseDaoImpl() {
		entityClass = Reflections.getClassGenricType(getClass());
	}

	/**
	 * 获取实体工厂管理对象
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * 获取 Session
	 */
	public Session getSession() {
		return (Session) getEntityManager().getDelegate();
	}

	/**
	 * 强制与数据库同步
	 */
	public void flush() {
		getSession().flush();
	}

	/**
	 * 清除缓存数据
	 */
	public void clear() {
		getSession().clear();
	}

	// -------------- QL Query --------------

	/**
	 * QL 分页查询
	 * 
	 * @param page
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> Page<E> find(Page<E> page, String qlString, Object... parameter) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()) {
			String countQlString = "select count(*) "
					+ removeSelect(removeOrders(qlString));
			// page.setCount(Long.valueOf(createQuery(countQlString,
			// parameter).uniqueResult().toString()));
			Query query = createQuery(countQlString, parameter);
			List<Object> list = query.list();
			if (list.size() > 0) {
				page.setCount(Long.valueOf(list.get(0).toString()));
			} else {
				page.setCount(list.size());
			}
			if (page.getCount() < 1) {
				return page;
			}
		}
		// order by
		String ql = qlString;
		if (StringUtils.isNotBlank(page.getOrderBy())) {
			ql += " order by " + page.getOrderBy();
		}
		Query query = createQuery(ql, parameter);
		// set page
		if (!page.isDisabled()) {
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}
		page.setList(query.list());
		return page;
	}

	/**
	 * QL 查询
	 * 
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> find(String qlString, Object... parameter) {
		Query query = createQuery(qlString, parameter);
		return query.list();
	}

	/**
	 * QL 更新
	 * 
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	@Transactional(readOnly = false)
	public int update(String qlString, Object... parameter) {
		return createQuery(qlString, parameter).executeUpdate();
	}

	/**
	 * 创建 QL 查询对象
	 * 
	 * @param qlString
	 * @param parameter
	 * @return
	 */
	public Query createQuery(String qlString, Object... parameter) {
		Query query = getSession().createQuery(qlString);
		setParameter(query, parameter);
		return query;
	}

	// -------------- SQL Query --------------

	/**
	 * SQL 分页查询
	 * 
	 * @param page
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public <E> Page<E> findBySql(Page<E> page, String sqlString,
			Object... parameter) {
		return findBySql(page, sqlString, null, parameter);
	}

	public void exeSql(String sql) {
		Logger log = Logger.getLogger("ex");
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			ps = conn.prepareStatement(sql);
			ps.execute();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			log.error(StringHelper.getStackInfo(e));
		} finally {
			try {
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
				log.error(StringHelper.getStackInfo(e1));
			}
		}
	}

	// @Transactional(readOnly = false)
	// public void exeSql(final String sql) {
	// try {
	// getSession().doReturningWork(new ReturningWork() {
	// public SQLQuery execute(Connection connection)
	// throws SQLException {
	//
	// Session session = getSession();
	// try {
	// org.hibernate.Transaction tx = session.getTransaction();
	// tx.begin();
	// // session.getTransaction();
	// session.createSQLQuery(sql).executeUpdate();
	// tx.commit();
	// } catch (Exception e) {
	// e.printStackTrace();
	// Logger log = Logger.getLogger("ex");
	// log.error(StringHelper.getStackInfo(e));
	// }
	// return null;
	// }
	// });
	// } catch (Exception e) {
	// // e.printStackTrace();
	// Logger log = Logger.getLogger("ex");
	// log.error(StringHelper.getStackInfo(e));
	// }
	// }

	/**
	 * SQL 分页查询
	 * 
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> Page<E> findBySql(Page<E> page, String sqlString,
			Class<?> resultClass, Object... parameter) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()) {
			String countSqlString = "select count(*) "
					+ removeSelect(removeOrders(sqlString));
			// page.setCount(Long.valueOf(createSqlQuery(countSqlString,
			// parameter).uniqueResult().toString()));
			Query query = createSqlQuery(countSqlString, parameter);
			List<Object> list = query.list();
			if (list.size() > 0) {
				page.setCount(Long.valueOf(list.get(0).toString()));
			} else {
				page.setCount(list.size());
			}
			if (page.getCount() < 1) {
				return page;
			}
		}
		// order by
		String sql = sqlString;
		if (StringUtils.isNotBlank(page.getOrderBy())) {
			sql += " order by " + page.getOrderBy();
		}
		SQLQuery query = createSqlQuery(sql, parameter);
		// set page
		if (!page.isDisabled()) {
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}
		setResultTransformer(query, resultClass);
		page.setList(query.list());
		return page;
	}

	/**
	 * SQL 查询
	 * 
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public <E> List<E> findBySql(String sqlString, Object... parameter) {
		return findBySql(sqlString, null, parameter);
	}

	/**
	 * SQL 查询
	 * 
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> findBySql(String sqlString, Class<?> resultClass,
			Object... parameter) {
		SQLQuery query = createSqlQuery(sqlString, parameter);
		setResultTransformer(query, resultClass);
		return query.list();
	}

	/**
	 * SQL 更新
	 * 
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	@Transactional(readOnly = false)
	public int updateBySql(String sqlString, Object... parameter) {
		return createSqlQuery(sqlString, parameter).executeUpdate();
	}

	/**
	 * 创建 SQL 查询对象
	 * 
	 * @param sqlString
	 * @param parameter
	 * @return
	 */
	public SQLQuery createSqlQuery(String sqlString, Object... parameter) {
		SQLQuery query = getSession().createSQLQuery(sqlString);
		setParameter(query, parameter);
		return query;
	}

	// -------------- Query Tools --------------

	/**
	 * 设置查询结果类型
	 * 
	 * @param query
	 * @param resultClass
	 */
	private void setResultTransformer(SQLQuery query, Class<?> resultClass) {
		if (resultClass != null) {
			if (resultClass == Map.class) {
				query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
			} else if (resultClass == List.class) {
				query.setResultTransformer(Transformers.TO_LIST);
			} else {
				query.setResultTransformer(Transformers
						.aliasToBean(resultClass));
				// query.addEntity(resultClass);
			}
		}
	}

	/**
	 * 设置查询参数
	 * 
	 * @param query
	 * @param parameter
	 */
	private void setParameter(Query query, Object... parameter) {
		if (parameter != null) {
			for (int i = 0; i < parameter.length; i++) {
				query.setParameter(i, parameter[i]);
			}
		}
	}

	/**
	 * 去除qlString的select子句。
	 * 
	 * @param hql
	 * @return
	 */
	private String removeSelect(String qlString) {
		int beginPos = qlString.toLowerCase().indexOf("from");
		return qlString.substring(beginPos);
	}

	/**
	 * 去除hql的orderBy子句。
	 * 
	 * @param hql
	 * @return
	 */
	private String removeOrders(String qlString) {
		Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(qlString);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, "");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	// -------------- Criteria --------------

	/**
	 * 分页查询
	 * 
	 * @param page
	 * @return
	 */
	public Page<T> find(Page<T> page) {
		return find(page, createDetachedCriteria());
	}

	/**
	 * 使用检索标准对象分页查询
	 * 
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria) {
		return find(page, detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}

	@SuppressWarnings("unchecked")
	public List<T> manyFind(DetachedCriteria detachedCriteria,
			ResultTransformer resultTransformer, int size) {
		Criteria criteria = detachedCriteria
				.getExecutableCriteria(getSession());
		criteria.setResultTransformer(resultTransformer);
		criteria.setFirstResult(0);
		criteria.setMaxResults(size);
		return criteria.list();
	}

	/**
	 * 使用检索标准对象分页查询
	 * 
	 * @param page
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page<T> find(Page<T> page, DetachedCriteria detachedCriteria,
			ResultTransformer resultTransformer) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()) {
			page.setCount(count(detachedCriteria));
			if (page.getCount() < 1) {
				return page;
			}
		}
		Criteria criteria = detachedCriteria
				.getExecutableCriteria(getSession());
		criteria.setResultTransformer(resultTransformer);
		// set page
		if (!page.isDisabled()) {
			criteria.setFirstResult(page.getFirstResult());
			criteria.setMaxResults(page.getMaxResults());
		}
		// order by
		if (StringUtils.isNotBlank(page.getOrderBy())) {
			for (String order : StringUtils.split(page.getOrderBy(), ",")) {
				String[] o = StringUtils.split(order, " ");
				if (o.length == 1) {
					criteria.addOrder(Order.asc(o[0]));
				} else if (o.length == 2) {
					if ("DESC".equals(o[1].toUpperCase())) {
						criteria.addOrder(Order.desc(o[0]));
					} else {
						criteria.addOrder(Order.asc(o[0]));
					}
				}
			}
		}
		page.setList(criteria.list());
		return page;
	}

	/**
	 * 使用检索标准对象查询
	 * 
	 * @param detachedCriteria
	 * @return
	 */
	public List<T> find(DetachedCriteria detachedCriteria) {
		return find(detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY);
	}

	/**
	 * 使用检索标准对象查询
	 * 
	 * @param detachedCriteria
	 * @param resultTransformer
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<T> find(DetachedCriteria detachedCriteria,
			ResultTransformer resultTransformer) {
		Criteria criteria = detachedCriteria
				.getExecutableCriteria(getSession());
		criteria.setResultTransformer(resultTransformer);
		return criteria.list();
	}

	/**
	 * 使用检索标准对象查询记录数
	 * 
	 * @param detachedCriteria
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public long count(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria
				.getExecutableCriteria(getSession());
		long totalCount = 0;
		try {
			// Get orders
			Field field = CriteriaImpl.class.getDeclaredField("orderEntries");
			field.setAccessible(true);
			List orderEntrys = (List) field.get(criteria);
			// Remove orders
			field.set(criteria, new ArrayList());
			// Get count
			criteria.setProjection(Projections.rowCount());
			totalCount = Long.valueOf(criteria.uniqueResult().toString());
			// Clean count
			criteria.setProjection(null);
			// Restore orders
			field.set(criteria, orderEntrys);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return totalCount;
	}

	/**
	 * 创建与会话无关的检索标准对象
	 * 
	 * @param criterions
	 *            Restrictions.eq("name", value);
	 * @return
	 */
	public DetachedCriteria createDetachedCriteria(Criterion... criterions) {
		DetachedCriteria dc = DetachedCriteria.forClass(entityClass);
		for (Criterion c : criterions) {
			dc.add(c);
		}
		return dc;
	}

	// -------------- Hibernate search --------------

	/**
	 * 获取全文Session
	 */
	public FullTextSession getFullTextSession() {
		return Search.getFullTextSession(getSession());
	}

	/**
	 * 建立索引
	 */
	public void createIndex() {
		try {
			getFullTextSession().createIndexer(entityClass).startAndWait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 全文检索
	 * 
	 * @param page
	 *            分页对象
	 * @param query
	 *            关键字查询对象
	 * @param queryFilter
	 *            查询过滤对象
	 * @param sort
	 *            排序对象
	 * @return 分页对象
	 */
	@SuppressWarnings("unchecked")
	public Page<T> search(Page<T> page, BooleanQuery query,
			BooleanQuery queryFilter, Sort sort) {

		// 按关键字查询
		FullTextQuery fullTextQuery = getFullTextSession().createFullTextQuery(
				query, entityClass);

		// 过滤无效的内容
		if (queryFilter != null) {
			fullTextQuery.setFilter(new CachingWrapperFilter(
					new QueryWrapperFilter(queryFilter)));
		}

		// 设置排序
		if (sort != null) {
			fullTextQuery.setSort(sort);
		}

		// 定义分页
		page.setCount(fullTextQuery.getResultSize());
		fullTextQuery.setFirstResult(page.getFirstResult());
		fullTextQuery.setMaxResults(page.getMaxResults());

		// 先从持久化上下文中查找对象，如果没有再从二级缓存中查找
		fullTextQuery.initializeObjectsWith(
				ObjectLookupMethod.SECOND_LEVEL_CACHE,
				DatabaseRetrievalMethod.QUERY);

		// 返回结果
		page.setList(fullTextQuery.list());

		return page;
	}

	/**
	 * 获取全文查询对象
	 */
	public BooleanQuery getFullTextQuery(BooleanClause... booleanClauses) {
		BooleanQuery booleanQuery = new BooleanQuery();
		for (BooleanClause booleanClause : booleanClauses) {
			booleanQuery.add(booleanClause);
		}
		return booleanQuery;
	}

	/**
	 * 获取全文查询对象
	 * 
	 * @param q
	 *            查询关键字
	 * @param fields
	 *            查询字段
	 * @return 全文查询对象
	 */
	public BooleanQuery getFullTextQuery(String q, String... fields) {
		Analyzer analyzer = new IKAnalyzer();
		BooleanQuery query = new BooleanQuery();
		try {
			if (StringUtils.isNotBlank(q)) {
				for (String field : fields) {
					QueryParser parser = new QueryParser(Version.LUCENE_36,
							field, analyzer);
					query.add(parser.parse(q), Occur.SHOULD);
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return query;
	}

	/**
	 * 设置关键字高亮
	 * 
	 * @param query
	 *            查询对象
	 * @param list
	 *            设置高亮的内容列表
	 * @param subLength
	 *            截取长度
	 * @param fields
	 *            字段名
	 */
	public List<T> keywordsHighlight(BooleanQuery query, List<T> list,
			int subLength, String... fields) {
		Analyzer analyzer = new IKAnalyzer();
		Formatter formatter = new SimpleHTMLFormatter(
				"<span class=\"highlight\">", "</span>");
		Highlighter highlighter = new Highlighter(formatter, new QueryScorer(
				query));
		highlighter.setTextFragmenter(new SimpleFragmenter(subLength));
		for (T entity : list) {
			try {
				for (String field : fields) {
					String text = StringUtils.replaceHtml((String) Reflections
							.invokeGetter(entity, field));
					String description = highlighter.getBestFragment(analyzer,
							field, text);
					if (description != null) {
						Reflections
								.invokeSetter(entity, fields[0], description);
						break;
					}
					Reflections.invokeSetter(entity, fields[0],
							StringUtils.abbr(text, subLength * 2));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InvalidTokenOffsetsException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	// ------------------- add

	/**
	 * 
	 * 注意，普通的多表联查不能用此方法<br>
	 * 因为hibernate本身BUG：<br>
	 * 多表存在同名列时,用原生SQL查询返回的结果集将与sql在DB里查询的不符<br>
	 * 而原生SQL的外链接查询则没此情况发生...<br>
	 * 
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @param returnVO
	 *            - 指定返回的VO对象，自动映射查找所包含的属性类里,<br>
	 *            - 是否有与传入的resultClass想对应的，如果有则直接赋值<br>
	 * @return
	 */
	public <E> Page<E> findBySqlString(Page<E> page, String sqlString,
			Class[] resultClass, Class returnVO) {
		return findBySqlString(page, sqlString, null, resultClass, returnVO);
	}

	/**
	 * 注意，普通的多表联查不能用此方法<br>
	 * 因为hibernate本身BUG：<br>
	 * 多表存在同名列时,用原生SQL查询返回的结果集将与sql在DB里查询的不符<br>
	 * 而原生SQL的外链接查询则没此情况发生...<br>
	 * 
	 * @param page
	 * @param sqlString
	 * @param parameter
	 * @param resultClass
	 * @param returnVO
	 *            - 指定返回的VO对象，自动映射查找所包含的属性类里,<br>
	 *            - 是否有与传入的resultClass想对应的，如果有则直接赋值<br>
	 * @return
	 */
	public <E> Page<E> findBySqlStringParameter(Page<E> page, String sqlString,
			Parameter parameter, Class[] resultClass, Class returnVO) {
		return findBySqlString(page, sqlString, parameter, resultClass,
				returnVO);
	}

	/**
	 * SQL 分页查询
	 * 
	 * @param page
	 * @param sqlString
	 * @param resultClass
	 * @param parameter
	 * @return
	 */
	public <E> Page<E> findBySql(Page<E> page, String sqlString,
			Parameter parameter, Class<?> resultClass) {
		return findBySqlString(page, sqlString, parameter,
				new Class[] { resultClass }, null);
	}

	public <E> Page<E> findBySqlString(Page<E> page, String sqlString,
			Class[] resultClass) {
		return findBySqlString(page, sqlString, null, resultClass, null);
	}

	private void setResultTransformer(SQLQuery query, Class[] resultClass) {
		if (resultClass == null || resultClass.length <= 0)
			return;
		for (Class cls : resultClass) {
			query.addEntity(cls);

		}
	}

	@SuppressWarnings("unchecked")
	public <E> Page<E> findBySqlString(Page<E> page, String sqlString,
			Parameter parameter, Class[] resultClass, Class returnVO) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()) {
			String select = StringHelper.isNull(page.getCountDistinctColumn()) ? "select count(*) "
					: "select count(distinct " + page.getCountDistinctColumn()
							+ ") ";
			String countSqlString = select
					+ removeSelect(removeOrders(sqlString));
			// page.setCount(Long.valueOf(createSqlQuery(countSqlString,
			// parameter).uniqueResult().toString()));
			Query query = createSqlQuery(countSqlString, parameter);
			List<Object> list = query.list();
			if (list.size() > 0) {
				page.setCount(Long.valueOf(list.get(0).toString()));
			} else {
				page.setCount(list.size());
			}
			if (page.getCount() < 1) {
				return page;
			}
		}
		String sql = sqlString;
		// group by
		if (StringUtils.isNotBlank(page.getGroupBy())) {
			sql += " group by " + page.getGroupBy();
		}
		// order by
		if (StringUtils.isNotBlank(page.getOrderBy())) {
			sql += " order by " + page.getOrderBy();
		}
		SQLQuery query = createSqlQuery(sql, parameter);
		// set page
		if (!page.isDisabled()) {
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}

		boolean isMutilReturn = false;
		if (resultClass != null && resultClass.length == 1)
			setResultTransformer(query, resultClass[0]);
		else if (resultClass != null && resultClass.length > 1) {
			setResultTransformer(query, resultClass);
			isMutilReturn = true;
		}
		List l = query.list();

		page.setList(query.list());

		if (isMutilReturn)
			setResultReturnVO(returnVO, page);

		return page;
	}

	@SuppressWarnings("unchecked")
	public <E> Page<E> find(Page<E> page, String qlString, Parameter parameter,
			Class[] resultClass, Class returnVO) {
		// get count
		if (!page.isDisabled() && !page.isNotCount()) {
			String countQlString = "select count(*) "
					+ removeSelect(removeOrders(qlString));
			// page.setCount(Long.valueOf(createQuery(countQlString,
			// parameter).uniqueResult().toString()));
			Query query = createQuery(countQlString, parameter);
			List<Object> list = query.list();
			if (list.size() > 0) {
				page.setCount(Long.valueOf(list.get(0).toString()));
			} else {
				page.setCount(list.size());
			}
			if (page.getCount() < 1) {
				return page;
			}
		}
		String ql = qlString;
		// group by
		if (StringUtils.isNotBlank(page.getGroupBy())) {
			ql += " group by " + page.getGroupBy();
		}
		// order by

		if (StringUtils.isNotBlank(page.getOrderBy())) {
			ql += " order by " + page.getOrderBy();
		}
		Query query = createQuery(ql, parameter);
		// set page
		if (!page.isDisabled()) {
			query.setFirstResult(page.getFirstResult());
			query.setMaxResults(page.getMaxResults());
		}
		page.setList(query.list());

		boolean isMutilReturn = false;

		if (resultClass != null && resultClass.length > 1)
			isMutilReturn = true;

		if (isMutilReturn)
			setResultReturnVO(returnVO, page);
		return page;
	}

	private void setResultReturnVO(Class returnVO, Page result) {
		List<Class> lst = null;
		List<Object[]> o = result.getList();
		List resList = new ArrayList();
		if (returnVO == null)
			return;
		try {
			// 所有变量名
			lst = ReflectHelper.getAllvarClass(returnVO);

			// 队列里的值
			for (Object[] e : o) {
				Object obj = returnVO.newInstance();
				// 类似e=Object[]{a,b}
				// 所有数组对象循环
				for (Object ef : e) {
					// 假设对象里的属性均为对象
					for (Class cls : lst) {
						// 返回的对象数组有符合的就set值
						if (cls.equals(ef.getClass())) {
							try {
								VOAnnotation vo = ReflectHelper
										.getAnnotationByClass(returnVO, cls,
												VOAnnotation.class);
								ReflectHelper.setValue(obj, vo.varName(), ef);
							} catch (IllegalArgumentException
									| InvocationTargetException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
				resList.add(obj);
			}
			result.setList(resList);
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	public List<T> manyFind(DetachedCriteria detachedCriteria, int size) {
		return manyFind(detachedCriteria, Criteria.DISTINCT_ROOT_ENTITY, size);
	}

	public List<Map> findBySQLListMap(final String sql, final int pageNo,
			final int numPerPage) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			ClassNotFoundException, InstantiationException {

		List<Map> l = findBySQL(sql, pageNo, numPerPage).setResultTransformer(
				Transformers.ALIAS_TO_ENTITY_MAP).list();
		return l;

	}

	public SQLQuery findBySQL(final String sql, final int pageNo,
			final int numPerPage) {
		return (SQLQuery) (T) getSession().doReturningWork(new ReturningWork() {
			public SQLQuery execute(Connection connection) throws SQLException {
				SQLQuery sqlQuery = getSession().createSQLQuery(sql);
				if (pageNo > 0) {
					sqlQuery.setFirstResult((pageNo - 1) * numPerPage);
					sqlQuery.setMaxResults(numPerPage);
				}

				return sqlQuery;
			}
		});
	}

}