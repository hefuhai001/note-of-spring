package com.example.api.entity;

public enum ActivityStatus {
    NOT_STARTED("not_started"),
    IN_PROGRESS("in_progress"),
    ENDED("ended");

    private final String code;

    ActivityStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ActivityStatus fromCode(String code) {
        for (ActivityStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown activity status: " + code);
    }
}
