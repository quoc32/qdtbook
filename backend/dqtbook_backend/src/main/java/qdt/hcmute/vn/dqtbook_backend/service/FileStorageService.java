package qdt.hcmute.vn.dqtbook_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpSession;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    @Autowired
    private HttpSession session;

    /**
     * Lưu trữ file multipart vào thư mục upload đã cấu hình.
     * <p>
     * File sẽ được lưu với tiền tố UUID ngẫu nhiên để tránh trùng tên.
     * Nếu thư mục upload chưa tồn tại, sẽ tự động tạo mới.
     * </p>
     *
     * @param file file multipart cần lưu trữ
     * @return tên file đã được tạo dùng để lưu trữ
     * @throws IOException nếu xảy ra lỗi I/O trong quá trình lưu file
     */
    public String storeFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = session.getAttribute("userId") + "_" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName; // Lưu tên file để ghi vào DB
    }

    /**
     * Xóa file được chỉ định khỏi thư mục upload đã cấu hình (nếu tồn tại).
     *
     * Tên file được resolve so với thư mục upload. Nếu file không tồn tại,
     * thao tác sẽ kết thúc mà không phát sinh lỗi.
     *
     * @param fileName tên (hoặc đường dẫn tương đối) của file cần xóa trong thư mục upload
     * @throws IOException nếu xảy ra lỗi I/O trong quá trình xóa file
     */
    public void deleteFile(String fileName) throws IOException {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileName);
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Lấy danh sách tệp (Resource) của người dùng theo ID, lọc các tệp khớp mẫu "userId_*"
     * trong thư mục lưu trữ và chỉ bao gồm tệp tồn tại, đọc được. Trả về danh sách rỗng
     * nếu thư mục chưa tồn tại hoặc không có tệp phù hợp.
     *
     * @param userId ID người dùng cần truy xuất tệp
     * @return danh sách Resource các tệp của người dùng; rỗng nếu không tìm thấy
     * @throws IOException nếu xảy ra lỗi I/O khi duyệt thư mục hoặc truy cập tệp
     */
    public List<String> getFilesByUserId(String userId) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            return Collections.emptyList();
        }

        List<String> fileNames = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(uploadPath, userId + "_*")) {
            for (Path entry : stream) {
                fileNames.add(entry.getFileName().toString());
            }
        }
        return fileNames;
    }
}

