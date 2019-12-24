package by.arhor.university.web.api.v1;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import by.arhor.university.service.error.EntityNotFoundException;
import by.arhor.university.web.api.ApiError;

@RestControllerAdvice
public class ExceptionController {

  private static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

  private final MessageSource messageSource;

  @Autowired
  public ExceptionController(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ApiError typeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
    log.error("Argument class cast exception", ex);
    return new ApiError(
        ApiError.WRONG_METHOD_PARAMS,
        messageSource.getMessage(
            "error.wrong.argument",
            new Object[] { ex.getName(), ex.getValue() },
            request.getLocale()
        )
    );
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ApiError entityNotFoundException(EntityNotFoundException ex, WebRequest request) {
    log.error("Resource not found", ex);
    return new ApiError(
        ApiError.NOT_FOUND,
        messageSource.getMessage(
            ex.getErrorLabel().getValue(),
            new Object[]{ ex.getFieldName(), ex.getFieldValue() },
            request.getLocale()
        )
    );
  }

  @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ApiError mediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex, WebRequest request) {
    log.error("Request media type not supported", ex);
    return new ApiError(
        ApiError.TYPE_UNSUPPORTED,
        messageSource.getMessage(
            "error.type.unsupported",
            new Object[] { ex.getContentType() },
            request.getLocale()
        )
    );
  }

  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
  public String mediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex, WebRequest request) {
    log.error("Media type not accepted", ex);
    return new ApiError(
        ApiError.TYPE_NOT_ACCEPTED,
        messageSource.getMessage(
            "error.type.unaccepted",
            new Object[] { ex.getSupportedMediaTypes() },
            request.getLocale()
        )
    ).toString();
  }

  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ApiError requestMethodNotSupported(HttpRequestMethodNotSupportedException ex, WebRequest request) {
    log.error("Request method not supported", ex);
    return new ApiError(
        ApiError.METHOD_UNSUPPORTED,
        messageSource.getMessage(
            "error.method.unsupported",
            new Object[] { ex.getMethod(), ex.getSupportedHttpMethods() },
            request.getLocale()
        )
    );
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidFormatException.class)
  public ApiError invalidFormatException(InvalidFormatException ex, WebRequest request) throws IOException {
    log.error("Invalid JSON message", ex);
    JsonParser parser = (JsonParser) ex.getProcessor();

    Object value = ex.getValue();
    String fieldName = parser.getCurrentName();
    String targetType = ex.getTargetType().getSimpleName();

    return new ApiError(
        ApiError.INVALID_JSON_MESSAGE,
        messageSource.getMessage(
            "error.json.invalid",
            new Object[] { value, fieldName, targetType },
            request.getLocale()
        )
    );
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(JsonProcessingException.class)
  public ApiError jsonProcessingException(JsonProcessingException ex, WebRequest request) throws IOException {
    log.error("JSON parsing exception", ex);
    JsonParser parser = (JsonParser) ex.getProcessor();

    Object value = parser.getCurrentName() != null ? parser.getCurrentName() : parser.getText();

    JsonLocation location = ex.getLocation();

    int lineNr = location.getLineNr();
    int columnNr = location.getColumnNr();

    return new ApiError(
        ApiError.INVALID_JSON_MESSAGE,
        messageSource.getMessage(
            "error.json.parse",
            new Object[] { value, lineNr, columnNr },
            request.getLocale()
        )
    );
  }

}
