package com.example.userservicespring.dto; // или com.example.notificationservice.dto

// DTO с готовыми данными для отправки email
public record EmailNotificationDto(
        String to,
        String subject,
        String body
) {
}