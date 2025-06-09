package _02_;

public class _04_testStock {
    public static void main(String[] args) {
        //编写一个测试程序，创建一个Stock对象，他的股票代码是600000，股票名称是“浦发银行”
        _04_Stock stock = new _04_Stock("600000", "浦发银行");
        //前一日的收盘价是25.5
        stock.previousPrice = 25.50;
        //当前的最新价是28.6
        stock.currentPrice = 28.6;
        System.out.println(stock.getChangePercent() + " %");
    }
}
