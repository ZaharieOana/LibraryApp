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
import model.Book;

import java.time.LocalDate;
import java.util.List;

public class EmployeeView {
    private Label addBook;
    private TextField bookTitle;
    private TextField bookAuthor;
    private TextField bookDate;
    private TextField bookAmount;
    private TextField amount;
    private Text actiontargetNewBook;
    private Text actiontargetUpdate;
    private TableView<Book> table;
    private Button download;
    private Button exit;
    private Button update;
    private Button add;
    private Scene scene;
    private Long crtUser;

    public EmployeeView(){
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10, 10, 10, 10));

        addBook = new Label("Add a book");
        addBook.setFont(new Font("Arial", 30));

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);
        initializeFields(gridPane);

        table = new TableView<>();
        setTable();

        GridPane gridPane2 = new GridPane();
        initializeGridPane(gridPane2);
        initializeFields2(gridPane2);

        download = new Button("Download Report");
        download.setPrefSize(130, 20);

        exit = new javafx.scene.control.Button();
        exit.setText("Exit");
        exit.setPrefSize(130, 20);

        vBox.getChildren().addAll(addBook, gridPane, table, gridPane2, download, exit);

        scene = new Scene(vBox, 900, 700);
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initializeFields(GridPane gridPane){
        Label title = new Label("Title:");
        gridPane.add(title, 0, 1);

        bookTitle = new TextField();
        gridPane.add(bookTitle, 1, 1);

        Label author = new Label("Author:");
        gridPane.add(author, 0, 2);

        bookAuthor = new TextField();
        gridPane.add(bookAuthor, 1, 2);

        Label date = new Label("Published Date:");
        gridPane.add(date, 0, 3);

        bookDate = new TextField();
        gridPane.add(bookDate, 1, 3);

        Label amount = new Label("Amount:");
        gridPane.add(amount, 0, 4);

        bookAmount = new TextField();
        gridPane.add(bookAmount, 1, 4);

        add = new Button("Add new Book");
        gridPane.add(add, 1, 6);


        actiontargetNewBook = new Text();
        actiontargetNewBook.setFill(Color.FIREBRICK);
        gridPane.add(actiontargetNewBook, 1, 5);
    }

    private void initializeFields2(GridPane gridPane){
        amount = new TextField();
        gridPane.add(amount, 0, 1);

        update = new Button("Add books");
        gridPane.add(update, 1, 1);

        actiontargetUpdate = new Text();
        actiontargetUpdate.setFill(Color.FIREBRICK);
        gridPane.add(actiontargetUpdate, 1, 2);
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

    public void setDataInTable(List<Book> books) {
        ObservableList<Book> bookList = FXCollections.observableArrayList(books);
        table.getItems().clear();
        table.setItems(bookList);
    }

    public void setCurrentUser(Long id) {
        crtUser = id;
    }

    public void setScene(Stage primaryStage) {
        primaryStage.setScene(scene);
        actiontargetUpdate.setText("");
        actiontargetNewBook.setText("");
        bookTitle.clear();
        bookAuthor.clear();
        bookDate.clear();
        bookAmount.clear();
        amount.clear();
    }

    public void addExitButtonListener(EventHandler<ActionEvent> exitButtonListener) {
        exit.setOnAction(exitButtonListener);
    }

    public void addDownloadReportButtonListener(EventHandler<ActionEvent> downloadReportButtonListener) {
        download.setOnAction(downloadReportButtonListener);
    }

    public void addUpdateAmountButtonListener(EventHandler<ActionEvent> updateAmountButtonListener) {
        update.setOnAction(updateAmountButtonListener);
    }

    public void addAddBookButtonListener(EventHandler<ActionEvent> addBookButtonListener) {
        add.setOnAction(addBookButtonListener);
    }

    public void setActiontargetNewBookText(String text) {
        this.actiontargetNewBook.setText(text);
    }

    public void setActiontargetUpdateText(String text) {
        this.actiontargetUpdate.setText(text);
    }

    public Long getCrtUser() {
        return crtUser;
    }

    public Book getSelectedBook() {
        return table.getSelectionModel().getSelectedItem();
    }

    public String getUpdateAmount(){
        return amount.getText();
    }

    public String getTitle() {
        return bookTitle.getText();
    }

    public String getAuthor() {
        return bookAuthor.getText();
    }

    public String getDate() {
        return bookDate.getText();
    }

    public String getAmount() {
        return bookAmount.getText();
    }

}
