public class Main {
    public static void main(String[] args) {
        Pathfinder pathfinder = new Pathfinder("maps\\Map.txt", 5, 10); // Change parameters later
        
        int[] start = { 0, 8 };
        int[] end   = { 3, 8 };

        System.out.println("Shortest Distance: " + pathfinder.calculateDistance(start, end));
    }
}