package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.BoardFactory;
import nl.tudelft.jpacman.npc.ghost.Blinky;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.internal.verification.VerificationModeFactory;

import java.util.ArrayList;
import java.lang.IllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This is a test class for MapParser.
 */
@ExtendWith(MockitoExtension.class)
public class MapParserTest {

    // Mock objects for the class-under-test's dependencies [cite: 32, 36]
    @Mock
    private BoardFactory boardFactory;

    @Mock
    private LevelFactory levelFactory;

    @Mock
    private Blinky blinky; // Mock for a ghost instance [cite: 33]

    /**
     * Test for the parseMap method with a correctly formatted map (good map).
     */
    @Test
    public void testParseMapGood() {
        // MockitoAnnotations.initMocks(this) is often not required when using @ExtendWith(MockitoExtension.class)
        // assertNotNull(boardFactory);
        // assertNotNull(levelFactory);

        // Stubbing: Tell the mocked levelFactory what to return when createGhost() is called.
        Mockito.when(levelFactory.createGhost()).thenReturn(blinky); [cite: 39]

        MapParser mapParser = new MapParser(levelFactory, boardFactory); [cite: 39]
        ArrayList<String> map = new ArrayList<>();

        // Map content: Walls (#), Pacman (P), Ghost (G) [cite: 38, 39]
        map.add("############"); // 12 Walls
        map.add("#P        G#"); // 2 Walls, 1 Pacman, 1 Ghost
        map.add("############"); // 12 Walls

        mapParser.parseMap(map); [cite: 39]

        // Verification: Check if reading the map led to the proper interactions with the factories. [cite: 42]

        // createBoard is called once (to start building the board)
        Mockito.verify(boardFactory, Mockito.times(1)).createBoard(Mockito.any());

        // createLevel is called once to finalize the level
        Mockito.verify(levelFactory, Mockito.times(1)).createLevel(Mockito.any(), Mockito.anyList(), Mockito.anyList());

        // createPacMan is called once (for the 'P')
        Mockito.verify(levelFactory, Mockito.times(1)).createPacMan();

        // createGhost is called exactly once (for the 'G') [cite: 43, 44]
        Mockito.verify(levelFactory, Mockito.times(1)).createGhost();

        // createWall is called 26 times (for every '#' character: 12 + 2 + 12 = 26) [cite: 45]
        Mockito.verify(boardFactory, Mockito.times(26)).createWall();
    }

    /**
     * Test for the parseMap method with an incorrectly formatted map (bad map).
     * Expects an IllegalArgumentException for unexpected characters. [cite: 47, 48]
     */
    @Test
    public void testParseMapWrong1() {
        // The expected exception and message for an unknown character 'X'
        final String expectedErrorMessage = "Invalid character: X";

        // Use assertThrows to execute the code and capture the expected exception [cite: 49]
        IllegalArgumentException thrown =
            Assertions.assertThrows(IllegalArgumentException.class, () -> { // ADDYOURCODEHERE [cite: 49]

                // MockitoAnnotations.initMocks(this);
                // assertNotNull(boardFactory);
                // assertNotNull(levelFactory);

                MapParser mapParser = new MapParser(levelFactory, boardFactory); // [cite: 49]
                ArrayList<String> map = new ArrayList<>();

                /*
                Create a map that contains an invalid character 'X'
                */
                map.add("##########");
                map.add("#P  X    #"); // 'X' is the invalid character
                map.add("##########");

                mapParser.parseMap(map); // [cite: 49]
            });

        // Verify that the exception message is correct [cite: 49]
        Assertions.assertEquals(expectedErrorMessage, thrown.getMessage()); // ADDYOURCODEHERE [cite: 49]
    }
}
