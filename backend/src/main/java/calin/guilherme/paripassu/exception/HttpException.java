package calin.guilherme.paripassu.exception;

abstract public class HttpException extends Exception {
    int statusCode;

    public HttpException(String message){
        super(message);
        this.statusCode = 400;
    }

    public HttpException(String message, int httpStatusCode){
        super(message);
        this.statusCode = httpStatusCode;
    }

    public void setStatusCode(int statusCode){
        this.statusCode = statusCode;
        return;
    };

    public int getStatusCode(){
        return this.statusCode;
    }
}
