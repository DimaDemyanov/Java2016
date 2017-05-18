import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Dmitriy on 4/27/2017.
 */
public class Main {
    public static void main(String [] args){
        ByteArrayInputStream inputStream = new ByteArrayInputStream(("3 2 -1 -2 1").getBytes());
        try {
            PipedIOStream io1 = new PipedIOStream();
            PipedIOStream io2 = new PipedIOStream();
            BoundaryFinder bf = new BoundaryFinder(inputStream, io1.out);
            RootsBoundaryFinder rbf = new RootsBoundaryFinder(io1.in, io2.out);
            RootsBoundariesDistributer rbd = new RootsBoundariesDistributer(io2.in);
            Thread th1 = new Thread(bf);
            Thread th2 = new Thread(rbf);
            Thread th3 = new Thread(rbd);
            th1.start();
            while (!bf.isWritten()){

            }
            th2.start();

            while (!rbf.isWritten()){

            }
            th3.start();
        }catch (Exception e){
            System.out.print(e.getMessage());
        }
    }
}
