package com.smartspend.model.validation;

import com.smartspend.model.UserRegistrationDTO;
import com.smartspend.service.UserRegistrationService;

public class NameValidation extends UserEntryValidator {
    @Override
    public ValidationResult check(UserRegistrationDTO userRegistrationDTO) {
        if (userRegistrationDTO.getFirstName() == null || userRegistrationDTO.getFirstName().isEmpty()) {
            String errorMessage = "First name is required";
            return new  ValidationResult(false, errorMessage);
        }

        if  (userRegistrationDTO.getLastName() == null || userRegistrationDTO.getLastName().isEmpty()) {
            String errorMessage = "Last name is required";
            return new  ValidationResult(false, errorMessage);
        }

        return chainNext(userRegistrationDTO);
    }
}
