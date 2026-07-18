package supporthub_commerce_api.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import supporthub_commerce_api.exception.BadRequestException;
import supporthub_commerce_api.exception.ConflictException;
import supporthub_commerce_api.exception.ForbiddenException;
import supporthub_commerce_api.exception.NotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handlerBadRequestException(BadRequestException br) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(br.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handlerNotFoundException(NotFoundException nt){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(nt.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<String> HandlerForbiddenException(ForbiddenException fb){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(fb.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> HandlerConflictException(ConflictException ce){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ce.getMessage());
    }
}
