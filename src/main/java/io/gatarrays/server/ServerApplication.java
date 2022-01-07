package io.gatarrays.server;

import io.gatarrays.server.enumeration.Status;
import io.gatarrays.server.model.Server;
import io.gatarrays.server.repo.ServerRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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

}
