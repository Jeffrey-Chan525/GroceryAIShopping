package com.smartspend.model.validation;

import com.smartspend.model.UserRegistrationDTO;

public class UserPasswordValidator extends UserEntryValidator {
    @Override
    public ValidationResult check(UserRegistrationDTO userRegistrationDTO) {
        String password = userRegistrationDTO.getPassword();
        String confirmPassword = userRegistrationDTO.getConfirmPassword();
        if (password == null || password.isEmpty()){
            return new  ValidationResult(false, "Password cannot be empty");
        }
        if (confirmPassword == null || confirmPassword.isEmpty()){
            return new ValidationResult(false, "Confirm Password cannot be empty");
        }

        if (password.length() < 8) {
            return new ValidationResult(false, "Password must be at least 8 characters");
        }
        if (!password.equals(confirmPassword)) {
            return new  ValidationResult(false, "Passwords do not match");
        }
        return chainNext(userRegistrationDTO);
    }
}
