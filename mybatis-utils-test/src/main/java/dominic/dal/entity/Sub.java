package dominic.dal.entity;

import dominic.mybatis.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * ls_sub
 * 
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("`order`")
@IdName("sub_id")
@UseUnderScoreToCamelCase
public class Sub implements Serializable {
	/**
	 * 订购id
	 */
	private Long subId;
	/**
	 * 订购用户名称
	 */
	private String userName;
	/**
	 * 用户Id
	 */
	private String userId;
	/**
	 * 订购时间
	 */
	private java.util.Date subDate;
	/**
	 * 购买时间
	 */
	private java.util.Date payDate;
	/**
	 * 订购流水号
	 */
	private String subNumber;
	/**
	 * 实际总值
	 */
	private java.math.BigDecimal actualTotal;
	/**
	 * 订单状态
	 */
	private Integer status;
	/** 订单类型 */
	@MyTransient
	private String subType;

	@HandleNull(value = false)
	private Date createTime;
}
