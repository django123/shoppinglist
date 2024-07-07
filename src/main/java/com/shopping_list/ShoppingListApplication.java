package com.shopping_list;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;


@SpringBootApplication
public class ShoppingListApplication {


    private static final Logger LOG = LoggerFactory.getLogger(ShoppingListApplication.class);
    private static  final String HTTP_DEFAULT_PORT = "8080";

    public static void main(final String [] args)throws UnknownHostException {
        final Environment env = SpringApplication.run(ShoppingListApplication.class, args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(final Environment env)throws UnknownHostException{
        String protocol ="http";
        if (env.getProperty("server.ssl.key-store") != null){
            protocol = "https";
        }
        final String serverPort = Optional.ofNullable(env.getProperty("server.port")).orElse(HTTP_DEFAULT_PORT);
        String contextPath = env.getProperty("server.servlet.context-path");
        if (!StringUtils.hasText(contextPath)){
            contextPath = "/";
        }

        final String hostAddress = InetAddress.getLocalHost().getHostAddress();

        LOG.info("\n---------------------------------------------------------------------\n\t" //
                        +"Application '{} ({})' is running!\n\tAccess URLs:\n\t "//
                        +"Local: \t\t{}: //localhost:{}{}\n\t"//
                        +"External: \t{}: //{}:{}{}\n\t"//
                        +"Profile(s): \t{}: \n-------------------------------------------------------------", //
                env.getProperty("spring.application.name"), env.getProperty("application.version"), //
                protocol, serverPort, contextPath, protocol, hostAddress, //
                serverPort, contextPath, env.getActiveProfiles());
    }

}
