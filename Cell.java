public class Cell {
    public char character;
    public boolean isObstacle;
    public boolean discovered = false;
    public Cell parent;

    public int gCost = 0;
    public int hCost = 0;
    public int fCost = 0;

    public int x;
    public int y;    

    public Cell(char character, int x, int y) {
        this.character = character;
        if (this.character != '.') this.isObstacle = true;

        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        String character = "Character: " + this.character;
        String costs = "\n gCost: " + this.gCost + "\n hCost: " + this.hCost + "\n fCost: " + this.fCost;
        String location = "\n x: " + this.x + "\n y: " + this.y + '\n';
        return character + costs + location;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Cell.class) return false;

        Cell cell = (Cell) o;

        return this.x == cell.x && this.y == cell.y;
    }
}