package com.expensetracker.login;

import com.expensetracker.db.DBConnection;
import com.expensetracker.dashboard.Dashboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Login extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label("EXPENSE TRACKER");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("LOGIN");

        Label message = new Label();

        loginButton.setOnAction(e -> {

            String username = usernameField.getText();
            String password = passwordField.getText();

            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

            try (
                Connection con = DBConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
            ) {

                ps.setString(1, username);
                ps.setString(2, password);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    message.setText("Login Successful!");

                    Dashboard dashboard = new Dashboard();
                    dashboard.start(new Stage());

                    stage.close();

                } else {

                    message.setText("Invalid Username or Password");

                }

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
                usernameField,
                passwordField,
                loginButton,
                message
        );

        Scene scene = new Scene(root, 400, 400);

        stage.setTitle("Expense Tracker - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
