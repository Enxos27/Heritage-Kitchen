package vincenzocalvaruso.Heritage_Kitchen.exceptions;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
