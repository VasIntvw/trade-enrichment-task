package com.verygoodbank.tes.web.model;

/**
 * A record representing a trade request.
 *
 * @param date the date of the trade request
 * @param productId the unique identifier of the product
 * @param currency the currency in which the price is denoted
 * @param price the price of the product
 */
public record TradeRequest(String date,
                           Long productId,
                           String currency,
                           String price) {
}
