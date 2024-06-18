package com.verygoodbank.tes.web.service;

import com.verygoodbank.tes.web.model.Product;
import com.verygoodbank.tes.web.model.TradeRequest;
import com.verygoodbank.tes.web.repository.ProductRepository;

import com.verygoodbank.tes.web.utils.DateUtil;
import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
public class DataPreparationService {
    private static final Logger logger = LoggerFactory.getLogger(DataPreparationService.class);

    private final ProductRepository productRepository;

    @Value("${products.file.csv}")
    private String productsFilePath;

    public DataPreparationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public StreamingResponseBody getStreamingResponseBody(InputStream inputStream) {
        return outputStream -> {
            try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream), CSVFormat.DEFAULT.withFirstRecordAsHeader());
                 CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(outputStream), CSVFormat.DEFAULT.withHeader("date", "product_name", "currency", "price"))) {
                for (CSVRecord record : csvParser) {
                    TradeRequest tradeRequest = mapToTradeRequest(record);
                    if (tradeRequest == null) {
                        continue;
                    }
                    if (!DateUtil.isValidDate(tradeRequest.date())) {
                        logger.warn("Invalid date: '{}' record discarded: {}", tradeRequest.date(), record);
                        continue;
                    }
                    String productName = productRepository.getProductName(tradeRequest.productId());

                    csvPrinter.printRecord(tradeRequest.date(), productName, tradeRequest.currency(), tradeRequest.price());
                }
                csvPrinter.flush();
            } catch (IOException e) {
                logger.error("Error on preparing Trades", e);
            }
        };
    }

    private TradeRequest mapToTradeRequest(CSVRecord record) {
        try {
            String date = record.get("date");
            Long productId = Long.parseLong(record.get("product_id"));
            String currency = record.get("currency");
            String price = record.get("price");
            return new TradeRequest(date, productId, currency, price);
        } catch (Exception e) {
            logger.error("Error while parsing TradeRequest record: {}", record);
            return null;
        }
    }

    @PostConstruct
    private void prepareProducts() {
        try {
            ClassPathResource resource = new ClassPathResource(productsFilePath);
            InputStream inputStream = resource.getInputStream();
            List<Product> products = parseProducts(inputStream);
            productRepository.saveAll(products);
        } catch (Exception e) {
            logger.error("Error on preparing products", e);
            throw new RuntimeException(e);
        }
    }

    private List<Product> parseProducts(InputStream inputStream) {
        try (CSVParser csvParser =
                     new CSVParser(new InputStreamReader(inputStream), CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
            return StreamSupport.stream(csvParser.spliterator(), true)
                    .map(this::mapToProduct)
                    .filter(Objects::nonNull)
                    .toList();

        } catch (IOException e) {
            logger.error("Error parsing CSV Products file: {}", e.getMessage());
            throw new RuntimeException("Error parsing CSV Products file: ", e);
        }

    }

    private Product mapToProduct(CSVRecord record) {
        try {
            Long productId = Long.parseLong(record.get("product_id"));
            String productName = record.get("product_name");
            return new Product(productId, productName);
        } catch (Exception e) {
            logger.error("Error while parsing product record: {}", record);
            return null;
        }
    }
}
