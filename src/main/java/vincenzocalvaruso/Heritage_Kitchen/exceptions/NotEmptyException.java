package vincenzocalvaruso.Heritage_Kitchen.exceptions;

public class NotEmptyException extends RuntimeException {
    public NotEmptyException() {
        super("Il file non puo' essere vuoto!");
    }
}
