package dominic.mybatis.constants;

import lombok.Getter;

/**
 * Created by Administrator:herongxing on 2018/1/10 9:38.
 */
public enum HandleNullScope {

    INSERT("INSERT"),
    UPDATE("UPDATE"),
//    CONDITION("CONDITION"), //todo where condition
    ALL("ALL");

    @Getter
    private String scope;

    HandleNullScope(String scope) {
        this.scope = scope;
    }
}
