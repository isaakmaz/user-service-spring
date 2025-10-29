package com.example.userservicespring.kafka;

import com.example.userservicespring.dto.EmailNotificationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);
    private static final String TOPIC = "email-notifications";

    private final KafkaTemplate<String, EmailNotificationDto> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, EmailNotificationDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmailNotification(EmailNotificationDto notificationDto) {
        log.info("Отправка email-уведомления в Kafka: {}", notificationDto);
        kafkaTemplate.send(TOPIC, notificationDto.to(), notificationDto);
    }
}