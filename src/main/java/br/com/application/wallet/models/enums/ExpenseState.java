package br.com.application.wallet.models.enums;

import java.util.Arrays;
import java.util.Optional;

public enum ExpenseState {
    CLOSED(1), OPEN(2);

    private final int id;

    ExpenseState(final int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static Optional<ExpenseState> getExpenseStateById(final Integer enumId) {
        return Arrays.stream(ExpenseState.values()).filter(value -> value.getId() == enumId).findFirst();
    }
}
