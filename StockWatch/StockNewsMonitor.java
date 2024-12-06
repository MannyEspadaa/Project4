import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class StockData {
    String date;
    double closePrice;

    public StockData(String date, double closePrice) {
        this.date = date;
        this.closePrice = closePrice;
    }
}

public class StockBot {
    private ArrayList<StockData> stockDataList = new ArrayList<>();
    private double startingBalance = 10000; // Starting balance
    private double currentBalance = startingBalance;
    private int sharesOwned = 0;

    public void loadStockData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String date = values[0];
                double closePrice = Double.parseDouble(values[4]); // Assuming close price is in the 5th column
                stockDataList.add(new StockData(date, closePrice));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double calculateRSI(int period) {
        double avgGain = 0;
        double avgLoss = 0;
        for (int i = 1; i < period; i++) {
            double change = stockDataList.get(i).closePrice - stockDataList.get(i - 1).closePrice;
            if (change > 0) {
                avgGain += change;
            } else {
                avgLoss -= change;
            }
        }
        avgGain /= period;
        avgLoss /= period;

        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }

    public double calculateMovingAverage(int period, int index) {
        double sum = 0;
        for (int i = index; i < index + period; i++) {
            sum += stockDataList.get(i).closePrice;
        }
        return sum / period;
    }

    public int tradeEvaluator(int index) {
        double rsi = calculateRSI(14); // Example RSI period
        double movingAverage = calculateMovingAverage(14, index); // Example MA period

        if (rsi < 30 && stockDataList.get(index).closePrice < movingAverage) {
            return (int) (currentBalance / stockDataList.get(index).closePrice); // Buy all possible shares
        } else if (rsi > 70 && stockDataList.get(index).closePrice > movingAverage) {
            return -sharesOwned; // Sell all owned shares
        }
        return 0; // No action
    }

    public void runBot() {
        for (int i = 14; i < stockDataList.size(); i++) { // Start after the RSI calculation period
            int sharesToTrade = tradeEvaluator(i);
            if (sharesToTrade > 0) {
                // Buy shares
                sharesOwned += sharesToTrade;
                currentBalance -= sharesToTrade * stockDataList.get(i).closePrice;
            } else if (sharesToTrade < 0) {
                // Sell shares
                currentBalance += -sharesToTrade * stockDataList.get(i).closePrice;
                sharesOwned += sharesToTrade; // sharesToTrade is negative
            }
            System.out.println("Date: " + stockDataList.get(i).date + ", Balance: " + currentBalance + ", Shares Owned: " + sharesOwned);
        }
    }

    public static void main(String[] args) {
        StockBot bot = new StockBot();
        bot.loadStockData("path/to/your/stockdata.csv");
        bot.runBot();
    }
}