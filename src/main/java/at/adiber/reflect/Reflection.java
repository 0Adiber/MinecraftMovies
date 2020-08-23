package at.adiber.reflect;

import at.adiber.reflect.exception.*;
import java.lang.reflect.*;

public class Reflection {

    /**
     * Find a field in the given class with the given name and
     * get its value from it using the given object instance.
     * If the field is not currently accessible, then an attempt
     * will be made to make it accessible.
     * <p>
     * Any checked {@link Exception}s will be wrapped into a similar
     * unchecked {@link RuntimeException} and re-thrown.
     *
     * @param clazz The class to get the field from.
     * @param instance The instance invoke the field getter on.
     * @param name The name of the field to find.
     * @param <T> The class type of the field.
     * @return The value contained within the field.
     * @throws ClassCastException If the {@link T} is not the field's type.
     * @throws SecurityException If the field was inaccessible
     *                           and could not be made accessible.
     * @throws UncheckedNoSuchFieldException If the field could not be found.
     * @throws UncheckedIllegalAccessException If there was an attempt to access the
     *                                         field that failed.
     */
    public static <T> T getFieldValue(Class<?> clazz, Object instance, String name) throws ClassCastException,
            SecurityException, UncheckedNoSuchFieldException, UncheckedIllegalAccessException {
        return getFieldValue(findField(clazz, name), instance);
    }

    /**
     * Get the value of the given field using the given object instance.
     * If the field is not currently accessible, then an attempt will
     * be made to make it accessible.
     * <p>
     * Any checked {@link Exception}s will be wrapped into a similar
     * unchecked {@link RuntimeException} and re-thrown.
     *
     * @param field The field to get the value from.
     * @param instance The instance invoke the field getter on.
     * @param <T> The class type of the field.
     * @return The value contained within the field.
     * @throws NullPointerException If the field is {@code null}.
     * @throws ClassCastException If the {@link T} is not the field's type.
     * @throws SecurityException If the field was inaccessible
     *                           and could not be made accessible.
     * @throws UncheckedIllegalAccessException If there was an attempt to access the
     *                                         field that failed.
     */
    // May need to validate the generic return type T by taking a Class<T> as parameter
    public static <T> T getFieldValue(Field field, Object instance) throws ClassCastException,
            SecurityException, UncheckedIllegalAccessException {

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        try {
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            throw wrapException(e);
        }
    }

    /**
     * Find a field in the given class with the given name and
     * set its value using the given object instance.
     * If the field is not currently accessible, then an attempt
     * will be made to make it accessible.
     * <p>
     * Any checked {@link Exception}s will be wrapped into a similar
     * unchecked {@link RuntimeException} and re-thrown.
     *
     * @param clazz The class to get the field from.
     * @param instance The instance invoke the field setter on.
     * @param name The name of the field to find.
     * @param value The value to set the value of the field to.
     * @throws SecurityException If the field was inaccessible
     *                           and could not be made accessible.
     * @throws UncheckedNoSuchFieldException If the field could not be found.
     * @throws UncheckedIllegalAccessException If there was an attempt to access the
     *                                         field that failed.
     */
    public static void setFieldValue(Class<?> clazz, Object instance, String name, Object value)
            throws SecurityException, UncheckedNoSuchFieldException, UncheckedIllegalAccessException {
        setFieldValue(findField(clazz, name), instance, value);
    }

    /**
     * Set the value of the given field using the given object instance.
     * If the field is not currently accessible, then an attempt
     * will be made to make it accessible.
     * <p>
     * Any checked {@link Exception}s will be wrapped into a similar
     * unchecked {@link RuntimeException} and re-thrown.
     *
     * @param field The field to set the value of.
     * @param instance The instance invoke the field setter on.
     * @param value The value to set the value of the field to.
     * @throws NullPointerException If the field is {@code null}.
     * @throws SecurityException If the field was inaccessible
     *                           and could not be made accessible.
     * @throws UncheckedIllegalAccessException If there was an attempt to access the
     *                                         field that failed.
     */
    public static void setFieldValue(Field field, Object instance, Object value)
            throws SecurityException, UncheckedIllegalAccessException {

        if (!field.isAccessible()) {
            field.setAccessible(true);
        }

        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw wrapException(e);
        }
    }

    /**
     * Find a field with the given name in the given class.
     * If the field is not declared by the given class, then
     * the super class (if applicable) will be searched for
     * a matching field as well.
     *
     * @param clazz The class to get the field from.
     * @param name The name of the field to find.
     * @return The field that matches the name.
     * @throws UncheckedNoSuchFieldException If no fields matching the search
     *                                       parameters are found.
     */
    public static Field findField(Class<?> clazz, String name) throws UncheckedNoSuchFieldException {

        NoSuchFieldException exception;
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            exception = e;
        }

        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {

            try {
                return superClazz.getField(name);
            } catch (NoSuchFieldException ignored) {
            }
        }

        throw wrapException(exception);
    }

    private static RuntimeException wrapException(Exception e) {

        if (e instanceof IllegalAccessException) {
            return new UncheckedIllegalAccessException(e.getMessage());
        }

        if (e instanceof InvocationTargetException) {
            return new UncheckedInvocationTargetException(e.getCause(), e.getMessage());
        }

        if (e instanceof InstantiationException) {
            return new UncheckedInstantiationException(e.getMessage());
        }

        if (e instanceof NoSuchMethodException) {
            throw new UncheckedNoSuchMethodException(e.getMessage());
        }

        if (e instanceof NoSuchFieldException) {
            throw new UncheckedNoSuchFieldException(e.getMessage());
        }

        if (e instanceof ClassNotFoundException) {
            throw new UncheckedClassNotFoundException(e.getMessage(), e.getCause());
        }

        return e instanceof RuntimeException ? (RuntimeException) e : new RuntimeException(e);
    }

}
