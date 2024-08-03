public int WIDTH = 1280, 
            HEIGHT = 720;
public int GRAPH_X = 30, 
            GRAPH_Y = 30, 
            GRAPH_WIDTH = (int)(WIDTH*0.7f), 
            GRAPH_HEIGHT = (int)(HEIGHT*.5);

public DataReader reader;
public Team[] teams;

public DisableAllCheckbox disableAll;
public ArrayList<Checkbox> checkboxes;

void setup() {
    windowResize(WIDTH, HEIGHT);
    
    reader = new DataReader("data.txt");
    teams = reader.loadData();
    
    // Checkboxes
    checkboxes = new ArrayList<Checkbox>();
    for(int i = 0; i < teams.length; i++)
        checkboxes.add(new TeamCheckbox(teams[i]));
        
    disableAll = new DisableAllCheckbox();
    checkboxes.add(disableAll);
}

void draw()
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

void DrawCheckboxes(int x, int y)
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

void DrawSpeedGraph(int x, int y, int w, int h)
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

void DrawGraphLine(Team t, int x, int y, int w, int h) 
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
            curveVertex(offset, -map((500f/(float)t.swimTimes[i])*3.6, 0, 70, y, y + h));
            offset += distance;
        }
        for (int i = 0; i < t.runTimes.length; i++)
        {
            // Map 2 - 5 km/h to y - h
            curveVertex(offset, -map((5000f/(float)t.runTimes[i])*3.6, 0, 70, y, y + h));
            offset += distance;
        }
        for (int i = 0; i < t.cycleTimes.length; i++)
        {
            // Map 2 - 5 km/h to y - h
            curveVertex(offset, -map((5000f/(float)t.cycleTimes[i])*3.6, 0, 70, y, y + h));
            offset += distance;
        }
        curveVertex(offset, -map((5000f/(float)t.cycleTimes[t.cycleTimes.length-1])*3.6, 0, 20, y, h));
    endShape();
    popStyle();
}

void DrawTeamInfo(Team t)
{
    int x = 30, y = (int)(HEIGHT*0.62f), w = (int)(WIDTH*0.7f), h = (int)(HEIGHT*0.35f);
    pushMatrix();
        translate(x, y);
        noFill();
        stroke(255);
        rect(0, 0, w, h);
        pushMatrix();
        scale(1.5);
        text("Team Rank: " + t.endPosition, 20, 20);
        text("Team Number: " + t.number, 20, 40);
        text("Team Name: " + t.name, 20, 60);
        text("Team Sponsor: " + t.sponsor, 20, 80);
        text("Team Finish Time: " + t.finishTime + " seconds", 20, 100);
        popMatrix();
    popMatrix();
}

void mouseOver() 
{
    for(Checkbox box : checkboxes)
        box.mouseOver();
}

void mousePressed()
{
    for(Checkbox box : checkboxes)
        box.mouseClicked();
}
