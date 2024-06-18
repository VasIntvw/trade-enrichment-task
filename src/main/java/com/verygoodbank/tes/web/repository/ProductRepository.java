package com.verygoodbank.tes.web.repository;

import com.verygoodbank.tes.web.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Repository class for storing Product entities.
 */
@Component
public class ProductRepository {
    private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);
    /**
     * A map storing products by their unique identifier.
     */
    private static final Map<Long, Product> PRODUCTS = new HashMap<>();
    /**
     * Placeholder text when product not found.
     */
    private static final String MISSING_PRODUCT_NAME = "Missing Product Name";

    /**
     * Saves a list of products to the repository.
     *
     * @param products the list of products to save
     */
    public void saveAll(List<Product> products) {
        logger.info("Begin save products {}", products.size());
        products.forEach(product -> PRODUCTS.put(product.productId(), product));
        logger.info("Saved {} products", PRODUCTS.size());
    }

    /**
     * Retrieves the name of the product with the specified ID.
     *
     * @param productId the ID of the product
     * @return the name of the product, or "Missing Product Name" if not found
     */
    public String getProductName(Long productId) {
        if (PRODUCTS.get(productId) == null || !PRODUCTS.containsKey(productId)) {
            logger.warn("No product found with id: {}", productId);
            return MISSING_PRODUCT_NAME;
        }
        return PRODUCTS.get(productId).productName();
    }
}