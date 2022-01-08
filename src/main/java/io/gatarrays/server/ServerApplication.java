package io.gatarrays.server;

import io.gatarrays.server.enumeration.Status;
import io.gatarrays.server.model.Server;
import io.gatarrays.server.repo.ServerRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.lang.reflect.Array;
import java.util.Arrays;

@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }


    @Bean
    CommandLineRunner run(ServerRepo serverRepo) {
        return args -> {
            serverRepo.save(new Server(null, "192.168.1.160",
                    "Ubuntu Linux", "16 GB", "Personal PC",
                    "http://localhost:8080/server/image/server1.png", Status.SERVER_UP));
            serverRepo.save(new Server(null, "192.168.1.58",
                    "Fedora Linux", "16 GB", "Personal PC",
                    "http://localhost:8080/server/image/server1.png", Status.SERVER_UP));
            serverRepo.save(new Server(null, "192.168.1.13",
                    "MS Linux", "16 GB", "Personal PC",
                    "http://localhost:8080/server/image/server1.png", Status.SERVER_UP));
            serverRepo.save(new Server(null, "192.168.1.111",
                    "Red Hat Entreprise Linux", "16 GB", "Personal PC",
                    "http://localhost:8080/server/image/server1.png", Status.SERVER_UP));
        };
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:4200"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-type",
                "Accept", "Jwt-Token", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-type", "Accept", "Jwt-Token", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Filename"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE", "OPTION"));
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

}
