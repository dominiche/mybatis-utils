package dominic.mybatis.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * <p>
 *  原始参数 : home; select * from ls_user; DROP table ls_user;
 * <p>
 *  过滤后参数: home; * ls_user; table ls_user;
 * <p>
 * 	if(StringUtils.isNotBlank(queryParam.getUserName())){<br>
 *      sql.append(" AND ud.user_name like '%" + SQLFilter.filterScript(queryParam.getUserName().trim()) + "%' ");<br>
 *  }
 * 
 * 
 * @author chenwenan
 * @since 2017.06.28
 * 
 */

@Slf4j
public class SQLFilter {
    public static String filterScript(String value) {

        String filterStr = value;

        //xss过滤
        filterStr = filterStr.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        filterStr = filterStr.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        filterStr = filterStr.replaceAll("'", "&#39;");
        filterStr = filterStr.replaceAll("eval\\((.*)\\)", "");
        filterStr = filterStr.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        filterStr = filterStr.replaceAll("script", "");

        //sql注入过滤
        StringBuilder sqlPattern = new StringBuilder("(?i)(from\\s|like\\s|having\\s|intersect\\s|merge\\s|minus\\s|exists\\s|union\\s|where\\s|session\\s|order\\s|group\\s|um_user\\s|or\\s|and\\s|grant\\s)");
        sqlPattern = sqlPattern.append("|insert\\s|delete\\s|update\\s|alter\\s|create\\s|drop\\s|exec\\s|execute\\s|select\\s");
        sqlPattern = sqlPattern.append("|user_users\\s|user_tables\\s|user_all_tables\\s|user_objects\\s|user_indexes\\s|user_ind_columns\\s|user_tab_columns\\s|user_tab_cols\\s|user_synonyms\\s|user_constraints\\s|user_views\\s|user_jobs\\s");
        sqlPattern = sqlPattern.append("|dba_tables\\s|dba_indexes\\s|dba_ind_columns\\s|dba_users\\s|dba_tab_cols\\s|dba_tab_columns\\s|dba_synonyms\\s|dba_constraints\\s|dba_views\\s|dba_objects\\s|dba_all_tables\\s|dba_jobs\\s");
        sqlPattern = sqlPattern.append("|all_indexes\\s|all_all_tables\\s|all_ind_columns\\s|all_tab_cols\\s|all_tab_columns\\s|all_synonyms\\s|all_constraints\\s|all_views\\s|all_objects\\s|all_users\\s|all_jobs\\s");
        
        
        filterStr = filterStr.replaceAll(sqlPattern.toString(), "");

        if (!filterStr.equalsIgnoreCase(value) && log.isDebugEnabled()) {
            log.warn("SQLFilter : " + value + "' replace by '" + filterStr + "'");
        }

        return filterStr;
    }
}
