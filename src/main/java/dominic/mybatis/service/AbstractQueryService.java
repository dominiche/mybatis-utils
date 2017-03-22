package dominic.mybatis.service;

import dominic.mybatis.annotation.IdName;
import dominic.mybatis.annotation.TableName;
import dominic.mybatis.bean.PageList;
import dominic.mybatis.bean.PageParam;
import dominic.mybatis.dao.BaseQueryDAO;
import dominic.mybatis.support.*;
import dominic.mybatis.utils.SqlBuildUtils;
import dominic.mybatis.utils.TransformUtils;
import lombok.Getter;
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
            throw new RuntimeException("还没有指定表名！请在entity上加TableName注解。entity: " + clazz.getName() );
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
    public <R> List<T> queryByIdList(List<R> idList) {
        SelectSupport support = SelectSupport.builder()
                .selectFields(SqlBuildUtils.getFieldsByClass(getClazz()).toString())
                .tableName(getTableName())
                .conditions(Restriction.in(getIdName(), idList).toSQL())
                .build();
        List<T> list = new ArrayList<>();
        List<HashMap<String, Object>> mapList = baseQueryDAO.queryBySql(support.toSQL());
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
    public <R> T queryById(R id) {
        SelectSupport support = SelectSupport.builder()
                .selectFields(SqlBuildUtils.getFieldsByClass(getClazz()).toString())
                .tableName(getTableName())
                .conditions(Restriction.eq(getIdName(), id).toSQL())
                .build();
        HashMap<String, Object> hashMap = baseQueryDAO.queryUniqueBySql(support.toSQL());
        return TransformUtils.hashMapToBean(hashMap, clazz);
    }

//    /**
//     * 根据主键名查询，默认主键名为id，主键名不是id的请在entity上加IdName注解
//     * @param idList
//     * @param <R>
//     * @return
//     */
//    public <R> List<T> queryCustomFieldsByIdList(String customSelectFields, List<R> idList) {
//        SelectSupport support = SelectSupport.builder()
//                .selectFields(customSelectFields)
//                .tableName(getTableName())
//                .conditions(Restriction.in(getIdName(), idList).toSQL())
//                .build();
//        List<T> list = new ArrayList<>();
//        List<HashMap<String, Object>> mapList = baseQueryDAO.queryBySql(support.toSQL());
//        if (CollectionUtils.isNotEmpty(mapList)) {
//            list = TransformUtils.hashMapToBean(mapList, clazz);
//        }
//        return list;
//    }

//    /**
//     * 根据主键名查询，默认主键名为id，主键名不是id的请在entity上加IdName注解
//     * @param id
//     * @param <R>
//     * @return
//     */
//    public <R> T queryCustomFieldsById(String customSelectFields, R id) {
//        SelectSupport support = SelectSupport.builder()
//                .selectFields(customSelectFields)
//                .tableName(getTableName())
//                .conditions(Restriction.eq(getIdName(), id).toSQL())
//                .build();
//        HashMap<String, Object> hashMap = baseQueryDAO.queryUniqueBySql(support.toSQL());
//        return TransformUtils.hashMapToBean(hashMap, clazz);
//    }

    public PageList<T> queryBySql(JoinSelectSupport joinSelectSupport) {
        int count = baseQueryDAO.queryCountBySql(joinSelectSupport.toCountSQL());
        List<T> list = new ArrayList<>();
        if (count > 0) {
            List<HashMap<String, Object>> mapList = baseQueryDAO.queryBySql(joinSelectSupport.toSQL());
            if (CollectionUtils.isNotEmpty(mapList)) {
                list = TransformUtils.hashMapToBean(mapList, clazz);
            }
        }
        return PageList.build(list, count, joinSelectSupport.getPageParam());
    }

    public PageList<T> queryBySql(SelectSupport support) {
        int count = baseQueryDAO.queryCountBySql(support.toCountSQL());
        List<T> list = new ArrayList<>();
        if (count > 0) {
            List<HashMap<String, Object>> mapList = baseQueryDAO.queryBySql(support.toSQL());
            if (CollectionUtils.isNotEmpty(mapList)) {
                list = TransformUtils.hashMapToBean(mapList, clazz);
            }
        }
        return PageList.build(list, count, support.getPageParam());
    }

    public <R> T queryUnique(String column, R columnValue) {
        SelectSupport support = SelectSupport.builder()
                .selectFields(SqlBuildUtils.getFieldsByClass(clazz).toString())
                .tableName(getTableName())
                .conditions(Restriction.eq(column, columnValue).toSQL())
                .build();
        HashMap<String, Object> hashMap = baseQueryDAO.queryUniqueBySql(support.toSQL());
        return TransformUtils.hashMapToBean(hashMap, clazz);
    }

    public T queryUnique(SelectSupport support) {
        HashMap<String, Object> hashMap = baseQueryDAO.queryUniqueBySql(support.toSQL());
        return TransformUtils.hashMapToBean(hashMap, clazz);
    }

    public T queryUnique(JoinSelectSupport support) {
        HashMap<String, Object> hashMap = baseQueryDAO.queryUniqueBySql(support.toSQL());
        return TransformUtils.hashMapToBean(hashMap, clazz);
    }

    public <K> K querySingleValue(SelectSupport support, Class<K> singleValueClassType) {
        return baseQueryDAO.querySingleValueBySql(support.toSQL(), singleValueClassType);
    }

    public List<HashMap<String, Object>> querySpecialList(String sql) {
        return baseQueryDAO.querySpecialListBySql(sql);
    }

    public HashMap<String, Object> querySpecial(String sql) {
        return baseQueryDAO.querySpecialBySql(sql);
    }

    /**
     * 默认Id名为id，不是id的请在entity上加IdName注解
     * @return
     */
    public Integer queryMaxIdValue() {
        SelectSupport support = SelectSupport.builder()
                .selectFields(getIdName())
                .tableName(getTableName())
                .orderSupportAppender(OrderSupportAppender.newInstance().append(OrderSupport.DESC(getIdName())))
                .pageParam(PageParam.builder().pageIndex(0).pageSize(1).build())
                .build();
        Integer maxId = baseQueryDAO.querySingleValueBySql(support.toSQL(), Integer.class);
        if (null == maxId) {
            maxId = 0;
        }
        return maxId;
    }

    public int queryCount(SelectSupport support) {
        return baseQueryDAO.queryCountBySql(support.toCountSQL());
    }

    public int queryCount(String sql) {
        return baseQueryDAO.queryCountBySql(sql);
    }

}
