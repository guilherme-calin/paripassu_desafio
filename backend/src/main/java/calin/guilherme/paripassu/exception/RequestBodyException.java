package calin.guilherme.paripassu.exception;

public class RequestBodyException extends HttpException {
    public RequestBodyException(String message){
        super(message);
    }
    public RequestBodyException(String message, int httpStatusCode){
        super(message);
        this.statusCode = httpStatusCode;
    }
}
