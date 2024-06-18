package com.verygoodbank.tes.web.controller;


import com.verygoodbank.tes.web.service.DataPreparationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;

@RestController
@RequestMapping("api/v1")
public class TradeEnrichmentController {
    Logger logger = LoggerFactory.getLogger(TradeEnrichmentController.class);

    private final DataPreparationService dataPreparationService;

    public TradeEnrichmentController(DataPreparationService dataPreparationService) {
        this.dataPreparationService = dataPreparationService;
    }

    /**
     * Controller method to enrich a CSV file.
     *
     * @param file the CSV file to enrich
     * @return a {@link ResponseEntity} containing the enriched CSV file as a stream,
     * or a bad request response if the file is empty,
     * or an internal server error response if an error occurs during processing
     */
    @PostMapping(path = "/enrich")
    public ResponseEntity<StreamingResponseBody> enrich(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            StreamingResponseBody responseBody = dataPreparationService.getStreamingResponseBody(file.getInputStream());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=trade.csv")
                    .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                    .body(responseBody);
        } catch (IOException e) {
            logger.error("Error while enriching file", e);
            return ResponseEntity.internalServerError().build();
        }
    }
}


