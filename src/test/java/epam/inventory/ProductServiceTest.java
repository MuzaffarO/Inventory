package epam.inventory;

import epam.inventory.model.Product;
import epam.inventory.repository.ProductRepository;
import epam.inventory.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product1;
    private Product product2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        product1 = new Product(1L, "Product A", "Description A", 100.0, 10);
        product2 = new Product(2L, "Product B", "Description B", 150.0, 5);
    }

    @Test
    public void testGetAllProducts() {
        List<Product> mockList = Arrays.asList(product1, product2);
        when(productRepository.findAll()).thenReturn(mockList);

        List<Product> result = productService.getAllProducts();
        assertEquals(2, result.size());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void testAddProduct() {
        when(productRepository.save(product1)).thenReturn(product1);
        Product saved = productService.addProduct(product1);

        assertNotNull(saved);
        assertEquals("Product A", saved.getName());
        verify(productRepository, times(1)).save(product1);
    }

    @Test
    public void testUpdateProductExists() {
        Product updatedProduct = new Product(null, "Updated", "Updated Desc", 200.0, 20);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Optional<Product> result = productService.updateProduct(1L, updatedProduct);
        assertTrue(result.isPresent());
        assertEquals("Updated", result.get().getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void testUpdateProductNotExists() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Product> result = productService.updateProduct(99L, product2);
        assertFalse(result.isPresent());
        verify(productRepository, never()).save(any(Product.class));
    }
}