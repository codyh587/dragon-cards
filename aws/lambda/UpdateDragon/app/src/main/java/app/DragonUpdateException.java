package app;

public class DragonUpdateException extends RuntimeException {

    public DragonUpdateException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

}
