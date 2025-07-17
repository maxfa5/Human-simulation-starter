package org.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.project.model.AuditMode;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class AuditSenderKafkaService {
    
    @Autowired(required = false)
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendAudit(String message, AuditMode mode, String kafkaTopic) {
        if (kafkaTemplate != null && kafkaTopic != null && !kafkaTopic.isEmpty()) {
            kafkaTemplate.send(kafkaTopic, message);
        } else {
            System.out.println(message);
        }
    }
}