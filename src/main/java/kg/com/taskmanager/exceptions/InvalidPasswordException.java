package kg.com.taskmanager.exceptions;

import lombok.Getter;

@Getter
public class InvalidPasswordException extends RuntimeException {
    private final String messageCode;

    public InvalidPasswordException(String messageCode) {
        super(messageCode);
        this.messageCode = messageCode;
    }
}