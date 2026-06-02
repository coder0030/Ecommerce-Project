package sumitproject.SpringCart.MyException;

public class DataSaveException extends RuntimeException{
    public DataSaveException(String msg) {
        super(msg);
    }
}
