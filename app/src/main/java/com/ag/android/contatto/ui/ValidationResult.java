package com.ag.android.contatto.ui;

public class ValidationResult {
    private boolean mIsOk;
    private String mMessage;

    public static final ValidationResult OK = new ValidationResult(true, null);

    public ValidationResult(boolean isOk, String message) {
        mIsOk = isOk;
        mMessage = message;
    }


    public boolean isOk() {
        return mIsOk;
    }
    public String getMessage() {
        return mMessage;
    }
}
