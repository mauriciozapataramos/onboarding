package com.jmzr.onboarding.model;

import lombok.Builder;
import lombok.Data;

/**
 * Clase que representa el detalle de un error de validación en un campo específico.
 * Se utiliza en las respuestas de error para proporcionar información estructurada.
 * 
 * Integración con MessageKeys:
 * - El campo 'message' puede contener claves de mensajes (ej: error.auth.invalid.email)
 * - El controlador utiliza MessageUtil para resolver el mensaje final
 */
@Data
@Builder
public class FieldErrorDetail {
    
    /**
     * Nombre del campo que falló la validación
     * Ej: "email", "password"
     */
    private String field;
    
    /**
     * Valor rechazado que causó el error
     * Puede ser null si el error no está relacionado con un valor específico
     */
    private String rejectedValue;
    
    /**
     * Mensaje de error descriptivo. Puede ser:
     * - Un mensaje directo ("El email es inválido")
     * - Una clave de mensaje ("error.auth.invalid.email")
     */
    private String message;
    
    /**
     * (Opcional) Tipo de error para clasificación frontend
     * Ej: "VALIDATION", "FORMAT", "REQUIRED"
     */
    @Builder.Default
    private String errorType = "VALIDATION";
}