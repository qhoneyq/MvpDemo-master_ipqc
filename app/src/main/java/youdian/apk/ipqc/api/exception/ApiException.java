package youdian.apk.ipqc.api.exception;



public class ApiException extends RuntimeException {
    private int errorCode;
    private String errorMessage;

    public ApiException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApiException(int errorCode, String message, String errorMessage) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
