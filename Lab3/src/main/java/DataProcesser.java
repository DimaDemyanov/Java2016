import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dmitriy on 4/26/2017.
 */
public interface DataProcesser {
    void dataProcessing() throws IOException;
    void readData() throws IOException;
    void writeData() throws IOException;
}
