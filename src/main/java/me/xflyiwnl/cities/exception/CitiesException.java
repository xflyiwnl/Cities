package me.xflyiwnl.cities.exception;

public class CitiesException extends Exception {

    private static final long serialVersionUID = 6726944385883432286L;
    private String message;

    public CitiesException() {
        super("noname exception");
    }

    public CitiesException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
