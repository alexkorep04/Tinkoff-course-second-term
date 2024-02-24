package edu.java.controller;

import edu.java.dto.response.ApiErrorResponse;
import edu.java.exception.ChatAlreadyExistsException;
import edu.java.exception.LinkAlreadyExistsException;
import edu.java.exception.NoChatException;
import edu.java.exception.NoLinkException;
import edu.java.exception.UpdateAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
public class ExceptionController {
    private static final String BAD_REQUEST = "400 BAD REQUEST";
    private static final String WRONG_PARAMETERS = "Wrong parameters!";

    @ExceptionHandler(UpdateAlreadyExistsException.class)
    public ApiErrorResponse handleUpdate(UpdateAlreadyExistsException exception) {
        return new ApiErrorResponse(
            WRONG_PARAMETERS,
            BAD_REQUEST,
            UpdateAlreadyExistsException.class.getName(),
            exception.getMessage(),
            getStackTrace(exception));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleUpdate(MethodArgumentNotValidException exception) {
        return new ApiErrorResponse(
            WRONG_PARAMETERS,
            exception.getStatusCode().toString(),
            exception.getClass().getSimpleName(),
            exception.getMessage(),
            getStackTrace(exception));
    }

    @ExceptionHandler(ChatAlreadyExistsException.class)
    public ApiErrorResponse handleRegistrationChat(ChatAlreadyExistsException exception) {
        return new ApiErrorResponse(
            WRONG_PARAMETERS,
            BAD_REQUEST,
            ChatAlreadyExistsException.class.getName(),
            exception.getMessage(),
            getStackTrace(exception));
    }

    @ExceptionHandler(NoChatException.class)
    public ApiErrorResponse handleRemovingChat(NoChatException exception) {
        return new ApiErrorResponse(
            WRONG_PARAMETERS,
            BAD_REQUEST,
            NoChatException.class.getName(),
            exception.getMessage(),
            getStackTrace(exception));
    }

    @ExceptionHandler(LinkAlreadyExistsException.class)
    public ApiErrorResponse handleLink(LinkAlreadyExistsException exception) {
        return new ApiErrorResponse(
            WRONG_PARAMETERS,
            BAD_REQUEST,
            LinkAlreadyExistsException.class.getName(),
            exception.getMessage(),
            getStackTrace(exception));
    }

    @ExceptionHandler(NoLinkException.class)
    public ApiErrorResponse handleLink(NoLinkException exception) {
        return new ApiErrorResponse(
            WRONG_PARAMETERS,
            BAD_REQUEST,
            NoLinkException.class.getName(),
            exception.getMessage(),
            getStackTrace(exception));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
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
