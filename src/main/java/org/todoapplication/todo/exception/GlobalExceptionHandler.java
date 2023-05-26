package org.todoapplication.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.todoapplication.todo.exception.response.ResponseError;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CantSaveException.class)
    public ResponseEntity<ResponseError> handleCantSaveException(CantSaveException cantSaveException) {
        int codigoHttp = HttpStatus.BAD_REQUEST.value();
        String descricaoStatus = HttpStatus.BAD_REQUEST.getReasonPhrase();

        ResponseError responseError = new ResponseError(codigoHttp, descricaoStatus, cantSaveException.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ResponseError> handleTaskNotFoundException(TaskNotFoundException taskNotFoundException) {
        int codigoHttp = HttpStatus.NOT_FOUND.value();
        String descricaoStatus = HttpStatus.NOT_FOUND.getReasonPhrase();

        ResponseError responseError = new ResponseError(codigoHttp, descricaoStatus, taskNotFoundException.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseError> handleUserNotFoundException(UserNotFoundException userNotFoundException) {
        int codigoHttp = HttpStatus.NOT_FOUND.value();
        String descricaoStatus = HttpStatus.NOT_FOUND.getReasonPhrase();

        ResponseError responseError = new ResponseError(codigoHttp, descricaoStatus, userNotFoundException.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseError);
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ResponseError> handlePasswordInvalidException(PasswordInvalidException passwordInvalidException) {
        int codigoHttp = HttpStatus.BAD_REQUEST.value();
        String descricaoStatus = HttpStatus.BAD_REQUEST.getReasonPhrase();

        ResponseError responseError = new ResponseError(codigoHttp, descricaoStatus, passwordInvalidException.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseError);
    }
}
