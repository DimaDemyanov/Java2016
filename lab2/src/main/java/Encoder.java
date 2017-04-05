import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dmitriy on 4/5/2017.
 */
public class Encoder {
    static final int BUFFER_SIZE = 3;

    public static void encode(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        byte[] space = new byte[1];
        int readedCnt = 0;
        byte b = 0;
        int t = 0;
        Integer sameCnt = 1;
        boolean isInCircle = false;
        space[0] = (char)' ';
        do {
            readedCnt = in.read(buf);
            int i;
            if(readedCnt < 0) break;
            if(isInCircle) {
                if (b == buf[0]) {
                    sameCnt++;
//                    if (i == readedCnt - 2){
//                        out.write(sameCnt.toString().getBytes());
//                        out.write(space);
//                        out.write(buf[i]);
//                        sameCnt = 1;
//                    }
                } else {
                    out.write(sameCnt.toString().getBytes());
                    out.write(space);
                    out.write(b);
                    sameCnt = 1;
                }
            }
            for( i = 0; i < readedCnt - 1; i++){
                isInCircle = true;
                t = i;
                if(buf[i] == buf[i + 1]) {
                    sameCnt++;
//                    if (i == readedCnt - 2){
//                        out.write(sameCnt.toString().getBytes());
//                        out.write(space);
//                        out.write(buf[i]);
//                        sameCnt = 1;
//                    }
                }
                else{
                    out.write(sameCnt.toString().getBytes());
                    out.write(space);
                    out.write(buf[i]);
                    sameCnt = 1;
                }
            }
            b = buf[i];
//            if(i != 0 && buf[i] == buf[i - 1]) {
//                sameCnt++;
////                    if (i == readedCnt - 2){
////                        out.write(sameCnt.toString().getBytes());
////                        out.write(space);
////                        out.write(buf[i]);
////                        sameCnt = 1;
////                    }
//            }
//            else{
//                out.write(sameCnt.toString().getBytes());
//                out.write(space);
//                out.write(buf[i]);
//                sameCnt = 1;
//            }
        } while(true);

        out.write(sameCnt.toString().getBytes());
        out.write(space);
        out.write(b);


    }
}
