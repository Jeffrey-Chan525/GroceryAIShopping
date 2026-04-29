package com.smartspend.model.validation;

import com.smartspend.model.UserRegistrationDTO;
import com.smartspend.service.UserAuthenticationService;

import java.sql.Connection;
import java.sql.SQLException;

public class LoginValidator extends UserEntryValidator{
    private final UserAuthenticationService userAuthenticationService;

    public LoginValidator(Connection connection) {
        userAuthenticationService = new UserAuthenticationService(connection);
    }

    @Override
    public ValidationResult check(UserRegistrationDTO userRegistrationDTO) {
        String email = userRegistrationDTO.getEmail();
        String password= userRegistrationDTO.getPassword();
        if (userRegistrationDTO.getEmail() == null) {return new  ValidationResult(false, "You must type in an Email");}
        try{
            if (userAuthenticationService.IsUserRegistered(email)){
            e    return new ValidationResult(false, "The email you entered is not registered");
            }
            if (!userAuthenticationService.isPasswordCorrect(email, password)) {
                return new ValidationResult(false, "Incorrect email or password. Please try again.");
            }
        } catch (SQLException e){
            return new ValidationResult(false, "Something went wrong on the server side. We are currently working on fixes.");
        }

        return chainNext(userRegistrationDTO);
    }
}
