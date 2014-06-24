package Connect4;
import Connect4.ConnectState;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import klesk.math.search.AlphaBetaSearcher;
import klesk.math.search.State;

public class ConnectSearcher extends AlphaBetaSearcher{
	public ConnectSearcher(State aStartState, boolean aIsMaximizingPlayerFirst,
			double aMaximumDepth) {
		super(aStartState, aIsMaximizingPlayerFirst, aMaximumDepth);
	}

	@Override
	public void buildChildren(State aParent) {
		List<State> children = new ArrayList<State>();
		ConnectState sParent = (ConnectState)aParent;
		for (int i = 0; i < sParent.numberOfColumns; i++) {
			ConnectState child = new ConnectState(sParent);
			child.makeMove(i);
			child.setRootMove(Integer.toString(i));
			children.add(child);
			child.switchPlayers();
		}
		sParent.setChildren(children);
	}

	public static void main(String []args) {
		try {
			System.out.println("Podaj ilosc wierszy");
			BufferedReader rowsReader = new BufferedReader(new InputStreamReader(System.in), 1);
			String line = rowsReader.readLine();
			int rows = Integer.valueOf(line);
			System.out.println("Podaj ilosc kolumn");
			BufferedReader columnsReader = new BufferedReader(new InputStreamReader(System.in), 1);
			line = columnsReader.readLine();
			int columns = Integer.valueOf(line);
			int whoStarts;
			do {
				System.out.println("Kto zaczyna?");
				System.out.println("1 - czlowiek");
				System.out.println("2 - komputer");
				BufferedReader whoStartsReader = new BufferedReader(new InputStreamReader(System.in),1);
				line = whoStartsReader.readLine();
				whoStarts = Integer.valueOf(line);
				if (whoStarts != 1 && whoStarts != 2) {
					System.out.println("Bledne dane");
				}
			}
			while (whoStarts != 1 && whoStarts != 2);
			
			ConnectState board = new ConnectState(rows, columns);
			
			//ZACZYNAMY GRE
			
			if (whoStarts == 1) {
				board.switchPlayers();
				System.out.println(board);
				board.playerMove();
				board.switchPlayers();
			}

			double best;
			int min = 0;
			
			while(true) {
				ConnectSearcher connect4 = new ConnectSearcher(board, true, 2.5);
				connect4.doSearch();
				best = Double.NEGATIVE_INFINITY;
				Map<String, Double> scores = connect4.getMovesMiniMaxes();
				Set<String> keys = connect4.getMovesMiniMaxes().keySet();
				
				System.out.println(connect4.getMovesMiniMaxes());
				
				for (String key : keys) {
					double value = scores.get(key);
					if (value > best) {
						best = value;
						min = Integer.valueOf(key);
					}	
				}
				board.makeMove(min);
				System.out.println(board);
				System.out.println("Komputer wykonal ruch: " + min);
				board.switchPlayers();
				
				if (board.computeHeuristicGrade() == Double.POSITIVE_INFINITY) {
					System.out.println("Przegrales!");
					break;
				}
				
				board.playerMove();
				System.out.println(board);
				board.switchPlayers();
				
				if (board.computeHeuristicGrade() == Double.NEGATIVE_INFINITY) {
					System.out.println("Wygrales!");
					break;
				}
			}
		}
		catch (Exception e) {
			System.out.println("Cos jest nie teges");
		}
	}
}
