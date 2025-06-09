package _02_;

public class _04_Stock {
    //一个名为symbol的字符串数据域表示股票代码：
    String symbol;
    //一个名为name的字符串域表示股票名称；
    String name;
    //一个名为 previousPrice 的double型数据域，用来存储股票的前一日收盘价；
    double previousPrice;
    //一个名为 currentPrice 的double型数据域，用来存储股票的当前价格；
    double currentPrice;

    //创建一个给特定代码和名称的股票构造方法；
    public _04_Stock(String symbol, String name) {
        this.name = name;
        this.symbol = symbol;
    }

    public double getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(double previousPrice) {
        this.previousPrice = previousPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    //一个名为 getChangePercent（）方法，返回从前一日价格到当前价格变化的百分比。
    public double getChangePercent() {
        double percent = (currentPrice - previousPrice) / previousPrice;
        return percent * 100;
    }
}
