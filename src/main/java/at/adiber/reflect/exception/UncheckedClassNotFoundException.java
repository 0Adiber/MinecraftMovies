package at.adiber.reflect.exception;

public class UncheckedClassNotFoundException extends UncheckedReflectiveOperationException {

    private final Throwable cause;

    public UncheckedClassNotFoundException(String s, Throwable cause) {
        super(s, null);  //  Disallow initCause
        this.cause = cause;
    }

    /**
     * Returns the cause of this exception (the exception that was raised
     * if an error occurred while attempting to load the class; otherwise
     * <tt>null</tt>).
     *
     * @return The cause of this exception.
     */
    @Override
    public Throwable getCause() {
        return cause;
    }
}