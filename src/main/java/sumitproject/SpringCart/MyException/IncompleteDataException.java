package sumitproject.SpringCart.MyException;

public class IncompleteDataException extends RuntimeException{
    public IncompleteDataException(String msg) {
        super(msg);
    }
}
