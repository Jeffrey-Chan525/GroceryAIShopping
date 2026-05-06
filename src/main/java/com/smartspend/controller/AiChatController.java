package com.smartspend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AiChatController extends BaseController {
    @FXML private TextField messageField;
    @FXML private TextArea transcriptArea;
    @FXML private Label helperLabel;

    @FXML
    public void initialize() {
        transcriptArea.setText("AI: Hi! I can help optimise your grocery basket, compare stores, and suggest pantry-friendly swaps.\n\n" +
                "You: Which store is best this week?\nAI: Aldi is currently cheapest overall, while Woolworths has the best milk price.\n");
    }

    @FXML
    private void handleSend() {
        String text = messageField.getText() == null ? "" : messageField.getText().trim();
        if (text.isBlank()) {
            helperLabel.setText("Type a question before sending.");
            return;
        }
        transcriptArea.appendText("\nYou: " + text + "\nAI: For the demo, try linking this response to your team's recommendation logic.\n");
        helperLabel.setText("Message added to transcript.");
        messageField.clear();
    }
}
