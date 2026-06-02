package sumitproject.SpringCart.MyException;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String msg) {
        super(msg);
    }
}
