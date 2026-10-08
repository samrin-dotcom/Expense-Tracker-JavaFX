package com.expensetracker.expense;

import com.expensetracker.db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;

public class Categories extends Application {

    @Override
    public void start(Stage stage) {

        Label title = new Label("CATEGORIES");

        ListView<String> categoryList = new ListView<>();

        ObservableList<String> categories =
                FXCollections.observableArrayList();

        String sql = "SELECT category_name FROM categories";

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                categories.add(
                    rs.getString("category_name")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        categoryList.setItems(categories);

        VBox root = new VBox(15);

        root.setPadding(new Insets(20));

        root.getChildren().addAll(
                title,
                categoryList
        );

        Scene scene = new Scene(root, 400, 400);

        stage.setTitle("Expense Tracker - Categories");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
