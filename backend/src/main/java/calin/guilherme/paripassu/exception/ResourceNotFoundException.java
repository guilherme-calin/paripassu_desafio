package calin.guilherme.paripassu.exception;

public class ResourceNotFoundException extends HttpException {
    public ResourceNotFoundException(String message){
        super(message);
    }
    public ResourceNotFoundException(String message, int httpStatusCode){
        super(message);
        this.statusCode = httpStatusCode;
    }
}

