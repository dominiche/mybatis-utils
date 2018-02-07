package dominic.config.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by chenwenan on 17/06/26.
 *
 */

@Configuration
@AutoConfigureAfter(DataSourceConfig.class)
@MapperScan(basePackages={"dominic.dal.dao.query", "dominic.mybatis.dao.query"},sqlSessionFactoryRef="sqlSessionFactorySlaver")
public class MyBatisSlaverConfig {

    @Bean(name = "sqlSessionFactorySlaver")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("readDatasource")DataSource dataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
//        bean.setTypeAliasesPackage("dominic.dal.entity");
        
        Properties properties = new Properties();
        properties.setProperty("useGeneratedKeys", "true");
        properties.setProperty("mapUnderscoreToCamelCase", "true");
        bean.setConfigurationProperties(properties);
        
        //添加XML目录
        try {
            //bean.setMapperLocations(resolver.getResources("classpath:mapper/read/*.xml"));
            bean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
