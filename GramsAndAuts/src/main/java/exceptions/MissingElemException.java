package exceptions;

/**
 * Created by Octavian on 11/26/2016.
 */
public class MissingElemException extends GrammarException{

    public MissingElemException() {
    }

    public MissingElemException(String message) {
        super(message);
    }

    public MissingElemException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingElemException(Throwable cause) {
        super(cause);
    }

    public MissingElemException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
