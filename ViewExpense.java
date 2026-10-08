package com.expensetracker.expense;

import com.expensetracker.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.application.Application;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewExpenses extends Application {

    @Override
    public void start(Stage stage) {

        TableView<Expense> table = new TableView<>();
        TextField searchField = new TextField();
        searchField.setPromptText("Search category...");
        Button clearButton = new Button("Clear");
        clearButton.setOnAction(e -> {
            searchField.clear();
        });
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select date");

        TableColumn<Expense, String> categoryColumn =
                new TableColumn<>("Category");

        TableColumn<Expense, Double> amountColumn =
                new TableColumn<>("Amount");

        TableColumn<Expense, String> dateColumn =
                new TableColumn<>("Date");

        TableColumn<Expense, String> descriptionColumn =
                new TableColumn<>("Description");

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("category"));

        amountColumn.setCellValueFactory(
                new PropertyValueFactory<>("amount"));

        dateColumn.setCellValueFactory(
                new PropertyValueFactory<>("date"));

        descriptionColumn.setCellValueFactory(
                new PropertyValueFactory<>("description"));

        table.getColumns().addAll(
                categoryColumn,
                amountColumn,
                dateColumn,
                descriptionColumn
        );

        ObservableList<Expense> expenses = FXCollections.observableArrayList();

        String sql = """
                SELECT c.category_name,
                       e.amount,
                       e.expense_date,
                       e.description
                FROM expenses e
                JOIN categories c
                ON e.category_id = c.category_id
                ORDER BY e.expense_date DESC
                """;

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                expenses.add(new Expense(
                        rs.getString("category_name"),
                        rs.getDouble("amount"),
                        rs.getString("expense_date"),
                        rs.getString("description")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setItems(expenses);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            ObservableList<Expense> filtered = FXCollections.observableArrayList();

            for (Expense expense : expenses) {

                if (expense.getCategory().toLowerCase()
                        .contains(newValue.toLowerCase())) {

                    filtered.add(expense);
                }
            }

            table.setItems(filtered);
        });
        
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {

            ObservableList<Expense> filtered = FXCollections.observableArrayList();

            for (Expense expense : expenses) {

                if (newValue != null &&
                    expense.getDate().equals(newValue.toString())) {

                    filtered.add(expense);
                }
            }

            table.setItems(filtered);
        });

        VBox root = new VBox(15);
        root.setPadding(new javafx.geometry.Insets(20));
        
        root.getChildren().addAll(
                searchField,
                clearButton,
                datePicker,
                table
        );

        Scene scene = new Scene(root, 600, 400);

        stage.setTitle("Expense Tracker - View Expenses");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
