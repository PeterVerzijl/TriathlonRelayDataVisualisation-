public class Team {
    
    public int endPosition = Integer.MAX_VALUE;
    public int number = Integer.MAX_VALUE;
    public String name = "";
    public String sponsor = "";

    public int finishTime = Integer.MAX_VALUE;

    public int[] swimTimes = new int[4];
    public int[] cycleTimes = new int[4];
    public int[] runTimes = new int[4];
    
    public color c;
    public boolean enabled = true;
    
    public Team() {
        c = color(random(20, 255), random(20, 255), random(20, 255));
    }
}