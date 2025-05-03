import java.io.IOException;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket s=new Socket("127.0.0.1", 1211);

        VisualInterface vi=new VisualInterface(s);
        vi.mainPage();
    }
}
