package dominic.mybatis.support;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Created by herongxing on 2017/2/28 14:54.
 */
public abstract class UpdateFieldUnit implements SupportUnit {
    /**
     * 参数的key-value
     */
    @Getter @Setter
    private Map<String, Object> paramMap = Maps.newHashMap();
}
