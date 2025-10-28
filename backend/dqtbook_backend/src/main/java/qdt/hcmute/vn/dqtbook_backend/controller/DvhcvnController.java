package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class DvhcvnController {

    // Serve the large dvhcvn.json placed at the project root (next to src)
    @GetMapping(path = "/dvhcvn.json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> serveDvhcvn() {
        try {
            Path p = Paths.get("dvhcvn.json").toAbsolutePath();
            if (!Files.exists(p) || !Files.isRegularFile(p)) {
                System.out.println("DvhcvnController: dvhcvn.json not found at " + p);
                return ResponseEntity.notFound().build();
            }
            FileSystemResource resource = new FileSystemResource(p.toFile());
            System.out.println("DvhcvnController: serving dvhcvn.json from " + p);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
