package me.xflyiwnl.cities.exception;

public class BankException extends Exception {

    private static final long serialVersionUID = -1887409379939713766L;
    private String message;

    public BankException() {
        super("noname exception");
    }

    public BankException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
