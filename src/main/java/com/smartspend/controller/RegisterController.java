package com.smartspend.controller;

import com.smartspend.util.SceneManager;
import javafx.fxml.FXML;

public class RegisterController {


    @FXML
    private void handleRegister() {

        SceneManager.switchTo("MainShell");
    }

    @FXML
    private void handleSignIn() {
        SceneManager.switchTo("Login");
    }
}
