package dominic.config.datasource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Created by admin on 16/12/18.
 */

@Configuration
public class DataSourceConfig {

	@Primary
	@Bean(name="writeDatasource")
	@ConfigurationProperties(prefix="spring.datasource-write")
    public DataSource writeDataSourace() throws Exception {
		return DataSourceBuilder.create().build();
    }

    @Bean(name="readDatasource")
    @ConfigurationProperties(prefix="spring.datasource-read")
    public DataSource readDataSourace1() throws Exception {
    	return DataSourceBuilder.create().build();
    }

}
