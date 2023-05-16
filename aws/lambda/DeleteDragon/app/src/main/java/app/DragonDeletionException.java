package app;

public class DragonDeletionException extends RuntimeException {

    public DragonDeletionException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

}
