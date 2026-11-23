package com.adopet.notifications;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record NotificationRequest(
        @NotNull @Positive Long solicitudId,
        @NotBlank @Email String destinatario,
        @NotBlank String estado,
        @NotNull LocalDateTime fechaCambio) { }
