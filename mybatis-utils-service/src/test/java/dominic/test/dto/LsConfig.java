package dominic.test.dto;

import dominic.mybatis.annotation.IdName;
import dominic.mybatis.annotation.InsertNull;
import dominic.mybatis.annotation.TableName;
import dominic.mybatis.annotation.UseUnderScoreToCamelCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 
 * ls_config
 * 
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("ls_config")
@IdName("id")
@UseUnderScoreToCamelCase
public class LsConfig implements Serializable {

	@InsertNull
	private Integer id;

	private String name;
	/**
	 * 描述
	 */
	private String description;

	private String key;

	private String value;

}
