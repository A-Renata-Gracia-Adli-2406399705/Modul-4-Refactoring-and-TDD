package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.service.CarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CarControllerTest {

    private CarController controller;
    private CarServiceImpl service;
    private Model model;

    @BeforeEach
    void setUp() {
        service = mock(CarServiceImpl.class);
        controller = new CarController();

        try {
            Field field = CarController.class.getDeclaredField("carservice");
            field.setAccessible(true);
            field.set(controller, service);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        model = mock(Model.class);
    }

    @Test
    void testCreateCarPage() {
        String view = controller.createCarPage(model);

        assertEquals("createCar", view);
        verify(model).addAttribute(eq("car"), any(Car.class));
    }

    @Test
    void testCreateCarPost() {
        Car car = new Car();

        String view = controller.createCarPost(car, model);

        assertEquals("redirect:listCar", view);
        verify(service).create(car);
    }

    @Test
    void testCarListPage() {
        List<Car> cars = List.of(new Car(), new Car());
        when(service.findAll()).thenReturn(cars);

        String view = controller.carListPage(model);

        assertEquals("carList", view);
        verify(model).addAttribute("cars", cars);
        verify(service).findAll();
    }

    @Test
    void testEditCarPage() {
        Car car = new Car();
        when(service.findById("1")).thenReturn(car);

        String view = controller.editCarPage("1", model);

        assertEquals("editCar", view);
        verify(model).addAttribute("car", car);
        verify(service).findById("1");
    }

    @Test
    void testEditCarPost() {
        Car car = new Car();
        car.setCarId("1");

        String view = controller.editCarPost(car, model);

        assertEquals("redirect:listCar", view);
        verify(service).update("1", car);
    }

    @Test
    void testDeleteCar() {
        String view = controller.deleteCar("1");

        assertEquals("redirect:listCar", view);
        verify(service).deleteCarById("1");
    }
}