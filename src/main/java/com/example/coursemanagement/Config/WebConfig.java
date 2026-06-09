package com.example.coursemanagement.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

// cấu hình đọc ảnh để đưa ra màn hình
// quy trình chạy của file này: Đăng ảnh khóa học mới => lưu ảnh => lấy & hiển thị ảnh
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    // class addResourceHandlers =. cấu hình thêm các đường dẫn để đọc file tĩnh
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 1. Giữ nguyên cấu hình tài nguyên tĩnh mặc định cho hệ thống (css, js,...)
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

        // 2.  đường dẫn đến thư mục chứa ảnh trong dự án
        Path uploadDir = Paths.get("src", "main", "resources", "static", "images");

        String uploadPath = uploadDir.toAbsolutePath().toUri().toString();

        // 3. Đăng ký Resource Handler
        // Thêm cả uploadPath (đọc trực tiếp từ ổ cứng) và classpath (đọc từ bản build) để bọc lót cho nhau
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath, "classpath:/static/images/");
    }
}