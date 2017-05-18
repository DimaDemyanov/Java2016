import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Created by Dmitriy on 4/29/2017.
 */
public class PipedIOStream {
    PipedInputStream in;
    PipedOutputStream out;
    PipedIOStream() throws IOException{
        out = new PipedOutputStream();
        in = new PipedInputStream(out);
    }
}
