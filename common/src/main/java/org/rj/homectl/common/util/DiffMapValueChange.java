package org.rj.homectl.common.util;

public class DiffMapValueChange<T> {
    private T previousValue;
    private T newValue;

    public DiffMapValueChange() { }

    public DiffMapValueChange(T previousValue, T newValue) {
        this.previousValue = previousValue;
        this.newValue = newValue;
    }

    public T getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(T previousValue) {
        this.previousValue = previousValue;
    }

    public T getNewValue() {
        return newValue;
    }

    public void setNewValue(T newValue) {
        this.newValue = newValue;
    }
}
