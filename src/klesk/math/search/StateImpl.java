package klesk.math.search;

import java.util.List;

/**
 * An abstract implementation of the State interface.
 * 
 * @author Przemyslaw Klesk (<a href="mailto:pklesk@wi.zut.edu.pl">pklesk@wi.zut.edu.pl</a>)
 */
public abstract class StateImpl implements State {

	/**
	 * Reference to the parent of this state (forms the tree structure).
	 */
	protected State parent = null;
	
	/**
	 * List of references to the children of this state (forms the tree structure).
	 */
	protected List<State> children = null;	
	
	/**
	 * Value of: g - cost of reaching this state from the initial state.
	 */
	protected double g = 0.0;
	
	/**
	 * Value of: h - heuristic grade.
	 */
	protected double h = 0.0;		
	
	/**
	 * How deep in the tree is this state.
	 */
	protected double depth = 0.0;	
	
	/**
	 * So far guaranteed payoff for maximizing player.
	 */
	private double alpha = Double.NEGATIVE_INFINITY;

	/**
	 * So far guaranteed payoff for minimizing player.
	 */
	private double beta = Double.POSITIVE_INFINITY;

	/**
	 * String name of the move that led reaching to this state.
	 */
	protected String rootMove = null;
	
	/**
	 * Creates a new instance of StateImpl. Computes
	 * the value of heuristic grade for it by calling
	 * computeHeuristicGrade() method.
	 * 
	 * @param aParent reference to the parent to be set.
	 */
	public StateImpl(State aParent) {
		parent = aParent;	
		//h = computeHeuristicGrade();
	}	
		
	/* (non-Javadoc)
	 * @see klesk.math.search.State#getChildren()
	 */
	public List<State> getChildren() {	
		return children;
	}

	/* (non-Javadoc)
	 * @see klesk.math.search.State#getParent()
	 */
	public State getParent() {
		return parent;
	}		
	
	/* (non-Javadoc)
	 * @see klesk.math.search.State#getH()
	 */
	public double getH() {
		return h;
	}

	/* (non-Javadoc)
	 * @see klesk.math.search.State#setH(double)
	 */
	public void setH(double aH) {
		h = aH;		
	}	
	
	/* (non-Javadoc)
	 * @see klesk.math.search.State#getG()
	 */
	public double getG() {
		return g;
	}

	/* (non-Javadoc)
	 * @see klesk.math.search.State#setG(double)
	 */
	public void setG(double aG) {
		g = aG;		
	}

	/* (non-Javadoc)
	 * @see klesk.math.search.State#getF()
	 */
	public double getF() {
		return getG() + getH();
	}
		
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(T)
	 */
	public int compareTo(Object aState) {		
		return (getF() > ((State) aState).getF()) ? 1 : -1;
	}

	/* (non-Javadoc)
	 * @see klesk.math.search.State#setChildren(java.util.Map)
	 */
	public void setChildren(List<State> aChildren) {
		children = aChildren;		
	}

	/* (non-Javadoc)
	 * @see klesk.math.search.State#setParent(klesk.math.search.State)
	 */
	public void setParent(State aParent) {
		parent = aParent;		
	}
		
	/* (non-Javadoc)
	 * @see klesk.math.search.State#getAlpha()
	 */
	public double getAlpha() {
		return alpha;
	}

	/* (non-Javadoc)
	 * @see klesk.math.search.State#getBeta()
	 */
	public double getBeta() {
		return beta;
	}

	/* (non-Javadoc)
	 * @see klesk.math.search.State#setAlpha(double)
	 */
	public void setAlpha(double aAlpha) {
		alpha = aAlpha;		
	}

	/* (non-Javadoc)
	 * @see klesk.math.search.State#setBeta(double)
	 */
	public void setBeta(double aBeta) {
		beta = aBeta;		
	}


	/* (non-Javadoc)
	 * @see klesk.math.search.State#getRootMove()
	 */
	public String getRootMove() {
		return rootMove;
	}					
	
	/* (non-Javadoc)
	 * @see klesk.math.search.State#setRootMove(java.lang.String)
	 */
	public void setRootMove(String aMove) {
		rootMove = aMove;		
	}

	/* (non-Javadoc)
	 * @see klesk.math.search.State#isAdmissible()
	 */
	public boolean isAdmissible() {
		return true;
	}

	/* (non-Javadoc)
	 * @see klesk.math.search.State#getDepth()
	 */
	public double getDepth() {
		return depth;
	}

	/* (non-Javadoc)
	 * @see klesk.math.search.State#setDepth(double)
	 */
	public void setDepth(double aDepth) {
		depth = aDepth;		
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object aObject) {
		return getHashCode().equals(((State) aObject).getHashCode());
	}

	/* (non-Javadoc)
	 * @see klesk.math.search.State#computeHeuristicGrade()
	 */
	public double computeHeuristicGrade() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see klesk.math.search.State#getHashCode()
	 */
	public String getHashCode() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}