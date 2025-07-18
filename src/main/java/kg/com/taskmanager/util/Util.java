package kg.com.taskmanager.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {
    public static <T extends Enum<T>> boolean isValidEnumValue(Class<T> enumClass, String value) {
        try {
            Enum.valueOf(enumClass, value.toUpperCase());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}
