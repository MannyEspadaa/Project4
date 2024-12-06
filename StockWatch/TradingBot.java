public class TradingBot {
    double balance;
    int sharesOwned = 0;
    double entry = 0;
    double previousRSI = 0;
    private static final int MA_PERIOD = 50;
    public TradingBot(double initialBalance) {
        this.balance = initialBalance;
    }
    public void tradeMaker(StockReader stockBot, int currentIndex) {
        double currentPrice = stockBot.stockDataList.get(currentIndex).price;
        double currentRSI = stockBot.calculateRSI(currentIndex);
        double movingAverage = stockBot.MA(MA_PERIOD);
        if (currentRSI < previousRSI && sharesOwned == 0) {
            double amountToInvest = balance * 0.25; 
            int sharesToBuy = (int) (amountToInvest / currentPrice);
            if (sharesToBuy > 0) {
                sharesOwned += sharesToBuy;
                entry = currentPrice;
                balance -= sharesToBuy * currentPrice;
                System.out.printf("Buying %d shares at $%.2f. New balance: $%.2f%n", sharesToBuy, currentPrice, balance);
            }
        } 
        else if (currentRSI > previousRSI && sharesOwned > 0 && stockBot.MATrend(currentIndex)) {
            balance += sharesOwned * currentPrice;
            System.out.printf("Selling %d shares at $%.2f. New balance: $%.2f%n", sharesOwned, currentPrice, balance);
            sharesOwned = 0; 
            entry = 0; 
        } else {
            System.out.println("No trade executed.");
        }
        previousRSI = currentRSI;
    }
}