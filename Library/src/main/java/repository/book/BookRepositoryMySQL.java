package repository.book;

import model.Book;
import model.builder.BookBuilder;

import java.sql.*;

public class BookRepositoryMySQL extends AbstractRepositoryMySQL{
    public BookRepositoryMySQL(Connection connection){
        super(connection);
    }

    protected String getTableName(){
        return "book";
    }
    protected String getTableValues(){
        return " VALUES(null, ?, ?, ?, ?);";
    }

    protected void setPreparedStatement(Book book, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, book.getAuthor());
        preparedStatement.setString(2, book.getTitle());
        preparedStatement.setInt(3, book.getAmount());
        preparedStatement.setDate(4, java.sql.Date.valueOf(book.getPublishedDate()));
    }

    protected Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setAuthor(resultSet.getString("author"))
                .setTitle(resultSet.getString("title"))
                .setPublishedDate(new java.sql.Date((resultSet.getDate("publishedDate")).getTime()).toLocalDate())
                .setAmount(resultSet.getInt("amount"))
                .build();
    }

    @Override
    public void updateAmount(Long id, int newAmount, int oldAmount){
        String sql = "UPDATE book set amount = ? where id = ?";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, newAmount + oldAmount);
            preparedStatement.setLong(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Long getBookId(Book book) {
        String sql = "SELECT id FROM book " +
                     "WHERE author = ? and title = ? and publishedDate = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
