package com.verygoodbank.tes.web.model;

/**
 * A record representing a Product.
 *
 * @param productId the unique identifier of the product
 * @param productName the name of the product
 */
public record Product(Long productId,
                      String productName) {
}