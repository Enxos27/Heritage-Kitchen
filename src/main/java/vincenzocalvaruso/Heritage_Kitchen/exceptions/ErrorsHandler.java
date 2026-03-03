package vincenzocalvaruso.Heritage_Kitchen.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vincenzocalvaruso.Heritage_Kitchen.payloads.ErrorsPayload;
import vincenzocalvaruso.Heritage_Kitchen.payloads.ErrorsWithListDTO;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorsHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorsPayload handleBadRequest(BadRequestException ex) {
        return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorsPayload handleNotFound(NotFoundException ex) {
        return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorsPayload handleForbidden(AuthorizationDeniedException ex) {
        return new ErrorsPayload("! ! ! ACCESSO NEGATO ! ! ! Non sei fornito dei permessi necessari per accedere!", LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public ErrorsPayload handleGenericServerError(Exception ex) {
        ex.printStackTrace();
        return new ErrorsPayload("C'è stato un imprevisto, ci scusiamo per il disagio e la preghiamo a riprovare più tardi!", LocalDateTime.now());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorsWithListDTO handleValidationException(ValidationException ex) {
        return new ErrorsWithListDTO(ex.getMessage(), LocalDateTime.now(), ex.getErrorsMessages());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    public ErrorsPayload handleUnauthorized(UnauthorizedException ex) {
        return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
    }

//    @ExceptionHandler(NotEmptyException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
//    public ErrorsPayload handleNotFound(NotEmptyException ex) {
//        return new ErrorsPayload(ex.getMessage(), LocalDateTime.now());
//    }

}
