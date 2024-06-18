package com.verygoodbank.tes.web.repository;

import com.verygoodbank.tes.web.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductRepositoryTest {

    @InjectMocks
    ProductRepository productRepository;

    @Test
    void whenProductExists_thenProductNameIsReturned() {
        //Given
        Long productId = 1L;
        String expectedProductName = "TEST";
        Product product = new Product(productId, expectedProductName);
        productRepository.saveAll(List.of(product));

        //When
        String actualProductName = productRepository.getProductName(productId);

        //Then
        Assertions.assertEquals(expectedProductName, actualProductName);
    }

    //TODO other cases:
    //- not exist
    //- save all empty collection
    //- test for invalid data
}