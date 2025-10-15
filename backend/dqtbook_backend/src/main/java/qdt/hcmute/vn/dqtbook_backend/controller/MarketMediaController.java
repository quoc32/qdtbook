package qdt.hcmute.vn.dqtbook_backend.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import qdt.hcmute.vn.dqtbook_backend.service.FileStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/media/market")
public class MarketMediaController {
    private final FileStorageService storageService;

    public MarketMediaController(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<?> upload(@RequestParam("files") MultipartFile[] files) {
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String saved = storageService.storeFile(file);
                urls.add("/api/media/market/files/" + saved);
            } catch (IOException e) {
                return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
            }
        }
        return ResponseEntity.ok(Map.of("urls", urls));
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> get(@PathVariable String filename) throws IOException {
        Path path = Paths.get("uploads").resolve(filename).normalize();
        Resource resource = new UrlResource(path.toUri());
        if (!resource.exists()) return ResponseEntity.notFound().build();
        String contentType = Files.probeContentType(path);
        if (contentType == null) contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
