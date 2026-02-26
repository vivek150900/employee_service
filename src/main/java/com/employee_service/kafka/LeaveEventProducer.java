package com.employee_service.kafka;

import com.employee_service.dto.LeaveCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;




@Service
public class LeaveEventProducer {

    private static final String TOPIC = "leave-create-topic";

    @Autowired
    private KafkaTemplate<String, LeaveCreateEvent> kafkaTemplate;

    public void sendLeaveCreateEvent(LeaveCreateEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
