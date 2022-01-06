package io.gatarrays.server.repo;

import io.gatarrays.server.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepo extends JpaRepository<Server,Long> {
    Server findByIpAddress(String ipAddress);
    Server findByName(String name);
}
