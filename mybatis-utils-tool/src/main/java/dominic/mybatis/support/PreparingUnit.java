package dominic.mybatis.support;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by Administrator:herongxing on 2018/2/1 11:01.
 */
public abstract class PreparingUnit implements SupportUnit {
    /**
     * 参数的key-value
     */
    @Getter
    @Setter
    private Map<String, Object> paramMap = Maps.newHashMap();

    public String directSQL() {
        return SupportUnit.directSQL(SQL(), this.paramMap);
    }
}
