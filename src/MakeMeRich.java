
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class MakeMeRich {
	public static final List<String> symbols = Arrays.asList("AMD", "HPQ", "IBM", "TXN", "VMW", "XRX", "AAPL", "ADBE", "AMZN", "CRAY", "CSCO", "SNE", "GOOG", "INTC", "INTU",
			"MSFT", "ORCL", "TIBX", "VRSN", "YHOO");

	public static void main(String[] args) {

		// 1. Print these symbols using a Java 8 for-each and lambdas

		symbols.stream().forEach((s) -> System.out.print(s + " "));

		// 2. Use the StockUtil class to print the price of Bitcoin

		System.out.println("\nPrice of BTC: " + StockUtil.prices.get("BTC-USD"));

		// 3. Create a new List of StockInfo that includes the stock price

		List<Double> listOfStockValues = StockUtil.prices.values().stream().collect(Collectors.toList());
		System.out.println(listOfStockValues);
		
		// 4. Find the highest-priced stock under $500

		double highestStockBelow500 = listOfStockValues.stream().filter(stockPrice -> stockPrice < 500).sorted(Comparator.reverseOrder()).findFirst().get();
		System.out.println("The highest-priced stock under $500 is " + findStockNameByPrice(StockUtil.prices, highestStockBelow500) + " valued at $" + highestStockBelow500 + ".");

	}

	private static String findStockNameByPrice(Map<String, Double> map, double stockPrice) {

		BidiMap<String, Double> bidiMap = new DualHashBidiMap<String, Double>();
		bidiMap.putAll(map);
		String stockName = bidiMap.getKey(stockPrice);
		return stockName;
	}

}
