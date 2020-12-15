package edu.kit.ipd.parse.topicExtraction;

import edu.kit.ipd.parse.topicExtraction.graph.WikiVertex;

/**
 * @author Jan Keim
 *
 */
class VertexScoreTuple implements Comparable<VertexScoreTuple> {
	WikiVertex	vertex;
	Double		score;

	public VertexScoreTuple(WikiVertex vertex, Double score) {
		this.vertex = vertex;
		this.score = score;
	}

	@Override
	public int compareTo(VertexScoreTuple o) {
		return Double.compare(score, o.score);
	}

}
