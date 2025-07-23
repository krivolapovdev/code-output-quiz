package io.github.nellshark.codeoutputquiz.notificationservice.event;

import io.github.nellshark.codeoutputquiz.notificationservice.enums.NotificationType;

public record EmailNotificationEvent(String recipientEmail, NotificationType type) {}
