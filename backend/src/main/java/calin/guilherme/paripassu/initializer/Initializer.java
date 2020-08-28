package calin.guilherme.paripassu.initializer;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import calin.guilherme.paripassu.exception.*;
import calin.guilherme.paripassu.root.config.RootScan;
import calin.guilherme.paripassu.web.config.RestScan;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;

public class Initializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // Cria o contexto "root" da aplicação
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(RootScan.class);

        //Configura o ambiente do contexto "root"
        ConfigurableEnvironment environment = rootContext.getEnvironment();

        try{
            this.configureEnvironment(environment);
        }catch(ConfigYamlNotFoundException exception){
            throw new ServletException(exception.getMessage());
        }

        // Faz o boostrap do contexto "root"
        servletContext.addListener(new ContextLoaderListener(rootContext));

        // Cria o contexto a ser utilizado no dispatcher servlet para API Rest
        AnnotationConfigWebApplicationContext dispatcherContext =
                new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(RestScan.class);

        // Registra e configura o dispatcher servlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(dispatcherContext);

        ServletRegistration.Dynamic registration =
                servletContext.addServlet("dispatcher", dispatcherServlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/*");
        registration.setAsyncSupported(true);
    }

    private void configureEnvironment(ConfigurableEnvironment environment) throws ConfigYamlNotFoundException{
            InputStream fileStream = this.getClass().getResourceAsStream("/config.yaml");

            if(fileStream == null){
                throw new ConfigYamlNotFoundException("Arquivo config.yaml não encontrado!");
            }

            Yaml yaml = new Yaml();
            HashMap<String, Object> customProperties = yaml.load(fileStream);

            MutablePropertySources propertySources = environment.getPropertySources();
            propertySources.addFirst(new MapPropertySource("yaml-config",customProperties));

            return;
    }
}
