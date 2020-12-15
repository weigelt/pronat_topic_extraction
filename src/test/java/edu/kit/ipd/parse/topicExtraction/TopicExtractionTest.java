package edu.kit.ipd.parse.topicExtraction;

import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.graphBuilder.GraphBuilder;
import edu.kit.ipd.parse.luna.data.MissingDataException;
import edu.kit.ipd.parse.luna.data.PrePipelineData;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.pipeline.PipelineStageException;
import edu.kit.ipd.parse.luna.tools.StringToHypothesis;
import edu.kit.ipd.parse.ner.NERTagger;
import edu.kit.ipd.parse.shallownlp.ShallowNLP;
import edu.kit.ipd.parse.topicExtraction.util.TestHelper;
import edu.kit.ipd.parse.topicExtraction.util.Text;
import edu.kit.ipd.parse.wikiWSD.WordSenseDisambiguation;

/**
 * @author Jan Keim
 *
 */
public class TopicExtractionTest {
	private static final Logger logger = LoggerFactory.getLogger(TopicExtractionTest.class);

	private static TopicExtraction				topicExtraction;
	private static HashMap<String, Text> texts;
	private PrePipelineData						ppd;
	private static WordSenseDisambiguation		wsd;
	private static ShallowNLP					snlp;
	private static GraphBuilder					graphBuilder;
	private static NERTagger					ner;

	@BeforeClass
	public static void beforeClass() {
		graphBuilder = new GraphBuilder();
		graphBuilder.init();
		snlp = new ShallowNLP();
		snlp.init();
		ner = new NERTagger();
		ner.init();
		wsd = new WordSenseDisambiguation();
		wsd.init();

		topicExtraction = new TopicExtraction();
		topicExtraction.init();

		texts = TestHelper.texts;
	}

	private void executePrepipeline(PrePipelineData ppd) {
		try {
			synchronized (snlp) {
				snlp.exec(ppd);
			}
			synchronized (ner) {
				ner.exec(ppd);
			}
			synchronized (graphBuilder) {
				graphBuilder.exec(ppd);
			}
			synchronized (wsd) {
				wsd.exec(ppd);
			}
		} catch (final PipelineStageException e) {
			e.printStackTrace();
		}
	}

	private void testOneText(String id) {
		logger.debug(id);
		this.ppd = new PrePipelineData();
		final Text text = texts.get(id);
		final String input = text.getText().replace("\n", " ");
		this.ppd.setMainHypothesis(StringToHypothesis.stringToMainHypothesis(input));
		this.executePrepipeline(this.ppd);
		try {
			final IGraph graph = this.ppd.getGraph();
			topicExtraction.setGraph(graph);
			topicExtraction.exec();
		} catch (final MissingDataException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testOneOne() {
		this.testOneText("1.1");
	}

	@Test
	public void testThreeTwo() {
		this.testOneText("3.2");
	}

}
