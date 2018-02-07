package dominic.config.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * MyBatis基础配置
 *
 * Created by chenwenan on 17/06/26.
 */
@Configuration
@AutoConfigureAfter(DataSourceConfig.class)
@MapperScan(basePackages={"dominic.dal.dao.update", "dominic.mybatis.dao.update"},sqlSessionFactoryRef="sqlSessionFactoryMaster")
public class MyBatisMasterConfig {

    @Bean(name="txManager")
    @ConditionalOnMissingBean
    public PlatformTransactionManager annotationDrivenTransactionManager(@Qualifier("writeDatasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionFactoryMaster")
    public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("writeDatasource")DataSource dataSource) {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setTypeAliasesPackage("dominic.dal.entity");
        
        Properties properties = new Properties();
        properties.setProperty("useGeneratedKeys", "true");
        properties.setProperty("mapUnderscoreToCamelCase", "true");
        bean.setConfigurationProperties(properties);
        
        
        //添加XML目录
        //ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            //bean.setMapperLocations(resolver.getResources("classpath:mapper/write/*.xml"));
            bean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
            return bean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    @Bean(name="sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactoryMaster")SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}
