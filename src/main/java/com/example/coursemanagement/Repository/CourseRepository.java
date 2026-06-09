package com.example.coursemanagement.Repository;

import com.example.coursemanagement.Model.Entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
//    Page<Course> findByCourseNameContainingIgnoreCaseOrInstructorContainingIgnoreCase(
//            String courseName,
//            String instructor,
//            Pageable pageable);
    Page<Course> findByCourseName(String name, Pageable pageable);
}