package dmu.dasom.dasom_homepage.exception.handler;

import dmu.dasom.dasom_homepage.exception.*;
import dmu.dasom.dasom_homepage.restful.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InsertConflictException.class)
    public ResponseEntity<ApiResponse<Void>> handleInsertConflictException(InsertConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(false));
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataNotFoundException(DataNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>(false));
    }

    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnAuthorizedAccessException(UnAuthorizedAccessException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false));
    }

    @ExceptionHandler(PwUpdateErrorException.class)
    public ResponseEntity<ApiResponse<Void>> handlePwUpdateErrorException(PwUpdateErrorException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false));
    }

    @ExceptionHandler(LogoutErrorException.class)
    public ResponseEntity<ApiResponse<Void>> handleLogoutErrorException(LogoutErrorException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false));
    }
  
    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<ApiResponse<Void>> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(new ApiResponse<>(false));
    }

    @ExceptionHandler(ProjectException.class)
    public ResponseEntity<ApiResponse<Void>> handleProjectException(ProjectException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false));
    }

    @ExceptionHandler(UniqueCodeExpiredException.class)
    public ResponseEntity<ApiResponse<Void>> handleUniqueCodeExpiredException(UniqueCodeExpiredException ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ApiResponse<>(false));
    }
}
