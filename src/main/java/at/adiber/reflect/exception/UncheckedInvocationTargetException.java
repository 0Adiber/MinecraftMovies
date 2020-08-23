package at.adiber.reflect.exception;

public class UncheckedInvocationTargetException extends UncheckedReflectiveOperationException {

    private final Throwable target;

    public UncheckedInvocationTargetException(Throwable target, String s) {
        super(s, null);
        this.target = target;
    }

    /**
     * Get the cause of this exception. This can be any exception
     * that could possibly be thrown during the invocation of a method.
     *
     * @return The cause of this exception.
     */
    @Override
    public Throwable getCause() {
        return target;
    }
}