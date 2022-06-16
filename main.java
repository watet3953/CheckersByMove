// Travis Waterman
// Feb 22nd, 2022 - March 11th, 2022
// CheckersByMove.java
// A move by move game of checkers

import java.util.Scanner;
import java.lang.Math; // only needed for ai, possible way to avoid import?

// TODO

// X is row
// Y is column

// -1 = invalid tile
// 0 = red king
// 1 = red man
// 2 = empty tile
// 3 = black man
// 4 = black king

class Main {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    System.out.println("Welcome to CheckersByMove! Please choose the game mode.");
    System.out.println("0: Player vs Player");
    System.out.println("1: Player vs AI");
    System.out.println("2: AI vs Player");
    System.out.println("3: AI vs AI"); // at one point the console would not display 3s
    boolean aiBlack = false;
    boolean aiRed = false;
    int choice = -1;
    try {
      choice = Integer.parseInt(input.nextLine());
      } catch (NumberFormatException nfe) {}
    if (choice == 1) {
      aiRed = true;
    } else if (choice == 2) {
      aiBlack = true;
    } else if (choice == 3) {
      aiRed = true;
      aiBlack = true;
    }
    System.out.println("Please specify board width");
    int x = 8;
    try {
      x = Integer.parseInt(input.nextLine());
      } catch (NumberFormatException nfe) {} // quick addition
    System.out.println("Please specify board height");
    int y = 8;
    try {
      y = Integer.parseInt(input.nextLine());
      } catch (NumberFormatException nfe) {}
    // recieve player choice, send other config options (board size, special rules)
    int[][] board = buildBoard(x,y);
    printBoard(board,x,y);
    boolean[] victoryCounter = {false,false};
    while (true) {
      // Black's turn
      if (aiBlack) {    
        int[] move;
        System.out.println("Black's turn, please input a valid move.");
        while(true) {
          move = aiMove(board);
          if (checkValidMove(board,move,true)) {
            for(int i = 0; i < move.length; i++) {
              System.out.print(move[i] + "-");
            }
            System.out.println();
            break;
          }
        }
        board = executeMove(board,move);
        printBoard(board,x,y);
      } else { 
        int[] move;
        while(true) {
        System.out.println("Black's turn, please input a valid move.");
        move = parseMove(input.nextLine());
          if (checkValidMove(board,move,true)) {
            break;
          }
        }
        board = executeMove(board,move);
        printBoard(board,x,y);
      }
      // check victory, break if true
      victoryCounter = checkVictory(board);
      if (victoryCounter[0] && victoryCounter[1]) {
        System.out.println("It's a Draw!");
        break;
      }
      if (victoryCounter[0]) {
        System.out.println("Black Wins!");
        break;
      }
      if (victoryCounter[1]) {
        System.out.println("Red Wins!");
        break;
      }
      // Red's turn
      if (aiRed) {    
        int[] move;
        System.out.println("Red's turn, please input a valid move.");
        while(true) {
          move = aiMove(board);
          if (checkValidMove(board,move,false)) {
            for(int i = 0; i < move.length; i++) {
              System.out.print(move[i] + "-");
            }
            System.out.println();
            break;
          }
        }
        board = executeMove(board,move);
        printBoard(board,x,y);
      } else {
        int[] move;
        while(true) {
        System.out.println("Red's turn, please input a valid move.");
        move = parseMove(input.nextLine());
          if (checkValidMove(board,move,false)) {
            break;
          }
        }
        board = executeMove(board,move);
        printBoard(board,x,y);
      }
      // check victory, break if true
      victoryCounter = checkVictory(board);
      if (victoryCounter[0] && victoryCounter[1]) {
        System.out.println("It's a Draw!");
        break;
      }
      if (victoryCounter[0]) {
        System.out.println("Black Wins!");
        break;
      }
      if (victoryCounter[1]) {
        System.out.println("Red Wins!");
        break;
      }
    }
    input.close();
  }

  public static boolean[] checkVictory(int[][] board) { // given the board, returns if a team has one and which team
    boolean[] output = {true,true}; // [0] is black's win, [1] is red's win
    // red's loss
    for (int x = 0; x < board.length; x++) {
      for (int y =0; y < board[0].length; y++) {
        if (board[x][y] < 2 && board[x][y] != -1) {
          output[0] = false;
        }
      }
    }
    // black's loss
     for (int x = 0; x < board.length; x++) {
      for (int y = 0; y < board[0].length; y++) {
        if (board[x][y] > 2) {
          output[1] = false;
        }
      }
    }
    if (output[0] || output[1]) { // if either is true then it is a win, can't tie against someone with no pieces, both is a draw anyways
      return output;
    }
    output[0] = true; // reset for next chunk of code
    output[1] = true;
    int[][] moveSet = {{1,1},{1,-1},{2,2},{2,-2},{-1,1},{-1,-1},{-2,2},{-2,-2}};
    // everything below this is move checking
    for (int x = 0; x < board.length; x++) {
      for (int y = 0; y < board[0].length; y++) {
        for (int i = 0; i < 4; i++) { //  hardcoded based off moveSet array
          try {
            if (board[x + moveSet[i][0]][y + moveSet[i][1]] == 2) {
              if (board[x][y] == 0) {
                output[0] = false;
                continue;
              }
              if (board[x][y] == 3 || board[x][y] == 4) {
                output[1] = false;
                continue;
              }
            }
          } catch (ArrayIndexOutOfBoundsException obe) {} // obe is invalid move
        }
        for (int i = 4; i < moveSet.length; i++) {
          try {
            if (board[x + moveSet[i][0]][y + moveSet[i][1]] == 2) {
              if (board[x][y] == 4) {
                output[1] = false;
                continue;
              }
              if (board[x][y] == 0 || board[x][y] == 1) {
                output[0] = false;
                continue;
              }
            }
          } catch (ArrayIndexOutOfBoundsException obe) {} // obe is invalid move
        }
      }
    }
    return output;
  }

  public static void printBoard(int[][] board, int x, int y) { // given a board, prints it
    for (int i = 0; i < x; i++) {
      for (int u = 0; u < y; u++) {
        int tile = board[i][u];
        if (tile == -1) {
          System.out.print("\t");
        } else if (tile == 0) {
          System.out.print("R" + "\t");
        } else if (tile == 1) {
          System.out.print("r" + "\t");
        } else if (tile == 2) {
          System.out.print("." + "\t");
        } else if (tile == 3) {
          System.out.print("b" + "\t");
        } else if (tile == 4) {
          System.out.print("B" + "\t");
        }
      }
      System.out.println();
    }
  }


  public static int[][] buildBoard(int x, int y) { // given the x and y dimensions of the board, returns a set up board.
    int[][] board = new int[x][y];
    for (int i = 0; i < (x * y);i++) {
      board[i/x][i%x] = -1;
    }
    for (int u = 0; u < y; u++) {
      for (int i = (u + 1)%2; i < x; i += 2) {
        board[i][u] = 2;
      }
    }
    for (int u = 0; u < (y/2)-1;u++) {
      for (int i = (u + 1)%2;i<x;i+=2) {
        board[u][i] = 3;
      }
    }
    for (int u = (y/2)+1;u < y;u++) {
      for (int i = (u + 1)%2;i < x; i+=2) {
        board[u][i] = 1;
      }
    }
    return board;
  }

  public static int[] aiMove(int[][] board) { // returns move (RAND METHOD)
    int[] output = {
      (int)(Math.random() * (double)(board.length - 1 / 2) * (double)(board[0].length - 1 / 2)) / 2 + 1,
      (int)(Math.random() * (double)(board.length - 1 / 2) * (double)(board[0].length - 1 / 2)) / 2 + 1,
      };
    //System.out.println(output[0] + " " + output[1]);
    return output;
  }

  public static int[] PDNtoXY(int PDN, int x) { // given a PDN position and the size of the board, returns the XY coordinates as a length 2 int array
    int[] output = new int[2];
    output[0] = (PDN - 1) / (x / 2);
    if (output[0] % 2 == 0) {
      output[1] = ((PDN - (output[0] * (x/2))) * 2) - 1;
    } else {
      output[1] = ((PDN - (output[0] * (x/2))) * 2) - 2;
    }
    //System.out.println(output[0] + "," + output[1]);
    return output;
  }
  
  public static int checkStepsAway (int[] pos1, int[] pos2) { // given two XY coordinates, returns distance in jumps
    int rise = Math.abs(pos1[0] - pos2[0]);
    int run = Math.abs(pos1[1] - pos2[1]);
    return (int)(Math.sqrt((rise*rise)+(run*run))); // int to double to int
  }

  public static int[][] executeMove(int[][] board, int[] moves) { // given the board and a set of moves, outputs the board with the moves completed.
    int[] posFirst = PDNtoXY(moves[0],board[0].length);
    int movedPiece = board[posFirst[0]][posFirst[1]];
    board[posFirst[0]][posFirst[1]] = 2;
    int[] posLast = PDNtoXY(moves[moves.length-1],board[0].length);
    board[posLast[0]][posLast[1]] = movedPiece;

    // kinging code
    if (movedPiece == 3 && posLast[0] == board.length - 1) { 
      board[posLast[0]][posLast[1]] = 4;
    } else if (movedPiece == 1 && posLast[0] == 0) {
      board[posLast[0]][posLast[1]] = 0;
    }

    // capturing code
    for (int i = 0; i < moves.length - 1;i++) {
      posFirst = PDNtoXY(moves[i],board[0].length);
      posLast = PDNtoXY(moves[i+1],board[0].length);
      if (board[(posFirst[0] + posLast[0]) / 2][(posFirst[1] + posLast[1]) / 2] != -1  // very nasty check
        && board[(posFirst[0] + posLast[0]) / 2][(posFirst[1] + posLast[1]) / 2] != movedPiece
        && board[(posFirst[0] + posLast[0]) / 2][(posFirst[1] + posLast[1]) / 2] != movedPiece + 1
        && board[(posFirst[0] + posLast[0]) / 2][(posFirst[1] + posLast[1]) / 2] != movedPiece - 1) {
        board[(posFirst[0] + posLast[0]) / 2][(posFirst[1] + posLast[1]) / 2] = 2;
      }
    }
    return board;
  }
  
  public static boolean checkValidMove(int[][] board, int[] moves, boolean isBlack) { // given the current positions of all the pieces in play, and the intended set of moves, return if the moveset is possible.
    if (moves.length < 2) {
      return false;
    }
    //System.out.println("e");
    int[] firstMove = PDNtoXY(moves[0],board[0].length);
    if (isBlack) {
      if (board[firstMove[0]][firstMove[1]] == 0 || board[firstMove[0]][firstMove[1]] == 1 || board[firstMove[0]][firstMove[1]] == 2) {
        return false;
      }
    } else {
      if (board[firstMove[0]][firstMove[1]] == 2 || board[firstMove[0]][firstMove[1]] == 3  || board[firstMove[0]][firstMove[1]] == 4) {
        return false;
      }
    }
    //System.out.println("e");
    int[] lastMove = firstMove;
    int[] nextMove = new int[2];
    int pieceType = board[firstMove[0]][firstMove[1]];
    int curMove = 1;
    while(curMove < moves.length) {
      nextMove = PDNtoXY(moves[curMove],board[0].length);
      //System.out.println(nextMove[0]);
      //System.out.println(nextMove[1]);
      if (pieceType == 3) {
        if (lastMove[0] - nextMove[0] > 0) {
          return false;
        }
      } else if (pieceType == 1) {
        if (lastMove[0] - nextMove[0] < 0) {
          return false;
        }
      }
      if (board[nextMove[0]][nextMove[1]] != 2) {
        return false;
      }
      //System.out.println("f");
      int oneTwoDistance = checkStepsAway(lastMove,nextMove);
      if (oneTwoDistance >= 3 || oneTwoDistance == 0) {
        return false;
      }
      //System.out.println("f");
      if (oneTwoDistance == 2) {
        if (isBlack) {
          //System.out.println(lastMove[0] + nextMove[0] / 2);
          //System.out.println(lastMove[1] + nextMove[1] / 2);
          if (board
            [(lastMove[1] + nextMove[1]) / 2]
            [(lastMove[0] + nextMove[0]) / 2]
            == -1 || board
            [(lastMove[1] + nextMove[1]) / 2]
            [(lastMove[0] + nextMove[0]) / 2]
            == 0 || board
            [(lastMove[1] + nextMove[1]) / 2]
            [(lastMove[0] + nextMove[0]) / 2]
            == 1 || board
            [(lastMove[1] + nextMove[1]) / 2]
            [(lastMove[0] + nextMove[0]) / 2]
            == 2) { // this is cursed, grave danger to those who modify this
            return false;            
          }
        } else {
          if (board
            [(lastMove[1] + nextMove[1]) / 2]
            [(lastMove[0] + nextMove[0]) / 2]
            == -1 || board
            [(lastMove[1] + nextMove[1]) / 2]
            [(lastMove[0] + nextMove[0]) / 2]
            == 2 || board
            [(lastMove[1] + nextMove[1]) / 2]
            [(lastMove[0] + nextMove[0]) / 2]
            == 3 || board
            [(lastMove[1] + nextMove[1]) / 2]
            [(lastMove[0] + nextMove[0]) / 2]
            == 4) {
            return false;            
          }
        }
      }
      //System.out.println("f");
      lastMove = nextMove;
      curMove++;
      //System.out.println("e");
    }
    return true;
  }


  public static int[] parseMove(String input) { // given a player input, parses and returns valid move.
    String[] splitInput = input.split("\\-|x"); // regex is evil
    int[] output = new int[splitInput.length];
    for (int x = 0; x < splitInput.length;x++) {
      try {
        output[x] = Integer.parseInt(splitInput[x]);
      } catch (NumberFormatException nfe) {}
    }
    return output;
  }
}
