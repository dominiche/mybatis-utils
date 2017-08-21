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

    @Override
    public String SQL() {
        return " limit " + getPageStart() + "," + pageSize;
    }

    public Integer getPageStart() {
        return pageIndex*pageSize;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        if (null == pageIndex || pageIndex < 0) {
            pageIndex = 0;
        }
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (null == pageSize || pageSize < 0) {
            pageSize = 10;
        }
        this.pageSize = pageSize;
    }
}
