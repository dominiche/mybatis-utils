package dominic.dto;

import dominic.mybatis.annotation.ColumnName;
import dominic.mybatis.annotation.DatePolicy;
import dominic.mybatis.annotation.UseUnderScoreToCamelCase;
import dominic.mybatis.constants.DateRangePolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UseUnderScoreToCamelCase
public class SubEntityQueryDTO implements Serializable{

	/** 订单编号 */
	@ColumnName("sub_number")
	private Collection<String> subNumbers;
	
	/** 用户Ids */
	@ColumnName("user_id")
	private Collection<String> userIds;

	/** 用户名 */
	@ColumnName("user_name")
	private Collection<String> userNames;

	/** 支付时间（起始查询条件） */
	@DatePolicy(range = DateRangePolicy.BEGIN, column = "pay_date")
	private Date payDateStart;
	
	/** 支付时间（终止查询条件） */
	@DatePolicy(range = DateRangePolicy.END, column = "pay_date")
	private Date payDateEnd;
	
	/** 下单时间（起始查询条件） */
	@DatePolicy(range = DateRangePolicy.BEGIN, column = "sub_date")
	private Date subDateStart;
	
	/** 下单时间（终止查询条件） */
	@DatePolicy(range = DateRangePolicy.END, column = "sub_date")
	private Date subDateEnd;
	
	/**
	 * 订单状态
	 */
	private Collection<Integer> status;
	/**
	 * 订单类型
	 */
	private String subType;
}
