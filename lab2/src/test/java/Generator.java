import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Created by Dmitriy on 4/4/2017.
 */
public class Generator {
    final static int BUFFER_SIZE = 300;

    private static void nextBytes(byte[] nextBytes, double prob, Random rand){
        byte lastByte = nextBytes[BUFFER_SIZE - 1];
        byte [] b = new byte[1];
        for (int i = 0; i < BUFFER_SIZE; i++)
            if(rand.nextFloat() < prob){
                nextBytes[i] = lastByte;
            }else{
                rand.nextBytes(b);
                nextBytes[i] = b[0];
                lastByte = b[0];
            }

    }

    public static void generate(OutputStream output, int size, double prob) throws IOException{
        Random rand = new Random();
        int sizeCpy = size;
        byte [] b = new byte[BUFFER_SIZE];
        rand.nextBytes(b);
        while(sizeCpy > 0){
            Generator.nextBytes(b, prob, rand);
            if(sizeCpy > BUFFER_SIZE) {
                output.write(b);
                sizeCpy -= BUFFER_SIZE;
            }
            else {
                output.write(b, 0, sizeCpy);
                sizeCpy = 0;
            }
        }
    }
}
