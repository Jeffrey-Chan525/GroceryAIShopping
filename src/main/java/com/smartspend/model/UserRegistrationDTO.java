package com.smartspend.model;

import com.smartspend.model.validation.UserEntryValidator;

import java.util.Objects;

public class UserRegistrationDTO {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;

    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getConfirmPassword() { return confirmPassword; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public UserRegistrationDTO(String firstName, String lastName, String email, String password,  String confirmPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = firstName + " " + lastName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public UserRegistrationDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRegistrationDTO userRegistrationDTO = (UserRegistrationDTO) o;
        boolean userNameEquals = Objects.equals(username, userRegistrationDTO.username);
        boolean emailEquals = Objects.equals(email, userRegistrationDTO.email);
        boolean passwordEquals = Objects.equals(password, userRegistrationDTO.password);
        return userNameEquals && emailEquals  && passwordEquals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, email, password);
    }
}