package de.hpi.urlcleaner.api;

import de.hpi.urlcleaner.dto.ErrorResponse;
import de.hpi.urlcleaner.exceptions.CouldNotCleanURLException;
import de.hpi.urlcleaner.exceptions.ShopBlacklistedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Component
@Slf4j
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ShopBlacklistedException.class})
    protected ResponseEntity<Object> handleBlacklistException(Exception e, WebRequest request) {
        log.info(e.getMessage());
        return new ErrorResponse().withError(e).send(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {CouldNotCleanURLException.class})
    protected ResponseEntity<Object> handleCouldNotCleanException(Exception e, WebRequest request) {
        log.info(e.getMessage());
        return new ErrorResponse().withError(e).send(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
