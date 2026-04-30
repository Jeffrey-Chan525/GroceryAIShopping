package com.smartspend.model.validation;

public class ValidationResult {
    private boolean valid;
    private String errorMessage;
    public boolean isValid() {
        return valid;
    }
    public void setValid(boolean valid) {}

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {}

    public ValidationResult(boolean valid, String errorMessage) {
        this.valid = valid;
        this.errorMessage = errorMessage;
    }
}
