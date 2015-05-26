public class DisableAllCheckbox extends Checkbox {
    
    public DisableAllCheckbox() 
    {
        checked = false;
        text = "Disable all";
        c = color(255);
    }
    
    void OnMouseClick() 
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
