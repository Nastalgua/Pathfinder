import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

import java.io.FileNotFoundException;

public class Pathfinder {
    public static final int DIAGONAL_DISTANCE = 14;
    public static final int REGULAR_DISTANCE = 10;

    Cell[][] map;

    public Pathfinder(String mapPath, int rows, int cols) {
        this.map = new Cell[rows][cols];
        
        try { this.loadFile(mapPath); } 
        catch (FileNotFoundException e) { System.out.println("File not found"); }
    }

    private void loadFile(String mapPath) throws FileNotFoundException {
        File map = new File(mapPath);
        Scanner scanner = new Scanner(map);

        int tempRow = 0;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().replaceAll(" ", ""); // trim() no work lmao
            Scanner lineScanner = new Scanner(line);

            while (lineScanner.hasNext()) {
                String token = lineScanner.next();

                for (int i = 0; i < line.length(); i++) {
                    this.map[tempRow][i] = new Cell(token.charAt(i), tempRow, i);
                }
            }

            tempRow++;
            lineScanner.close();
        }

        /* Prints out everything
        for (int row = 0; row < this.map.length; row++) {
            for (int col = 0; col < this.map[row].length; col++) {
                System.out.print(this.map[row][col].character);
            }
            System.out.println("\n");
        }
        */
        scanner.close();
    }

    public double calculateDistance(int[] start, int[] end) {
        ArrayList<Cell> openSet = new ArrayList<>();
        double distance = 0;

        int startX = start[0];
        int startY = start[1];
        
        int endX = end[0];
        int endY = end[1];

        Cell startCell = this.map[startX][startY];
        Cell endCell = this.map[endX][endY];

        openSet.add(startCell);

        while (openSet.size() > 0) {
            Cell currentCell = openSet.get(0);

            for (int i = 1; i < openSet.size(); i++) {
                if (openSet.get(i).fCost <  currentCell.fCost 
                 || openSet.get(i).fCost == currentCell.fCost 
                 && openSet.get(i).hCost <  currentCell.hCost) {
                    currentCell = openSet.get(i);
                }
            }

            openSet.remove(currentCell);
            currentCell.discovered = true;

            if (currentCell.equals(endCell)) {
                // Calculate distance after finding the best path
                ArrayList<Cell> bestPath = retracePath(startCell, endCell);

                Cell bestStartCell = bestPath.get(0);

                for (int i = 1; i < bestPath.size(); i++) {
                    Cell cell = bestPath.get(i);

                    if (calculateSlope(bestStartCell, cell) == 0) {
                        distance += REGULAR_DISTANCE;
                    } else {
                        distance += DIAGONAL_DISTANCE;
                    }
                }

                return distance;
            }

            for (Cell neighborCell : this.getSurroundingCells(currentCell.x, currentCell.y)) {
                int newMovementCost = currentCell.gCost + getDistance(currentCell, neighborCell);

                if (newMovementCost < neighborCell.gCost || !openSet.contains(neighborCell)) {
                    neighborCell.gCost = newMovementCost;
                    neighborCell.hCost = getDistance(neighborCell, endCell);
                    neighborCell.parent = currentCell;

                    if (!openSet.contains(neighborCell)) {
                        openSet.add(neighborCell);
                    }
                }
            }
        }

        return distance;
    }

    private ArrayList<Cell> retracePath(Cell startCell, Cell endCell) {
        ArrayList<Cell> path = new ArrayList<>();
        Cell currentCell = endCell;
        path.add(startCell);

        while (!currentCell.equals(startCell)) {
            path.add(currentCell);

            currentCell = currentCell.parent;
        }

        Collections.reverse(path);

        return path;
    }

    private int getDistance(Cell cellOne, Cell cellTwo) {
        int distX = Math.abs(cellOne.x - cellTwo.x);
        int distY = Math.abs(cellOne.y - cellTwo.y);

        if (distX > distY) {
            return 14 * distY + 10 * (distX - distY);
        } else {
            return 14 * distX + 10 * (distY - distX);
        }
    }

    /**
     * 
     * @param x
     * @param y
     * @return
     */
    private ArrayList<Cell> getSurroundingCells(int x, int y) {
        if (x >= map.length || x < 0 || y >= this.map[0].length || y < 0) return new ArrayList<Cell>();

        ArrayList<Cell> surroundingCells = new ArrayList<>();

        for (int i = -1; i < 2; i++) {
            if (x + i != -1 && x + i < this.map.length) {
                for (int j = -1; j < 2; j++) {
                    if (y + j != -1 && y + j < this.map[0].length) {
                        Cell cell = this.map[x + i][y + j];

                        if (!(this.map[x][y].equals(cell) || cell.isObstacle || cell.discovered)) {
                            surroundingCells.add(cell);
                        }
                    }
                }
            }
        }

        return surroundingCells;
    }

    /**
     * @param cellOne
     * @param cellTwo
     * @return The slope between two cells. 0 is returned if divided by 0
     */
    private int calculateSlope(Cell cellOne, Cell cellTwo) {
        try {
            return (cellTwo.y - cellOne.y) / (cellTwo.x - cellOne.x);
        } catch(ArithmeticException e) {
            return 0;
        }
    }

    // Used for debugging
    private void printMap() {
        for (int row = 0; row < this.map.length; row++) {
            for (int col = 0; col < this.map[row].length; col++) {
                System.out.print(this.map[row][col].character);
            }

            System.out.println("\n");
        }
    }
}