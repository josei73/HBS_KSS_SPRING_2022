
package de.hsbremen.mkss.restservice.controller;


import de.hsbremen.mkss.restservice.exception.ErrorMessage;
import de.hsbremen.mkss.restservice.exception.ItemNotFound;
import de.hsbremen.mkss.restservice.exception.OrderNotFoundException;
import de.hsbremen.mkss.restservice.exception.OrderStatusException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;

/**
 * This class is for error handling and helps to response an object with a message
 * @ExceptionHandler configures the advice to only respond if an EmployeeNotFoundException is thrown.
 * @ResponseStatus says to issue an HttpStatus.NOT_FOUND, i.e. an HTTP 404.
 *
 * The body of the advice generates the content. In this case, it gives the message of the exception.
 */
@ControllerAdvice
public class ChangeResultView {

    private String message;
    private Object object;


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    @ExceptionHandler({OrderNotFoundException.class, ItemNotFound.class, OrderStatusException.class})
    public final ResponseEntity<ErrorMessage> handleException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();

        if (ex instanceof OrderNotFoundException) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            OrderNotFoundException unfe = (OrderNotFoundException) ex;

            return handleOrderNotFoundException(unfe, headers, status, request);
        } else if (ex instanceof ItemNotFound) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            ItemNotFound unfe = (ItemNotFound) ex;
            return handleItemNotFoundException(unfe, headers, status, request);

        } else if (ex instanceof OrderStatusException) {
            HttpStatus status = HttpStatus.FORBIDDEN;
            OrderStatusException unf = (OrderStatusException) ex;
            return handleOrderStatusException(unf,headers,status,request);


        }
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        return handleExceptionInternal(ex, null, headers, status, request);
    }


    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private ResponseEntity<ErrorMessage> handleOrderNotFoundException(OrderNotFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorMessage errorMessages = new ErrorMessage(ex.getMessage(),HttpStatus.NOT_FOUND.toString());
        return handleExceptionInternal(ex, errorMessages, headers, status, request);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    private ResponseEntity<ErrorMessage> handleItemNotFoundException(ItemNotFound ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorMessage errorMessages = new ErrorMessage(ex.getMessage(),HttpStatus.NOT_FOUND.toString());
        return handleExceptionInternal(ex, errorMessages, headers, status, request);
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    private ResponseEntity<ErrorMessage> handleOrderStatusException(OrderStatusException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorMessage errorMessages = new ErrorMessage(ex.getMessage(),HttpStatus.FORBIDDEN.toString());
        return handleExceptionInternal(ex, errorMessages, headers, status, request);
    }


    /**
     * A single place to customize the response body of all Exception types.
     */
    protected ResponseEntity<ErrorMessage> handleExceptionInternal(Exception ex, ErrorMessage body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity<>(body, headers, status);
    }


    public ChangeResultView(BindingResult result) {
        this.message = result.toString();
    }

    public ChangeResultView(Object object, String message) {
        this.object = object;
        this.message = message;

    }

    public ChangeResultView(RuntimeException exception) {
        this.message = exception.toString();
    }

    public ChangeResultView() {
    }


    static ChangeResultView from(BindingResult bindingResult) {

        ChangeResultView view = new ChangeResultView(bindingResult);

        return view;
    }


    public static ChangeResultView success(Object o) {
        ChangeResultView view = new ChangeResultView(o, "Save Object");
        return view;
    }

    public static ChangeResultView update(Object o) {
        ChangeResultView view = new ChangeResultView(o, "Update Object");
        return view;
    }

    public static ChangeResultView delete(Object o) {

        ChangeResultView view = new ChangeResultView(o, "Delete Object");
        return view;
    }

    @Override
    public String toString() {
        return "ChangeResultView{" + "message=" + message + ", object=" + object + '}';
    }


    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }


}


