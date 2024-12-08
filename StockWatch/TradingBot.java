public class TradingBot {
    double balance;
    int sharesOwned = 0;
    double entry = 0;
    int nextSell = -1;
    int weeks = 52;

    public TradingBot(double initialBalance) {
        this.balance = initialBalance;
    }

    public void tradeMaker(StockReader stockBot, int currentIndex) {
        double currentPrice = stockBot.stockDataList.get(currentIndex).price;
        if (sharesOwned == 0) {
            double amountToInvest = balance * 0.25;
            int sharesToBuy = (int) (amountToInvest / currentPrice);
            if (sharesToBuy > 0) {
                sharesOwned += sharesToBuy;
                entry = currentPrice;
                balance -= sharesToBuy * currentPrice;
                nextSell = currentIndex + weeks;
                System.out.printf("Buying %d shares at $%.2f. New balance: $%.2f.", sharesToBuy, currentPrice, balance);
            }
        } 
        else if (sharesOwned > 0 && currentIndex == nextSell) {
            balance += sharesOwned * currentPrice;
            System.out.printf("Selling %d shares at $%.2f. New balance: $%.2f%n", sharesOwned, currentPrice, balance);
            sharesOwned = 0; 
            entry = 0; 
            nextSell = -1;
        } else {
            System.out.println("No trade executed.");
        }
    }
}