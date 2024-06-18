package com.verygoodbank.tes.web.controller;

import com.verygoodbank.tes.web.model.Product;
import com.verygoodbank.tes.web.repository.ProductRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.StringReader;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TradeEnrichmentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void testEnrichEndpoint() throws Exception {
        //Given
        long productId = 1L;
        String expectedProductName = "TEST NAME";
        productRepository.saveAll(List.of(new Product(productId, expectedProductName)));

        String csvContent = """
                date,product_id,currency,price
                20160101,%product_id%,EUR,10.0
                """.replace("%product_id%", String.valueOf(productId));
        MockMultipartFile mockFile = new MockMultipartFile("file", "trade.csv", TEXT_PLAIN_VALUE, csvContent.getBytes());

        //When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/enrich")
                        .file(mockFile)
                        .contentType(MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String responseContent = mvcResult.getResponse().getContentAsString();
        CSVParser csvParser = new CSVParser(new StringReader(responseContent), CSVFormat.DEFAULT.withFirstRecordAsHeader());
        List<CSVRecord> records = csvParser.getRecords();

        assertThat(records).hasSize(1);

        CSVRecord record1 = records.get(0);
        assertEquals("20160101", record1.get("date"));
        assertEquals(expectedProductName, record1.get("product_name"));
        assertEquals("EUR", record1.get("currency"));
        assertEquals("10.0", record1.get("price"));
    }
}