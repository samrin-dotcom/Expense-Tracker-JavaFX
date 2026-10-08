package com.expensetracker.dashboard;

import com.expensetracker.db.DBConnection;
import com.expensetracker.expense.AddExpense;
import com.expensetracker.expense.ViewExpenses;
import com.expensetracker.expense.Categories;
import com.expensetracker.income.AddIncome;
import com.expensetracker.login.Login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Dashboard extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label("EXPENSE TRACKER");

        Label welcome = new Label("Welcome, Samrin");

        Label totalTitle = new Label("TOTAL EXPENSE");

        Label totalAmount = new Label();
        totalAmount.setText("₹ " + new DecimalFormat("#,##0.00").format(getTotalExpense()));
        
        Label incomeTitle = new Label("TOTAL INCOME");

        Label incomeAmount = new Label();
        incomeAmount.setText("₹ " + new DecimalFormat("#,##0.00").format(getTotalIncome()));
        
        Label balanceTitle = new Label("BALANCE");

        Label balanceAmount = new Label();
        balanceAmount.setText("₹ " + new DecimalFormat("#,##0.00").format(getTotalIncome() - getTotalExpense()));
        Label monthlyTitle = new Label("THIS MONTH EXPENSE");
        
        Label monthlyAmount = new Label();
        monthlyAmount.setText("₹ " + new DecimalFormat("#,##0.00").format(getMonthlyExpense()));

        Button addExpenseButton = new Button("Add Expense");

        addExpenseButton.setOnAction(e -> {

            AddExpense addExpense = new AddExpense();
            addExpense.start(new Stage());

        });
        
        Button addIncomeButton = new Button("Add Income");
        addIncomeButton.setOnAction(e -> {

            AddIncome addIncome = new AddIncome();

            addIncome.start(new Stage());

        });
        
        Button viewExpenseButton = new Button("View Expenses");

        viewExpenseButton.setOnAction(e -> {

            ViewExpenses viewExpenses = new ViewExpenses();
            viewExpenses.start(new Stage());

        });
    
        Button categoryButton = new Button("Categories");
        categoryButton.setOnAction(e -> {

            Categories categories = new Categories();
            categories.start(new Stage());

        });

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {

            Login login = new Login();
            login.start(new Stage());

            stage.close();

        });

        VBox root = new VBox(20);

        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(30));

        root.getChildren().addAll(
                title,
                welcome,
                totalTitle,
                totalAmount,
                incomeTitle,
                incomeAmount,
                balanceTitle,
                balanceAmount,
                monthlyTitle,
                monthlyAmount,
                addExpenseButton,
                addIncomeButton,
                viewExpenseButton,
                categoryButton,
                logoutButton
        );

        Scene scene = new Scene(root, 500, 600);

        stage.setTitle("Expense Tracker - Dashboard");
        stage.setScene(scene);
        stage.show();
    }
    private double getTotalExpense() {

        double total = 0;

        String sql = "SELECT SUM(amount) FROM expenses";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                total = rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
    
    private double getTotalIncome() {

        double total = 0;

        String sql = "SELECT SUM(amount) FROM income";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                total = rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
    
    private double getMonthlyExpense() {

        double total = 0;

        String sql = """
                SELECT SUM(amount)
                FROM expenses
                WHERE MONTH(expense_date) = MONTH(CURDATE())
                AND YEAR(expense_date) = YEAR(CURDATE())
                """;

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                total = rs.getDouble(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
