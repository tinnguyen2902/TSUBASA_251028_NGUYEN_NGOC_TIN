package com.example.coursemanagement.Service;

import com.example.coursemanagement.Model.Entity.Course;
import com.example.coursemanagement.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
// xử lý logic nghiệp vụ
@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    // lâsy all khóa hc
    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }
    // thêm khóa học mới
    public Course createCourse(Course course){
        return courseRepository.save(course);
    }
   // xóa khóa hc
    public void deleteCourse(Long id){
        courseRepository.deleteById(id);
    }
   // tìm kiếm theo id
    public Optional<Course> fintById(Long id){
        return courseRepository.findById(id);
    }

}