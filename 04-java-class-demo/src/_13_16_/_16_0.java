package _13_16_;

import java.net.*;
import java.io.*;

public class _16_0 {
    public static void main(String[] args) throws Exception {
        String str = "http://www.sru.edu.cn";
        URL url = new URL(str);
        InputStream inputstream = url.openStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputstream, "UTF-8"));
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream("myfile.txt"), "UTF-8");
        // UTF-8
        BufferedWriter bw = new BufferedWriter(out);
        String line;
        while ((line = in.readLine()) != null) {
            bw.write(line);
            bw.newLine();
        }
        in.close();
        bw.close();
    }
}