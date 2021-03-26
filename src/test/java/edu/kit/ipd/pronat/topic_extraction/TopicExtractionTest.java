package edu.kit.ipd.pronat.topic_extraction;

import java.util.HashMap;

import edu.kit.ipd.pronat.graph_builder.GraphBuilder;
import edu.kit.ipd.pronat.ner.NERTagger;
import edu.kit.ipd.pronat.prepipedatamodel.PrePipelineData;
import edu.kit.ipd.pronat.prepipedatamodel.tools.StringToHypothesis;
import edu.kit.ipd.pronat.shallow_nlp.ShallowNLP;
import edu.kit.ipd.pronat.topic_extraction.util.TestHelper;
import edu.kit.ipd.pronat.topic_extraction.util.Text;
import edu.kit.ipd.pronat.wiki_wsd.WordSenseDisambiguation;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.luna.data.MissingDataException;
import edu.kit.ipd.parse.luna.graph.IGraph;
import edu.kit.ipd.parse.luna.pipeline.PipelineStageException;

/**
 * @author Jan Keim
 * @author Sebastian Weigelt
 *
 */
public class TopicExtractionTest {
	private static final Logger logger = LoggerFactory.getLogger(TopicExtractionTest.class);

	private static TopicExtraction topicExtraction;
	private static HashMap<String, Text> texts;
	private PrePipelineData ppd;
	private static MyWikiWSD wsd;
	private static ShallowNLP snlp;
	private static GraphBuilder graphBuilder;
	private static NERTagger ner;

	@BeforeClass
	public static void beforeClass() {
		graphBuilder = new GraphBuilder();
		graphBuilder.init();
		snlp = new ShallowNLP();
		snlp.init();
		ner = new NERTagger();
		ner.init();
		wsd = new MyWikiWSD();
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
				wsd.setGraph(ppd.getGraph());
				wsd.exec();
			}
		} catch (final PipelineStageException | MissingDataException e) {
			e.printStackTrace();
		}
	}

	private void testOneText(String id) {
		logger.debug(id);
		ppd = new PrePipelineData();
		final Text text = texts.get(id);
		final String input = text.getText().replace("\n", " ");
		ppd.setMainHypothesis(StringToHypothesis.stringToMainHypothesis(input));
		executePrepipeline(ppd);
		try {
			final IGraph graph = ppd.getGraph();
			topicExtraction.setGraph(graph);
			topicExtraction.exec();
		} catch (final MissingDataException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testOneOne() {
		testOneText("1.1");
	}

	@Test
	public void testThreeTwo() {
		testOneText("3.2");
	}

	static class MyWikiWSD extends WordSenseDisambiguation {

		@Override
		public void exec() {
			super.exec();
		}
	}
}
