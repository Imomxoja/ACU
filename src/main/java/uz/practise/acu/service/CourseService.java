package uz.practise.acu.service;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.practise.acu.domain.entity.CourseEntity;
import uz.practise.acu.domain.entity.mapper.CourseEntityMapper;
import uz.practise.acu.domain.entity.user.UserEntity;
import uz.practise.acu.domain.request.CourseRequest;
import uz.practise.acu.domain.response.BaseResponse;
import uz.practise.acu.domain.response.CourseResponse;
import uz.practise.acu.repository.CourseRepository;
import uz.practise.acu.repository.UserRepository;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseEntityMapper mapper;

    private static final String BUCKET_NAME = "acu_project";
    private static final String PROJECT_ID = "acuproject-435313";
    private static final String SERVICE_ACCOUNT_KEY_PATH = "C:/Users/HP/Downloads/acuproject-435313-f176782b1965.json";

    public BaseResponse<CourseResponse> uploadVideo(MultipartFile file, CourseRequest courseRequest, UUID userId) throws IOException {
        Optional<UserEntity> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            return BaseResponse.<CourseResponse>builder()
                    .message("User not found")
                    .status(400)
                    .build();
        }

        Storage storage = StorageOptions.newBuilder()
                .setProjectId(PROJECT_ID)
                .setCredentials(ServiceAccountCredentials.fromStream(
                        new FileInputStream(SERVICE_ACCOUNT_KEY_PATH)))
                .build()
                .getService();


        courseRequest.setFileName(file.getOriginalFilename());
        CourseEntity course = courseRepository.save(mapper.toEntity(courseRequest));
        BlobId blobId = BlobId.of(BUCKET_NAME, course.getId().toString());
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());

        return BaseResponse.<CourseResponse>builder()
                .status(200)
                .data(mapper.toResponse(course))
                .message("File successfully uploaded")
                .build();
    }


    public BaseResponse<CourseResponse> download(UUID courseId) throws IOException {
        Optional<CourseEntity> course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            return BaseResponse.<CourseResponse>builder()
                    .message("Course not found")
                    .status(400)
                    .build();
        }

        Storage storage = getStorage();
        Blob blob = storage.get(BUCKET_NAME, course.get().getId().toString());

        if (blob == null) {
            return BaseResponse.<CourseResponse>builder()
                    .message("File not found")
                    .status(400)
                    .build();
        }

        byte[] content = blob.getContent();
        course.get().setContent(content);

        return BaseResponse.<CourseResponse>builder()
                .data(mapper.toResponse(course.get()))
                .message("File successfully downloaded")
                .status(200)
                .build();
    }

    private Storage getStorage() throws IOException {
        return StorageOptions.newBuilder()
                .setProjectId(PROJECT_ID)
                .setCredentials(ServiceAccountCredentials.fromStream(
                        Files.newInputStream(Paths.get(SERVICE_ACCOUNT_KEY_PATH))))
                .build()
                .getService();
    }


    public BaseResponse<CourseResponse> update(CourseRequest courseRequest, UUID courseId) {
        Optional<CourseEntity> course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            return BaseResponse.<CourseResponse>builder()
                    .message("Course not found")
                    .status(400)
                    .build();
        }

        if (courseRequest.getDescription() != null) {
            course.get().setDescription(courseRequest.getDescription());
        }
        if (courseRequest.getName() != null) {
            course.get().setName(courseRequest.getName());
        }
        if (courseRequest.getPrice() != null) {
            course.get().setPrice(courseRequest.getPrice());
        }

        CourseEntity entity = courseRepository.save(course.get());

        return BaseResponse.<CourseResponse>builder()
                .message("Course successfully updated")
                .data(mapper.toResponse(entity))
                .status(200)
                .build();
    }

    public BaseResponse<CourseResponse> pushLike(UUID courseId) {
        Optional<CourseEntity> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            return BaseResponse.<CourseResponse>builder()
                    .message("Course not found")
                    .status(400)
                    .build();
        }

        course.get().getRanking().setLikes(course.get().getRanking().getLikes() + 1);
        CourseEntity saved = courseRepository.save(course.get());
        return BaseResponse.<CourseResponse>builder()
                .data(mapper.toResponse(saved))
                .status(200)
                .build();
    }

    public BaseResponse<CourseResponse> commenting(UUID courseId, String comment) {
        Optional<CourseEntity> course = courseRepository.findById(courseId);
        if (course.isEmpty()) {
            return BaseResponse.<CourseResponse>builder()
                    .message("Course not found")
                    .status(400)
                    .build();
        }

        course.get().getRanking().getComments().add(comment);
        CourseEntity saved = courseRepository.save(course.get());

        return BaseResponse.<CourseResponse>builder()
                .status(200)
                .data(mapper.toResponse(saved))
                .build();
    }

    public BaseResponse<CourseResponse> delete(UUID courseId) {
        Optional<CourseEntity> course = courseRepository.findById(courseId);

        if (course.isEmpty()) {
            return BaseResponse.<CourseResponse>builder()
                    .message("Course not found")
                    .status(400)
                    .build();
        }
        course.get().setNotDeleted(false);
        courseRepository.save(course.get());
        return BaseResponse.<CourseResponse>builder()
                .message("Course successfully deleted")
                .status(200)
                .build();
    }

    public BaseResponse<List<CourseResponse>> getSavedCourses(UUID userId) throws IOException {
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return BaseResponse.<List<CourseResponse>>builder()
                    .message("User not found")
                    .status(400)
                    .build();
        }

        Storage storage = getStorage();
        Bucket bucket = storage.get(BUCKET_NAME);

        List<CourseResponse> res = new ArrayList<>();
        for (Blob blob : bucket.list().iterateAll()) {
            if(checkIfItWasSaved(user, blob.getName())) {
                res.add(mapping(blob.getContent(), blob.getName()));
            }
        }

        String message = res.isEmpty() ? "You have no any saved courses" : "Courses you saved";
        return BaseResponse.<List<CourseResponse>>builder()
                .data(res)
                .message(message)
                .status(200)
                .build();
    }

    private boolean checkIfItWasSaved(Optional<UserEntity> user, String courseId) {
        for (CourseEntity saved : user.get().getSavedCourses()) {
            if (saved.getId().equals(UUID.fromString(courseId))) return true;
        }
        return false;
    }

    public BaseResponse<List<CourseResponse>> getAll() throws IOException {
        Storage storage = getStorage();
        Bucket bucket = storage.get(BUCKET_NAME);

        List<CourseResponse> res = new ArrayList<>();
        for (Blob blob : bucket.list().iterateAll()) {
            if (checkIfNotDeleted(blob.getName())) {
                res.add(mapping(blob.getContent(), blob.getName()));
            }
        }

        return BaseResponse.<List<CourseResponse>>builder()
                .data(res)
                .status(200)
                .build();
    }

    private CourseResponse mapping(byte[] content, String name) {
        Optional<CourseEntity> course = courseRepository.findById(UUID.fromString(name));
        course.get().setContent(content);
        return mapper.toResponse(course.get());
    }

    private boolean checkIfNotDeleted(String name) {
        Optional<CourseEntity> course = courseRepository.findById(UUID.fromString(name));
        return course.map(CourseEntity::isNotDeleted).orElse(false);
    }
}
