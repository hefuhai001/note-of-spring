package _04_12_;

import javax.swing.*;
import java.awt.*;

public class _11_1 {
    public static void main(String[] args) {
        Frame frame = new Frame("teat");
        frame.setLocation(500, 400);
        frame.setSize(500, 300);
        frame.setBounds(100, 100, 500, 300);

//        ScrollPane sp = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
//        sp.add(new TextField("ScrollPane"));
//        frame.add(sp);

        Panel p = new Panel();
        String bt = "";
        p.add(new TextField(bt));
        p.add(new Button("reset"));
        frame.add(p);

        Box hbox = Box.createHorizontalBox();
        hbox.add(new Button("第一页"));
        hbox.add(Box.createHorizontalGlue());
        hbox.add(new Button("上一页"));
        hbox.add(Box.createHorizontalGlue());
        hbox.add(new Button("下一页"));
        hbox.add(Box.createHorizontalGlue());
        frame.add(hbox, BorderLayout.NORTH);

//        Box vbox = Box.createVerticalBox();
//        vbox.add(new Button("444"));
//        vbox.add(Box.createVerticalGlue());
//        vbox.add(new Button("555"));
//        vbox.add(Box.createVerticalGlue());
//        vbox.add(new Button("666"));
//        vbox.add(Box.createVerticalGlue());
//        frame.add(vbox);
//        frame.pack();


        frame.setVisible(true);
    }
}
