import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;

import java.io.*;
import java.util.Scanner;

public class Main {
    private static String helpMessage = "Для работы программы есть две группы параметров. \n" +
                                        "Первая: -p --pipe - для работы в режиме пайп\n" +
                                        "        -f --file - для работы в фаловом режиме\n" +
                                        " Для работы в файловом режиме есть два параметра\n" +
                                        " названия входного и выходного файла. \n" +
                                        "Вторая: -e --encode - для сжатия данных\n" +
                                        "        -d --decode - для распаковки данных\n" +
                                        " -h для вызова helpMassege\n";

    public static void main(String [] args){
        CommandLine cmd;

        Option opt[];

        MyParser parser = new MyParser();

        InputStream fin = System.in;
        OutputStream fout = System.out;

        try {
            parser.parse(args);
        } catch (ParseException e) {
            System.out.print(e.getMessage());
            System.exit(1);
        }

        cmd = parser.getCommandLine();

        if (cmd.hasOption('f')){
            String[] files = cmd.getOptionValues('f');
            try {
                fin = new BufferedInputStream(new FileInputStream(files[0]));
            } catch (IOException e) {
                System.out.print(e.getMessage());
                System.exit(1);
            }

            try {
                fout = new BufferedOutputStream(new FileOutputStream(files[1]));
            } catch (IOException e) {
                System.out.print(e.getMessage());
                System.exit(1);
            }

        }
        if (cmd.hasOption('h')) {
            System.out.print(helpMessage);
            System.exit(0);
        }


        if (cmd.hasOption('e')){
            try {
                Encoder.encode(fin, fout);
            } catch (IOException e) {
                System.out.print(e.getMessage());
                System.exit(1);
            }
        }
        else{
            try {
                Decoder.decode(fin, fout);
            } catch (Exception e) {
                System.out.print(e.getMessage());
                System.exit(1);
            }
        }

    }
}
