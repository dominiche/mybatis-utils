package dominic.dto;

import dominic.mybatis.annotation.HandleNull;
import dominic.mybatis.annotation.UseUnderScoreToCamelCase;
import dominic.mybatis.constants.HandleNullScope;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UseUnderScoreToCamelCase
public class SubEntityUpdateDTO implements Serializable{

	private String subNumber;

	private String userId;

	private String userName;

	@HandleNull(scope = HandleNullScope.UPDATE)
	//对update处理null,即如果dto中payDate为null，则update时会set为null
	/**此处只是为了展示该功能，使用时请谨慎使用
	 * @see HandleNull*/
	private Date payDate;

	private Date subDate;

	private Integer status;
}
