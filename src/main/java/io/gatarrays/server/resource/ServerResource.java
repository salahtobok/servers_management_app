package io.gatarrays.server.resource;


import io.gatarrays.server.enumeration.Status;
import io.gatarrays.server.model.Response;
import io.gatarrays.server.model.Server;
import io.gatarrays.server.service.ServerService;
import io.gatarrays.server.service.implementation.ServerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.gatarrays.server.enumeration.Status.SERVER_UP;
import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/server")
@RequiredArgsConstructor
public class ServerResource {
    private final ServerServiceImpl serverService;

    @GetMapping("/list")
    public ResponseEntity<Response> getServers() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
//        throw new RuntimeException("Something went wrong");
        return ResponseEntity.ok(Response.builder()
                .localDateTime(now())
                .data(of("servers", serverService.list(30)))
                .message("Servers retrieved")
                .status(OK)
                .statusCode(OK.value())
                .build());
    }

    @GetMapping("/ping/{ipAddress}")
    public ResponseEntity<Response> pingServer(@PathVariable("ipAddress") String ipAddress) throws IOException {
        Server server = serverService.ping(ipAddress);
        return ResponseEntity.ok(Response.builder()
                .localDateTime(now())
                .data(of("server", server))
                .message(server.getStatus() == SERVER_UP ? "Ping success" : "Ping failed")
                .status(OK)
                .statusCode(OK.value())
                .build());
    }


    @PostMapping("/save")
    public ResponseEntity<Response> saveServer(@RequestBody @Valid Server server) {
        return ResponseEntity.ok(Response.builder()
                .localDateTime(now())
                .data(of("server", serverService.create(server)))
                .message("Server created")
                .status(CREATED)
                .statusCode(CREATED.value())
                .build());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Response> getServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(Response.builder()
                .localDateTime(now())
                .data(of("server", serverService.get(id)))
                .message("Server retrieved")
                .status(OK)
                .statusCode(OK.value())
                .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response> deleteServer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(Response.builder()
                .localDateTime(now())
                .data(of("deleted", serverService.delete(id)))
                .message("Server deleted")
                .status(OK)
                .statusCode(OK.value())
                .build());
    }

    @GetMapping(path = "/image/{fileName}", produces = IMAGE_PNG_VALUE)
    public byte[] getServerImage(@PathVariable("fileName") String fileName) throws IOException, URISyntaxException {
//        return Files.readAllBytes(Paths.get(System.getProperty("user.home")+"Download/images/"+fileName));
        return Files.readAllBytes(Paths.get(ServerResource.class.getClassLoader().getResource("static/images/servers/" + fileName).toURI()));
    }
}
