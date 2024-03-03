package edu.java.bot.controller;

import edu.java.bot.dto.response.ApiErrorResponse;
import edu.java.bot.exception.UpdateAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class BotExceptionController {
    private static final String BAD_REQUEST = "400 BAD REQUEST";
    private static final String WRONG_PARAMETERS = "Wrong parameters!";

    @ExceptionHandler(UpdateAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleUpdate(UpdateAlreadyExistsException exception) {
        return new ApiErrorResponse(
            WRONG_PARAMETERS,
            BAD_REQUEST,
            UpdateAlreadyExistsException.class.getName(),
            exception.getMessage(),
            getStackTrace(exception));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleUpdate(MethodArgumentNotValidException exception) {
        return new ApiErrorResponse(
            WRONG_PARAMETERS,
            exception.getStatusCode().toString(),
            exception.getClass().getSimpleName(),
            exception.getMessage(),
            getStackTrace(exception));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handle(HandlerMethodValidationException exception) {
        return new ApiErrorResponse(
            WRONG_PARAMETERS,
            BAD_REQUEST,
            HandlerMethodValidationException.class.getName(),
            exception.getMessage(),
            getStackTrace(exception));
    }

    private List<String> getStackTrace(Exception exception) {
        List<String> stack = new ArrayList<>();
        for (StackTraceElement trace: exception.getStackTrace()) {
            stack.add(trace.toString());
        }
        return stack;
    }
}
