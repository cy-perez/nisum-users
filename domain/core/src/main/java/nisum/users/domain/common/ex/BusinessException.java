package nisum.users.domain.common.ex;

import java.util.function.Supplier;

public class BusinessException extends ApplicationException {

    public enum Type {

        ERROR_SAVE_USER("Error save user"),
        ERROR_CONSULT_USER_BY_ID("Error consult user by id"),
        ERROR_CONSULT_USERS("Error consult users");

        private final String message;

        public String getMessage() {
            return message;
        }

        public BusinessException build(String exceptionInfo) {
            return new BusinessException(this, exceptionInfo);
        }

        public Supplier<Throwable> defer() {
            return () -> new BusinessException(this, "");
        }

        Type(String message) {
            this.message = message;
        }

    }

    private final Type type;

    public BusinessException(Type type, String exceptionInfo) {
        super(type.message + " " + exceptionInfo);
        this.type = type;
    }

    @Override
    public String getCode() {
        return type.name();
    }

    public Type getType() {
        return type;
    }


}
