package dominic.mybatis.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dominic.mybatis.annotation.IdName;
import dominic.mybatis.annotation.TableName;
import dominic.mybatis.bean.IdContainer;
import dominic.mybatis.constants.MybatisUtils;
import dominic.mybatis.dao.update.BaseUpdateDAO;
import dominic.mybatis.support.Restriction;
import dominic.mybatis.support.RestrictionUnit;
import dominic.mybatis.support.UpdateField;
import dominic.mybatis.support.UpdateFieldUnit;
import dominic.mybatis.support.build.UpdateSupport;
import dominic.mybatis.utils.RestrictionsUtils;
import dominic.mybatis.utils.SqlBuildUtils;
import dominic.mybatis.utils.UpdateFieldsUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by herongxing on 2017/2/27 16:54.
 */
@Slf4j
public abstract class AbstractUpdateService<T> {
    @Autowired
    private BaseUpdateDAO baseUpdateDAO;

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
     * 主键支持泛型
     * 主键名不是id的请在entity上加IdName注解
     * @param id
     * @param <R>
     * @return
     */
    public <R extends Number> int updateById(UpdateFieldUnit updateFieldUnit, R id) {
        SQL sql = new SQL();
        sql.UPDATE(getTableName())
                .SET(updateFieldUnit.SQL())
                .WHERE(getIdName() + "=" + MybatisUtils.segment(MybatisUtils.ID_NAME));
        Map<String, Object> map = getParamMap(sql.toString());
        map.putAll(updateFieldUnit.getParamMap());
        map.put(MybatisUtils.ID_NAME, id);
        return baseUpdateDAO.update(map);
    }

    public <R extends Number> int updateById(UpdateFieldUnit updateFieldUnit, Collection<R> ids) {
        SQL sql = new SQL();
        Restriction restriction = Restriction.in(getIdName(), ids);
        sql.UPDATE(getTableName())
                .SET(updateFieldUnit.SQL())
                .WHERE(restriction.SQL());
        Map<String, Object> map = getParamMap(sql.toString());
        map.putAll(restriction.getParamMap());
        return baseUpdateDAO.update(map);
    }

    @Deprecated
    public <R extends Number> int updateById(String updateFields, R id) {
        UpdateFieldUnit updateField = UpdateField.setBySql(updateFields);
        return updateById(updateField, id);
    }

    @Deprecated
    public <R extends Number> int updateById(String updateFields, Collection<R> ids) {
        UpdateFieldUnit updateField = UpdateField.setBySql(updateFields);
        return updateById(updateField, ids);
    }


    private Map<String, Object> getParamMap(String sql) {
        Map<String, Object> map = Maps.newHashMap();
        map.put(MybatisUtils.SQL, sql);
        return map;
    }


    @Deprecated
    public int update(UpdateSupport updateSupport) {
        return baseUpdateDAO.updateBySQL(updateSupport.SQL());
    }
    @Deprecated
    public int update(String updateSql) {
        return baseUpdateDAO.updateBySQL(updateSql);
    }

    public int update(UpdateFieldUnit updateFieldUnit, RestrictionUnit restrictions) {
        UpdateSupport updateSupport = UpdateSupport.builder()
                .updateFields(updateFieldUnit.SQL())
                .tableName(getTableName())
                .conditions(restrictions.SQL())
                .build();
        Map<String, Object> map = getParamMap(updateSupport.toString());
        map.putAll(updateFieldUnit.getParamMap());
        map.putAll(restrictions.getParamMap());
        return baseUpdateDAO.update(map);
    }

    public <U> int update(U bean, RestrictionUnit restrictions) {
        return update(UpdateFieldsUtils.buildUpdateFields(bean), restrictions);
    }

    public <U, Q> int updateByQueryBean(U bean, Q conditionBean) {
        return update(UpdateFieldsUtils.buildUpdateFields(bean), RestrictionsUtils.buildConditions(conditionBean));
    }


    /**
     * 会回填id
     * @param t
     * @return
     */
    public int save(T t) {
        IdContainer idContainer = new IdContainer();
        int result = baseUpdateDAO.save(t, idContainer, getIdName(), getTableName());
        try {
            Field idField = t.getClass().getDeclaredField(SqlBuildUtils.doUnderscoreToCamelCase(getIdName()));
            idField.setAccessible(true);
            if (idField.getGenericType().toString().equals(
                    "class java.lang.Integer")) {
                idField.set(t, idContainer.getId().intValue());
            } else {
                idField.set(t, idContainer.getId());
            }
        } catch (NoSuchFieldException e) {
            log.error(t + " has no field:" + getIdName(), e);
        } catch (IllegalAccessException e) {
            log.error("IllegalAccessException:", e);
        }
        return result;
    }

    /**
     * 不会回填id
     * @param t
     * @return
     */
    public int insert(T t) {
        return baseUpdateDAO.insert(t, getTableName());
    }

    /**
     * 会回填id
     * @param tList
     * @return
     */
    public int save(List<T> tList) {
        int result = 0;
        for (T t : tList) {
            result += save(t);
        }
        return result;
    }

    /**
     * 不回填id
     */
    public int insert(List<T> sourceList) {
        if (CollectionUtils.isEmpty(sourceList)) {
            return 0;
        }

        int result = 0;
        List<List<T>> partitions = Lists.partition(sourceList, 1000);
        for (List<T> list : partitions) {
            Map<String, Object> map = Maps.newHashMap();
            map.put(MybatisUtils.TABLE_NAME, getTableName());
            map.put(MybatisUtils.BEAN_NAME, list);
            for (int i=0; i<list.size(); ++i) {
                map.put(MybatisUtils.wrapBeanName(i), list.get(i));
            }
            result += baseUpdateDAO.insertList(map);
        }
        return result;
    }

    /**
     * 不回填id
     * @param tList
     * @see AbstractUpdateService#insert(java.util.List)
     */
    @Deprecated
    public int insertList(List<T> tList) {
        int result = 0;
        for (T t : tList) {
            result += insert(t);
        }
        return result;
    }
}
