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
