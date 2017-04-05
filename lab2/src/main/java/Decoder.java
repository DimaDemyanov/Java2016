import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dmitriy on 4/5/2017.
 */
public class Decoder {
    static final int BUFFER_SIZE = 300;

    public static void decode(InputStream in, OutputStream out) throws IOException, DecodeException {
        int num = 0;
        byte[] bufIn = new byte[BUFFER_SIZE];
        byte[] bufOut = new byte[BUFFER_SIZE];
        byte[] space = new byte[1];
        int readedCnt = 0;
        Integer sameCnt = 1;
        boolean wasLastSpace = false;
        space[0] = (char)' ';
        do {
            readedCnt = in.read(bufIn);
            int i = -1;
            while (++i < readedCnt){
                if (bufIn[i] != space[0] && wasLastSpace == false){
                    if((byte)'0' <= bufIn[i] && bufIn[i] <= (byte)'9') {
                        num = num * 10 + ((char) bufIn[i] - '0');
                    }
                    else{
                        throw(new DecodeException());
                    }
                }

                if (wasLastSpace == false && bufIn[i] == space[0]) {
                    wasLastSpace = true;
                    continue;
                }
                if (wasLastSpace == true){
                    for(int j = 0; j < num; j++)
                        if (j % BUFFER_SIZE == 0 && j != 0)
                            out.write(bufOut, 0, BUFFER_SIZE);
                        else {
                            bufOut[j % BUFFER_SIZE] = bufIn[i];
                            if (j == num - 1)
                                out.write(bufOut, 0, num % BUFFER_SIZE);
                        }
                    wasLastSpace = false;
                    num = 0;
                }
            }
        }while(0 <= readedCnt);
    }
}
