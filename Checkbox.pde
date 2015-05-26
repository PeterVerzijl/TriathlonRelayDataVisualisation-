public class Checkbox {
    
    public boolean checked = false;
    public String text;
    
    private int m_x = 0, m_y = 0;
    private int size = 15;
    
    public color c;
    
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
    
    final void mouseClicked()
    {
        if (mouseX > m_x && mouseX < m_x + size + 35 && mouseY > m_y && mouseY < m_y + size)
        {
            OnMouseClick();
        }
    }
    
    final void mouseOver()
    {
        if (mouseX > m_x && mouseX < m_x + size + 35 && mouseY > m_y && mouseY < m_y + size)
        {
            OnMouseOver();
        }
    }
    
    void OnMouseClick() {}
    void OnMouseOver() {}
}