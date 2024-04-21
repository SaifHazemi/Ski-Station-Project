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
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddRegistrationAndAssignToSkier() {
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
        Registration result = registrationService.assignRegistrationToCourse(1L, 1L);
        assertNotNull(result);
        assertEquals(course, result.getCourse());
    }

    @Test
    public void testAddRegistrationAndAssignToSkierAndCourse() {
        Skier skier = new Skier();
        Course course = new Course();
        Registration registration = new Registration();
        registration.setNumWeek(1);
        registration.setSkier(skier);
        registration.setCourse(course);
        when(skierRepository.findById(anyLong())).thenReturn(Optional.of(skier));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(anyInt(), anyLong(), anyLong())).thenReturn(0L); // Use 0L for long return type
        when(registrationRepository.countByCourseAndNumWeek(any(Course.class), anyInt())).thenReturn(0L); // Use 0L for long return type
        Registration result = registrationService.addRegistrationAndAssignToSkierAndCourse(registration, 1L, 1L);
        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        assertEquals(course, result.getCourse());
    }

    @Test
    public void testNumWeeksCourseOfInstructorBySupport() {
        Instructor instructor = new Instructor();
        Course course = new Course();
        Registration registration = new Registration();
        registration.setNumWeek(1);
        registration.setCourse(course);
        Set<Course> courses = new HashSet<>(Arrays.asList(course));
        instructor.setCourses(courses);
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(instructor));
        when(registrationRepository.numWeeksCourseOfInstructorBySupport(anyLong(), any(Support.class))).thenReturn(Arrays.asList(1));
        List<Integer> result = registrationService.numWeeksCourseOfInstructorBySupport(1L, Support.SKI);
        assertNotNull(result);
        assertTrue(result.contains(1));
    }
}
