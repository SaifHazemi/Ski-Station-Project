package tn.esprit.SkiStationProject.ProjectTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.SkiStationProject.entities.Course;
import tn.esprit.SkiStationProject.entities.Piste;
import tn.esprit.SkiStationProject.entities.Registration;
import tn.esprit.SkiStationProject.entities.Skier;
import tn.esprit.SkiStationProject.entities.Subscription;
import tn.esprit.SkiStationProject.entities.enums.Color;
import tn.esprit.SkiStationProject.entities.enums.Support;
import tn.esprit.SkiStationProject.entities.enums.TypeCourse;
import tn.esprit.SkiStationProject.entities.enums.TypeSubscription;
import tn.esprit.SkiStationProject.repositories.CourseRepository;
import tn.esprit.SkiStationProject.repositories.PisteRepository;
import tn.esprit.SkiStationProject.repositories.RegistrationRepository;
import tn.esprit.SkiStationProject.repositories.SkierRepository;
import tn.esprit.SkiStationProject.repositories.SubscriptionRepository;
import tn.esprit.SkiStationProject.services.SkierServicesImpl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SkierServicesTest {

    @Mock
    SkierRepository skierRepository;

    @Mock
    PisteRepository pisteRepository;

    @Mock
    CourseRepository courseRepository;

    @Mock
    RegistrationRepository registrationRepository;

    @Mock
    SubscriptionRepository subscriptionRepository;

    @InjectMocks
    SkierServicesImpl skierServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addSkierAndAssignToCourse() {
        // Mocking repository behavior
        Skier skier = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), new HashSet<>());
        Course course = new Course(1, TypeCourse.COLLECTIVE_ADULT, Support.SKI, 50.0f, 1, null);
        when(skierRepository.save(skier)).thenReturn(skier);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Calling the method under test
        Skier savedSkier = skierServices.addSkierAndAssignToCourse(skier, 1L);

        // Verifying the result
        assertEquals(skier, savedSkier);
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    void removeSkier() {
        // Calling the method under test
        skierServices.removeSkier(1L);

        // Verifying the method call
        verify(skierRepository, times(1)).deleteById(1L);
    }

    @Test
    void retrieveSkier() {
        // Mocking repository behavior
        Skier skier = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), new HashSet<>());
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));

        // Calling the method under test
        Skier retrievedSkier = skierServices.retrieveSkier(1L);

        // Verifying the result
        assertEquals(skier, retrievedSkier);
    }
    @Test
    void retrieveAllSkiers() {
        // Mocking repository behavior
        List<Skier> skiers = Collections.singletonList(new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), new HashSet<>()));
        when(skierRepository.findAll()).thenReturn(skiers);

        // Calling the method under test
        List<Skier> retrievedSkiers = skierServices.retrieveAllSkiers();

        // Verifying the result
        assertEquals(skiers, retrievedSkiers);
    }

    @Test
    void addSkier() {
        // Mocking repository behavior
        Skier skierToAdd = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), new HashSet<>());
        when(skierRepository.save(skierToAdd)).thenReturn(skierToAdd);

        // Calling the method under test
        Skier addedSkier = skierServices.addSkier(skierToAdd);

        // Verifying the result
        assertEquals(skierToAdd, addedSkier);
    }

    @Test
    void assignSkierToSubscription() {
        // Mocking repository behavior
        Skier skier = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), new HashSet<>());
        Subscription subscription = new Subscription(LocalDate.now(), LocalDate.now().plusMonths(1), 100.0f, TypeSubscription.MONTHLY);
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier); // Mocking save method

        // Calling the method under test
        Skier assignedSkier = skierServices.assignSkierToSubscription(1L, 1L);

        // Verifying the result
        assertEquals(subscription, assignedSkier.getSubscription());
    }

    @Test
    void assignSkierToPiste() {
        // Mocking repository behavior
        Skier skier = new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), new HashSet<>());
        Piste piste = new Piste("Red", Color.RED, 1000, 50, new HashSet<>());
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier); // Mocking save method

        // Calling the method under test
        Skier assignedSkier = skierServices.assignSkierToPiste(1L, 1L);

        // Verifying the result
        assertEquals(1, assignedSkier.getPistes().size());
        assertEquals(piste, assignedSkier.getPistes().iterator().next());
    }

    @Test
    void retrieveSkiersBySubscriptionType() {
        // Mocking repository behavior
        List<Skier> skiers = Collections.singletonList(new Skier("John", "Doe", LocalDate.of(1990, 5, 15), "City", null, new HashSet<>(), new HashSet<>()));
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.MONTHLY)).thenReturn(skiers);

        // Calling the method under test
        List<Skier> retrievedSkiers = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.MONTHLY);

        // Verifying the result
        assertEquals(skiers, retrievedSkiers);
    }

}