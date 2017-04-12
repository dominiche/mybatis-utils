package dominic.mybatis.support;

import lombok.Data;

/**
 * Created by herongxing on 2017/2/28 14:26.
 */
@Data
public class TableSupport implements ISupport {
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表别名
     */
    private String alias;

    public static TableSupport newInstance(String tableName, String alias) {
        TableSupport tableSupport = new TableSupport();
        tableSupport.setTableName(tableName);
        tableSupport.setAlias(alias);
        return tableSupport;
    }

    @Override
    public String SQL() {
        return tableName + " AS " + alias;
    }
}
