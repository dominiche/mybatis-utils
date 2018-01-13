package dominic.mybatis.service;

import com.google.common.collect.Maps;
import dominic.mybatis.annotation.IdName;
import dominic.mybatis.annotation.TableName;
import dominic.mybatis.bean.PageParam;
import dominic.mybatis.constants.MybatisUtils;
import dominic.mybatis.dao.query.BaseQueryDAO;
import dominic.mybatis.support.Restriction;
import dominic.mybatis.support.RestrictionUnit;
import dominic.mybatis.support.appender.AbstractAppender;
import dominic.mybatis.support.appender.OrderSupportAppender;
import dominic.mybatis.support.build.OrderSupport;
import dominic.mybatis.support.build.SelectSupport;
import dominic.mybatis.support.build.SelectSupportUnit;
import dominic.mybatis.support.stream.Restrictions;
import dominic.mybatis.utils.RestrictionsUtils;
import dominic.mybatis.utils.SqlBuildUtils;
import dominic.mybatis.utils.TransformUtils;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Created by herongxing on 2017/2/23 19:34.
 */
public abstract class AbstractQueryService<T> {
    @Autowired
    private BaseQueryDAO baseQueryDAO;

    @Getter
    private Class<T> clazz;
    @Getter
    private String tableName;
    @Getter
    private String idName = "id";

    {
        clazz = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

        TableName tableNameAnnotation = clazz.getAnnotation(TableName.class);
        if (null == tableNameAnnotation) {
            throw new RuntimeException("还没有指定表名！请在entity上加@TableName注解。entity: " + clazz.getName() );
        }
        tableName = tableNameAnnotation.value();

        IdName idNameAnnotation = clazz.getAnnotation(IdName.class);
        if (null != idNameAnnotation) {
            idName = idNameAnnotation.value();
        }
    }

    /**
     * 根据主键名查询，默认主键名为id，主键名不是id的请在entity上加IdName注解
     * @param idList
     * @param <R>
     * @return
     * @see AbstractQueryService#queryById(java.util.Collection)
     */
    @Deprecated
    public <R extends Number> List<T> queryByIdList(List<R> idList) {
        Restriction restriction = Restriction.in(getIdName(), idList);
        return query(SqlBuildUtils.getFieldsByClass(clazz).toString(), restriction);
    }

    /**
     * 根据主键名查询，默认主键名为id，主键名不是id的请在entity上加IdName注解
     * @param ids
     * @param <R>
     * @return
     */
    public <R extends Number> List<T> queryById(Collection<R> ids) {
        return query(Restriction.in(getIdName(), ids));
    }

    /**
     * 根据主键名查询，默认主键名为id，主键名不是id的请在entity上加IdName注解
     * @param id
     * @param <R>
     * @return
     */
    public <R extends Number> T queryById(R id) {
        Restriction restriction = Restriction.eq(getIdName(), id);
        SelectSupport support = SelectSupport.builder()
                .selectFields(SqlBuildUtils.getFieldsByClass(getClazz()).toString())
                .tableName(getTableName())
                .conditions(restriction.SQL())
                .build();
        Map<String, Object> map = getParamMap(support.SQL());
        map.putAll(restriction.getParamMap());
        return doQueryUnique(map);
    }

    private T doQueryUnique(Map<String, Object> map) {
        HashMap<String, Object> hashMap = baseQueryDAO.queryUnique(map);
        return TransformUtils.hashMapToBean(hashMap, clazz);
    }
    private List<T> doQueryList(Map<String, Object> map) {
        List<HashMap<String, Object>> mapList = baseQueryDAO.query(map);
        List<T> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(mapList)) {
            list = TransformUtils.hashMapToBean(mapList, clazz);
        }
        return list;
    }

    @Deprecated
    public List<T> query(SelectSupportUnit support) {
        return query(support.SQL());
    }
    @Deprecated
    public List<T> query(@NonNull String sql) {
        List<T> list = new ArrayList<>();
        List<HashMap<String, Object>> mapList = baseQueryDAO.queryBySql(sql);
        if (CollectionUtils.isNotEmpty(mapList)) {
            list = TransformUtils.hashMapToBean(mapList, clazz);
        }
        return list;
    }

    public List<T> query(@NonNull RestrictionUnit restrictions) {
        return query(SqlBuildUtils.getFieldsByClass(clazz).toString(), restrictions);
    }

    public List<T> query(@NonNull String customColumns, @NonNull RestrictionUnit restrictions) {
        return query(customColumns, restrictions, null, null);
    }

    public List<T> query(@NonNull RestrictionUnit restrictions, PageParam pageParam, AbstractAppender<OrderSupport> orderSupportAppender) {
        return query(SqlBuildUtils.getFieldsByClass(clazz).toString(), restrictions, pageParam, orderSupportAppender);
    }

    public List<T> query(@NonNull String customColumns, @NonNull RestrictionUnit restrictions, PageParam pageParam, AbstractAppender<OrderSupport> orderSupportAppender) {
        SelectSupport support = getSelectSupport(customColumns, restrictions.SQL());
        support.setPageParam(pageParam);
        support.setOrderSupportAppender(orderSupportAppender);
        Map<String, Object> map = getParamMap(support.SQL());
        map.putAll(restrictions.getParamMap());
        return doQueryList(map);
    }


    public <R> List<T> queryByBean(@NonNull R bean) {
        Restrictions restrictions = RestrictionsUtils.buildConditions(bean);
        return query(SqlBuildUtils.getFieldsByClass(clazz).toString(), restrictions);
    }

    public <R> List<T> queryByBean(@NonNull String customColumns, @NonNull R bean) {
        Restrictions restrictions = RestrictionsUtils.buildConditions(bean);
        return query(customColumns, restrictions);
    }

    public <R> List<T> queryByBean(@NonNull R bean, PageParam pageParam) {
        return queryByBean(bean, pageParam, null);
    }

    public <R> List<T> queryByBean(@NonNull R bean, PageParam pageParam, AbstractAppender<OrderSupport> orderSupportAppender) {
        return queryByBean(SqlBuildUtils.getFieldsByClass(clazz).toString(), bean, pageParam, orderSupportAppender);
    }

    public <R> List<T> queryByBean(@NonNull String customColumns, @NonNull R bean, PageParam pageParam, AbstractAppender<OrderSupport> orderSupportAppender) {
        return query(customColumns, RestrictionsUtils.buildConditions(bean), pageParam, orderSupportAppender);
    }


    public <R> T queryUnique(@NonNull String conditionColumn, @NonNull R conditionColumnValue) {
        Restriction restriction = Restriction.eq(conditionColumn, conditionColumnValue);
        return queryUnique(restriction);
    }

    public T queryUnique(@NonNull RestrictionUnit restriction) {
        SelectSupport support = getSelectSupport(SqlBuildUtils.getFieldsByClass(clazz).toString(), restriction.SQL());
        Map<String, Object> map = getParamMap(support.SQL());
        map.putAll(restriction.getParamMap());
        return doQueryUnique(map);
    }

    private SelectSupport getSelectSupport(String columns, String conditions) {
        return SelectSupport.builder()
                    .selectFields(columns)
                    .tableName(getTableName())
                    .conditions(conditions)
                    .build();
    }
    private Map<String, Object> getParamMap(String sql) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(MybatisUtils.SQL, sql);
        return map;
    }

    public <R> R querySingleValue(@NonNull String column, @NonNull RestrictionUnit restrictions) {
        SelectSupport support = getSelectSupport(column, restrictions.SQL());
        Map<String, Object> map = getParamMap(support.SQL());
        map.putAll(restrictions.getParamMap());
        return baseQueryDAO.querySingleValueBySql(map);
    }

    @Deprecated
    public List<HashMap<String, Object>> querySpecialList(String sql) {
        return baseQueryDAO.querySpecialListBySql(sql);
    }
    @Deprecated
    public HashMap<String, Object> querySpecial(String sql) {
        return baseQueryDAO.querySpecialBySql(sql);
    }

    /**
     * 默认Id名为id，不是id的请在entity上加IdName注解
     */
    public Long queryMaxIdLongValue() {
        SelectSupport support = SelectSupport.builder()
                .selectFields(getIdName())
                .tableName(getTableName())
                .orderSupportAppender(OrderSupportAppender.newInstance().append(OrderSupport.DESC(getIdName())))
                .pageParam(PageParam.builder().pageIndex(0).pageSize(1).build())
                .build();
        Map<String, Object> map = getParamMap(support.SQL());
        Long maxId = baseQueryDAO.querySingleValueBySql(map);
        if (null == maxId) {
            maxId = 0L;
        }
        return maxId;
    }

    /**
     * 默认Id名为id，不是id的请在entity上加IdName注解
     */
    public Integer queryMaxIdValue() {
        return queryMaxIdLongValue().intValue();
    }

    public <R> long queryCount(@NonNull R bean) {
        Restrictions restrictions = RestrictionsUtils.buildConditions(bean);
        SelectSupport support = getSelectSupport("count(1)", restrictions.SQL());
        Map<String, Object> map = getParamMap(support.SQL());
        map.putAll(restrictions.getParamMap());
        return baseQueryDAO.queryCountBySql(map);
    }

    public long queryCount(SelectSupportUnit support) {
        return queryCount(support.countSQL());
    }

    public long queryCount(String sql) {
        Map<String, Object> map = getParamMap(sql);
        return baseQueryDAO.queryCountBySql(map);
    }

}
