package org.rj.homectl.common.util;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

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

    public <T2, E2> Result<T2, E2> map(Function<T, T2> onOk, Function<E, E2> onErr) {
        return new Result<>(
                getValueOptional().map(onOk).orElse(null),
                getErrorOptional().map(onErr).orElse(null));
    }

    public <U> Result<U, E> map(Function<T, U> f) {
        if (isOk()) return Ok(f.apply(value));
        else        return Err(error);
    }

    public <U> Result<T, U> mapError(Function<E, U> f) {
        if (isErr()) return Err(f.apply(error));
        else         return Ok(value);
    }

    public <U> Result<U, E> flatMap(Function<T, Result<U, E>> onOk) {
        if (isOk()) return onOk.apply(value);
        else        return Result.Err(error);
    }

    public <T2, E2> Result<T2, E2> flatMap(Function<T, Result<T2, E2>> onOk, Function<E, Result<T2, E2>> onErr) {
        if (isOk())     return onOk.apply(getValue());
        else            return onErr.apply(getError());
    }

    public T orElse(T onError) {
        return isOk() ? value : onError;
    }

    public T orElseGet(Function<E, T> onError) {
        return isOk() ? value : onError.apply(error);
    }

    public T orElseThrow(Function<E, RuntimeException> onError) {
        if (isErr()) throw onError.apply(error);
        return value;
    }
}
