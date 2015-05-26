import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class DataVisualisation2 extends PApplet {

public int WIDTH = 1280, 
            HEIGHT = 720;
public int GRAPH_X = 30, 
            GRAPH_Y = 30, 
            GRAPH_WIDTH = (int)(WIDTH*0.7f), 
            GRAPH_HEIGHT = (int)(HEIGHT*.5f);

public DataReader reader;
public Team[] teams;

public DisableAllCheckbox disableAll;
public ArrayList<Checkbox> checkboxes;

public void setup()
{
    size(WIDTH, HEIGHT);
    
    reader = new DataReader("data.txt");
    teams = reader.loadData();
    
    // Checkboxes
    checkboxes = new ArrayList<Checkbox>();
    for(int i = 0; i < teams.length; i++)
        checkboxes.add(new TeamCheckbox(teams[i]));
        
    disableAll = new DisableAllCheckbox();
    checkboxes.add(disableAll);
}

public void draw()
{ 
    background(0);
    
    // Chart title
    pushMatrix();
    scale(2);
    fill(255);
    text("Race speeds for the Triathlon Relays Championship 2015", 80, 15);
    popMatrix();
    
    pushMatrix();
    translate(15, 250);
    rotate(PI*-0.5f);
    text("Speed (km/h)", 0, 0);
    popMatrix();
    
    text("Teams: ", (int)(WIDTH * 0.75f), 15);
    disableAll.setPosition((int)(WIDTH * 0.9f), 5);
    disableAll.draw();
    
    DrawCheckboxes((int)(WIDTH * 0.75f), 30);
    
    DrawSpeedGraph(GRAPH_X, GRAPH_Y, GRAPH_WIDTH, GRAPH_HEIGHT);
       
    mouseOver();
}

public void DrawCheckboxes(int x, int y)
{   
    int xoffset = x + 2, yoffset = y + 2;
    for(int i = 0; i < checkboxes.size(); i++)
    {
        Checkbox b = checkboxes.get(i);
        if (!(b instanceof TeamCheckbox))
            continue;
        
        b.setPosition(xoffset, yoffset);
        b.draw();
        yoffset += b.size * 1.5f;
        if (yoffset + b.size > HEIGHT)
        {
            xoffset += 45;
            yoffset = y + 2;
        }
    }
}

public void DrawSpeedGraph(int x, int y, int w, int h)
{
    pushMatrix();
        translate(x, y);
        
        pushMatrix();
        translate(-10, 10);
        rotate(PI*-0.5f);
        text("30", 0, 0);
        popMatrix();
        
        // Axis
        pushStyle();
        noFill();
        stroke(255);
        line(0, 0, 0, h);    // X axis
        line(0, h, w, h);    // Y axis
        // Horizontal axis markers
        int distance = (int)(w / 12);
        for (int i = 0; i <= 12; i++)
        {
            pushMatrix();
                translate(i * distance, h);
                line(0, 0, 0, 10);
                text(i, -5, 25);
            popMatrix();
        }
        // Title indexes
        pushMatrix();
            translate(0, h + 40);
            text("Swiming (1-4)", 100, 0);
            text("Swiming (5-8)", 400, 0);
            text("Swiming (9-12)", 700, 0);
        popMatrix();
        
        popStyle();
        
        // Lines
        pushMatrix();
            translate(0, h);
            for (Team t : teams)
            {
                if (!t.enabled) 
                    continue;
                DrawGraphLine(t, x, y, w, h);
            }
        popMatrix();
    popMatrix();
}

public void DrawGraphLine(Team t, int x, int y, int w, int h) 
{
    int distance = (int)(w / 12);
    
    pushStyle();
    stroke(t.c);
    noFill();
    beginShape();
        curveVertex(0, 0);
        curveVertex(0, 0);
        int offset = distance;
        for (int i = 0; i < t.swimTimes.length; i++)
        {
            // Map 2 - 5 km/h to y - h
            curveVertex(offset, -map((500f/(float)t.swimTimes[i])*3.6f, 0, 70, y, y + h));
            offset += distance;
        }
        for (int i = 0; i < t.runTimes.length; i++)
        {
            // Map 2 - 5 km/h to y - h
            curveVertex(offset, -map((5000f/(float)t.runTimes[i])*3.6f, 0, 70, y, y + h));
            offset += distance;
        }
        for (int i = 0; i < t.cycleTimes.length; i++)
        {
            // Map 2 - 5 km/h to y - h
            curveVertex(offset, -map((5000f/(float)t.cycleTimes[i])*3.6f, 0, 70, y, y + h));
            offset += distance;
        }
        curveVertex(offset, -map((5000f/(float)t.cycleTimes[t.cycleTimes.length-1])*3.6f, 0, 20, y, h));
    endShape();
    popStyle();
}

public void DrawTeamInfo(Team t)
{
    int x = 30, y = (int)(HEIGHT*0.62f), w = (int)(WIDTH*0.7f), h = (int)(HEIGHT*0.35f);
    pushMatrix();
        translate(x, y);
        noFill();
        stroke(255);
        rect(0, 0, w, h);
        pushMatrix();
        scale(1.5f);
        text("Team Rank: " + t.endPosition, 20, 20);
        text("Team Number: " + t.number, 20, 40);
        text("Team Name: " + t.name, 20, 60);
        text("Team Sponsor: " + t.sponsor, 20, 80);
        text("Team Finish Time: " + t.finishTime + " seconds", 20, 100);
        popMatrix();
    popMatrix();
}

public void mouseOver() 
{
    for(Checkbox box : checkboxes)
        box.mouseOver();
}

public void mousePressed()
{
    for(Checkbox box : checkboxes)
        box.mouseClicked();
}
public class Checkbox {
    
    public boolean checked = false;
    public String text;
    
    private int m_x = 0, m_y = 0;
    private int size = 15;
    
    public int c;
    
    public Checkbox() {}
    
    public void setPosition(int x, int y)
    {
        m_x = x;
        m_y = y;
    }
    
    public void draw()
    {        
        pushMatrix();
        pushStyle();
        translate(m_x, m_y);
        // Draw rectangle
            noFill();
            stroke(c);
            rect(0, 0, size, size);
            if (checked)
            {
                fill(c);
                noStroke();
                rect(2, 2, size - 3, size - 3);
            }
        // Draw text besides
            fill(255);
            text(text, size + 5, size);
        popStyle();
        popMatrix();
    }
    
    public final void mouseClicked()
    {
        if (mouseX > m_x && mouseX < m_x + size + 35 && mouseY > m_y && mouseY < m_y + size)
        {
            OnMouseClick();
        }
    }
    
    public final void mouseOver()
    {
        if (mouseX > m_x && mouseX < m_x + size + 35 && mouseY > m_y && mouseY < m_y + size)
        {
            OnMouseOver();
        }
    }
    
    public void OnMouseClick() {}
    public void OnMouseOver() {}
}
/**
    Data Reader class.
    Written by Peter Verzijl at 20-5-2015 (ddmmyyyy)
*/
public class DataReader {
    
    private String m_filePath;
    private String[] m_data;
    
    public DataReader() {}
    public DataReader(String filePath)
    {
        m_filePath = filePath;
    }
    
    /**
        
    */
    public Team[] loadData() 
    {
        if (m_filePath == null || m_filePath.length() < 1)
        {
            print("Error: file path is not set! " + m_filePath);
            return null;
        }   
        return loadData(m_filePath);   
    }
    
    public Team[] loadData(String filePath)
    {
        m_data = loadStrings(filePath);
        if (m_data == null || m_data.length < 1)
        {
            print("Error: The data file is empty!");
            return null;
        }
        ArrayList<Team> teamList = new ArrayList<Team>();
        // Loop trough the lines.
        // Ditch the first line if the first line is the header
        for (int i = 0; i < m_data.length; i++)
        {
            // Create new team
            Team t = new Team();

            // Split the line at the ; sign
            String[] data = split(m_data[i], ',');
            
            // Fill the team
            // Team ranking
            if (data[0].equals("DNF")) // Did not finish
            {                
                t.endPosition = -1;
            }
            else 
            {
                t.endPosition = Integer.parseInt(data[0]);
            }
            // Team number
            t.number = Integer.parseInt(data[1]);
            // Team name
            t.name = data[2];
            // Team sponsor
            t.sponsor = data[3];
            // Team finish time
            String[] timeData = split(data[4], ':');
            if (timeData[0].equals("-"))
                t.finishTime = -1;
            else
                t.finishTime = 3600 * Integer.parseInt(timeData[0]) + 
                    60 * Integer.parseInt(timeData[1]) + 
                        Integer.parseInt(timeData[2]);
            // Swimming times
            for (int j = 0; j < 4; j++)
                t.swimTimes[j] = parseTimeSeconds(data[5 + j]);
            // Cycling times
            for (int j = 0; j < 4; j++)
                t.cycleTimes[j] = parseTimeSeconds(data[9 + j]);
            // Running times
            for (int j = 0; j < 4; j++)
                t.runTimes[j] = parseTimeSeconds(data[13 + j]);
            
            // Add the team
            teamList.add(t);;
        }
        println("Parsed and created " + m_data.length + " teams.");
        return teamList.toArray(new Team[teamList.size()]);
    }
    
    private int parseTimeSeconds(String s)
    {
        if (s == "-" || s.length() < 4) // at least #:##
            return 0;

        String[] result = split(s, ':');
        return 60 * Integer.parseInt(result[0]) + Integer.parseInt(result[1]);
    }
}
public class DisableAllCheckbox extends Checkbox {
    
    public DisableAllCheckbox() 
    {
        checked = false;
        text = "Disable all";
        c = color(255);
    }
    
    public void OnMouseClick() 
    {
        checked = !checked;
        
        for(Team t : teams)
            t.enabled = !checked;
            
        for(Checkbox b : checkboxes)
        {
            if (b != this) 
                b.checked = !checked;
        }
    }
}
public class Team {
    
    public int endPosition = Integer.MAX_VALUE;
    public int number = Integer.MAX_VALUE;
    public String name = "";
    public String sponsor = "";

    public int finishTime = Integer.MAX_VALUE;

    public int[] swimTimes = new int[4];
    public int[] cycleTimes = new int[4];
    public int[] runTimes = new int[4];
    
    public int c;
    public boolean enabled = true;
    
    public Team() {
        c = color(random(20, 255), random(20, 255), random(20, 255));
    }
}
public class TeamCheckbox extends Checkbox {
    
    private Team m_team;
    
    public TeamCheckbox(Team t) 
    {
        m_team = t;
        text = Integer.toString(m_team.endPosition);
        c = t.c;
        checked = m_team.enabled = true;
    }
    
    public void OnMouseClick() 
    {
        m_team.enabled = !m_team.enabled;
        checked = m_team.enabled;
    }
    
    public void OnMouseOver()
    {
        pushMatrix();
            pushStyle();
            noStroke();
            fill(0, 150);
            rect(GRAPH_X + 1, GRAPH_Y, GRAPH_WIDTH, GRAPH_HEIGHT - 1);
            translate(GRAPH_X, GRAPH_HEIGHT + GRAPH_Y);
            stroke(m_team.c);
            strokeWeight(4);
            DrawGraphLine(m_team, 0, GRAPH_Y, GRAPH_WIDTH, GRAPH_HEIGHT);
            popStyle();
        popMatrix();
        
        DrawTeamInfo(m_team);
    }
}
    static public void main(String[] passedArgs) {
        String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "DataVisualisation2" };
        if (passedArgs != null) {
          PApplet.main(concat(appletArgs, passedArgs));
        } else {
          PApplet.main(appletArgs);
        }
    }
}
