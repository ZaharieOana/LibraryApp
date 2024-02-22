package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;

import java.util.List;

public class AdminView {
    private Label addEmpl;
    private Button buttonAdd;
    private TextField userTextField;
    private PasswordField passwordField;
    private Text actiontarget;
    private TableView<User> table;
    private Button downloadReport;
    private Button exit;
    private Scene scene;

    public AdminView(){
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 10, 10, 10));

        addEmpl = new Label("Add an employee");
        addEmpl.setFont(new Font("Arial", 30));

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        initializeFields(gridPane);

        table = new TableView<>();
        setTable();

        downloadReport = new Button();
        downloadReport.setText("Download Report");
        downloadReport.setPrefSize(130, 20);

        exit = new Button();
        exit.setText("Exit");
        exit.setPrefSize(130, 20);

        vBox.getChildren().addAll(addEmpl, gridPane, table, downloadReport, exit);

        scene = new Scene(vBox, 720, 500);
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initializeFields(GridPane gridPane){
        Label userName = new Label("Employee username:");
        gridPane.add(userName, 0, 1);

        userTextField = new TextField();
        gridPane.add(userTextField, 1, 1);

        Label password = new Label("Password:");
        gridPane.add(password, 0, 2);

        passwordField = new PasswordField();
        gridPane.add(passwordField, 1, 2);

        buttonAdd = new Button("Add Employee");
        buttonAdd.setPrefSize(130, 20);
        gridPane.add(buttonAdd, 1, 4);

        actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);
        gridPane.add(actiontarget, 1, 3);
    }

    public void setTable() {
        TableColumn<User, String> userUsernameCol = new TableColumn<>("Username");
        userUsernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        userUsernameCol.setMinWidth(400);

        table.setMaxWidth(400);

        table.getColumns().addAll(userUsernameCol);

    }

    public void setDataInTable(List<User> employees) {
        ObservableList<User> employeeList = FXCollections.observableArrayList(employees);
        table.getItems().clear();
        table.setItems(employeeList);
    }

    public void setScene(Stage primaryStage) {
        primaryStage.setScene(scene);
        actiontarget.setText("");
        userTextField.clear();
        passwordField.clear();
    }

    public void addAddEmployeeButtonListener(EventHandler<ActionEvent> addEmployeeButtonListener) {
        buttonAdd.setOnAction(addEmployeeButtonListener);
    }
    public void addExitButtonListener(EventHandler<ActionEvent> exitButtonListener) {
        exit.setOnAction(exitButtonListener);
    }

    public void addDownloadReportButtonListener(EventHandler<ActionEvent> downloadReportButtonListener){
        downloadReport.setOnAction(downloadReportButtonListener);
    }

    public String getUsername() {
        return userTextField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public void setActionTargetText(String text) {
        this.actiontarget.setText(text);
    }

}
