package com.synectiks.search.web.rest;

import com.synectiks.search.service.SearchKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/search-kafka")
public class SearchKafkaResource {

    private final Logger log = LoggerFactory.getLogger(SearchKafkaResource.class);

    private SearchKafkaProducer kafkaProducer;

    public SearchKafkaResource(SearchKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping(value = "/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.sendMessage(message);
    }
}
