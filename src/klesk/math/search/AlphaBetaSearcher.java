package klesk.math.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Searcher object using alpha-beta cut-offs algorithm.
 * 
 * @author Przemyslaw Klesk (<a href="mailto:pklesk@wi.ps.pl">pklesk@wi.ps.pl</a>)
 */
public abstract class AlphaBetaSearcher {

	/**
	 * Keeps assements (mini-maxes) of currenlty analyzed moves.
	 * The map keeps pairs (string representation of the move, mini-max grade of leaves).
	 */
	private Map<String, Double> movesMiniMaxes = null;	
	
	/**
	 * Maximum depth constant.
	 */
	protected final double maximumDepth;

	/**
	 * Boolean stating if the maximizing player is the first to move.
	 */
	protected final boolean isMaximizingPlayerFirst;	
	
	/**
	 * Reference to the start state.
	 */
	protected State startState = null;
	
	/**
	 * Map of pairs (hash code of state, reference to that state)
	 * keeping all the visited states.
	 */
	protected Map<String, State> visited = null;	
	
	public int useOfVisited = 0;
	
	/**
	 * Creates an instance of alpha-beta searcher.
	 *
	 * @param aStartState state from which to start
	 * @param aIsMaximizingPlayerFirst player first to move
	 * @param aMaximumDepth maximum depth 
	 */
	public AlphaBetaSearcher(State aStartState, boolean aIsMaximizingPlayerFirst, double aMaximumDepth) {
		startState = aStartState;
		isMaximizingPlayerFirst = aIsMaximizingPlayerFirst;
		maximumDepth = aMaximumDepth;
		visited = new HashMap<String, State>();
		movesMiniMaxes = new HashMap<String, Double>();
	}		
	
	/**
	 * Returns the maximum depth.
	 * 
	 * @return maximum depth.
	 */
	public double getMaximumDepth() {
		return maximumDepth;
	}

	/**
	 * This method is meant to contain all operations needed
	 * by the user to populate a given state with suitable (in some sense)
	 * children.  
	 * 
	 * @param aParent reference to the parent state 
	 */
	public abstract void buildChildren(State aParent);

	/**
	 * Starts the alpha-beta search.
	 */
	public void doSearch() {
		if (isMaximizingPlayerFirst) evaluateMaxState(startState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
		else evaluateMinState(startState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, 0);
	}
	
	/**
	 * Evaluates a maximizing state.
	 * 
	 * @param aState reference to the state
	 * @param aAlpha so far alpha value 
	 * @param aBeta so far beta value
	 * @param aDepth depth
	 * @return the new calculated alpha value
	 */
	protected double evaluateMaxState(State aState, double aAlpha, double aBeta, double aDepth) {
		double startAlpha = aAlpha;
		double startBeta = aBeta;
		
		double grade = aState.computeHeuristicGrade();
		if ((grade == Double.POSITIVE_INFINITY) || (grade == Double.NEGATIVE_INFINITY) || (aDepth == maximumDepth)) {					
			aState.setAlpha(grade);
			aState.setBeta(grade);
			visited.put(aState.getHashCode() + "," + startAlpha + "," + startBeta + "," + aState.getDepth(), aState);						
			return grade;
		}
		
		double tempAlpha = Double.NEGATIVE_INFINITY;
		
		buildChildren(aState);
		List<State> children = aState.getChildren();
		for (int i = 0; i < children.size(); i++) {
			State child = children.get(i);
			
			boolean wasVisitedWithSameDepthAndAlphaBeta = false;
			String key = child.getHashCode() + "," + aAlpha + ","  + aBeta + "," + child.getDepth();
			if (visited.containsKey(key)) { //depth is a part of the hashCode
					wasVisitedWithSameDepthAndAlphaBeta = true;
					useOfVisited++;
			}
			 
			if (wasVisitedWithSameDepthAndAlphaBeta) tempAlpha = visited.get(key).getBeta();			
			else tempAlpha = evaluateMinState(child, aAlpha, aBeta, aDepth + 0.5);									
																		
			if (tempAlpha > aAlpha) {											
				aAlpha = tempAlpha;				
				if (aState.getParent() == null) { //we are at the top of the tree
					movesMiniMaxes.put(child.getRootMove(), tempAlpha);
				}							
			}
			
			if (aAlpha >= aBeta) break; //pruning condition
		}

		aState.setAlpha(aAlpha);
		aState.setBeta(aBeta);		
		
		visited.put(aState.getHashCode() + "," + startAlpha + "," + startBeta + "," + aState.getDepth(), aState);
		
		return aAlpha;
	}
	
	/**
	 * Evaluates a minimizing state.
	 * 
	 * @param aState reference to the state
	 * @param aAlpha so far alpha value 
	 * @param aBeta so far beta value
	 * @param aDepth depth
	 * @return the new calculated beta value
	 */
	protected double evaluateMinState(State aState, double aAlpha, double aBeta, double aDepth) {
		double startAlpha = aAlpha;
		double startBeta = aBeta;
		
		double grade = aState.computeHeuristicGrade();
		if ((grade == Double.POSITIVE_INFINITY) || (grade == Double.NEGATIVE_INFINITY) || (aDepth == maximumDepth)) {						
			aState.setAlpha(grade);
			aState.setBeta(grade);
			visited.put(aState.getHashCode() + "," + startAlpha + "," + startBeta + "," + aState.getDepth(), aState);
						
			return grade;
		}
				
		double tempBeta = Double.POSITIVE_INFINITY;
		
		buildChildren(aState);
		List<State> children = aState.getChildren();
		for (int i = 0; i < children.size(); i++) {
			State child = children.get(i);			
			
			boolean wasVisitedWithSameDepthAndAlphaBeta = false;	
			String key = child.getHashCode() + "," + aAlpha + ","  + aBeta + "," + child.getDepth();
			if (visited.containsKey(key)) { //depth is a part of the hashCode
				wasVisitedWithSameDepthAndAlphaBeta = true;
				useOfVisited++;
			}
			
			if (wasVisitedWithSameDepthAndAlphaBeta) tempBeta = visited.get(key).getAlpha();
			else tempBeta = evaluateMaxState(child, aAlpha, aBeta, aDepth + 0.5);
			
								
			if (tempBeta < aBeta) { 											
				aBeta = tempBeta;
				if (aState.getParent() == null) { //we are at the top of the tree
					movesMiniMaxes.put(child.getRootMove(), tempBeta);
				}				
			}

			if (aAlpha >= aBeta) break; //pruning condition			
		}				
		
		aState.setAlpha(aAlpha);
		aState.setBeta(aBeta);					
		
		visited.put(aState.getHashCode() + "," + startAlpha + "," + startBeta + "," + aState.getDepth(), aState);
		
		return aBeta;
	}

	/**
	 * Returns the moves mini maxes
	 * 
	 * @return moves mini maxes.
	 */
	public Map<String, Double> getMovesMiniMaxes() {
		return movesMiniMaxes;
	}

	/**
	 * Returns the map of visited states.
	 * 
	 * @return map of visited states.
	 */
	public Map<String, State> getVisited() {
		return visited;
	}		
	
	
}
