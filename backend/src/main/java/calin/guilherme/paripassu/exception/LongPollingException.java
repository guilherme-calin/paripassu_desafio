package calin.guilherme.paripassu.exception;

public class LongPollingException extends HttpException {
    public LongPollingException(String message){
        super(message);
    }
    public LongPollingException(String message, int httpStatusCode){
        super(message);
        this.statusCode = httpStatusCode;
    }
}
