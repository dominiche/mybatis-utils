package dominic.mybatis.bean;

import dominic.mybatis.support.SupportUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by herongxing on 2017/2/23 20:38.
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageParam implements SupportUnit, Serializable{
    private Integer pageIndex = 0;
    private Integer pageSize = 10;

    public Integer getPageStart() {
        return pageIndex*pageSize;
    }

    @Override
    public String SQL() {
        return " limit " + getPageStart() + "," + pageSize;
    }
}
