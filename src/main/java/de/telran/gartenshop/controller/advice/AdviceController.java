package de.telran.gartenshop.controller.advice;

import de.telran.gartenshop.exception.*;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Hidden
@RestControllerAdvice
public class AdviceController {

    String errorStr = "error";

    // Обрабатывает ошибки, связанные с отсутствием запрашиваемых данных (404 Not Found)
    // Handles errors related to missing requested data (404 Not Found)
    @ExceptionHandler({IllegalArgumentException.class, FileNotFoundException.class})
    public ResponseEntity<String> handleTwoExceptionNotFound(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    // Обрабатывает ошибки преобразования запроса и нарушения целостности данных (400 Bad Request)
    // Handles request conversion errors and data integrity violations (400 Bad Request)
    @ExceptionHandler({HttpMessageConversionException.class, DataIntegrityViolationException.class, NullPointerException.class})
    public ResponseEntity<String> handleTwoExceptionBadRequest(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    // Обрабатывает ошибку при попытке зарегистрировать уже существующего пользователя (409 Conflict)
    // Handles an error when trying to register an already existing user (409 Conflict)
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(errorStr, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Обрабатывает ситуацию, когда пользователь не найден (404 Not Found)
    // Handles the case when the user is not found (404 Not Found)
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put(errorStr, "User not found");
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }

    // Обрабатывает ошибку при сохранении пользователя (500 Internal Server Error)
    // Handles an error when saving a user (500 Internal Server Error)
    @ExceptionHandler(UserSaveException.class)
    public ResponseEntity<Map<String, String>> handleUserSaveException(UserSaveException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(errorStr, "User registration failed: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    // Обрабатывает ошибку при удалении пользователя (409 Conflict)
    // Handles an error when deleting a user (409 Conflict)
    @ExceptionHandler(UserDeleteException.class)
    public ResponseEntity<Map<String, String>> handleUserDeleteException(UserDeleteException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(errorStr, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    // Обрабатывает ошибку, если данные не найдены в базе данных (404 Not Found)
    // Handles an error if data is not found in the database (404 Not Found)
    @ExceptionHandler(DataNotFoundInDataBaseException.class)
    public ResponseEntity<Map<String, String>> handleDataNotFoundInDataBaseException(DataNotFoundInDataBaseException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(errorStr, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // Обрабатывает некорректные запросы (400 Bad Request)
    // Handles invalid requests (400 Bad Request)
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(errorStr, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Обрабатывает ошибки валидации (400 Bad Request)
    // Handles validation errors (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(fe -> fe.getDefaultMessage()).toList();

        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put(errorStr, errors);
        return errorResponse;
    }

    // Обрабатывает ошибки валидации (400 Bad Request)
    // Handles validation errors (400 Bad Request)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<Map<String, String>> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(errorStr, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // Обрабатывает ошибки валидации, связанные с аннотациями @Valid (400 Bad Request)
    // Handles validation errors related to @Valid annotations (400 Bad Request)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<String>> handleConstraintViolationException(ConstraintViolationException ex) {
        List<String> errorMessages = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }

    // Обрабатывает ошибки несовпадения типов параметров в контроллерах (400 Bad Request)
    // Handles parameter type mismatch errors in controllers (400 Bad Request)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();
        String parameterName = ex.getParameter().getParameterName();
        String errorMessage;
        Class<?> requiredType = ex.getRequiredType();

        if (requiredType != null) {
            errorMessage = String.format("The parameter '%s' should be of type '%s'", parameterName, requiredType.getSimpleName());
        } else {
            errorMessage = String.format("The parameter '%s' has an invalid type (expected type could not be determined)", parameterName);
        }

        errors.put(errorStr, errorMessage);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // Обрабатывает ошибки с установленным статусом через ResponseStatusException (например, 404, 403 и другие)
    // Handles errors with a status set via ResponseStatusException (e.g., 404, 403, etc.)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
    }

    // Обрабатывает ошибки аутентификации (401 Unauthorized)
    // Handles authentication errors (401 Unauthorized)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Authentication required. Please log in."));
    }

    // Обрабатывает ошибки, связанные с отсутствием доступа (403 Forbidden)
    // Handles errors related to lack of access (403 Forbidden)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", "You are not allowed to access this resource"));
    }

    // Обрабатывает все непредвиденные ошибки (418 I'm a teapot)
    // Handles all unexpected errors (418 I'm a teapot)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneralException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put(errorStr, "Something went wrong, please try again later: " + ex.getClass() + " " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(response);
    }
}

