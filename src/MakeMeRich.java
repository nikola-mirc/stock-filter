
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

public class MakeMeRich {
	public static final List<String> symbols = Arrays.asList("AMD", "HPQ", "IBM", "TXN", "VMW", "XRX", "AAPL", "ADBE", "AMZN", "CRAY", "CSCO", "SNE", "GOOG", "INTC", "INTU",
			"MSFT", "ORCL", "TIBX", "VRSN", "YHOO");
	static final List<String> cryptoList = Arrays.asList("BTC", "ETH", "XRP", "BCH", "LTC", "USDT", "EOS", "BNB", "BSV", "XMR");
	static final List<String> currencyList = Arrays.asList("USD", "EUR");
	private static String API_KEY = "&api_key=bd3ad93179f71bf33526ffdc04ce6acb59d2c2451932b27adeece91f62d039d4";

	public static void main(String[] args) throws IOException {

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

		// printing Copyright

		File folder = new File("src");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			FileUtils.write(listOfFiles[i], "UTF-8", "// Copyright Nikola Mircic 2019");
		}

		getCryptoPrice();
	}

	private static void getCryptoPrice() throws MalformedURLException, IOException {
		Scanner scanner = new Scanner(System.in);
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		String rootURL = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=";
		String cryptoName = "";
		String currency = "";
		String cryptoPrice;

		System.out.println("Tracked cryptos: " + cryptoList.toString() + "\nPlease enter desired crypto:");
		String userEnteredCrypto = scanner.nextLine();
		System.out.println("Tracked currencies: " + currencyList.toString() + "\nPlease enter desired currency:");
		String userEnteredCurrency = scanner.nextLine();

		if (!cryptoList.contains(userEnteredCrypto)) {
			System.out.println("Untracked crypto.");
		} else {
			cryptoName = userEnteredCrypto;
			if (!currencyList.contains(userEnteredCurrency)) {
				System.out.println("Untracked currency.");
			} else {
				currency = userEnteredCurrency;
				rootURL += cryptoName + "&tsyms=" + currency + API_KEY;
				URL request = new URL(rootURL);
				String response = IOUtils.toString(request.openStream(), "UTF-8");
				JSONObject root = new JSONObject(response);
				JSONObject body = (JSONObject) root.get(cryptoName);
				cryptoPrice = body.toString().replaceAll("[{}USD:EUR]", "").replace("\"", "");
				System.out.println("Price for " + cryptoName + " on date " + formatter.format(date) + " in " + currency + " is " + cryptoPrice + ".");
			}
		}
		scanner.close();
	}

	private static String findStockNameByPrice(Map<String, Double> map, double stockPrice) {
		BidiMap<String, Double> bidiMap = new DualHashBidiMap<String, Double>();
		bidiMap.putAll(map);
		String stockName = bidiMap.getKey(stockPrice);
		return stockName;
	}

}
