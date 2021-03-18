package br.com.application.wallet.models.enums;

import java.util.Arrays;
import java.util.Optional;

public enum ExpenseType {
    HOUSE(1), CAR(2), WATER(3), OTHER(4);

    private final int id;

    ExpenseType(final int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static ExpenseType getExpenseTypeById(final Integer enumId) {
        return Arrays.stream(ExpenseType.values()).filter(value -> value.getId() == enumId).findFirst().orElse(ExpenseType.OTHER);
    }
}
