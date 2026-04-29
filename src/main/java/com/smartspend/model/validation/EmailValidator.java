package com.smartspend.model.validation;


import com.smartspend.model.UserRegistrationDTO;

public class EmailValidator extends UserEntryValidator {
    @Override
    public ValidationResult check(UserRegistrationDTO userRegistrationDTO) {
        String email = userRegistrationDTO.getEmail();

        if (email == null || email.isEmpty()) {
            String errorMessage = "Email address is empty";
            return new ValidationResult(false, errorMessage);
        }
        return chainNext(userRegistrationDTO);
    }
}
