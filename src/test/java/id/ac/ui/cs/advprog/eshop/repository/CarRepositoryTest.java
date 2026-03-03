package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

public class CarRepositoryTest {

    private CarRepository repository;
    private Car car;

    @BeforeEach
    void setUp() {
        repository = new CarRepository();

        car = new Car();
        car.setCarName("Toyota");
        car.setCarColor("Red");
        car.setCarQuantity(5);
    }

    @Test
    void testCreateCar() {
        Car created = repository.create(car);

        assertNotNull(created.getCarId());
        assertEquals("Toyota", created.getCarName());
    }

    @Test
    void testCreateCarWithExistingId() {

        Car carWithId = new Car();
        carWithId.setCarId("fixed-id");
        carWithId.setCarName("Honda");
        carWithId.setCarColor("Black");
        carWithId.setCarQuantity(3);

        Car result = repository.create(carWithId);

        assertEquals("fixed-id", result.getCarId());
    }

    @Test
    void testFindAll() {
        repository.create(car);
        Iterator<Car> iterator = repository.findAll();

        assertTrue(iterator.hasNext());

        Car found = iterator.next();

        assertEquals(car.getCarId(), found.getCarId());
    }

    @Test
    void testFindByIdFound() {
        repository.create(car);

        Car result = repository.findById(car.getCarId());

        assertNotNull(result);
        assertEquals(car.getCarId(), result.getCarId());
    }

    @Test
    void testFindByIdNotFound() {
        Car result = repository.findById("not_exist");

        assertNull(result);
    }

    @Test
    void testFindByIdEmptyRepository() {

        Car result = repository.findById("anything");

        assertNull(result);
    }

    @Test
    void testFindByIdMultipleCarsOneMatch() {

        Car car1 = new Car();
        car1.setCarName("Toyota");
        car1.setCarColor("Red");
        car1.setCarQuantity(5);

        Car car2 = new Car();
        car2.setCarName("Honda");
        car2.setCarColor("Blue");
        car2.setCarQuantity(3);

        repository.create(car1);
        repository.create(car2);

        Car result = repository.findById(car2.getCarId());

        assertNotNull(result);
        assertEquals(car2.getCarId(), result.getCarId());
    }

    @Test
    void testUpdateCar() {
        repository.create(car);

        Car updated = new Car();
        updated.setCarName("Honda");
        updated.setCarColor("Blue");
        updated.setCarQuantity(10);

        Car result = repository.update(car.getCarId(), updated);

        assertEquals("Honda", result.getCarName());
        assertEquals("Blue", result.getCarColor());
        assertEquals(10, result.getCarQuantity());
    }

    @Test
    void testUpdateCarNotFound() {
        Car updated = new Car();

        Car result = repository.update("not_exist", updated);

        assertNull(result);
    }

    @Test
    void testUpdateEmptyRepository() {

        Car updated = new Car();
        updated.setCarName("Mazda");

        Car result = repository.update("no-id", updated);

        assertNull(result);
    }

    @Test
    void testUpdateMultipleCarsOneMatch() {

        Car car1 = new Car();
        car1.setCarName("Toyota");
        car1.setCarColor("Red");
        car1.setCarQuantity(5);

        Car car2 = new Car();
        car2.setCarName("Honda");
        car2.setCarColor("Blue");
        car2.setCarQuantity(3);

        repository.create(car1);
        repository.create(car2);

        Car updated = new Car();
        updated.setCarName("Mazda");
        updated.setCarColor("Black");
        updated.setCarQuantity(10);

        Car result = repository.update(car2.getCarId(), updated);

        assertEquals("Mazda", result.getCarName());
    }

    @Test
    void testDeleteCar() {
        repository.create(car);

        repository.delete(car.getCarId());

        Car result = repository.findById(car.getCarId());

        assertNull(result);
    }
}