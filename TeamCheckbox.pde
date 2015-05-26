public class TeamCheckbox extends Checkbox {
    
    private Team m_team;
    
    public TeamCheckbox(Team t) 
    {
        m_team = t;
        text = Integer.toString(m_team.endPosition);
        c = t.c;
        checked = m_team.enabled = true;
    }
    
    void OnMouseClick() 
    {
        m_team.enabled = !m_team.enabled;
        checked = m_team.enabled;
    }
    
    void OnMouseOver()
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