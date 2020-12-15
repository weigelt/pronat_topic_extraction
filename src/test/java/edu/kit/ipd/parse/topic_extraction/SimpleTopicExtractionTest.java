package edu.kit.ipd.parse.topic_extraction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.topic_extraction_common.Topic;
import edu.kit.ipd.parse.topic_extraction_common.TopicExtractionCommon;

public class SimpleTopicExtractionTest {
	private static final Logger	logger	= LoggerFactory.getLogger(SimpleTopicExtractionTest.class);
	static TopicExtractionCommon		topicExtraction;

	private static final boolean	fileLog	= true;
	private static final String		logFile	= SimpleTopicExtractionTest.class.getResource("/logfile.txt").getFile();	// TODO

	@BeforeClass
	public static void beforeClass() {
		topicExtraction = new TopicExtractionCommon();
		topicExtraction.init();
		// topicExtraction.setTopicSelectionMethod(TopicSelectionMethod.MaxConnectivity);

		// reset log-file
		if (fileLog) {
			File file = new File(logFile);
			if (file.exists()) {
				file.delete();
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
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

	private void writeToFile(String path, String text) {
		// get file, create it, if not yet created
		File file = new File(path);
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

	@Test
	public void testDroneOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("gate");
		wordSenses.add("degree (angle)");
		wordSenses.add("obstacle");
		wordSenses.add("trail");
		wordSenses.add("gate");
		// "finish" (from "finish line")
		wordSenses.add("line (geometry)");
		wordSenses.add("earth's surface"); // for ground
		test(wordSenses, "drone1.1");
	}

	@Test
	public void testDroneOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("gate");
		wordSenses.add("gate");
		wordSenses.add("obstacle");
		wordSenses.add("gate");
		// "finish" (from "finish line")
		wordSenses.add("line (geometry)");
		wordSenses.add("line (geometry)");
		wordSenses.add("earth's surface"); // for ground
		test(wordSenses, "drone1.2");
	}

	// TODO finish following tests

	@Ignore
	@Test
	public void testMindstormOneOne() {
		String id = "mindstorm1.1";
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add(""); // TODO
		test(wordSenses, id);
	}

	@Ignore
	@Test
	public void testMindstormOneTwo() {
		String id = "mindstorm1.2";
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add(""); // TODO
		test(wordSenses, id);
	}

	@Ignore
	@Test
	public void testAlexaOneOne() {
		String id = "alexa1.1";
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add(""); // TODO
		test(wordSenses, id);
	}

	@Ignore
	@Test
	public void testAlexaOneTwo() {
		String id = "alexa1.2";
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add(""); // TODO
		test(wordSenses, id);
	}

	@Ignore
	@Test
	public void testGardenOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("lawn");
		wordSenses.add("mower");
		wordSenses.add("grass");
		test(wordSenses, "Garden1.1");
	}

	@Ignore
	@Test
	public void testGardenOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("shed");
		wordSenses.add("mower");
		wordSenses.add("lawn");
		test(wordSenses, "Garden1.2");
	}

	@Ignore
	@Test
	public void testGardenOneThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("tree");
		wordSenses.add("hedge");
		test(wordSenses, "Garden1.3");
	}

	@Ignore
	@Test
	public void testGardenOneFour() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("saw");
		wordSenses.add("table (furniture)");
		wordSenses.add("table (furniture)");
		test(wordSenses, "Garden1.4");
	}

	@Ignore
	@Test
	public void testGardenOneFive() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("tank");
		wordSenses.add("rake");
		wordSenses.add("shed");
		test(wordSenses, "Garden1.5");
	}

	@Ignore
	@Test
	public void testBarOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("tonic water");
		wordSenses.add("glass (drinkware)");
		wordSenses.add("gin");
		test(wordSenses, "Bar1.1");
	}

	@Ignore
	@Test
	public void testBarOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("cuba");
		wordSenses.add("cola");
		wordSenses.add("rum");
		wordSenses.add("lime (fruit)");
		wordSenses.add("juice");
		wordSenses.add("glass (drinkware)");
		test(wordSenses, "Bar1.2");
	}

	@Ignore
	@Test
	public void testBarOneThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("champagne");
		wordSenses.add("champagne");
		wordSenses.add("glass");
		wordSenses.add("counter (furniture)");
		test(wordSenses, "Bar1.3");
	}

	@Ignore
	@Test
	public void testBarOneFour() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("lime (fruit)");
		wordSenses.add("juice");
		wordSenses.add("cocktail");
		wordSenses.add("cocktail shaker");
		wordSenses.add("basil");
		wordSenses.add("syrup");
		wordSenses.add("gin");
		wordSenses.add("cocktail");
		wordSenses.add("glass (drinkware)");
		wordSenses.add("counter (furniture)");
		test(wordSenses, "Bar1.4");
	}

	@Ignore
	@Test
	public void testBarOneFive() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("beer");
		wordSenses.add("table (furniture)");
		test(wordSenses, "Bar1.5");
	}

	@Ignore
	@Test
	public void testOneOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("popcorn");
		wordSenses.add("popcorn");
		wordSenses.add("hand");
		test(wordSenses, "1.1");
	}

	@Ignore
	@Test
	public void testOneTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		test(wordSenses, "1.2");
	}

	@Ignore
	@Test
	public void testOneThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("fridge");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("refrigerator");
		wordSenses.add("juice");
		test(wordSenses, "1.3");
	}

	@Ignore
	@Test
	public void testTwoOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("popcorn");
		wordSenses.add("bag");
		test(wordSenses, "2.1");
	}

	@Ignore
	@Test
	public void testTwoTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		test(wordSenses, "2.2");
	}

	@Ignore
	@Test
	public void testTwoThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("refrigerator");
		test(wordSenses, "2.3");
	}

	@Ignore
	@Test
	public void testThreeOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("popcorn");
		wordSenses.add("kitchen");
		wordSenses.add("table (furniture)");
		test(wordSenses, "3.1");
	}

	@Ignore
	@Test
	public void testThreeTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("cup");
		wordSenses.add("kitchen");
		wordSenses.add("table (furniture)");
		wordSenses.add("dishwasher");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		test(wordSenses, "3.2");
	}

	@Ignore
	@Test
	public void testThreeThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		test(wordSenses, "3.3");
	}

	@Ignore
	@Test
	public void testFourOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("popcorn");
		wordSenses.add("bag");
		test(wordSenses, "4.1");
	}

	@Ignore
	@Test
	public void testFourTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("cup");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		test(wordSenses, "4.2");
	}

	@Ignore
	@Test
	public void testFourThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		test(wordSenses, "4.3");
	}

	@Ignore
	@Test
	public void testIfFourOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("cupboard");
		test(wordSenses, "If.4.1");
	}

	@Ignore
	@Test
	public void testIfFourTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("cupboard");
		test(wordSenses, "If.4.2");
	}

	@Ignore
	@Test
	public void testIfFourThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("table (furniture)");
		wordSenses.add("dishware");
		wordSenses.add("dishware");
		wordSenses.add("dishwasher");
		wordSenses.add("dishwasher");
		wordSenses.add("cupboard");
		test(wordSenses, "If.4.3");
	}

	@Ignore
	@Test
	public void testIfFiveOne() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		wordSenses.add("vodka");
		test(wordSenses, "If.5.1");
	}

	@Ignore
	@Test
	public void testIfFiveTwo() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("refrigerator");
		wordSenses.add("orange (fruit)");
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		test(wordSenses, "If.5.2");
	}

	@Ignore
	@Test
	public void testIfFiveThree() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("orange (fruit)");
		wordSenses.add("refrigerator");
		wordSenses.add("vodka");
		wordSenses.add("orange (fruit)");
		wordSenses.add("orange (fruit)");
		wordSenses.add("juice");
		test(wordSenses, "If.5.3");
	}

	@Ignore
	@Test
	public void testSSevenPEight() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("plate (dishware)");
		wordSenses.add("dishwasher");
		wordSenses.add("water");
		wordSenses.add("sink");
		wordSenses.add("refrigerator");
		wordSenses.add("refrigerator");
		wordSenses.add("food");
		wordSenses.add("plate (dishware)");
		wordSenses.add("refrigerator");
		wordSenses.add("plate (dishware)");
		wordSenses.add("microwave");
		wordSenses.add("door");
		wordSenses.add("table (furniture)");
		test(wordSenses, "s7p08");
	}

	@Ignore
	@Test
	public void testSSevenPNine() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("plate (dishware)");
		wordSenses.add("dishwasher");
		wordSenses.add("meal");
		wordSenses.add("refrigerator");
		wordSenses.add("plate (dishware)");
		wordSenses.add("microwave");
		wordSenses.add("table (furniture)");
		test(wordSenses, "s7p09");
	}

	@Ignore
	@Test
	public void testSSevenPTen() {
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("dishwasher");
		wordSenses.add("plate (dishware)");
		wordSenses.add("plate (dishware)");
		wordSenses.add("refrigerator");
		wordSenses.add("meal");
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

	@Ignore
	@Test
	public void testSEightPOne() {
		String id = "s8p01";
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("machine");
		wordSenses.add("laundry");
		wordSenses.add("laundry");
		wordSenses.add("clothes dryer");
		wordSenses.add("clothes dryer");
		test(wordSenses, id);
	}

	@Ignore
	@Test
	public void testSEightPTwo() {
		String id = "s8p02";
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("machine");
		wordSenses.add("push-button");
		wordSenses.add("machine");
		wordSenses.add("door");
		wordSenses.add("laundry");
		wordSenses.add("machine");
		wordSenses.add("clothes dryer");
		wordSenses.add("push-button");
		wordSenses.add("clothes dryer");
		wordSenses.add("door");
		wordSenses.add("laundry");
		wordSenses.add("clothes dryer");
		wordSenses.add("door");
		wordSenses.add("push-button");
		test(wordSenses, id);
	}

	@Ignore
	@Test
	public void testSEightPThree() {
		String id = "s8p03";
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("laundry");
		wordSenses.add("clothes dryer");
		wordSenses.add("laundry");
		wordSenses.add("clothes dryer");
		wordSenses.add("clothes dryer");
		wordSenses.add("program (machine)");
		test(wordSenses, id);
	}

	@Ignore
	@Test
	public void testSEightPFour() {
		String id = "s8p04";
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("basement");
		wordSenses.add("laundry");
		wordSenses.add("room");
		wordSenses.add("laundry");
		wordSenses.add("machine");
		wordSenses.add("clothes dryer");
		wordSenses.add("clothes");
		wordSenses.add("door");
		wordSenses.add("program (machine)");
		wordSenses.add("minute");
		test(wordSenses, id);
	}

	@Ignore
	@Test
	public void testSEightPFive() {
		String id = "s8p05";
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("machine");
		wordSenses.add("machine");
		wordSenses.add("action (philosophy)"); // for process
		wordSenses.add("action (philosophy)"); // for process
		wordSenses.add("clothes dryer");
		wordSenses.add("door");
		wordSenses.add("door");
		wordSenses.add("machine");
		wordSenses.add("laundry");
		wordSenses.add("clothes dryer");
		wordSenses.add("door");
		wordSenses.add("clothes dryer");
		wordSenses.add("clothes dryer");
		test(wordSenses, id);
	}

	@Ignore
	@Test
	public void testSEightPSix() {
		String id = "s8p06";
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
		test(wordSenses, id);
	}

	@Ignore
	@Test
	public void testSEightPSeven() {
		String id = "s8p07";
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("machine");
		wordSenses.add("clothes");
		wordSenses.add("clothes dryer");
		wordSenses.add("clothes dryer");
		wordSenses.add("clothes");
		wordSenses.add("clothes dryer");
		test(wordSenses, id);
	}

	@Ignore
	@Test
	public void testSEightPEight() {
		String id = "s8p08";
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("machine");
		wordSenses.add("laundry");
		wordSenses.add("machine");
		wordSenses.add("clothes dryer");
		wordSenses.add("laundry");
		wordSenses.add("clothes dryer");
		wordSenses.add("clothes dryer");
		test(wordSenses, id);
	}

	@Ignore
	@Test
	public void testSEightPNine() {
		String id = "s8p09";
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("machine");
		wordSenses.add("machine");
		wordSenses.add("laundry");
		wordSenses.add("laundry");
		wordSenses.add("basket");
		wordSenses.add("clothes dryer");
		wordSenses.add("laundry");
		wordSenses.add("clothes dryer");
		wordSenses.add("program (machine)");
		test(wordSenses, id);
	}

	@Ignore
	@Test
	public void testSEightPTen() {
		String id = "s8p10";
		List<String> wordSenses = new ArrayList<>();
		wordSenses.add("clothes dryer");
		wordSenses.add("door");
		wordSenses.add("machine");
		wordSenses.add("clothes dryer");
		wordSenses.add("clothes dryer");
		test(wordSenses, id);
	}
}
