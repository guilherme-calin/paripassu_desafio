package calin.guilherme.paripassu.web.config;

import calin.guilherme.paripassu.root.model.Numbering;
import calin.guilherme.paripassu.root.service.NumberingDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
@ComponentScan({"calin.guilherme.paripassu.web",
        "calin.guilherme.paripassu.root.service"})
public class RestScan implements WebMvcConfigurer {
    private NumberingDAO dao;

    public RestScan(NumberingDAO dao){
        this.dao = dao;
    }

    public void configureAsyncSupport(AsyncSupportConfigurer configurer){
        configurer.setDefaultTimeout(30 * 1000);
    }





    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Css resource.
        registry.addResourceHandler("/static/js/**") //
                .addResourceLocations("classpath:/WEB-INF/classes/static/static/js").setCachePeriod(31556926);
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/WEB-INF/classes/static/static/**");
        registry.addResourceHandler("/static/css/**").addResourceLocations("classpath:classpath:/WEB-INF/classes/static/static/css/**");
        registry.addResourceHandler("/static/img/**").addResourceLocations("classpath:classpath:/WEB-INF/classes/static/static/img/**");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/**");
    }


    @Bean("pendingQueue")
    public Vector<Numbering> getPendingQueue(){
        List<Map<String, Object>> pendingList = this.dao.getPendingToServe();
        Vector<Numbering> pendingQueue = new Vector<Numbering>();

        for(Map<String, Object> tuple : pendingList){
            Long id = (Long) tuple.get("numbering_id");
            Integer numbering = (Integer) tuple.get("numbering");
            String numberingType = (String) tuple.get("numbering_type");
            Timestamp dateTimeRequest = (Timestamp) tuple.get("datetime_request");

            Numbering item = new Numbering(id, numbering, numberingType, dateTimeRequest);

            pendingQueue.add(item);
        }

        return pendingQueue;
    }

    @Bean("lastServedList")
    public Vector<Numbering> getLastServedList(){
        final int NUMBER_OF_PAST_RECORDS = 5;

        List<Map<String, Object>> pendingList = this.dao.getLastServed(NUMBER_OF_PAST_RECORDS);
        Vector<Numbering> lastServedQueue = new Vector<Numbering>(NUMBER_OF_PAST_RECORDS);

        for(Map<String, Object> tuple : pendingList){
            Long id = (Long) tuple.get("numbering_id");
            Integer numbering = (Integer) tuple.get("numbering");
            String numberingType = (String) tuple.get("numbering_type");
            Timestamp dateTimeRequest = (Timestamp) tuple.get("datetime_request");
            Timestamp dateTimeServed = (Timestamp) tuple.get("datetime_served");

            Numbering item = new Numbering(id.longValue(), numbering.intValue(), numberingType, dateTimeRequest, dateTimeServed);

            lastServedQueue.add(item);
        }
        return lastServedQueue;
    }
}
