package io.github.krivolapovdev.codeoutputquiz.notificationservice.event;

import io.github.krivolapovdev.codeoutputquiz.notificationservice.enums.NotificationType;

public record EmailNotificationEvent(String recipientEmail, NotificationType type) {}
