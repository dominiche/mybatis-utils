package dominic.mybatis.service;

import dominic.mybatis.annotation.IdName;
import dominic.mybatis.annotation.TableName;
import dominic.mybatis.bean.IdContainer;
import dominic.mybatis.dao.BaseUpdateDAO;
import dominic.mybatis.support.UpdateField;
import dominic.mybatis.support.UpdateSupport;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

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
            throw new RuntimeException("还没有指定表名！请在entity上加TableName注解。entity: " + clazz.getName() );
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
     * @param updateField
     * @param id
     * @param <R>
     * @return
     */
    public <R> int updateById(UpdateField updateField, R id) {
        SQL sql = new SQL();
        sql.UPDATE(getTableName())
                .SET(updateField.getField())
                .WHERE(getIdName() + "=" + id);
        return baseUpdateDAO.update(sql.toString());
    }

    public <R> int updateById(String updateFields, R id) {
        String sql = "UPDATE " + getTableName() + " SET " + updateFields + " where " + getIdName() + "=" + id;
        return baseUpdateDAO.update(sql);
    }

    public int update(UpdateSupport updateSupport) {
        return baseUpdateDAO.update(updateSupport.toSQL());
    }

    public int update(String updateSql) {
        return baseUpdateDAO.update(updateSql);
    }

    /**
     * 会回填id
     * @param t
     * @return
     */
    public int save(T t) {
        IdContainer idContainer = new IdContainer();
        int result = baseUpdateDAO.insert(t, idContainer, getIdName(), getTableName());
        try {
            Field idField = t.getClass().getDeclaredField(getIdName());
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
        return baseUpdateDAO.insertWithId(t, getTableName());
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
     * 不会回填id
     * @param tList
     * @return
     */
    public int insertList(List<T> tList) {
        int result = 0;
        for (T t : tList) {
            result += insert(t);
        }
        return result;
    }

    //todo 一个批量insert list的方法
}
