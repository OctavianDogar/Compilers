package exceptions;

/**
 * Created by Octavian on 11/27/2016.
 */
public class AutomatonException extends GrammarException {

    public AutomatonException() {
    }

    public AutomatonException(String message) {
        super(message);
    }

    public AutomatonException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutomatonException(Throwable cause) {
        super(cause);
    }

    public AutomatonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
