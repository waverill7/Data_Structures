import java.util.Iterator;

public class JosephusSolver {
    private Iterator players;
    private int numberOfPlayers;
    private int skipFactor;
    
    /**
        Determines and prints the name of the last player standing.    
    */
    public static void main ( String[] args ) {
        JosephusSolver game = new JosephusSolver( Integer.parseInt( args[ 0 ] ), Integer.parseInt( args[ 1 ] ) );
        System.out.println( "The Last Player Standing Is:  " + game.solve() );         
    }
    
    /**
        Constructs a JosephusSolver that contains:
            a number of players, specified by an integer value n that must be greater than 0;
            a skip factor, specified by an integer value k that must be greater than or equal to 0;
            and an Iterator for a CircularList of players that is also constructed.  
    */
    public JosephusSolver ( int n, int k ) {
        
        if ( n > 0 && k >= 0 ) {
            this.numberOfPlayers = n;
            this.skipFactor = k;
            this.players = new CircularList( this.numberOfPlayers ).iterator();
        }
        else {
            throw new IllegalArgumentException();
        }
    }
    
    /**
        Solves the specified instance of the Josephus problem and returns the name of the last player standing.
            It carries this out by moving counter-clockwise around the circle by the number of skips specified by the skipFactor,
            removing the player that was reached after performing all of the skips, and restarting this process from the next available player.
            Once all of the players have been removed, the name of the last player that was standing is returned.
    */
    public int solve () {
        Object lastPlayerStanding = null;
    
        while ( players.hasNext() ) {
            
            for ( int i = 0; i < this.skipFactor; i++ ) {
                lastPlayerStanding = players.next();
            }
            players.remove();
        }
    
        return Integer.parseInt( lastPlayerStanding.toString() );
    }
}