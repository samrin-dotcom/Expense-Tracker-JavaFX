package com.expensetracker.expense;

import com.expensetracker.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddExpense extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label("ADD EXPENSE");

        ComboBox<String> categoryBox = new ComboBox<>();

        categoryBox.setItems(FXCollections.observableArrayList(
                "Food",
                "Travel",
                "Shopping",
                "Education",
                "Entertainment",
                "Bills",
                "Other"
        ));

        categoryBox.setPromptText("Select Category");

        TextField amountField = new TextField();
        amountField.setPromptText("Amount");

        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        Button saveButton = new Button("SAVE EXPENSE");

        Label message = new Label();

        saveButton.setOnAction(e -> {

            String category = categoryBox.getValue();
            String amountText = amountField.getText();
            LocalDate date = datePicker.getValue();
            String description = descriptionField.getText();

            if (category == null || amountText.isEmpty() || date == null) {
                message.setText("Please fill all required fields");
                return;
            }

            try {

                double amount = Double.parseDouble(amountText);

                String sql = """
                        INSERT INTO expenses
                        (user_id, category_id, expense_date, amount, description)
                        VALUES (?, ?, ?, ?, ?)
                        """;

                try (
                    Connection con = DBConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement(sql)
                ) {

                    // Currently using user_id = 1
                    ps.setInt(1, 1);

                    // Category IDs from your MySQL table
                    int categoryId = getCategoryId(category);
                    ps.setInt(2, categoryId);

                    ps.setDate(3, java.sql.Date.valueOf(date));
                    ps.setDouble(4, amount);
                    ps.setString(5, description);

                    ps.executeUpdate();

                    message.setText("Expense Saved Successfully!");

                    amountField.clear();
                    descriptionField.clear();
                    categoryBox.setValue(null);

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
                categoryBox,
                amountField,
                datePicker,
                descriptionField,
                saveButton,
                message
        );

        Scene scene = new Scene(root, 450, 550);

        stage.setTitle("Expense Tracker - Add Expense");
        stage.setScene(scene);
        stage.show();
    }

    private int getCategoryId(String category) {

        switch (category) {
            case "Food":
                return 1;

            case "Travel":
                return 2;

            case "Shopping":
                return 3;

            case "Education":
                return 4;

            case "Entertainment":
                return 5;

            case "Bills":
                return 6;

            case "Other":
                return 7;

            default:
                return 7;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
