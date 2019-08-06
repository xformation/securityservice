package com.synectiks.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchKafkaConsumer {

    private final Logger log = LoggerFactory.getLogger(SearchKafkaConsumer.class);
    private static final String TOPIC = "topic_search";

    @KafkaListener(topics = "topic_search", groupId = "group_id")
    public void consume(String message) throws IOException {
        log.info("Consumed message in {} : {}", TOPIC, message);
    }
}
