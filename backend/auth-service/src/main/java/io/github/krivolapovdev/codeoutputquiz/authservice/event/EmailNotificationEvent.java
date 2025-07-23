package io.github.krivolapovdev.codeoutputquiz.authservice.event;

import io.github.krivolapovdev.codeoutputquiz.authservice.enums.NotificationType;

public record EmailNotificationEvent(String recipientEmail, NotificationType type) {}
