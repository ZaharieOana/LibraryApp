package repository.book;

import model.Book;
import model.EBook;
import model.builder.EBookBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EBookRepositoryMySQL extends AbstractRepositoryMySQL{
    public EBookRepositoryMySQL(Connection connection) {
        super(connection);
    }

    @Override
    protected String getTableName(){
        return "ebook";
    }

    @Override
    protected String getTableValues(){
        return " VALUES(null, ?, ?, ?, ?);";
    }

    @Override
    protected void setPreparedStatement(Book book, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, book.getAuthor());
        preparedStatement.setString(2, book.getTitle());
        preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
        preparedStatement.setString(4, ((EBook) book).getFormat());
    }

    @Override
    protected Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new EBookBuilder()
                .setId(resultSet.getLong("id"))
                .setAuthor(resultSet.getString("author"))
                .setTitle(resultSet.getString("title"))
                .setPublishedDate(new java.sql.Date((resultSet.getDate("publishedDate")).getTime()).toLocalDate())
                .setFormat(resultSet.getString("format"))
                .build();
    }

    @Override
    public void updateAmount(Long id, int newAmount, int oldAmount) {

    }

    @Override
    public Long getBookId(Book book) {
        return null;
    }
}
