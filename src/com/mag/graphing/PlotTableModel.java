package com.mag.graphing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import com.mag.parsing.Expression;
import com.mag.parsing.UnresolvedNameException;

/**
 *
 * @author michael
 */
public class PlotTableModel implements TableModel {
    private ArrayList<Expression> functions = new ArrayList<Expression>();
    private double lowestX = -4, xStep = 1;
    private int length = 9;
    private HashMap<String, Double> hmap = new HashMap<String, Double>();
    private HashSet<TableModelListener> listeners = new HashSet<TableModelListener>();

    public PlotTableModel(ArrayList<Expression> ar, double lowestX, double xStep, int length) {
        functions.addAll(ar);
        this.lowestX = lowestX;
        this.xStep = xStep;
        this.length = length;
    }

    public void recalculate(double lowestX, double xStep, int length) {
        this.lowestX = lowestX;
        this.xStep = xStep;
        this.length = length;

        for (TableModelListener t : listeners)
            t.tableChanged(new TableModelEvent(this));
    }

    @Override
    public int getRowCount() {
        return length;
    }

    @Override
    public int getColumnCount() {
        return functions.size() + 1;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0)
            return "X";
        else
            return (char)('A' + columnIndex - 1) + "(x)";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0)
            return (lowestX + rowIndex * xStep) + "";
        else {
            Expression e = functions.get(columnIndex - 1);
            hmap.put("x", lowestX + rowIndex * xStep);
            double output = Double.NaN;

            try {
                output = e.solve(hmap);
            } catch (UnresolvedNameException ex) {
                //TODO: handle this
            }

            return Double.isNaN(output) ? "NaN" : output + "";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }
}
