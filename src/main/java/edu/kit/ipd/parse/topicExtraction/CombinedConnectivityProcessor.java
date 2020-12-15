/**
 *
 */
package edu.kit.ipd.parse.topicExtraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.kit.ipd.parse.topicExtraction.graph.TopicGraph;
import edu.kit.ipd.parse.topicExtraction.graph.WikiVertex;

/**
 * @author Jan Keim
 *
 */
public class CombinedConnectivityProcessor implements VertexScoreProcessor {
	private static final Logger logger = LoggerFactory.getLogger(CombinedConnectivityProcessor.class);

	private TopicGraph	topicGraph;
	private int			topics;

	public CombinedConnectivityProcessor(TopicGraph topicGraph) {
		this(topicGraph, 8);
	}

	public CombinedConnectivityProcessor(TopicGraph topicGraph, int topics) {
		super();
		this.topicGraph = topicGraph;
		this.topics = topics;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see im.janke.topicExtraction.VertexScoreProcessor#processCentralityScores(java.util.List,
	 * im.janke.topicExtraction.graph.TopicGraph)
	 */
	@Override
	public Set<VertexScoreTuple> processCentralityScores(List<VertexScoreTuple> centralityScoresTuples) {
		Objects.requireNonNull(topicGraph);
		Objects.requireNonNull(centralityScoresTuples);
		centralityScoresTuples.sort(Collections.reverseOrder());

		Set<VertexScoreTuple> vsSet = new HashSet<>();

		int maxConnectivity = topicGraph.getMaxSenseConnectivity();
		if (logger.isDebugEnabled()) {
			logger.debug("Max Sense Connectivity = " + maxConnectivity);
		}
		int currentConnectivity = maxConnectivity;
		Set<WikiVertex> processedVertices = new HashSet<>();

		while ((vsSet.size() < topics) && (currentConnectivity > 0)) {
			// get possible topic vertices
			Set<WikiVertex> currPossTopicVertices = new HashSet<>();
			while (currPossTopicVertices.isEmpty() && (currentConnectivity > 0)) {
				Set<WikiVertex> connSenses = topicGraph.getVerticesWithSenseConnectivity(currentConnectivity);
				for (WikiVertex wv : connSenses) {
					if (processedVertices.contains(wv)) {
						continue;
					} else {
						currPossTopicVertices.add(wv);
					}
				}
				if (currPossTopicVertices.isEmpty()) {
					currentConnectivity--;
				}
			}

			if ((currentConnectivity <= 0) || currPossTopicVertices.isEmpty()) {
				break;
			}

			// go through (sorted) vst
			for (VertexScoreTuple vst : centralityScoresTuples) {
				if (!currPossTopicVertices.contains(vst.vertex)) {
					// continue until a possible topic vertex is found
					// as input should be sorted by centrality score, finds the
					// possible vertex with highest score
					continue;
				}
				vsSet.add(vst);
				processedVertices.add(vst.vertex);

				// act for vertices, that were not connected before
				// try to get a as high as possible connectivity between
				// remaining senses
				List<WikiVertex> remainingSenses = new ArrayList<>(topicGraph.getSenses());
				remainingSenses.removeAll(topicGraph.getInitialVerticesFor(vst.vertex));
				int remainingConnectivity = Math.min(currentConnectivity,
						topicGraph.getSenses().size() - currentConnectivity);
				if (vsSet.size() < topics) {
					processRemainingTopics(centralityScoresTuples, vsSet, processedVertices, remainingSenses,
							remainingConnectivity);
				}
				break;
			}
		}

		return vsSet;
	}

	private void processRemainingTopics(List<VertexScoreTuple> centralityScoresTuples, Set<VertexScoreTuple> vsSet,
			Set<WikiVertex> processedVertices, List<WikiVertex> remainingSenses, int remainingConnectivity) {
		// TODO!
		while ((remainingSenses.size() > 0) && (remainingConnectivity > 0)) {
			Set<WikiVertex> remConnectedVertices = topicGraph.getVerticesWithSenseConnectivity(remainingConnectivity);
			// find vertices, for which the list of connected vertices
			// contains only vertices, that are "remaining"
			List<WikiVertex> selectedRemVertices = new ArrayList<>();
			testRem: for (WikiVertex currTry : remConnectedVertices) {
				List<WikiVertex> vConnListTry = topicGraph.getInitialVerticesFor(currTry);
				for (WikiVertex ttt : vConnListTry) {
					if (!remainingSenses.contains(ttt)) {
						continue testRem;
					}
				}
				selectedRemVertices.add(currTry);
			}
			if (selectedRemVertices.isEmpty()) {
				remainingConnectivity--;
			} else {
				boolean found = false;
				for (VertexScoreTuple vst : centralityScoresTuples) {
					if (!selectedRemVertices.contains(vst.vertex) || vsSet.contains(vst)) {
						continue;
					}
					List<WikiVertex> vertexConnList = topicGraph.getInitialVerticesFor(vst.vertex);
					vsSet.add(vst);
					processedVertices.add(vst.vertex);
					remainingSenses.removeAll(vertexConnList);
					found = true;
					break;
				}
				if (!found) {
					// it hasn't found a proper node for the remaining connectivity
					remainingConnectivity--;
				}
			}
		}
	}
}
