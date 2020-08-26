package com.biubiu;

import com.biubiu.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    public static ApplicationContext applicationContext;


    public static void main(String[] args) {
        applicationContext = SpringApplication.run(Application.class, args);
        SpringContextUtil.applicationContext = applicationContext;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        WebApplicationContext rootAppContext = this.createRootApplicationContext(servletContext);
        if (rootAppContext != null) {
            applicationContext = rootAppContext;
            servletContext.addListener(new ContextLoaderListener(rootAppContext) {
                public void contextInitialized(ServletContextEvent event) {
                }
            });
        } else {
            this.logger.debug("No ContextLoaderListener registered, as createRootApplicationContext() did not return an application context");
        }
    }

}
