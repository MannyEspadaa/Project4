import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StockReader {
    List<Data> stockDataList = new ArrayList<>();
    private static final int RSI_PERIOD = 14; 
    private static final int MA_PERIOD = 50; 
    public void loadStockData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); 

            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); 
                if (values.length < 5) {
                    System.out.println("not enough data in line: " + line);
                    continue; 
                }
                String date = values[0];
                String priceStr = values[1]; 
                priceStr = priceStr.replace("$", "").replace(",", "").trim();
                double closePrice = 0;
                try {
                    closePrice = Double.parseDouble(priceStr);
                } catch (NumberFormatException e) {
                    System.out.println("not a number for date " + date + ": " + priceStr);
                    continue; 
                }
                stockDataList.add(new Data(date, closePrice));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public double calculateRSI(int index) {
        if (index < RSI_PERIOD) {
            return 0; 
        }
        double avgGain = 0;
        double avgLoss = 0;
        for (int i = index - RSI_PERIOD + 1; i <= index; i++) {
            double change = stockDataList.get(i).price - stockDataList.get(i - 1).price;
            if (change > 0) {
                avgGain += change;
            } else {
                avgLoss -= change; 
            }
        }
        avgGain /= RSI_PERIOD;
        avgLoss /= RSI_PERIOD;
        double rs = avgGain / avgLoss;
        double rsi = 100 - (100 / (1 + rs));
        return rsi;
    }
    public double MA(int period) {
        if (stockDataList.size() < period) {
            return 0;
        }

        double sum = 0;
        for (int i = stockDataList.size() - period; i < stockDataList.size(); i++) {
            sum += stockDataList.get(i).price;
        }

        return sum / period;
    }
    public boolean MATrend(int index) {
        if (index < MA_PERIOD) {
            return false; 
        }
        double currentMA = MA(MA_PERIOD);
        double previousMA = MA(MA_PERIOD + 1);
        return currentMA > previousMA; 
    }
}