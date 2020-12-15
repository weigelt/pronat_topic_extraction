package edu.kit.ipd.parse.topicExtraction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.topicExtraction.Topic;
import edu.kit.ipd.parse.topicExtraction.TopicExtraction;

public class TopicExtractionEval {
	private static final Logger	logger	= LoggerFactory.getLogger(TopicExtractionEval.class);
	static TopicExtraction		topicExtraction;

	private static final boolean	fileLog	= true;
	private static URL				logFile	= TopicExtractionEval.class.getResource("logfile.txt");

	@BeforeClass
	public static void beforeClass() {
		topicExtraction = new TopicExtraction();
		topicExtraction.init();
		// topicExtraction.setTopicSelectionMethod(TopicSelectionMethod.MaxConnectivity);

		// reset log-file
		if (fileLog) {
			try {
				if (logFile == null) {
					String resPath = TopicExtractionEval.class.getResource(".").getPath();
					File file = new File(resPath + File.separator + "logfile.txt");
					file.createNewFile();
				} else {
					File file = new File(logFile.toURI());
					if (file.exists()) {
						file.delete();
						file.createNewFile();
					}
				}
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
		}

	}

	private void test(Collection<String> wordSenses, String name) {
		List<Topic> resultingList = topicExtraction.getTopicsForSenses(wordSenses);
		if (fileLog) {
			StringBuilder strBuilder = new StringBuilder(name + "\n");
			strBuilder.append(String.join(",", wordSenses));
			strBuilder.append("\n\n");
			for (Topic t : resultingList) {
				strBuilder.append(t.getLabel());
				strBuilder.append(" (" + String.join(", ", t.getRelatedSenses()) + ")");
				strBuilder.append("\n");
			}
			strBuilder.append("---------------------------------\n");
			String out = strBuilder.toString();
			writeToFile(logFile, out);
			logger.debug(out);
		}
	}

	private void writeToFile(URL path, String text) {
		// get file, create it, if not yet created
		File file = new File(path.getFile());
		try {
			file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// write to file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
			writer.append(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 1
	@Test
	public void testOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("popcorn");
		wordSenses.add("popcorn");
		wordSenses.add("hand");
		test(wordSenses, "1.1");
	}

	@Test
	public void testTwoOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("popcorn");
		wordSenses.add("bag");
		test(wordSenses, "2.1");
	}

	// 2
	@Test
	public void testThirtyOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("cup");
		wordSenses.add("kitchen");
		wordSenses.add("table (furniture)");
		wordSenses.add("dishwasher");
		test(wordSenses, "31.2");
	}

	@Test
	public void testEighteenTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		test(wordSenses, "18.2");
	}

	// 3
	@Test
	public void testThreeThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		test(wordSenses, "3.3");
	}

	@Test
	public void testTwentyfiveThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		test(wordSenses, "25.3");
	}

	// 4
	@Test
	public void testIfFourOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("cupboard");
		test(wordSenses, "If.4.1");
	}

	@Test
	public void testIfFourTen() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("dishware");
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		wordSenses.add("table (furniture)");
		wordSenses.add("dishware");
		wordSenses.add("dishware");
		wordSenses.add("cupboard");
		wordSenses.add("cupboard");
		wordSenses.add("dishware");
		wordSenses.add("cupboard");
		test(wordSenses, "If.4.10");
	}

	// 5
	@Test
	public void testIfFiveFive() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("drink");
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("orange (fruit)");
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("vodka");
		test(wordSenses, "If.5.5");
	}

	@Test
	public void testIfFiveTwelve() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("fridge");
		wordSenses.add("orange (fruit)");
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		test(wordSenses, "If.5.12");
	}

	// 6
	@Test
	public void testSSixPThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("cup");
		wordSenses.add("table (furniture)");
		wordSenses.add("refrigerator");
		wordSenses.add("refrigerator");
		wordSenses.add("water");
		wordSenses.add("bottle");
		wordSenses.add("bottle");
		wordSenses.add("water");
		wordSenses.add("cup");
		wordSenses.add("bottle");
		wordSenses.add("fridge");
		wordSenses.add("fridge");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		wordSenses.add("cup");
		wordSenses.add("cupboard");
		wordSenses.add("cupboard");
		wordSenses.add("cup");
		wordSenses.add("shelf (storage)");
		wordSenses.add("cupboard");
		test(wordSenses, "s6p03");
	}

	@Test
	public void testSSixPTen() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("fridge");
		wordSenses.add("refrigerator");
		wordSenses.add("door");
		wordSenses.add("water");
		wordSenses.add("bottle");
		wordSenses.add("refrigerator");
		wordSenses.add("door");
		wordSenses.add("table (furniture)");
		wordSenses.add("water");
		wordSenses.add("bottle");
		wordSenses.add("cup");
		wordSenses.add("water");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		wordSenses.add("cupboard");
		test(wordSenses, "s6p10");
	}

	// 7
	@Test
	public void testSSevenPEight() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("plate (dishware)");
		wordSenses.add("dishwasher");
		wordSenses.add("water");
		wordSenses.add("sink");
		wordSenses.add("fridge");
		wordSenses.add("fridge");
		wordSenses.add("food");
		wordSenses.add("plate (dishware)");
		wordSenses.add("fridge");
		wordSenses.add("plate (dishware)");
		wordSenses.add("microwave");
		wordSenses.add("door");
		wordSenses.add("table (furniture)");
		test(wordSenses, "s7p08");
	}

	@Test
	public void testSSevenPTen() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("dishwasher");
		wordSenses.add("plate (dishware)");
		wordSenses.add("plate (dishware)");
		wordSenses.add("fridge");
		wordSenses.add("meal");
		wordSenses.add("plate (dishware)");
		wordSenses.add("microwave");
		wordSenses.add("microwave");
		wordSenses.add("meal");
		wordSenses.add("plate (dishware)");
		wordSenses.add("plate (dishware)");
		wordSenses.add("table (furniture)");
		test(wordSenses, "s7p10");
	}

	// 8
	@Test
	public void testSEightPOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("machine");
		wordSenses.add("laundry");
		wordSenses.add("laundry");
		wordSenses.add("clothes dryer");
		wordSenses.add("clothes dryer");
		test(wordSenses, "s8p01");
	}

	@Test
	public void testSEightPSix() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("clothes dryer");
		wordSenses.add("clothes dryer");
		wordSenses.add("machine");
		wordSenses.add("machine");
		wordSenses.add("window");
		wordSenses.add("machine");
		wordSenses.add("arm");
		wordSenses.add("machine");
		wordSenses.add("laundry");
		wordSenses.add("laundry");
		wordSenses.add("machine");
		wordSenses.add("clothes dryer");
		wordSenses.add("clothes dryer");
		wordSenses.add("push-button");
		wordSenses.add("clothes dryer");
		test(wordSenses, "s8p06");
	}

	// 9
	@Test
	public void testDroneOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("gate");
		wordSenses.add("degree (angle)");
		wordSenses.add("table (furniture)");
		wordSenses.add("greenhouse");
		wordSenses.add("pond");
		wordSenses.add("pond");
		wordSenses.add("lawn");
		test(wordSenses, "drone1.1");
	}

	@Test
	public void testDroneOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("gate");
		wordSenses.add("greenhouse");
		wordSenses.add("table (furniture)");
		wordSenses.add("greenhouse");
		wordSenses.add("pond");
		wordSenses.add("lawn");
		test(wordSenses, "drone1.2");
	}

	// 10
	@Test
	public void testMindstormOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("line (geometry)");
		wordSenses.add("carpet");
		wordSenses.add("carpet");
		wordSenses.add("rattle (percussion instrument)");
		wordSenses.add("rattle (percussion instrument)");
		test(wordSenses, "mindstorm1.1");
	}

	@Test
	public void testMindstormOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("line (geometry)");
		wordSenses.add("carpet");
		wordSenses.add("rattle (percussion instrument)");
		test(wordSenses, "mindstorm1.2");
	}

	// 11
	@Test
	public void testAlexaOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("temperature");
		wordSenses.add("radiator");
		wordSenses.add("degree (temperature)");
		wordSenses.add("playlist");
		test(wordSenses, "alexa1.1");
	}

	@Test
	public void testAlexaOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("playlist");
		wordSenses.add("radiator");
		test(wordSenses, "alexa1.2");
	}

}
