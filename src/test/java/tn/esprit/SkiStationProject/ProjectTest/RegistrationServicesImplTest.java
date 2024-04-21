package tn.esprit.SkiStationProject.ProjectTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.List;
import tn.esprit.SkiStationProject.entities.*;
import tn.esprit.SkiStationProject.entities.enums.Support;
import tn.esprit.SkiStationProject.entities.enums.TypeCourse;
import tn.esprit.SkiStationProject.repositories.*;
import tn.esprit.SkiStationProject.services.RegistrationServicesImpl;

public class RegistrationServicesImplTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private SkierRepository skierRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddRegistrationAndAssignToSkier() {
        Skier skier = new Skier();
        Registration registration = new Registration();
        when(skierRepository.findById(anyLong())).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);
        Registration result = registrationService.addRegistrationAndAssignToSkier(registration, 1L);
        assertNotNull(result);
        assertEquals(skier, result.getSkier());
    }

    @Test
    public void testAssignRegistrationToCourse() {
        Course course = new Course();
        Registration registration = new Registration();
        when(registrationRepository.findById(anyLong())).thenReturn(Optional.of(registration));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration); // Mocking the save method
        Registration result = registrationService.assignRegistrationToCourse(1L, 1L);
        assertNotNull(result); // Ensure the result is not null
        assertEquals(course, result.getCourse());
    }

    @Test
    public void testAddRegistrationAndAssignToSkierAndCourse() {
        Skier skier = new Skier();
        skier.setDateOfBirth(LocalDate.of(1990, 1, 1)); // Set a valid date of birth
        Course course = new Course();
        course.setTypeCourse(TypeCourse.INDIVIDUAL); // Set a valid TypeCourse enum value
        Registration registration = new Registration();
        registration.setNumWeek(1);
        registration.setSkier(skier);
        registration.setCourse(course);
        when(skierRepository.findById(anyLong())).thenReturn(Optional.of(skier));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong())).thenReturn(0L);
        when(registrationRepository.countByCourseAndNumWeek(any(Course.class), anyInt())).thenReturn(0L);
        Registration result = registrationService.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);
        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        assertEquals(course, result.getCourse());
    }



    @Test
    public void testNumWeeksCourseOfInstructorBySupport() {
        Instructor instructor = new Instructor();
        Course course = new Course();
        course.setSupport(Support.SKI); // Ensure Support enum value is set
        Registration registration = new Registration();
        registration.setNumWeek(1);
        registration.setCourse(course);
        Set<Course> courses = new HashSet<>(Arrays.asList(course));
        instructor.setCourses(courses);
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(instructor));
        when(registrationRepository.numWeeksCourseOfInstructorBySupport(anyLong(), any(Support.class)))
                .thenReturn(Arrays.asList(1, 2, 3)); // Mocking the list of weeks
        List<Integer> result = registrationService.numWeeksCourseOfInstructorBySupport(1L, Support.SKI);
        assertNotNull(result);
        assertEquals(3, result.size()); // Asserting the size of the returned list
        assertTrue(result.contains(1));
        assertTrue(result.contains(2));
        assertTrue(result.contains(3));
    }
}
