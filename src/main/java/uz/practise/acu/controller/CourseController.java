package uz.practise.acu.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.practise.acu.domain.request.CourseRequest;
import uz.practise.acu.domain.response.BaseResponse;
import uz.practise.acu.domain.response.CourseResponse;
import uz.practise.acu.service.CourseService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/upload")
    public BaseResponse<CourseResponse> upload(
            HttpSession session,
            @ModelAttribute CourseRequest courseRequest,
            @Param("file") MultipartFile file) {

        UUID userId = (UUID) session.getAttribute("userId");
        try {
            return courseService.uploadVideo(file, courseRequest, userId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/download")
    public BaseResponse<CourseResponse> download(@Param("id") UUID courseId) {
        try {
            return courseService.download(courseId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/update")
    public BaseResponse<CourseResponse> update(@ModelAttribute CourseRequest courseRequest, UUID courseId) {
        return courseService.update(courseRequest, courseId);
    }

    @GetMapping("/push-like")
    public BaseResponse<CourseResponse> pushLike(@Param("id") UUID courseId) {
        return courseService.pushLike(courseId);
    }

    @PatchMapping("/commenting")
    public BaseResponse<CourseResponse> commenting(@Param("courseId") UUID courseId,
                                                   @Param("comment") String comment) {
        return courseService.commenting(courseId, comment);
    }

    @DeleteMapping("/delete")
    public BaseResponse<CourseResponse> delete(@Param("courseId") UUID courseId) {
        return courseService.delete(courseId);
    }

    @GetMapping("/get-saved-courses")
    public BaseResponse<List<CourseResponse>> getSavedCourses(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("userId");
        try {
            return courseService.getSavedCourses(userId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/get-all")
    public BaseResponse<List<CourseResponse>> getAllCourses() {
        try {
            return courseService.getAll();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
