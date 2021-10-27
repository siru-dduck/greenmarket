package com.hanium.product.repository.querydsl;

public enum SearchOperation {
    EQUALITY, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS;

    public static final String[] SIMPLE_OPERATION_SET = {"=", ">", "<", "~", "[]", "[", "]"};

    public static SearchOperation getSimpleOperation(String input) {
        switch (input) {
            case "=":
                return EQUALITY;
            case ">":
                return GREATER_THAN;
            case "<":
                return LESS_THAN;
            case "~":
                return LIKE;
            case "[]":
                return CONTAINS;
            case "[":
                return STARTS_WITH;
            case "]":
                return ENDS_WITH;
            default:
                return null;
        }
    }
}
