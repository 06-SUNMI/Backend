package capstone.everyhealth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooksCreationDto {
    private List<Book> books = new ArrayList<>();

    // default and parameterized constructor

    public void addBook(Book book) {
        this.books.add(book);
    }

    // getter and setter
}
