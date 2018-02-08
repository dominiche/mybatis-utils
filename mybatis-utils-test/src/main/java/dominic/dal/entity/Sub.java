package dominic.dal.entity;

import dominic.mybatis.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 注：`@Data @Builder @NoArgsConstructor @AllArgsConstructor` 这四个注解是Lombok的注解，IDE需要装相应插件支持（idea，eclipse等都有支持）
 通过Lombok能帮我们减少很多样板代码，如`@Data`可以帮我们自动生成getter/setter方法，
 同时重写toString，hashCode方法，其他的可以自行了解；`@TableName @IdName`和`@UseUnderScoreToCamelCase`是mybatis-utils的自定义注解，
 前两个分别是说entity对应的表名和主键id名，后一个是告诉mybatis-utils，表字段要使用驼峰转下划线，如字段payDate转成SQL时为pay_date
 */
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
