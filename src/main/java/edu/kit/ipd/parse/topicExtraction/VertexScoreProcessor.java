/**
 *
 */
package edu.kit.ipd.parse.topicExtraction;

import java.util.List;
import java.util.Set;

/**
 * @author Jan Keim
 *
 */
public interface VertexScoreProcessor {
	public Set<VertexScoreTuple> processCentralityScores(List<VertexScoreTuple> centralityScoresTuples);
}
