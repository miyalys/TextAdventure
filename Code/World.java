import static statics.StaticLib.*;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Random;

public class World {
  // WorldTiles[] worldTiles;
  // User configurable
  private final int SIZE = 21; // The length of one side/axis
  private Player player;
  private ArrayList<Building> buildings = new ArrayList<Building>();
  private ArrayList<Person> persons = new ArrayList<Person>();

  public World() {
    Random rand = new Random(); 

    for (int x = 0; x<SIZE; x++) {

      for (int y = 0; y<SIZE; y++) {

        // Set the starter building and Player
        if (x == 10 && y == 10 ) {
          buildings.add( new Building(x,y) );
          player = new Player(x,y); // Make sure the player added is added to the World class to identify it in toString
          persons.add( player );
        }
        else {
          if (rand.nextInt(15) == 1) persons.add( new NPC(x, y) );
          // 20% chance of replacing the null placeholder building we just added as default above, with an actual building
          else if (rand.nextInt(20) == 1) buildings.add(new Building(x, y));
        }
      }
    }

    // Just for testing. Remove afterwards, probably
    print(this.toString(), true);

    new Command(this);
  }


  // TODO: Loop through the ArrayLists for persons and buildings
  public String toString() {

    String[] tempArray = new String[SIZE*SIZE];

    // Fill array with empty symbols.
    for (int x = 0; x<SIZE*SIZE; x++) {
        tempArray[x] = EMPTY + "   ";
    }

    // Update it with buildings
    for (Building building : buildings) {
      tempArray[ (int) (building.getX() + SIZE * building.getY()) ] = BUILDING + "   ";
    }

    // Update it with NPCs and Player(s)
    for (Person person : persons) {
      String personToPrint;
      if (person instanceof NPC) {
        personToPrint = NPC + "   ";
      }
      else personToPrint = PLAYER + "   ";
      tempArray[ (int) (person.getX() + SIZE * person.getY()) ] = personToPrint;
    }

    String ret = "";

    // Creating the final string, adding newlines where necessary
    for (int i = 0; i<tempArray.length; i++) {
      if (i > 0 && i % SIZE == 0) {
        ret += "\n\n";
      }
      ret += tempArray[i];
    }
    return ret;
  }

  
  public void movePlayer(Command.Direction direction, int distance) {
    move(player, direction, distance);
  }

  // TODO: Loop through the worldTile array and move the Player as much as possible in the requested direction.
  // Remove the player from the old WorldTile and insert her/him into the new one.
  // TODO: Make similar move method for Rooms!
  private void move(Person reqPerson, Command.Direction direction, int reqDistance) {

    for (Person person : persons) {
      if ( person == reqPerson) { 
        double newX, newY = 0;

        if (direction == Command.Direction.NORTH) {
          newY = Math.max(person.getY() - reqDistance, 0);
          person.setLocation(person.getX(), newY);
        }
        else if (direction == Command.Direction.EAST) {
          newX = Math.min(person.getX() + reqDistance, SIZE-1);
          person.setLocation(newX, person.getY());
        }
        else if (direction == Command.Direction.SOUTH) {
          newY = Math.min(person.getY() + reqDistance, SIZE-1);
          person.setLocation(person.getX(), newY);
        }
        else { // West
          newX = Math.max(person.getX() - reqDistance, 0);
          person.setLocation(newX, person.getY());
        }
        break; // We've found the right person and moved her/him. Break!
        
      }
    }
  }
  
}
