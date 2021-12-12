package org.edwork.goodcoffee.exceptions;

public class GoodCoffeeException extends RuntimeException {

    public GoodCoffeeException(Throwable cause)
    {
        super(cause);
    }

    public GoodCoffeeException(String message) {
        super(message);
    }

    public GoodCoffeeException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
