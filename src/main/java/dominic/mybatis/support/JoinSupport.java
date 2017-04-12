package dominic.mybatis.support;

import lombok.Data;

/**
 * Created by herongxing on 2017/2/28 15:08.
 */
@Data
public class JoinSupport implements ISupport {
    public static final String INNER_JOIN = "INNER JOIN";
    public static final String LEFT_JOIN = "LEFT JOIN";
    public static final String RIGHT_JOIN = "RIGHT JOIN";

    /**
     * join类型
     */
    private String joinType;
    /**
     * 表名
     */
    private String tableName;
    /**
     * 表别名
     */
    private String alias;
    /**
     * join条件
     */
    private String onCondition;

    public static JoinSupport INNER_JOIN(String tableName, String alias, String onCondition) {
        return buildJoinSupport(JoinSupport.INNER_JOIN, tableName, alias, onCondition);
    }

    public static JoinSupport LEFT_JOIN(String tableName, String alias, String onCondition) {
        return buildJoinSupport(JoinSupport.LEFT_JOIN, tableName, alias, onCondition);
    }

    public static JoinSupport RIGHT_JOIN(String tableName, String alias, String onCondition) {
        return buildJoinSupport(JoinSupport.RIGHT_JOIN, tableName, alias, onCondition);
    }


    private static JoinSupport buildJoinSupport(String joinType, String tableName, String alias, String onCondition) {
        JoinSupport support = new JoinSupport();
        support.setJoinType(joinType);
        support.setTableName(tableName);
        support.setAlias(alias);
        support.setOnCondition(onCondition);
        return support;
    }

    @Override
    public String SQL() {
        return joinType + " " + tableName + " AS " + alias + " on " + onCondition;
    }
}
