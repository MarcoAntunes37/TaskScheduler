package taskScheduller.api.util;

import java.util.Optional;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;
import taskScheduller.api.exception.ProblemDetails;

public final class ExceptionUtil {

    public static final String UNKNOWN_TYPE = "Unknown type";
    public static final String UNKNOWN_VALUE = "Unknown value";

    private ExceptionUtil() {
        throw new IllegalStateException("Cannot be instantiated");
    }

    public static ProblemDetails getProblemDetails(HttpServletRequest request, Exception ex) {
        return switch (ex.getClass().getSimpleName()) {
            case "MethodArgumentTypeMismatchException" ->
                handleMethodArgumentTypeMismatch((MethodArgumentTypeMismatchException) ex, request);
            case "MissingServletRequestParameterException" ->
                handleMissingServletRequestParameter((MissingServletRequestParameterException) ex, request);
            case "DataIntegrityViolationException" -> handleDataIntegrityViolation(request);
            case "MethodArgumentNotValidException" ->
                handleMethodArgumentNotValid((MethodArgumentNotValidException) ex, request);
            case "ConversionFailedException" -> handleConversionFailed((ConversionFailedException) ex, request);
            default -> new ProblemDetails(
                    "A unespected error occurred.",
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    "A unespected error occurred.",
                    request.getRequestURI());
        };
    }

    private static ProblemDetails handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {
        String title = "Request with invalid field";
        String fieldName = ex.getName();
        String requiredType = Optional.ofNullable(ex.getRequiredType())
                .map(Class::getSimpleName)
                .orElse(UNKNOWN_TYPE);
        String invalidValue = Optional.ofNullable(ex.getValue())
                .map(Object::toString)
                .orElse(UNKNOWN_VALUE);
        String detail = String.format(
                "The field '%s' received the value '%s', which is not of type '%s'.", fieldName,
                invalidValue, requiredType);

        return new ProblemDetails(title, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                detail, request.getRequestURI());
    }

    private static ProblemDetails handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
            HttpServletRequest request) {
        String title = "Required field is missing";
        String detail = String.format("The field '%s' is required.", ex.getParameterName());

        return new ProblemDetails(title, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                detail, request.getRequestURI());
    }

    private static ProblemDetails handleDataIntegrityViolation(HttpServletRequest request) {
        String title = "Integrity violation";
        String detail = "A database integrity violation occurred.";

        return new ProblemDetails(title, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                detail, request.getRequestURI());
    }

    private static ProblemDetails handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        String title = "Field validation failed";
        String failedValidationMessage = "A field validation failed for one or more fields.";
        String detail;

        if (ex.getBindingResult().hasFieldErrors()) {
            FieldError fieldError = ex.getBindingResult().getFieldError();
            if (fieldError != null) {
                String violatedField = fieldError.getField();
                String errorMessage = fieldError.getDefaultMessage();

                if (errorMessage != null && errorMessage.contains("Failed to convert value of type")) {
                    String rejectedValue = Optional.ofNullable(fieldError.getRejectedValue())
                            .map(Object::toString)
                            .orElse(UNKNOWN_VALUE);
                    errorMessage = String.format("'%s' is not valid.", rejectedValue);
                }

                detail = String.format("The field validation for '%s' failed: %s.", violatedField, errorMessage);
            } else {
                detail = failedValidationMessage;
            }
        } else {
            detail = failedValidationMessage;
        }

        return new ProblemDetails(title, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                detail, request.getRequestURI());
    }

    private static ProblemDetails handleConversionFailed(ConversionFailedException ex, HttpServletRequest request) {
        String title = "Invalid field";
        String invalidValue = Optional.ofNullable(ex.getValue())
                .map(Object::toString)
                .orElse(UNKNOWN_VALUE);
        String requiredType = Optional.of(ex.getTargetType())
                .map(typeDescriptor -> typeDescriptor.getType().getSimpleName())
                .orElse(UNKNOWN_TYPE);
        String detail = String.format("The field received the value '%s', which is not of type '%s'.",
                invalidValue, requiredType);

        return new ProblemDetails(title, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
                detail, request.getRequestURI());
    }
}