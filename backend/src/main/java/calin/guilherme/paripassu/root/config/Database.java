package calin.guilherme.paripassu.root.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import calin.guilherme.paripassu.exception.DataSourcePropertiesNotFoundException;

@Configuration
public class Database {
    private Environment environment;

    public Database(Environment environment){
        this.environment = environment;
    }

    @Bean
    public DataSource getDataSource() throws DataSourcePropertiesNotFoundException {
        String host = this.environment.getProperty("datasource.host");
        String port = this.environment.getProperty("datasource.port");
        String user = this.environment.getProperty("datasource.user");
        String password = this.environment.getProperty("datasource.password");
        String dbname = this.environment.getProperty("datasource.dbname");

        if(host == null || port == null || user == null || password == null || dbname == null){
            throw new DataSourcePropertiesNotFoundException("Uma ou mais propriedades do datasource não puderam ser lidas");
        }

        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setDriverClassName("org.postgresql.Driver");
        ds.setUrl("jdbc:postgresql://" + host + ":" + port + "/" + dbname);
        ds.setUsername(user);
        ds.setPassword(password);

        return ds;
    }

    @Bean
    @DependsOn("getDataSource")
    public JdbcTemplate getJdbcTemplate(){
        JdbcTemplate template;
        try{
            template = new JdbcTemplate(this.getDataSource());
        }catch(DataSourcePropertiesNotFoundException exception){
            System.err.println(exception.getStackTrace());
            template = null;
        }

        return template;
    }

    @Bean
    @DependsOn("getDataSource")
    public SimpleJdbcInsert getSimpleJdbcInsert(){
        SimpleJdbcInsert simpleInsert;
        try{
            simpleInsert = new SimpleJdbcInsert(this.getDataSource());
        }catch(DataSourcePropertiesNotFoundException exception){
            System.err.println(exception.getStackTrace());
            simpleInsert = null;
        }

        return simpleInsert;
    }

    @Bean
    @DependsOn("getDataSource")
    public SimpleJdbcCall getSimpleJdbcCall(){
        SimpleJdbcCall simpleCall;
        try{
            simpleCall = new SimpleJdbcCall(this.getDataSource());
        }catch(DataSourcePropertiesNotFoundException exception){
            System.out.println("**************************");
            System.out.println(exception.getStackTrace());
            simpleCall = null;
        }

        return simpleCall;
    }
}
