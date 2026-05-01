package com.smartspend.model.validation;

import com.smartspend.model.UserRegistrationDTO;

/**
 * the base Validator class
 * using the chain of responsibility design pattern
 */
public abstract class UserEntryValidator {
    private UserEntryValidator next;

    private String errorMessage;

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String message){
        this.errorMessage = message;
    }

    /**
     * Builds the chain of validator objects
     * @param first the first validator in the chain
     * @param chain all the other validators in the chain
     * @return the first validator
     */
    public static UserEntryValidator link(UserEntryValidator first, UserEntryValidator... chain) {
        UserEntryValidator head = first;
        for (UserEntryValidator nextValidator : chain) {
            head.next = nextValidator;
            head = nextValidator;
        }
        return first;
    }

    /**
     * this is for subclasses to implement
     */
    public abstract ValidationResult check(UserRegistrationDTO userRegistrationDTO);


    /**
     * checks the next object in the chain or ends traversal if we're
     * in the last chain
     */
    protected ValidationResult chainNext(UserRegistrationDTO userRegistrationDTO) {
        if (next == null) {
            String errorMessage = null;
            return new ValidationResult(true, errorMessage);
        }
        return next.check(userRegistrationDTO);
    }
}
