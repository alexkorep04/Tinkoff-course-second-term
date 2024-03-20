package edu.java.controller;

import edu.java.dto.response.ApiErrorResponse;
import edu.java.exception.ChatAlreadyExistsException;
import edu.java.exception.LinkAlreadyExistsException;
import edu.java.exception.NoChatException;
import edu.java.exception.NoResourceException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class ScrapperExceptionController {
    private static final String BAD_REQUEST = "400 BAD REQUEST";
    private static final String NOT_FOUND = "404 NOT FOUND";
    private static final String WRONG_PARAMETERS = "Wrong parameters!";
    private static final String LINK_NOT_FOUND = "Link not found!";
    private static final String CHAT_NOT_FOUND = "Chat not found!";

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

    @ExceptionHandler(ChatAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleRegistrationChat(ChatAlreadyExistsException exception) {
        return new ApiErrorResponse(
            WRONG_PARAMETERS,
            BAD_REQUEST,
            ChatAlreadyExistsException.class.getName(),
            exception.getMessage(),
            getStackTrace(exception));
    }

    @ExceptionHandler(NoChatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleRemovingChat(NoChatException exception) {
        return new ApiErrorResponse(
            WRONG_PARAMETERS,
            BAD_REQUEST,
            NoChatException.class.getName(),
            exception.getMessage(),
            getStackTrace(exception));
    }

    @ExceptionHandler(LinkAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleLink(LinkAlreadyExistsException exception) {
        return new ApiErrorResponse(
            WRONG_PARAMETERS,
            BAD_REQUEST,
            LinkAlreadyExistsException.class.getName(),
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

    @ExceptionHandler(NoResourceException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handle(NoResourceException exception) {
        return new ApiErrorResponse(
            LINK_NOT_FOUND,
            NOT_FOUND,
            NoResourceException.class.getName(),
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
