package com.mag.parsing;

import java.util.Map;

/**
 *
 * @author admin
 */
public abstract class BasicNode implements Expression {
    public int precedence;

    @Override
    public final double solve(Map<String, Double> variables) throws UnresolvedNameException {
        return toNumberValue(variables);
    }

    protected abstract double toNumberValue(Map<String, Double> vars) throws UnresolvedNameException;
    protected abstract boolean hasNull();
    public abstract String toString();
}
