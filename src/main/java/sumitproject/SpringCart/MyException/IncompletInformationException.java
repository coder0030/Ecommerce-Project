package sumitproject.SpringCart.MyException;

public class IncompletInformationException extends RuntimeException{
    public IncompletInformationException(String msg) {
        super(msg);
    }
}
