package supporthub_commerce_api.exception.dto;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import supporthub_commerce_api.exception.*;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandlerDto {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerNotFound(NotFoundException ex,
                                                         HttpServletRequest request){

        ErrorResponse errorNotFound = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorNotFound);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handlerBadRequest(BadRequestException br,
                                                           HttpServletRequest request){

        ErrorResponse errorBadRequest = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                br.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBadRequest);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handlerForbidden(ForbiddenException fb,
                                                          HttpServletRequest request){

        ErrorResponse errorForbidden = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                fb.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorForbidden);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handlerConflict(ConflictException ce,
                                                         HttpServletRequest request){

        ErrorResponse errorConflict = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ce.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorConflict);
    }
}
