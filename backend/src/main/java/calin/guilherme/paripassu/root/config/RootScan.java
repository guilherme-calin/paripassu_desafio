package calin.guilherme.paripassu.root.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan("calin.guilherme.paripassu.root")
public class RootScan implements EnvironmentAware {
    private Environment rootEnvironment;

    @Bean
    public Environment rootEnvironment(){
        return this.rootEnvironment;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.rootEnvironment = environment;
    }
}
