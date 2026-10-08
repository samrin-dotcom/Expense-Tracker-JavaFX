package com.expensetracker.income;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.expensetracker.db.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddIncome extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label("ADD INCOME");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        DatePicker datePicker = new DatePicker();

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        Button saveButton = new Button("SAVE INCOME");
        
        Label message = new Label();
        
        saveButton.setOnAction(e -> {

            String amountText = amountField.getText();
            String description = descriptionField.getText();

            if (amountText.isEmpty() || datePicker.getValue() == null) {
                message.setText("Please fill all required fields");
                return;
            }

            try {

                double amount = Double.parseDouble(amountText);

                String sql = """
                        INSERT INTO income
                        (amount, income_date, description)
                        VALUES (?, ?, ?)
                        """;

                try (
                    Connection con = DBConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement(sql)
                ) {

                    ps.setDouble(1, amount);
                    ps.setDate(2, java.sql.Date.valueOf(datePicker.getValue()));
                    ps.setString(3, description);

                    ps.executeUpdate();

                    message.setText("Income Saved Successfully!");

                    amountField.clear();
                    descriptionField.clear();

                }

            } catch (NumberFormatException ex) {

                message.setText("Enter a valid amount");

            } catch (Exception ex) {

                ex.printStackTrace();
                message.setText("Database Error");
            }
        });

        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        root.getChildren().addAll(
                title,
                amountField,
                datePicker,
                descriptionField,
                saveButton,
                message
        );

        Scene scene = new Scene(root, 450, 450);

        stage.setTitle("Expense Tracker - Add Income");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
