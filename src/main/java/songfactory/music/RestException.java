package songfactory.music;

/**
 * Rest exception class. Should be thrown
 * when operations meant for notes are
 * called on rests.
 */
public class RestException extends Exception {

    /**
     * RestException constructor.
     *
     * @param errorMessage message to print when error is thrown
     */
    public RestException(String errorMessage) {
        super(errorMessage);

    } // Constructor

} // RestException