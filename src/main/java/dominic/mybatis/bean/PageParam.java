package dominic.mybatis.bean;

import dominic.mybatis.support.ISupport;
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
public class PageParam implements ISupport, Serializable{
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
