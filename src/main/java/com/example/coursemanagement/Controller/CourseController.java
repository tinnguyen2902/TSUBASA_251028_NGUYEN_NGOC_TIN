package com.example.coursemanagement.Controller;

import com.example.coursemanagement.Model.Entity.Course;
import com.example.coursemanagement.Service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.coursemanagement.Repository.CourseRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

// xử lý
@Controller
@RequestMapping("course")
public class CourseController {

   @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepository courseRepository;

    //vừa hiển thị vừa tìm kiếm
   @GetMapping("")
   public String loadCourseManagerPage(@RequestParam(value = "keyword", required = false) String keyword,
                                       @RequestParam(value = "page",defaultValue = "0") int page,
                                       Model model) {
        int pageSize = 5;  // giới hạn 5 khóa hc 1 trang
       Pageable pageable = PageRequest.of(page,pageSize);
       Page<Course> coursePage;
       if (keyword != null && !keyword.isEmpty()){
           coursePage =  courseRepository.findByCourseNameContainingIgnoreCaseOrInstructorContainingIgnoreCase(keyword,keyword,pageable);
       } else {
           coursePage = courseRepository.findAll(pageable);
       }
       // đưa data ra giao diện
       // courses => gửi danh sách khóa học ra giao diện
       model.addAttribute("courses", coursePage.getContent());
       // currentPage => số thứ tự trang hiện tại
       model.addAttribute("currentPage", page);
       // totalPage => tổng số trang, mỗi trang giới hạn 5 khóa học nên khóa học thứ 6,11,... trở đi thì tổng số trang tăng
       model.addAttribute("totalPages", coursePage.getTotalPages());
       // người dùng nhập vào ô tìm kiếm và trả về kết quả theo key
       model.addAttribute("keyword", keyword);

       return "Manager";
   }
    // thêm mới => 2 bước :
      //1 hiển thị form thêm mới
    @GetMapping("/add")
    // truyền vào model
    public String showAddForm(Model model) {
        model.addAttribute("newCourse", new Course());
        return "add";
    }
      //2 xử lý lưu khóa hc thêm mới + cập nhật
    // b1: lấy ra thông tin dữ liệu @ModelAttribute
    @PostMapping("/add")
    public String saveCourse(@ModelAttribute("newCourse") Course course,
                             @RequestParam("imageFile") MultipartFile imageFile) {
        handleImageUpload(course, imageFile);
        courseService.createCourse(course);
        return "redirect:/course";
    }
    //lấy thông tin từ file sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Course course = courseService.fintById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học với ID: " + id));
        model.addAttribute("editCourse", course);
        return "edit";
    }
    // khi ấn lưu
    @PostMapping("/edit")
    public String updateCourse(@ModelAttribute("editCourse") Course course,
                               @RequestParam("imageFile") MultipartFile imageFile) {

        // Lấy lại dữ liệu gốc từ DB để giữ lại ảnh cũ nếu người dùng không chọn ảnh mới khi sửa
        Course existingCourse = courseService.fintById(course.getId()).orElse(null);

        if (!imageFile.isEmpty()) {
            // Nếu người dùng upload ảnh mới -> xử lý lưu ảnh mới
            handleImageUpload(course, imageFile);
        } else if (existingCourse != null) {
            // Nếu không upload ảnh mới -> giữ nguyên đường dẫn ảnh cũ
            course.setThumbnail(existingCourse.getThumbnail());
            handleImageUpload(course, imageFile);
        }

        // Thực hiện lưu đè (Update) xuống database
        courseService.createCourse(course);
        return "redirect:/course";
    }
    //Xóa
    @PostMapping("/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long id) {
        try {
            courseService.deleteCourse(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/course";
    }
    //hàm upload file ảnh

    private void handleImageUpload(Course course, MultipartFile imageFile) {
       // check ng dùng có tải lên hay không
        if (!imageFile.isEmpty()) {
            try {
                // nơi lưu ảnh
                Path uploadPath = Paths.get("src","main","resources","static","images");
               // tự động tạo ảnh mới
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                // tạo tên file
                String uniqueFileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
                Path filePath = uploadPath.resolve(uniqueFileName);
                //copy file ảnh vào thư mục đích
                Files.copy(imageFile.getInputStream(), filePath);
                // lưu vào database
                course.setThumbnail(uniqueFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // nếu không up gì thì cho ảnh mặc định
        } else if (course.getThumbnail() == null || course.getThumbnail().trim().isEmpty()) {
            course.setThumbnail("https://csc.edu.vn/data/images/tin-tuc/lap-trinh-csdl/kien-thuc-lap-trinh/hoc-lap-trinh-bat-dau-tu-dau(2).jpg");
        }
    }

}