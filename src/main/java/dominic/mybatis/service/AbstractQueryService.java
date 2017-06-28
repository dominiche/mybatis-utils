package dominic.mybatis.service;

import dominic.mybatis.annotation.IdName;
import dominic.mybatis.annotation.TableName;
import dominic.mybatis.bean.PageParam;
import dominic.mybatis.dao.query.BaseQueryDAO;
import dominic.mybatis.support.OrderSupport;
import dominic.mybatis.support.Restriction;
import dominic.mybatis.support.RestrictionUnit;
import dominic.mybatis.support.appender.OrderSupportAppender;
import dominic.mybatis.support.build.SelectSupport;
import dominic.mybatis.support.build.SelectSupportUnit;
import dominic.mybatis.utils.RestrictionsUtils;
import dominic.mybatis.utils.SqlBuildUtils;
import dominic.mybatis.utils.TransformUtils;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
     */
    public <R extends Number> List<T> queryByIdList(List<R> idList) {
        SelectSupport support = SelectSupport.builder()
                .selectFields(SqlBuildUtils.getFieldsByClass(getClazz()).toString())
                .tableName(getTableName())
                .conditions(Restriction.in(getIdName(), idList).SQL())
                .build();
        List<T> list = new ArrayList<>();
        List<HashMap<String, Object>> mapList = baseQueryDAO.queryBySql(support.SQL());
        if (CollectionUtils.isNotEmpty(mapList)) {
            list = TransformUtils.hashMapToBean(mapList, clazz);
        }
        return list;
    }

    /**
     * 根据主键名查询，默认主键名为id，主键名不是id的请在entity上加IdName注解
     * @param id
     * @param <R>
     * @return
     */
    public <R extends Number> T queryById(R id) {
        SelectSupport support = SelectSupport.builder()
                .selectFields(SqlBuildUtils.getFieldsByClass(getClazz()).toString())
                .tableName(getTableName())
                .conditions(Restriction.eq(getIdName(), id).SQL())
                .build();
        HashMap<String, Object> hashMap = baseQueryDAO.queryUniqueBySql(support.SQL());
        return TransformUtils.hashMapToBean(hashMap, clazz);
    }

    public List<T> query(@NonNull RestrictionUnit restrictions) {
        SelectSupport support = getSelectSupport(SqlBuildUtils.getFieldsByClass(clazz).toString(), restrictions.SQL());
        return queryBySql(support.SQL());
    }

    public List<T> query(@NonNull String customColumns,@NonNull RestrictionUnit restrictions) {
        SelectSupport support = getSelectSupport(customColumns, restrictions.SQL());
        return queryBySql(support.SQL());
    }

    public List<T> queryByBean(@NonNull T bean) {
        SelectSupport support = getSelectSupport(SqlBuildUtils.getFieldsByClass(clazz).toString(), RestrictionsUtils.buildConditions(bean).SQL());
        return queryBySql(support.SQL());
    }

    public List<T> queryByBean(@NonNull String customColumns, @NonNull T bean) {
        SelectSupport support = getSelectSupport(customColumns, RestrictionsUtils.buildConditions(bean).SQL());
        return queryBySql(support.SQL());
    }

    public List<T> queryBySql(SelectSupportUnit support) {
        return queryBySql(support.SQL());
    }

    public List<T> queryBySql(@NonNull String sql) {
        List<T> list = new ArrayList<>();
        List<HashMap<String, Object>> mapList = baseQueryDAO.queryBySql(sql);
        if (CollectionUtils.isNotEmpty(mapList)) {
            list = TransformUtils.hashMapToBean(mapList, clazz);
        }
        return list;
    }

    public <R> T queryUnique(@NonNull String conditionColumn, @NonNull R conditionColumnValue) {
        SelectSupport support = getSelectSupport(SqlBuildUtils.getFieldsByClass(clazz).toString(),
                Restriction.eq(conditionColumn, conditionColumnValue).SQL());
        HashMap<String, Object> hashMap = baseQueryDAO.queryUniqueBySql(support.SQL());
        return TransformUtils.hashMapToBean(hashMap, clazz);
    }

    private SelectSupport getSelectSupport(String columns, String conditions) {
        return SelectSupport.builder()
                    .selectFields(columns)
                    .tableName(getTableName())
                    .conditions(conditions)
                    .build();
    }

    public T queryUnique(@NonNull RestrictionUnit restrictions) {
        SelectSupport support = getSelectSupport(SqlBuildUtils.getFieldsByClass(clazz).toString(), restrictions.SQL());
        HashMap<String, Object> hashMap = baseQueryDAO.queryUniqueBySql(support.SQL());
        return TransformUtils.hashMapToBean(hashMap, clazz);
    }

    public <R> R querySingleValue(@NonNull String column, @NonNull Class<R> singleValueType, @NonNull RestrictionUnit restrictions) {
        SelectSupport support = getSelectSupport(column, restrictions.SQL());
        return baseQueryDAO.querySingleValueBySql(support.SQL(), singleValueType);
    }

    public List<HashMap<String, Object>> querySpecialList(String sql) {
        return baseQueryDAO.querySpecialListBySql(sql);
    }

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
        Long maxId = baseQueryDAO.querySingleValueBySql(support.SQL(), Long.class);
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

    public long queryCount(SelectSupportUnit support) {
        return baseQueryDAO.queryCountBySql(support.countSQL());
    }

    public long queryCount(String sql) {
        return baseQueryDAO.queryCountBySql(sql);
    }

}
