package edu.eci.cvds.tdd.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import edu.eci.cvds.tdd.library.book.Book;
import edu.eci.cvds.tdd.library.loan.Loan;
import edu.eci.cvds.tdd.library.loan.LoanStatus;
import edu.eci.cvds.tdd.library.user.User;

public class LibraryTest {

    private static Library library;
    private static Book book;
    private static User user;
    private static User anotherUser;

    @BeforeAll
    public static void setUp() {

        library = new Library();
        book = new Book("Moon Knight: white, black & blood", "Jonathan Hickman", "9781302946043");
        user = new User("Nicolas Pachon", "023");
        anotherUser = new User("Juan Rodriguez", "024");

    }

    @Test
    public void shouldCreateNewBookIfBookDoesNotExist() {
        boolean verification = library.addBook(book);
        assertTrue(verification);
        assertEquals(library.getBooks().get(book), 1);
    }

    @Test
    public void shouldIncreaseAmountBy1IfBookExists() {
        library.addBook(book);
        library.addBook(book);
        assertEquals(library.getBooks().get(book), 2);
    }


    @Test
    public void shouldLoanBookIfAvailable() {

        library.addBook(book);
        library.addUser(user);
        Loan verification = library.loanABook(user.getId(), book.getIsbn());
        assertNotNull(verification);
        assertEquals(verification.getStatus(), LoanStatus.ACTIVE);
        assertEquals(library.getBooks().get(book), 0);
        assertEquals(verification.getLoanDate().truncatedTo(ChronoUnit.SECONDS), LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

    }

    @Test
    public void shouldNotLoanBookIfNotAvailable() {

        library.addUser(user);
        Loan verification = library.loanABook(user.getId(), book.getIsbn());
        assertNull(verification);

    }

    @Test
    public void shouldNotLoanBookIfUserDoesNotExist() {

        library.addBook(book);
        Loan verification = library.loanABook(user.getId(), book.getIsbn());
        assertNull(verification);

    }

    @Test
    public void shouldNotLoanBookIfUserLoansTheSameBook() {
        library.addBook(book);
        library.addBook(book);
        library.addUser(user);
        Loan verification = library.loanABook(user.getId(), book.getIsbn());
        Loan other = library.loanABook(user.getId(), book.getIsbn());
        assertNotNull(verification);
        assertNull(other);

    }

    @Test
    public void shouldNotLoanBookIfBooksAreLessThanZero(){
        library.addBook(book);
        library.addUser(user);
        library.addUser(anotherUser);
        Loan verification = library.loanABook(user.getId(), book.getIsbn());
        Loan other = library.loanABook(anotherUser.getId(), book.getIsbn());
        assertNotNull(verification);
        assertNull(other);
    }



    @Test
    public void shouldReturnALoan() {
        library.addBook(book);
        library.addUser(user);
        Loan loan = library.loanABook("023", "9781302946043");
        Loan verification = library.returnLoan(loan);
        assertNotNull(verification);
        assertEquals(library.getBooks().get(book), 1);
        assertEquals(verification.getStatus(), LoanStatus.RETURNED);
        assertEquals(verification.getReturnDate().truncatedTo(ChronoUnit.SECONDS), LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

    }

    @Test
    public void validateExistingActiveLoan() {
        library.addBook(book);
        library.addUser(user);
        Loan loan = library.loanABook("023", "9781302946043");
        Loan verification = library.returnLoan(loan);
        assertNotNull(verification);
        assertEquals(library.getBooks().get(book), 1);
        assertEquals(verification.getStatus(), LoanStatus.RETURNED);
        assertEquals(verification.getReturnDate().truncatedTo(ChronoUnit.SECONDS), LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        Loan secondVerification = library.returnLoan(verification);
        assertNull(secondVerification);


    }

}
