package edu.eci.cvds.tdd.library;

import edu.eci.cvds.tdd.library.book.Book;
import edu.eci.cvds.tdd.library.loan.Loan;
import edu.eci.cvds.tdd.library.loan.LoanStatus;
import edu.eci.cvds.tdd.library.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Library responsible for manage the loans and the users.
 */
public class Library {

    private final List<User> users;
    private final Map<Book, Integer> books;
    private final List<Loan> loans;

    public Library() {
        users = new ArrayList<>();
        books = new HashMap<>();
        loans = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public Map<Book, Integer> getBooks() {
        return books;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    /**
     * Adds a new {@link edu.eci.cvds.tdd.library.book.Book} into the system, the
     * book is store in a Map that contains
     * the {@link edu.eci.cvds.tdd.library.book.Book} and the amount of books
     * available, if the book already exist the
     * amount should increase by 1 and if the book is new the amount should be 1,
     * this method returns true if the
     * operation is successful false otherwise.
     *
     * @param book The book to store in the map.
     * @return true if the book was stored false otherwise.
     */
    public boolean addBook(Book book) {
        if (books.containsKey(book)) {
            books.put(book, books.get(book) + 1);
            return true;
        } else if (!books.containsKey(book)) {
            books.put(book, 1);
            return true;
        }

        return false;
    }

    /**
     * This method creates a new loan with for the User identify by the userId and
     * the book identify by the isbn,
     * the loan should be store in the list of loans, to successfully create a loan
     * is required to validate that the
     * book is available, that the user exist and the same user could not have a
     * loan for the same book
     * {@link edu.eci.cvds.tdd.library.loan.LoanStatus#ACTIVE}, once these
     * requirements are meet the amount of books is
     * decreased and the loan should be created with
     * {@link edu.eci.cvds.tdd.library.loan.LoanStatus#ACTIVE} status and
     * the loan date should be the current date.
     *
     * @param userId id of the user.
     * @param isbn   book identification.
     * @return The new created loan.
     */
    public Loan loanABook(String userId, String isbn) {
        // TODO Implement the login of loan a book to a user based on the UserId and the
        if (this.findUser(userId) != null && this.findBook(isbn) != null && this.books.get(findBook(isbn)) > 0) {
            Loan loan = new Loan(this.findBook(isbn), this.findUser(userId), LocalDateTime.now(), LoanStatus.ACTIVE, LocalDateTime.now());
            if (!comprobateLoan(loan)) {
                loans.add(loan);
                books.put(this.findBook(isbn), this.books.get(findBook(isbn)) - 1);
                return loan;
            }
        } else {
            return null;
        }
        // isbn.
        return null;
    }

    /**
     * This method return a loan, meaning that the amount of books should be
     * increased by 1, the status of the Loan
     * in the loan list should be
     * {@link edu.eci.cvds.tdd.library.loan.LoanStatus#RETURNED} and the loan return
     * date should be the current date, validate that the loan exist.
     *
     * @param loan loan to return.
     * @return the loan with the RETURNED status.
     */
    public Loan returnLoan(Loan loan) {
        if (comprobateLoan(loan) && loan.getStatus() == LoanStatus.ACTIVE) {
            loan.setReturnDate(LocalDateTime.now());
            books.put(loan.getBook(), this.books.get(loan.getBook()) + 1);
            loan.setStatus(LoanStatus.RETURNED);
            return loan;
        }
        return null;
    }

    public boolean addUser(User user) {
        return users.add(user);
    }

    private User findUser(String userId) {
        User user = null;
        for (User u : users) {
            if (u.getId().equals(userId)) {
                user = u;
                break;
            }
        }
        return user;
    }

    private Book findBook(String isbn) {
        Book book = null;
        for (Book b : books.keySet()) {
            if (b.getIsbn().equals(isbn)) {
                book = b;
                break;
            }
        }
        return book;
    }

    private boolean comprobateLoan(Loan loan) {
        for (Loan l : loans) {
            if (l.getUser().getId().equals(loan.getUser().getId()) && l.getBook().getIsbn().equals(loan.getBook().getIsbn())) {
                return true;
            }
        }
        return false;
    }
}