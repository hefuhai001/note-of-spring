package _01_JavaJiChu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class _20_ZhenZeBiaoDaShi {

//    private static final String REGEX = "\\bcat\\b";
//    private static final String INPUT =
//            "cat cat cat cattie cat";
//    public static void main(String[] args) {
//        Pattern p = Pattern.compile(REGEX);
//        Matcher m = p.matcher(INPUT); // 获取 matcher 对象
//        int count = 0;
//
//        while (m.find()) {
//            count++;
//            System.out.println("Match number " + count);
//            System.out.println("start(): " + m.start());
//            System.out.println("end(): " + m.end());
//        }
//    }

    private static final String REGEX = "foo";
    private static final String INPUT = "fooooooooooooooooo";
    private static final String INPUT2 = "ooooofoooooooooooo";
    private static Pattern pattern;
    private static Matcher matcher;
    private static Matcher matcher2;

    public static void main(String[] args) {
        pattern = Pattern.compile(REGEX);
        matcher = pattern.matcher(INPUT);
        matcher2 = pattern.matcher(INPUT2);

        System.out.println("Current REGEX is: " + REGEX);
        System.out.println("Current INPUT is: " + INPUT);
        System.out.println("Current INPUT2 is: " + INPUT2);


        System.out.println("lookingAt(): " + matcher.lookingAt());
        System.out.println("matches(): " + matcher.matches());
        System.out.println("lookingAt(): " + matcher2.lookingAt());
    }

    /*
     * replaceFirst 和 replaceAll 方法
     * replaceFirst 和 replaceAll 方法用来替换匹配正则表达式的文本。不同的是，replaceFirst 替换首次匹配，replaceAll 替换所有匹配。
     * */
//    private static String REGEX = "dog";
//    private static String INPUT = "The dog says meow. " +
//            "All dogs say meow.";
//    private static String REPLACE = "cat";
//
//    public static void main(String[] args) {
//        Pattern p = Pattern.compile(REGEX);
//        // get a matcher object
//        Matcher m = p.matcher(INPUT);
//        INPUT = m.replaceAll(REPLACE);
//        System.out.println(INPUT);
//    }

    /*
     * appendReplacement 和 appendTail 方法
     * Matcher 类也提供了appendReplacement 和 appendTail 方法用于文本替换：
     * */
//    private static String REGEX = "a*b";
//    private static String INPUT = "aabfooaabfooabfoobkkk";
//    private static String REPLACE = "-";
//    public static void main(String[] args) {
//        Pattern p = Pattern.compile(REGEX);
//        // 获取 matcher 对象
//        Matcher m = p.matcher(INPUT);
//        StringBuffer sb = new StringBuffer();
//        while(m.find()){
//            m.appendReplacement(sb,REPLACE);
//        }
//        m.appendTail(sb);
//        System.out.println(sb.toString());
//    }

    /*
     * PatternSyntaxException 类的方法
     * PatternSyntaxException 是一个非强制异常类，它指示一个正则表达式模式中的语法错误。
     * PatternSyntaxException 类提供了下面的方法来帮助我们查看发生了什么错误。
     * 序号	方法及说明
     * 1	public String getDescription()
     * 获取错误的描述。
     * 2	public int getIndex()
     * 获取错误的索引。
     * 3	public String getPattern()
     * 获取错误的正则表达式模式。
     * 4	public String getMessage()
     * 返回多行字符串，包含语法错误及其索引的描述、错误的正则表达式模式和模式中错误索引的可视化指示。
     * */


}
