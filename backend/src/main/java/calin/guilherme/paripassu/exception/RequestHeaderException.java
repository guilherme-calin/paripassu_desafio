package calin.guilherme.paripassu.exception;

public class RequestHeaderException extends HttpException {
    public RequestHeaderException(String message){
        super(message);
    }
    public RequestHeaderException(String message, int httpStatusCode){
        super(message);
        this.statusCode = httpStatusCode;
    }
}
