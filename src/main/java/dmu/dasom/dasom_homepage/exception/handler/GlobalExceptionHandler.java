package dmu.dasom.dasom_homepage.exception.handler;

import dmu.dasom.dasom_homepage.exception.AccessTokenExpiredException;
import dmu.dasom.dasom_homepage.exception.DataNotFoundException;
import dmu.dasom.dasom_homepage.exception.InsertConflictException;
import dmu.dasom.dasom_homepage.exception.UnAuthorizedAccessException;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InsertConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleInsertConflictException(InsertConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(false));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataNotFoundException(DataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false));
    }

    @ExceptionHandler(AccessTokenExpiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessTokenExpiredException(AccessTokenExpiredException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false));
    }

    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnAuthorizedAccessException(UnAuthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ApiResponse<>(false));
    }
}
