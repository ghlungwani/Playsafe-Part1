package za.co.playsafe.conversion.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtility {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    public static String convertObjectToString(Object objectToConvertToString) {
        NullUtility.validateIfObjectIsNullWithErrorMessage("convertObjectToString: objectToConvertToString", objectToConvertToString);

        try {
            return OBJECT_MAPPER.writeValueAsString(objectToConvertToString);
        } catch (JsonProcessingException jpe) {
            throw new RuntimeException("Could not convert objectToConvertToString to string. Object = " + objectToConvertToString);
        }
    }

    public static <T> T convertStringToObject(String stringToConvertToObject, Class<T> expectedClassType) {
        NullUtility.validateIfObjectsAreNullWithErrorMessage("convertStringToObject: stringToConvertToObject, expectedClassType", stringToConvertToObject, expectedClassType);

        try {
            return OBJECT_MAPPER.readValue(stringToConvertToObject, expectedClassType);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Json Processing Exception while converting string to object. String = " + stringToConvertToObject + " Class = " + expectedClassType.toString());
        } catch (IOException e) {
            throw new RuntimeException("Unknown exception occurred while converting string to object. String = " + stringToConvertToObject + " Class = " + expectedClassType.toString());
        }

    }
}
