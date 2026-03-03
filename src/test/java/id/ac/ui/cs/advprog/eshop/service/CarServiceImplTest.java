package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CarServiceImplTest {

    private CarServiceImpl service;
    private CarRepository repository;

    @BeforeEach
    void setUp() {

        repository = mock(CarRepository.class);
        service = new CarServiceImpl();

        try {
            Field field =
                    CarServiceImpl.class.getDeclaredField("carRepository");
            field.setAccessible(true);
            field.set(service, repository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreate() {
        Car car = new Car();

        service.create(car);

        verify(repository).create(car);
    }

    @Test
    void testFindAll() {

        Iterator<Car> iterator =
                List.of(new Car(), new Car()).iterator();

        when(repository.findAll()).thenReturn(iterator);

        List<Car> result = service.findAll();

        assertEquals(2, result.size());

        verify(repository).findAll();
    }

    @Test
    void testFindById() {

        Car car = new Car();

        when(repository.findById("1")).thenReturn(car);

        Car result = service.findById("1");

        assertEquals(car, result);

        verify(repository).findById("1");
    }

    @Test
    void testUpdate() {

        Car car = new Car();

        service.update("1", car);

        verify(repository).update("1", car);
    }

    @Test
    void testDeleteCarById() {

        service.deleteCarById("1");

        verify(repository).delete("1");
    }
}