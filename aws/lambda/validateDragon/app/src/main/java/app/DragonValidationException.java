package app;

public class DragonValidationException extends RuntimeException {

    public DragonValidationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

}
