package za.co.playsafe.conversion.util;

import java.util.Objects;

public class NullUtility {

    public static final String NULL_OBJECT_PASSED_ERROR_MESSAGE = "Object list passed through is null.";
    public static final String DEFAULT_NULL_ERROR_MESSAGE = "Null Value.";
    public static final String PARAMETER_IS_NULL_ERROR_MESSAGE = "Parameter %s is null.";
    public static final String CHARACTER_SEPARATOR = " ";

    public static void validateIfObjectsAreNullWithErrorMessage(String errorMessage, Object... objects) {
        if (Objects.isNull(objects)) {
            throw new RuntimeException(String.format(NULL_OBJECT_PASSED_ERROR_MESSAGE, errorMessage));
        }

        errorMessage = errorMessage == null ? DEFAULT_NULL_ERROR_MESSAGE : errorMessage;

        StringBuffer errorMessageBuilder = new StringBuffer(errorMessage);

        if (hasNullObjects(objects, errorMessageBuilder)) {
            throw new RuntimeException(errorMessageBuilder.toString());
        }
    }

    private static boolean hasNullObjects(Object[] objects, StringBuffer errorMessageBuilder) {
        boolean hasANullObject = false;

        for (int i = 0; i < objects.length; i++) {
            if (Objects.isNull(objects[i])) {
                errorMessageBuilder.append(CHARACTER_SEPARATOR);
                errorMessageBuilder.append(String.format(PARAMETER_IS_NULL_ERROR_MESSAGE, i + 1));
                if (!hasANullObject) {
                    hasANullObject = true;
                }
            }
        }

        return hasANullObject;
    }

    public static void validateIfObjectsAreNull(Object... objects) {
        validateIfObjectsAreNullWithErrorMessage(DEFAULT_NULL_ERROR_MESSAGE, objects);
    }

    public static void validateIfObjectIsNull(Object object) {
        if (Objects.isNull(object)) {
            throw new RuntimeException(DEFAULT_NULL_ERROR_MESSAGE);
        }
    }

    public static void validateIfObjectIsNullWithErrorMessage(String errorMessage, Object object) {
        if (Objects.isNull(object)) {
            errorMessage = errorMessage == null ? DEFAULT_NULL_ERROR_MESSAGE : String.format("%s %s", errorMessage, DEFAULT_NULL_ERROR_MESSAGE);;
            throw new RuntimeException(errorMessage);
        }
    }
}