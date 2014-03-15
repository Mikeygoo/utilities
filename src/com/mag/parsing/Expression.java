package com.mag.parsing;

import java.util.Map;

/**
 *
 * @author michael
 */
public interface Expression {
    double solve(Map<String, Double> variables) throws UnresolvedNameException;
}
