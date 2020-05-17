import java.io.File;
import java.util.Scanner;

import java.io.FileNotFoundException;

public class Pathfinder {

    char[][] map;

    public Pathfinder(String mapPath, int rows, int cols) {
        this.map = new char[rows][cols];
        
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
                    this.map[tempRow][i] = token.charAt(i);
                }
            }

            tempRow++;
            lineScanner.close();
        }

        /* Prints out everything
        for (int row = 0; row < this.map.length; row++) {
            for (int col = 0; col < this.map[row].length; col++) {
                System.out.print(this.map[row][col]);
            }
            System.out.println("\n");
        }
        */
        scanner.close();
    }
}