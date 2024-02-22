package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import model.Book;
import model.User;
import service.book.BookService;
import service.user.UserService;
import view.CustomerView;

import java.util.List;
import java.util.Random;

public class CustomerController {
    private final CustomerView customerView;
    private final BookService bookService;
    private final UserService userService;


    public CustomerController(CustomerView customerView, BookService bookService, UserService userService){
        this.customerView = customerView;
        this.bookService = bookService;
        this.userService = userService;

        customerView.addBuyBookbuttonListener(new BuyBookButtonListener());
    }

    public void setTableData(){
        List<Book> books = bookService.findAll();
        customerView.setDataInTable(books);
    }

    public void setScene(Stage primaryStage){
        customerView.setScene(primaryStage);
    }

    public void setCurrentUser(Long id){
        customerView.setCurrentUser(id);
    }

    public void addExitButtonListener(EventHandler<ActionEvent> exitButtonListener){
        customerView.addExitButtonListener(exitButtonListener);
    }

    private class BuyBookButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Book book = customerView.getSelectedBook();
            if(book != null) {
                if(book.getAmount() <= 0) {
                    customerView.setActionTargetText("Not enough books in stock!");
                } else {
                    List<User> empl = userService.getEmployees();
                    Long emplId = empl.get(new Random().nextInt(empl.size())).getId();
                    bookService.buyBook(book, customerView.getCrtUser(), emplId);
                    customerView.setActionTargetText("Book bought successfully!");
                    setTableData();
                }
            }
        }
    }
}
