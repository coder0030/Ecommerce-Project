package sumitproject.SpringCart.MyException;

public class DuplicateValueException extends RuntimeException{
    public DuplicateValueException(String msg) {
        super(msg);
    }
}
