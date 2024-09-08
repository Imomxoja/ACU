package uz.practise.acu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.practise.acu.repository.CourseRepository;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;


}
