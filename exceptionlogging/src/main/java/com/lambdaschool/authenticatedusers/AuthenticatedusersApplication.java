package com.lambdaschool.authenticatedusers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc //Not good to use unless you want to do your own error handling for invalid endpoints
@EnableJpaAuditing
@SpringBootApplication
public class AuthenticatedusersApplication
{

    public static void main(String[] args)
    {
        ApplicationContext ctx = SpringApplication.run(AuthenticatedusersApplication.class, args);

        // finds out which endpoint goes to a method
        DispatcherServlet dispatcherServlet = (DispatcherServlet) ctx.getBean("dispatcherServlet");
                            //has to be spelled                                 //the same

        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true); // setting our own no handler found
    }
}
