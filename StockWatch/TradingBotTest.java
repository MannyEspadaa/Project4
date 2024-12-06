public class TradingBotTest {
    public static void main(String[] args) {
        StockReader stockBot = new StockReader();
        stockBot.loadStockData("C:\\Users\\Manny\\Downloads\\tsla.csv");
        TradingBot tradingBot = new TradingBot(10000); 
        for (int i = 0; i < stockBot.stockDataList.size(); i++) {
            tradingBot.tradeMaker(stockBot, i);
        }
        double totalValue = tradingBot.balance + (tradingBot.sharesOwned * stockBot.stockDataList.get(stockBot.stockDataList.size() - 1).price);
        System.out.printf("Final balance: $%.2f, Shares owned: %d, Value of shares: $%.2f, Total value: $%.2f%n",
                tradingBot.balance, tradingBot.sharesOwned, tradingBot.sharesOwned * stockBot.stockDataList.get(stockBot.stockDataList.size() - 1).price, totalValue);
    }
}