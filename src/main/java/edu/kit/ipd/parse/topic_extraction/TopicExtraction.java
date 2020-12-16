package edu.kit.ipd.parse.topic_extraction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import org.kohsuke.MetaInfServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.luna.agent.AbstractAgent;
import edu.kit.ipd.parse.luna.data.MissingDataException;
import edu.kit.ipd.parse.luna.data.PrePipelineData;
import edu.kit.ipd.parse.luna.graph.INode;
import edu.kit.ipd.parse.luna.graph.INodeType;
import edu.kit.ipd.parse.luna.tools.ConfigManager;
import edu.kit.ipd.parse.topic_extraction_common.Topic;
import edu.kit.ipd.parse.topic_extraction_common.TopicExtractionCore;
import edu.kit.ipd.parse.topic_extraction_common.TopicSelectionMethod;
import edu.kit.ipd.parse.topic_extraction_common.ontology.DBPediaConnector;
import edu.kit.ipd.parse.topic_extraction_common.ontology.OfflineDBPediaConnector;

/**
 * @author Jan Keim
 *
 */
@MetaInfServices(AbstractAgent.class)
public class TopicExtraction extends AbstractAgent {
	private static final Logger logger = LoggerFactory.getLogger(TopicExtraction.class);
	private static final String ID = "TopicExtraction";
	private static final String TOKEN_NODE_TYPE = "token";
	private static final String POS_ATTRIBUTE = "pos";
	private static final String NER_ATTRIBUTE = "ner";
	private static final String WSD_ATTRIBUTE = "wsd";
	public static final String TOPIC_ATTRIBUTE = "topic";
	public static final String TOPICS_NODE_TYPE = "topics";

	private TopicSelectionMethod topicSelectionMethod = TopicSelectionMethod.CombinedConnectivity;

	private TopicExtractionCore topicExtractionCore;

	//TODO
	@Override
	public void init() {
		this.setId(TopicExtraction.ID);

		final Properties props = ConfigManager.getConfiguration(this.getClass());
		final String offline = props.getProperty("OFFLINE_USE");
		DBPediaConnector resourceConnector;
		if ((offline == null) || offline.isEmpty() || offline.equals("N")) {
			String url = props.getProperty("URL", DBPediaConnector.DEFAULT_SERVICE_URL);
			if (url.isEmpty()) {
				url = DBPediaConnector.DEFAULT_SERVICE_URL;
			}
			resourceConnector = new DBPediaConnector(url);
		} else {
			if (TopicExtraction.logger.isInfoEnabled()) {
				TopicExtraction.logger.info("Using offline version for resource connection.");
			}
			final String owl = props.getProperty("OWL");
			if ((owl == null) || owl.isEmpty()) {
				throw new IllegalArgumentException("Could not load proper owl file from properties configuration.");
			}
			final String turtle1 = props.getProperty("TURTLE1");
			final String turtle2 = props.getProperty("TURTLE2");
			final String turtle3 = props.getProperty("TURTLE3");
			final String turtle4 = props.getProperty("TURTLE4");
			resourceConnector = new OfflineDBPediaConnector(owl, turtle1, turtle2, turtle3, turtle4);
		}
		int numTopics = -1;
		try {
			numTopics = Integer.parseInt(props.getProperty("TOPICS", "-1"));
		} catch (final NumberFormatException e) {
			TopicExtraction.logger.warn("Could not parse provided number for amount of topics. Use default instead.");
		}

		this.topicExtractionCore = new TopicExtractionCore(resourceConnector);
		this.topicExtractionCore.setNumTopics(numTopics);
	}

	public void setTopicSelectionMethod(TopicSelectionMethod tsm) {
		this.topicSelectionMethod = tsm;
	}

	public TopicSelectionMethod getTopicSelectionMethod() {
		return this.topicSelectionMethod;
	}

	private void prepareGraph() {
		// add graph attribute
		INodeType tokenType;
		if (this.graph.hasNodeType(TopicExtraction.TOPICS_NODE_TYPE)) {
			tokenType = this.graph.getNodeType(TopicExtraction.TOPICS_NODE_TYPE);
		} else {
			tokenType = this.graph.createNodeType(TopicExtraction.TOPICS_NODE_TYPE);
		}
		if (!tokenType.containsAttribute(TopicExtraction.TOPIC_ATTRIBUTE, "java.util.List")) {
			tokenType.addAttributeToType("java.util.List", TopicExtraction.TOPIC_ATTRIBUTE);
		}
	}

	@Override
	protected synchronized void exec() {
		this.prepareGraph();
		final List<INode> nodes = this.getTokenNodesFromGraph();
		final Map<INode, String> nodeToWSD = new HashMap<>();
		for (final INode node : nodes) {
			final String pos = node.getAttributeValue(TopicExtraction.POS_ATTRIBUTE).toString();
			if (pos.startsWith("NN")) {
				if (this.nodeIsNamedEntity(node)) {
					continue;
				}
				final String wsd = Objects.toString(node.getAttributeValue(TopicExtraction.WSD_ATTRIBUTE));
				if (!wsd.equals("null")) {
					nodeToWSD.put(node, wsd);
				}
			}
		}
		final List<Topic> topics = this.topicExtractionCore.getTopicsForSenses(nodeToWSD.values());

		TopicExtractionCore.setTopicsToInputGraph(topics, this.graph);

		if (TopicExtraction.logger.isDebugEnabled()) {
			final List<Topic> retrievedTopics = TopicExtractionCore.getTopicsFromIGraph(this.graph);
			TopicExtraction.logger.debug("Retrieved {} topics:", retrievedTopics.size());
			for (final Topic t : retrievedTopics) {
				if (logger.isDebugEnabled()) {
					TopicExtraction.logger.debug("{} ({})", t.getLabel(), String.join(", ", t.getRelatedSenses()));
				}

			}
		}
	}

	public void exec(PrePipelineData ppd) {
		try {
			this.graph = ppd.getGraph();
		} catch (final MissingDataException e) {
			e.printStackTrace();
		}
		this.exec();
	}

	private List<INode> getTokenNodesFromGraph() {
		return this.graph.getNodesOfType(this.graph.getNodeType(TOKEN_NODE_TYPE));
	}

	private boolean nodeIsNamedEntity(INode node) {
		// use NER from parse
		final String nodeString = node.getAttributeValue(NER_ATTRIBUTE).toString();
		return (!nodeString.equals("O") || nodeString.equalsIgnoreCase("armar"));
	}

}
