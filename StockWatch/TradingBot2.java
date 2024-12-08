import java.util.List;
public class TradingBot2 {
    double balance;
    int shares;
    double buyPrice; 
    boolean holding;
    public TradingBot2(double initialBalance) {
        this.balance = initialBalance;
        this.shares = 0;
        this.holding = false;
    }
    public void tradeMaker(List<Data> data) {
        for (int i = 1; i < data.size(); i++) {
            double currentPrice = data.get(i).price; 
            double previousPrice = data.get(i - 1).price; 
            if (!holding) {
                if (currentPrice < previousPrice) {
                    int sharesToBuy = (int) (balance / currentPrice);
                    if (sharesToBuy > 0) {
                        shares += sharesToBuy;
                        balance -= sharesToBuy * currentPrice;
                        buyPrice = currentPrice; 
                        holding = true;
                        System.out.println("Bought " + sharesToBuy + " shares at " + currentPrice);
                    }
                }
            } else {
                if (currentPrice >= buyPrice * 1.05) {
                    balance += shares * currentPrice; 
                    System.out.println("Sold " + shares + " shares at " + currentPrice);
                    shares = 0;
                    holding = false; 
                }
            }
        }
    }

}