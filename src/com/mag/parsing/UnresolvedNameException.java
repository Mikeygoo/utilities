package com.mag.parsing;

/**
 *
 * @author michael
 */
public class UnresolvedNameException extends Exception {
    public UnresolvedNameException(String message) {
        super(String.format("Couldn't resolve variable or function (with specified parameters) \'%s\'", message));
    }
}
