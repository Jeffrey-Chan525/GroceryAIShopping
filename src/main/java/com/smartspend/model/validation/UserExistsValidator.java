package com.smartspend.model.validation;

import com.smartspend.model.UserRegistrationDTO;
import com.smartspend.service.RegistrationValidationService;

import java.sql.Connection;
import java.sql.SQLException;

public class UserExistsValidator extends UserEntryValidator {
    private final RegistrationValidationService registrationValidationService;

    public UserExistsValidator(Connection connection) throws SQLException {
        registrationValidationService = new RegistrationValidationService(connection);
    }
    @Override
    public ValidationResult check(UserRegistrationDTO userRegistrationDTO) {
        try{
            if (!registrationValidationService.uniqueEmail(userRegistrationDTO.getEmail())){
                String errorMessage = "Email already exists";
                return new ValidationResult(false, errorMessage);
            }
            if (!registrationValidationService.uniqueUserName(userRegistrationDTO.getUsername())){
                setErrorMessage("Username already exists");
                String  errorMessage = "Username already exists";
                return new  ValidationResult(false, errorMessage);
            }
        } catch(Exception e){
            return new ValidationResult(false, e.getMessage());
        }

        return chainNext(userRegistrationDTO);

    }
}
