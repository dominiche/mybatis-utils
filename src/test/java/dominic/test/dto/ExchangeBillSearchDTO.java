package dominic.test.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

/**
 * Created by Administrator:herongxing on 2017/6/27 15:34.
 */
@Data
public class ExchangeBillSearchDTO {

    /**
     * 订单号
     */
    private Set<String> subNumbers;
    /**
     * 订单项编号
     */
    private Set<String> subItemNumbers;
    /**
     * 换货单号
     */
    private Set<String> exchangeNumbers;
    /**
     * 供应商名字
     */
    private String supplierName;
    /**
     * 申请换货单时间：开始区间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyExchangeStartDate;
    /**
     * 申请换货单时间：结束区间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date applyExchangeEndDate;
    /**
     * 确认换货时间：开始区间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishExchangeStartDate;
    /**
     * 确认换货时间：结束区间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishExchangeEndDate;
    /**
     * 状态：1-待审核，2-待换货，3-审核失败，4-已完成
     */
    private Integer status;
}
