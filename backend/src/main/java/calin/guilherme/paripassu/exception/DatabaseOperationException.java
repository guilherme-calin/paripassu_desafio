package calin.guilherme.paripassu.exception;

public class DatabaseOperationException extends HttpException {
    public DatabaseOperationException(String message){
        super(message);
    }
    public DatabaseOperationException(String message, int httpStatusCode){
        super(message);
        this.statusCode = httpStatusCode;
    }
}
