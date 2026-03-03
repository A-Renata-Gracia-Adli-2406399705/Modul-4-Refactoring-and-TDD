package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    private ProductController controller;
    private ProductService service;
    private Model model;

    @BeforeEach
    void setUp() {
        service = mock(ProductService.class);
        controller = new ProductController();

        try {
            java.lang.reflect.Field field =
                    ProductController.class.getDeclaredField("service");
            field.setAccessible(true);
            field.set(controller, service);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        model = mock(Model.class);
    }

    @Test
    void testCreateProductPage() {
        String view = controller.createProductPage(model);

        assertEquals("createProduct", view);
        verify(model).addAttribute(eq("product"), any(Product.class));
    }

    @Test
    void testCreateProductSuccess() {
        Product product = new Product();
        product.setProductQuantity(5);

        String view = controller.createProductPost(product, model);

        assertEquals("redirect:/product/list", view);
        verify(service).create(product);
        verify(model, never()).addAttribute(eq("error"), anyString());
    }

    @Test
    void testCreateProductNegativeQuantity() {
        Product product = new Product();
        product.setProductQuantity(-5);

        String view = controller.createProductPost(product, model);

        assertEquals("createProduct", view);
        verify(model).addAttribute(eq("error"), anyString());
        verify(service, never()).create(any());
    }

    @Test
    void testProductListPage() {
        List<Product> products = List.of(new Product(), new Product());
        when(service.findAll()).thenReturn(products);

        String view = controller.productListPage(model);

        assertEquals("productList", view);
        verify(model).addAttribute("products", products);
        verify(service).findAll();
    }

    @Test
    void testEditProductPage() {
        Product product = new Product();
        when(service.findById("123")).thenReturn(product);

        String view = controller.editProductPage("123", model);

        assertEquals("editProduct", view);
        verify(model).addAttribute("product", product);
        verify(service).findById("123");
    }

    @Test
    void testEditProductSuccess() {
        Product product = new Product();
        product.setProductQuantity(10);

        String view = controller.editProductPost(product, model);

        assertEquals("redirect:/product/list", view);
        verify(service).update(product);
        verify(model, never()).addAttribute(eq("error"), anyString());
    }

    @Test
    void testEditProductNegativeQuantity() {
        Product product = new Product();
        product.setProductQuantity(-10);

        String view = controller.editProductPost(product, model);

        assertEquals("editProduct", view);
        verify(model).addAttribute(eq("error"), anyString());
        verify(service, never()).update(any());
    }

    @Test
    void testDeleteProduct() {
        String view = controller.deleteProduct("123");

        assertEquals("redirect:/product/list", view);
        verify(service).delete("123");
    }
}