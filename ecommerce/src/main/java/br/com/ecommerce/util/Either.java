package br.com.ecommerce.util;


import java.util.function.Function;

/**
 * Allows to explicitly handle errors, without throwing exceptions.
 * @param <L> Represents the side with "wrong" object.
 * @param <R> Represents the side with the expected result.
 */
public class Either<L, R> {
    private final L wrong;
    private final R right;
    private final boolean isCorrect;

    private Either(R right, L wrong) {
        this.right = right;
        this.wrong = wrong;
        this.isCorrect = right != null;
    }

    public static <L, R> Either<L, R> error(L wrong) {
        return new Either<>(null, wrong);
    }

    public static <L, R> Either<L, R> correct(R right) {
        return new Either<>(right, null);
    }

    public L getError() {
        return wrong;
    }

    public <X extends RuntimeException> R orElseThrow(Function<L, X> otherwiseFunction) {
        if (valid()) {
            return this.right;
        }
        throw otherwiseFunction.apply(wrong);
    }

    public boolean valid() {
        return isCorrect;
    }
}