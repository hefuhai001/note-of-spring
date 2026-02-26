import java.util.Scanner;

public class GenerateCss {
    public static void main(String[] args) {
        int start = 1;
        int end = 100;
        String[] color = {"--f1", "--f2", "--f3", "--f4", "--f5", "--f6", "--f7", "--f8", "--f9",
                "--white", "--black", "--gray-dark", "--blue", "--indigo", "--purple", "--pink", "--red", "--orange", "--yellow", "--green", "--teal", "--cyan", "--gray", "--primary", "--secondary", "--success", "--info", "--warning", "--danger", "--light", "--dark"};

        for (int i = 0; i <= 100; i++) {
            System.out.println(".h" + i + "vh" + " {" + "height: " + i + "vh;" + "}");
            System.out.println(".w" + i + "vw" + " {" + "width: " + i + "vw;" + "}");
        }

        for (int i = 0; i <= 100; i++) {
            System.out.println(".h" + i + "" + " {" + "height: " + i + "%;" + "}");
            System.out.println(".w" + i + "" + " {" + "width: " + i + "%;" + "}");
        }

        for (int i = 0; i <= 100; i++) {
            System.out.println(".h" + i + "px" + " {" + "height: " + i + "px;" + "}");
            System.out.println(".w" + i + "px" + " {" + "width: " + i + "px;" + "}");
        }

        for (int i = start; i <= end; i++) {
            // px
            System.out.println(".m" + i + "px" + " {" + "margin: " + i + "px;" + "}");
            System.out.println(".mt" + i + "px" + " {" + "margin-top: " + i + "px;" + "}");
            System.out.println(".mb" + i + "px" + " {" + "margin-bottom: " + i + "px;" + "}");
            System.out.println(".mtb" + i + "px" + " {" + "margin-top: " + i + "px;" + "margin-bottom: " + i + "px;" + "}");
            System.out.println(".ml" + i + "px" + " {" + "margin-left: " + i + "px;" + "}");
            System.out.println(".mr" + i + "px" + " {" + "margin-right: " + i + "px;" + "}");
            System.out.println(".mlr" + i + "px" + " {" + "margin-left: " + i + "px;" + "margin-right: " + i + "px;" + "}");
            // rem
            System.out.println(".m" + i + "rem" + " {" + "margin: " + i + "rem;" + "}");
            System.out.println(".mt" + i + "rem" + " {" + "margin-top: " + i + "rem;" + "}");
            System.out.println(".mb" + i + "rem" + " {" + "margin-bottom: " + i + "rem;" + "}");
            System.out.println(".mtb" + i + "rem" + " {" + "margin-top: " + i + "rem;" + "margin-bottom: " + i + "vh;" + "}");
            System.out.println(".ml" + i + "rem" + " {" + "margin-left: " + i + "rem;" + "}");
            System.out.println(".mr" + i + "rem" + " {" + "margin-right: " + i + "rem;" + "}");
            System.out.println(".mlr" + i + "rem" + " {" + "margin-left: " + i + "rem;" + "margin-right: " + i + "vh;" + "}");
            // %
            System.out.println(".m" + i + "" + " {" + "margin: " + i + "%;" + "}");
            System.out.println(".mt" + i + "" + " {" + "margin-top: " + i + "%;" + "}");
            System.out.println(".mb" + i + "" + " {" + "margin-bottom: " + i + "%;" + "}");
            System.out.println(".mtb" + i + "" + " {" + "margin-top: " + i + "%;" + "margin-bottom: " + i + "%;" + "}");
            System.out.println(".ml" + i + "" + " {" + "margin-left: " + i + "%;" + "}");
            System.out.println(".mr" + i + "" + " {" + "margin-right: " + i + "%;" + "}");
            System.out.println(".mlr" + i + "" + " {" + "margin-left: " + i + "%;" + "margin-right: " + i + "%;" + "}");
            //内边距
            //px
            System.out.println(".p" + i + "px" + " {" + "padding: " + i + "px;" + "}");
            System.out.println(".pt" + i + "px" + " {" + "padding-top: " + i + "px;" + "}");
            System.out.println(".pb" + i + "px" + " {" + "padding-bottom: " + i + "px;" + "}");
            System.out.println(".ptb" + i + "px" + " {" + "padding-top: " + i + "px;" + "padding-bottom: " + i + "px;" + "}");
            System.out.println(".pl" + i + "px" + " {" + "padding-left: " + i + "px;" + "}");
            System.out.println(".pr" + i + "px" + " {" + "padding-right: " + i + "px;" + "}");
            System.out.println(".plr" + i + "px" + " {" + "padding-left: " + i + "px;" + "padding-right: " + i + "px;" + "}");
            // rem
            System.out.println(".p" + i + "rem" + " {" + "padding: " + i + "rem;" + "}");
            System.out.println(".pt" + i + "rem" + " {" + "padding-top: " + i + "rem;" + "}");
            System.out.println(".pb" + i + "rem" + " {" + "padding-bottom: " + i + "rem;" + "}");
            System.out.println(".ptb" + i + "rem" + " {" + "padding-top: " + i + "rem;" + "padding-bottom: " + i + "vh;" + "}");
            System.out.println(".pl" + i + "rem" + " {" + "padding-left: " + i + "rem;" + "}");
            System.out.println(".pr" + i + "rem" + " {" + "padding-right: " + i + "rem;" + "}");
            System.out.println(".plr" + i + "rem" + " {" + "padding-left: " + i + "rem;" + "padding-right: " + i + "vh;" + "}");
            // %
            System.out.println(".p" + i + "" + " {" + "padding: " + i + "%;" + "}");
            System.out.println(".pt" + i + "" + " {" + "padding-top: " + i + "%;" + "}");
            System.out.println(".pb" + i + "" + " {" + "padding-bottom: " + i + "%;" + "}");
            System.out.println(".ptb" + i + "" + " {" + "padding-top: " + i + "%;" + "padding-bottom: " + i + "%;" + "}");
            System.out.println(".pl" + i + "" + " {" + "padding-left: " + i + "%;" + "}");
            System.out.println(".pr" + i + "" + " {" + "padding-right: " + i + "%;" + "}");
            System.out.println(".plr" + i + "" + " {" + "padding-left: " + i + "%;" + "padding-right: " + i + "%;" + "}");

            //圆角
            //px
            System.out.println(".r" + i + "px" + " {" + "border-radius:" + i + "px;" + "}");
            // %
            System.out.println(".r" + i + "" + " {" + "border-radius:" + i + "%;" + "}");

            //行高
            //px
            System.out.println(".lh" + i + "px" + " {" + "line-height:" + i + "px;" + "}");
            // vh
            System.out.println(".lh" + i + "vh" + " {" + "line-height:" + i + "%;" + "}");

            //行高
            //px
            System.out.println(".text" + i + "px" + " {" + "font-size:" + i + "px;" + "}");

            //偏移
            //px
            System.out.println(".tY" + i + "px" + " {" + "transform: translateY(" + i + "px" + ");}");
            System.out.println(".tY" + i + "px" + " {" + "transform: translateY(" + -i + "px" + ");}");
            System.out.println(".tX" + i + "px" + " {" + "transform: translateX(" + i + "px" + ");}");
            System.out.println(".tX" + i + "px" + " {" + "transform: translateX(" + -i + "px" + ");}");

            //缩放
            System.out.println(".scale" + i + "px" + " {" + "transform: scale(" + (double) i / 100 + "" + ");}");
        }

        for (int i = 0; i < color.length; i++) {
            String substring = color[i].substring(2, color[i].length());
            System.out.println(".bg" + substring + "" + " {" + "background-color: var(" + color[i] + "" + ");}");
            System.out.println("." + substring + "" + " {" + "color: var(" + color[i] + "" + ");}");
            System.out.println(".s" + substring + "" + " {" + "border: var(" + color[i] + "" + ") 1px solid;}");
            System.out.println(".sb" + substring + "" + " {" + "border-bottom: var(" + color[i] + "" + ") 1px solid;}");
            System.out.println(".d" + substring + "" + " {" + "border: var(" + color[i] + "" + ") 1px dashed;}");
            System.out.println(".db" + substring + "" + " {" + "border-bottom: var(" + color[i] + "" + ") 1px dashed;}");
        }

        String[] normal = {"fill", "auto"};
        System.out.println(".w" + normal[0] + "" + " {" + "width: " + normal[0] + ";" + "}");
        System.out.println(".h" + normal[0] + "" + " {" + "height: " + normal[0] + ";" + "}");
        System.out.println(".m" + normal[1] + "" + " {" + "margin: 0 " + normal[1] + ";" + "}");


    }
}
