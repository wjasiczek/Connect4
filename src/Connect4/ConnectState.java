package Connect4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Vector; 
import java.util.Enumeration;
import klesk.math.search.StateImpl;

public class ConnectState extends StateImpl {
	public static final byte x = 2;
	public static final byte o = 1;
	public static final byte e = 0; // puste
	byte board[][];
	int numberOfRows;
	int numberOfColumns;
	boolean whichPlayer; // o-komputer maksymalizacujacy, x-czlowiek minimalizujacy

	public ConnectState(int rows, int columns) {
		super(null);
		numberOfRows = rows;
		numberOfColumns = columns;
		whichPlayer = true;
		board = new byte[numberOfRows][numberOfColumns];
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				board[i][j] = 0;
			}
		}
		computeHeuristicGrade();
	}

	public ConnectState(ConnectState parent) {
		super(parent);
		this.numberOfColumns = parent.numberOfColumns;
		this.numberOfRows = parent.numberOfRows;
		this.whichPlayer = parent.whichPlayer;
		this.board = new byte[parent.numberOfRows][parent.numberOfColumns];
		for (int i = 0; i < parent.numberOfRows; i++) {
			for (int j = 0; j < parent.numberOfColumns; j++) {
				this.board[i][j] = parent.board[i][j];
			}
		}
		computeHeuristicGrade();
	}

	public void switchPlayers() {
		whichPlayer = (whichPlayer) ? false : true;
	}
	
	public void playerMove()
	{
		BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in), 1);
		int choice = 0;
		do
		{
			String move=new String();
			System.out.println("Wybierz kolumne:");
			try 
			{
				move = keyboard.readLine();
				choice = Integer.parseInt(move);
			}
			catch (Exception e)
			{
				System.out.println("Blad czytania z klawiatury");
			}
		} while (choice > numberOfColumns - 1 || choice < 0);
		makeMove(choice);
	}

	public void makeMove(int column) {
		int place = numberOfRows - 1;
		for (int i = 0; i < numberOfRows; i++) {
			if (board[i][column] == e) {
				continue;
			} else {
				place = i - 1;
				break;
			}
		}
		if (whichPlayer) { // true = o
			board[place][column] = o;
		} else {
			board[place][column] = x;
		}
	}

	public String toString() {
		String result = "";
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				if (board[i][j] == o) {
					result += "O ";
				} else {
					if (board[i][j] == x) {
						result += "X ";
					} else {
						result += "- ";
					}
				}
			}
			result += "\n";
		}
		for (int i = 0; i < numberOfColumns; i++) {
			result += "* ";
		}
		result += "\n";
		for (int i = 0; i < numberOfColumns; i++) {
			result += Integer.toString(i);
			result += " ";
		}
		return result;
	}

	public double computeHeuristicGrade() {
		double heuristicGrade1 = 0;
		double heuristicGrade2 = 0;
		byte[][] tempBoard = new byte[numberOfRows][numberOfColumns];
		
		for (int i = 0; i < numberOfColumns; i++) {
			if (board[0][i] == o) {
				setH(Double.POSITIVE_INFINITY);
				return Double.POSITIVE_INFINITY;
			}
			else if (board[0][i] == x) {
				setH(Double.NEGATIVE_INFINITY);
				return Double.NEGATIVE_INFINITY;
			}
		}
		
		//HEURYSTYKA DLA AKTUALNEGO GRACZA
		for (int i = 0; i < numberOfRows; i++) {						//WIERSZE
			heuristicGrade1 += countString(board[i], o, x);
		}
		
		byte[] buforRow = new byte[numberOfRows];						//KOLUMNY
		for (int i = 0; i < numberOfColumns; i++) {
			for (int j = 0; j < numberOfRows; j++) {
				buforRow[j] = board[j][i];		
			}
			heuristicGrade1 += countString(buforRow, o, x);
		}
	
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {					//SKOSY
				tempBoard[i][j] = 0;
			}
		}
		for (int j = 0; j <= numberOfColumns - 4; j++) {				
			for (int i = numberOfRows - 4 ; i >= 0; i--) {
				int k = i;
				int l = j;
				int size = 0;
				while (k <= numberOfRows - 1 && l < numberOfColumns) {
					if (tempBoard[k][l] == 1) {
						break;
					}
					tempBoard[k][l] = 1;
					size++;
					k++;
					l++;
				}
			
				if (size > 1) {
					byte[] cross1 = new byte[size];
					k = i;
					l = j;
					for (int counter = 0; counter < size; counter++) {
						cross1[counter] = board[k][l];
						k++;
						l++;	
					}
					heuristicGrade1 += countString(cross1, o, x);
					if(heuristicGrade1 == Double.NEGATIVE_INFINITY || heuristicGrade1 == Double.POSITIVE_INFINITY) {
						setH(heuristicGrade1);
						return heuristicGrade1;
					}
				}
			}
		}
		
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				tempBoard[i][j] = 0;
			}
		}
		for (int j = numberOfColumns - 1; j >= 0; j--)
		{
			for (int i = numberOfRows - 1; i >= 0; i--)
			{
				int k = i;
				int l = j;
				int size = 0;
				while (k <= numberOfRows - 1 && l >= 0)
				{
					if (tempBoard[k][l] == 1)
					{
						break;
					}
					tempBoard[k][l] = 1;
					size++;
					k++;
					l--;
				}
			
				if(size > 1)
				{
					byte[] cross2 = new byte[size];
					k = i;
					l = j;
					for (int counter = 0; counter < size; counter++)
					{
						cross2[counter] = board[k][l];
						k++;
						l--;	
					}
					heuristicGrade1 += countString(cross2, o, x);
					if(heuristicGrade1 == Double.NEGATIVE_INFINITY || heuristicGrade1 == Double.POSITIVE_INFINITY) {
						setH(heuristicGrade1);
						return heuristicGrade1;
					}
				}
			}
		}

		//HEURYSTYKA DLA PRZECIWNIKA
		for (int i = 0; i < numberOfRows; i++) {						//WIERSZE
			heuristicGrade2 -= countString(board[i], x, o);
		}
														
		for (int i = 0; i < numberOfColumns; i++) {						//KOLUMNY
			for (int j = 0; j < numberOfRows; j++) {
				buforRow[j] = board[j][i];
			}
			heuristicGrade2 -= countString(buforRow, x, o);
		}
		
		
		for (int i = 0; i < numberOfRows; i++) {						//SKOSY
			for (int j = 0; j < numberOfColumns; j++) {
				tempBoard[i][j] = 0;
			}
		}
		for (int j = 0; j <= numberOfColumns - 1; j++) {				
			for (int i = numberOfRows - 1 ; i >= 0; i--)
			{
				int k = i;
				int l = j;
				int size = 0;
				while (k <= numberOfRows - 1 && l < numberOfColumns)
				{
					if (tempBoard[k][l] == 1) {
						break;
					}
					tempBoard[k][l] = 1;
					size++;
					k++;
					l++;
				}
			
				if (size > 1)
				{
					byte[] cross1 = new byte[size];
					k = i;
					l = j;
					for (int counter = 0; counter < size; counter++)
					{
						cross1[counter] = board[k][l];
						k++;
						l++;	
					}
					heuristicGrade2 -= countString(cross1, x, o);
					if(heuristicGrade2 == Double.NEGATIVE_INFINITY || heuristicGrade2 == Double.POSITIVE_INFINITY) {
						setH(heuristicGrade2);
						return heuristicGrade2;
					}
				}
			}
		}
		
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				tempBoard[i][j] = 0;
			}
		}
		for (int j = numberOfColumns - 1; j >= 3; j--)
		{
			for (int i = numberOfRows - 1; i >= 0; i--)
			{
				int k = i;
				int l = j;
				int size = 0;
				while (k <= numberOfRows - 1 && l >= 0)
				{
					if (tempBoard[k][l] == 1)
					{
						break;
					}
					tempBoard[k][l] = 1;
					size++;
					k++;
					l--;
				}
			
				if(size > 1)
				{
					byte[] cross2 = new byte[size];
					k = i;
					l = j;
					for (int counter = 0; counter < size; counter++)
					{
						cross2[counter] = board[k][l];
						k++;
						l--;	
					}
					heuristicGrade2 -= countString(cross2, x, o);
					if(heuristicGrade2 == Double.NEGATIVE_INFINITY || heuristicGrade2 == Double.POSITIVE_INFINITY) {
						setH(heuristicGrade2);
						return heuristicGrade2;
					}
				}
			}
		}
	

		if ((heuristicGrade1 != Double.NEGATIVE_INFINITY && heuristicGrade1 != Double.POSITIVE_INFINITY)
				&& (heuristicGrade2 != Double.NEGATIVE_INFINITY && heuristicGrade2 != Double.POSITIVE_INFINITY)) {
			this.h = heuristicGrade1 + heuristicGrade2;
		} 
		else {
			if (heuristicGrade1 == Double.NEGATIVE_INFINITY || heuristicGrade1 == Double.POSITIVE_INFINITY) {
				this.h = heuristicGrade1;
			} 
			else {
				this.h = heuristicGrade2;
			}
		}
		
		setH(this.h);
		return this.h;
	}

	public String getHashCode() {
		String result = "";
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				result += this.board[i][j] + ", ";
			}
		}
		return result;
	}
	
	public double countString(byte[] tab, byte player, byte opponent) {
		double heuristicGrade = 0, tempHeuristic = 0;
		int p = 0, k = 0, count = 0;
        
		for (int i = 0; i < tab.length; i++) {
			count = 0;
			tempHeuristic = 0;
			if (tab[i] != opponent) {
				p = i;
				while ( i < tab.length && tab[i] != opponent) {
					i += 1;
				}
				k = i - 1;
				if (k - p >= 3) {
					for (int j = p + 1; j <= k + 1; j++) {
						if (tab[j-1] == player) {
							count += 1;
							if (j == k + 1) {
								tempHeuristic += computeWeight(count);
							}
						}
						else if (tab[j-1] == e) {
							tempHeuristic += computeWeight(count);
							count = 0;
						}
					}
					heuristicGrade += tempHeuristic;
				}
			}
		}
	return heuristicGrade;
}

	/*public double countString(byte player, byte opponent) {
		int count = 0;
		int whileCount = 0;
		double heuristicGrade = 0;
		// =====================================POZIOM==============
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfColumns; j++) {
				if (board[i][j] == opponent) {
					continue;
				}
				if (board[i][j] == player) {
					count = 1;
				} else {
					count = 0;
				}
				while (count != 4 && j < numberOfColumns - 1) {
					if (whileCount < 2 && j + 1 == numberOfColumns - 1) {
						count = 0;
						break;
					}

					if (board[i][j + 1] == opponent) {
						if (whileCount < 3) {
							count = 0;
						}
						break;
					}
					if (board[i][j + 1] == e) {
						whileCount += 1;
						if (i > 0) {
							if (board[i - 1][j + 1] == e) {
								count = 0;
								break;
							}
						}
					}
					if (board[i][j + 1] == player) {
						count += 1;
						whileCount += 1;
						if (i > 0) {
							if (board[i - 1][j + 1] == e) {
								count = 0;
								break;
							}
						}
					}
					j += 1;
				}
				heuristicGrade += computeWeight(count);// TUTAJ LICZENIE
														// HEURYSTYKI DLA
														// UZYSKANEGO COUNTA
				whileCount = 0;
			}
		}
		// =====================================PION================
		for (int i = 0; i < numberOfColumns; i++) {
			for (int j = 0; j < numberOfRows; j++) {
				if (board[j][i] == opponent) {
					continue;
				}
				if (board[j][i] == player) {
					count = 1;
				} else {
					count = 0;
				}
				while (count != 4 && j < numberOfRows - 1) {
					if (whileCount < 2 && j + 1 == numberOfRows - 1) {
						count = 0;
						break;
					}
					if (board[j + 1][i] == opponent) {
						if (whileCount < 3) {
							count = 0;
						}
						break;
					}
					if (board[j + 1][i] == e) {
						whileCount += 1;
					}
					if (board[j + 1][i] == player) {
						count += 1;
						whileCount += 1;
					}
					j += 1;
				}
				heuristicGrade += computeWeight(count);// TUTAJ LICZENIE
														// HEURYSTYKI DLA
														// UZYSKANEGO COUNTA
				whileCount = 0;
			}
		}

		// =====================================SLASH1==============
		int k;

		for (int i = 3; i < numberOfRows; i++) {
			k = i;
			for (int j = 0; k >= 0 && j < numberOfColumns; j++) {// numberOfColumns
																	// - 3; j++)
																	// {
				if (board[k][j] == opponent) {
					k -= 1;
					continue;
				}
				if (board[k][j] == player) {
					count = 1;
				} else {
					count = 0;
				}
				while (count != 4 && k >= 1 && j < numberOfColumns - 1) {
					if (whileCount < 2 && k - 1 == 0) {
						count = 0;
						break;
					}
					if (board[k - 1][j + 1] == opponent) {
						if (whileCount < 3) {
							count = 0;
						}
						break;
					}
					if (board[k - 1][j + 1] == e) {
						whileCount += 1;
						if (k - 2 >= 0) {
							if (board[k - 2][j + 1] == e) {
								count = 0;
								break;
							}
						}
					}
					if (board[k - 1][j + 1] == player) {
						count += 1;
						whileCount += 1;
						if (k - 2 >= 0) {
							if (board[k - 2][j + 1] == e) {
								count = 0;
								break;
							}
						}
					}
					j += 1;
					k -= 1;

					if (count == 4) {
						return Double.POSITIVE_INFINITY;
					}
				}
				heuristicGrade += computeWeight(count);// TUTAJ LICZENIE
														// HEURYSTYKI DLA
														// UZYSKANEGO COUNTA
				whileCount = 0;
			}
		}

		// =====================================SLASH2==============

		for (int i = 1; i < numberOfColumns - 3; i++) {
			k = i;
			for (int j = numberOfRows - 1; k < numberOfColumns && j >= 0; j--) {
				if (board[j][k] == opponent) {
					k += 1;
					continue;
				}
				if (board[j][k] == player) {
					count = 1;
				} else {
					count = 0;
				}
				while (count != 4 && k < numberOfColumns - 1 && j > 0) {
					if (whileCount < 2 && k + 1 == numberOfColumns - 1) {
						count = 0;
						break;
					}
					if (board[j - 1][k + 1] == opponent) {
						if (whileCount < 3) {
							count = 0;
						}
						break;
					}
					if (board[j - 1][k + 1] == e) {
						whileCount += 1;
						if (j - 2 >= 0) {
							if (board[j - 2][k + 1] == e) {
								count = 0;
								break;
							}
						}
					}
					if (board[j - 1][k + 1] == player) {
						count += 1;
						whileCount += 1;
						if (j - 2 >= 0) {
							if (board[j - 2][k + 1] == e) {
								count = 0;
								break;
							}
						}
					}
					j -= 1;
					k += 1;

					if (count == 4) {
						return Double.POSITIVE_INFINITY;
					}
				}
				heuristicGrade += computeWeight(count);// TUTAJ LICZENIE
														// HEURYSTYKI DLA
														// UZYSKANEGO COUNTA
				whileCount = 0;
			}
		}
		// =====================================BackSLASH1==============

		for (int i = 0; i < numberOfRows - 3; i++) {
			k = i;
			for (int j = 0; k < numberOfRows && j < numberOfColumns; j++) {
				if (board[k][j] == opponent) {
					k += 1;
					continue;
				}
				if (board[k][j] == player) {
					count = 1;
				} else {
					count = 0;
				}
				while (count != 4 && k < numberOfRows - 1
						&& j < numberOfColumns - 1) {
					if (whileCount < 2 && k + 1 == numberOfRows - 1) {
						count = 0;
						break;
					}
					if (board[k + 1][j + 1] == opponent) {
						if (whileCount < 3) {
							count = 0;
						}
						break;
					}
					if (board[k + 1][j + 1] == e) {
						whileCount += 1;
						if (k < numberOfRows) {
							if (board[k][j + 1] == e) {
								count = 0;
								break;
							}
						}
					}
					if (board[k + 1][j + 1] == player) {
						count += 1;
						whileCount += 1;
						if (k < numberOfRows) {
							if (board[k][j + 1] == e) {
								count = 0;
								break;
							}
						}
					}
					j += 1;
					k += 1;

					if (count == 4) {
						return Double.POSITIVE_INFINITY;
					}
				}
				heuristicGrade += computeWeight(count);// TUTAJ LICZENIE
														// HEURYSTYKI DLA
														// UZYSKANEGO COUNTA
				whileCount = 0;
			}
		}

		// =====================================BackSLASH2==============

		for (int i = 1; i < numberOfColumns - 3; i++) {
			k = i;
			for (int j = 0; k < numberOfColumns && j < numberOfRows; j++) {
				if (board[j][k] == opponent) {
					k += 1;
					continue;
				}
				if (board[j][k] == player) {
					count = 1;
				} else {
					count = 0;
				}
				while (count == 4 && k < numberOfColumns - 1
						&& j < numberOfRows - 1) {
					if (whileCount < 2 && k + 1 == numberOfColumns - 1) {
						count = 0;
						break;
					}
					if (board[j + 1][k + 1] == opponent) {
						if (whileCount < 3) {
							count = 0;
						}
						break;
					}
					if (board[j + 1][k + 1] == e) {
						whileCount += 1;
					}
					if (board[j + 1][k + 1] == player) {
						count += 1;
						whileCount += 1;
					}
					j += 1;
					k += 1;

					if (count == 4) {
						return Double.POSITIVE_INFINITY;
					}
				}
				heuristicGrade += computeWeight(count);// TUTAJ LICZENIE
														// HEURYSTYKI DLA
														// UZYSKANEGO COUNTA
				whileCount = 0;
			}
		}
		return heuristicGrade; // return heuristic
	}*/

	public double computeWeight(int count) {
		switch(count)
		{
		case 0:
			return 0;
		case 1:
			return 1;
		case 2:
			return 10;
		case 3:
			return 100;
		default:
			return Double.POSITIVE_INFINITY; 
		}
	}
}
