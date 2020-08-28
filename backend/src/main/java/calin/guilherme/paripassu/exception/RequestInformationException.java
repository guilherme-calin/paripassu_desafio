package calin.guilherme.paripassu.exception;

public class RequestInformationException extends HttpException {
    public RequestInformationException(String message){
        super(message);
    }
    public RequestInformationException(String message, int httpStatusCode){
        super(message);
        this.statusCode = httpStatusCode;
    }
}
