package com.EnumDayTask.data.Enum;

public enum ProfileCompleteness {

    ZERO(0),
    TWENTY(20),
    SIXTY(60),
    EIGHTY(80),
    HUNDRED(100);

    private final int value;

    ProfileCompleteness(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

}
