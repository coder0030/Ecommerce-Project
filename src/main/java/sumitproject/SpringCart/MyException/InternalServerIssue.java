package sumitproject.SpringCart.MyException;

public class InternalServerIssue extends RuntimeException{
    public InternalServerIssue(String msg) {
        super(msg);
    }
}
