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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Book;

import java.time.LocalDate;
import java.util.List;


public class CustomerView {

    private Label welcome;
    private TableView<Book> table;
    private Button buyBook;
    private Button exit;
    private Scene scene;
    private Long crtUser;
    private Text actiontarget;

    public CustomerView() {
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 10, 10, 10));

        welcome = new Label("Welcome!");
        welcome.setAlignment(Pos.TOP_CENTER);
        welcome.setFont(new Font("Arial", 30));

        table = new TableView<>();
        setTable();

        actiontarget = new Text();
        actiontarget.setFill(Color.FIREBRICK);

        buyBook = new Button();
        buyBook.setText("Buy");
        buyBook.setPrefSize(50, 20);

        exit = new Button();
        exit.setText("Exit");
        exit.setPrefSize(50, 20);

        vBox.getChildren().addAll(welcome, table, actiontarget, buyBook, exit);

        scene = new Scene(vBox, 720, 480);

    }

    public void setTable() {
        TableColumn<Book, String> bookTitleCol = new TableColumn<>("Title");
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookTitleCol.setMinWidth(174);
        TableColumn<Book, String> bookAuthorCol = new TableColumn<>("Author");
        bookAuthorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookAuthorCol.setMinWidth(174);
        TableColumn<Book, LocalDate> bookDateCol = new TableColumn<>("Published Date");
        bookDateCol.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        bookDateCol.setMinWidth(174);
        TableColumn<Book, String> bookAmountCol = new TableColumn<>("Amount");
        bookAmountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        bookAmountCol.setMinWidth(174);

        table.getColumns().addAll(bookTitleCol, bookAuthorCol, bookDateCol, bookAmountCol);

    }

    public void setCurrentUser(Long id) {
        crtUser = id;
    }

    public void setDataInTable(List<Book> books) {
        ObservableList<Book> bookList = FXCollections.observableArrayList(books);
        table.getItems().clear();
        table.setItems(bookList);
    }

    public void setScene(Stage primaryStage) {
        primaryStage.setScene(scene);
        actiontarget.setText("");
    }

    public void addExitButtonListener(EventHandler<ActionEvent> exitButtonListener) {
        exit.setOnAction(exitButtonListener);
    }

    public void addBuyBookbuttonListener(EventHandler<ActionEvent> buyBookButtonListener) {
        buyBook.setOnAction(buyBookButtonListener);
    }

    public void setActionTargetText(String text) {
        this.actiontarget.setText(text);
    }

    public Book getSelectedBook() {
        return table.getSelectionModel().getSelectedItem();
    }

    public Long getCrtUser() {
        return crtUser;
    }
}
