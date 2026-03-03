package vincenzocalvaruso.Heritage_Kitchen.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private List<String> errorsMessages;

    public ValidationException(List<String> errorsMessages) {
        super("ATTENZIONE! errori nel payload");
        this.errorsMessages = errorsMessages;
    }
}