package klesk.math.search;

import java.util.List;

/**
 * Represents a general state that can be used in an arbitrary
 * search problem.
 * 
 * @author Przemyslaw Klesk (<a href="mailto:pklesk@wi.zut.edu.pl">pklesk@wi.zut.edu.pl</a>)
 */
public interface State extends Comparable {
	
	/**
	 * Computes the h value - heuristic grade for this state. This
	 * method is called only once in the StateImpl class at the moment of 
	 * object creation, the value is stored in an inner variable, and then 
	 * be accessed by getH() method. 
	 * 
	 * @return h value - heuristic grade for this state
	 */
	public double computeHeuristicGrade();
	
	/**
	 * Returns the h value - heuristic grade for this state.
	 * 
	 * @return h value - heuristic grade for this state
	 */
	public double getH();	
	
	/**
	 * Returns the g value - cost of reaching this state from initial state.
	 * 
	 * @return g value - cost of reaching this state from initial state
	 */
	public double getG();	

	/**
	 * Sets the g value. 
	 * 
	 * @param aG value to be set
	 */
	public void setG(double aG);
	
	/**
	 * Returns the f value - sum of g and h values (f = g + h).
	 * 
	 * @return f value
	 */
	public double getF();
	
	/**
	 * Returns the hash code for this state.
	 * 
	 * @return hash code for this state
	 */
	public String getHashCode();

	/**
	 * Returns a list of children of this state (in the search tree).
	 * 
	 * @return list of children of this state
	 */
	public List<State> getChildren();
	
	/**
	 * Sets the reference to the children of this state.
	 * 
	 * @param aChildren reference to the children to be set
	 */
	public void setChildren(List<State> aChildren);
	
	/**
	 * Returns the reference to the parent of this state (in the tree)
	 * or null if this state is the root. 
	 * 
	 * @return reference to the parent of this state
	 */
	public State getParent();
	
	/**
	 * Sets the reference to the parent of this state.
	 * 
	 * @param aParent reference to the parent of this state to be set
	 */
	public void setParent(State aParent);
	
	/**
	 * Returns the boolean showing wether this state is admissible.
	 * (E.g. in sudoku search some states may be inadmissible, so
	 * their children should not be considered further.)
	 * 
	 * @return boolean showing wether this state is admissible
	 */
	public boolean isAdmissible();
	
	/**
	 * Useful method for games searching algorithms. 
	 * Returns the string name of the root move (at the top of the
	 * tree) that led to reaching this state. 
	 * 
	 * @return string representing the root move
	 */
	public String getRootMove();
	
	/**
	 * Sets the string name of the root move. 
	 * 
	 * @param aMove string name of the root move to be set.
	 */
	public void setRootMove(String aMove);
	
	/**
	 * Returns the guaranteed minimum score (payoff) for the maximizing player.
	 * (Used only in games search searching algorithms.)
	 * 
	 * @return guaranteed minimum score for the maximizing player
	 */
	public double getAlpha();

	/**
	 * Sets the new value for alpha.
	 * (Used only in games search searching algorithms.)
	 * 
	 * @aAlpha new value to be set
	 */
	public void setAlpha(double aAlpha);	
	
	/**
	 * Returns the guaranteed maximum score (loss) for the minimizing player.
	 * (Used only in games search searching algorithms.)
	 * 
	 * @return guaranteed maximum score for the minimizing player
	 */	
	public double getBeta();
	
	/**
	 * Sets the new value for beta.
	 * (Used only in games search searching algorithms.)
	 * 
	 * @aBeta new value to be set
	 */
	public void setBeta(double aBeta);
	
	/**
	 * Returns the depth property (how deep in the tree is this state).
	 * 
	 * @return depth property
	 */
	public double getDepth();
	
	/**
	 * Sets the depth property.
	 * 
	 * @param aDepth depth value to be set
	 */
	public void setDepth(double aDepth);
}