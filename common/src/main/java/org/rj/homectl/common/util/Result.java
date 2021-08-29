package org.rj.homectl.common.util;

import java.util.Optional;
import java.util.function.Function;

public class Result<T, E> {
    private final T value;
    private final E error;

    public static<T, E> Result<T, E> Ok(T value) {
        return new Result<>(value, null);
    }

    public static <T, E> Result<T, E> Err(E err) {
        if (err == null) throw new IllegalArgumentException("Cannot create an error result with null error");
        return new Result<>(null, err);
    }

    private Result(T value, E error) {
        this.value = value;
        this.error = error;
    }

    public boolean isOk() { return error == null; }
    public boolean isErr() { return !isOk(); }

    public T getValue() {
        if (!isOk()) throw new RuntimeException("Cannot retrieve value of an error result");
        return value;
    }

    public E getError() {
        if (!isErr()) throw new RuntimeException("Cannot retrieve error for a successful result");
        return error;
    }

    public Optional<T> getValueOptional() { return isOk() ? Optional.ofNullable(getValue()) : Optional.empty(); }
    public Optional<E> getErrorOptional() { return isErr() ? Optional.of(getError()) : Optional.empty(); }

    public <U> Result<U, E> mapValue(Function<T, U> f) {
        return Ok(f.apply(getValue()));
    }

    public <U> Result<T, U> mapError(Function<E, U> f) {
        return Err(f.apply(getError()));
    }

    public <U> U mapResult(Function<T, U> onOk, Function<E, U> onErr) {
        if (isOk())     return onOk.apply(getValue());
        else            return onErr.apply(getError());
    }
}
