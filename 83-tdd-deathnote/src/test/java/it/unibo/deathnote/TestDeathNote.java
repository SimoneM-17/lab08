package it.unibo.deathnote;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.deathnote.api.DeathNote;
import it.unibo.deathnote.impl.DeathNoteImpl;

class TestDeathNote {

    private DeathNote deathNote;
    private String human;
    private String otherHuman;

    @BeforeEach
    public void setUp() {
        this.deathNote = new DeathNoteImpl();
        this.human = "Joe Bartolozzi";
        this.otherHuman = "Gerry Biscotti";
    }

    @Test
    public void testRuleGetter() {
        assertThrows(IllegalArgumentException.class, () -> deathNote.getRule(0));
        assertThrows(IllegalArgumentException.class, () -> deathNote.getRule(-1));
        assertThrows(IllegalArgumentException.class, () -> deathNote.getRule(DeathNote.RULES.size() + 1));
        assertDoesNotThrow(() -> deathNote.getRule(1));
        try {
            deathNote.getRule(-1);
        } catch (IllegalArgumentException e) {
            assertNotNull(e.getMessage());
            assertFalse(e.getMessage().isBlank());
        }
    }

    @Test
    public void testRuleList() {
        for (String rule: DeathNote.RULES) {
            assertNotNull(rule);
            assertFalse(rule.isBlank());
        }
    }

    @Test
    public void testNameWriting() {
        assertFalse(deathNote.isNameWritten(human));
        deathNote.writeName(human);
        assertTrue(deathNote.isNameWritten(human));
        assertFalse(deathNote.isNameWritten(otherHuman));
        assertThrows(NullPointerException.class, () -> deathNote.writeName(null));
        // ???
        assertThrows(NullPointerException.class, () -> deathNote.writeName(""));
        assertThrows(NullPointerException.class, () -> deathNote.writeName(" "));
        assertFalse(deathNote.isNameWritten(""));
    }

    @Test
    public void testDeathCause() {
        assertThrows(IllegalStateException.class, () -> deathNote.writeDeathCause(null));
        assertThrows(IllegalStateException.class, () -> deathNote.writeDeathCause("cause with no name written"));
        deathNote.writeName(human);
        assertEquals("heart attack", deathNote.getDeathCause(human));
        deathNote.writeName(otherHuman);
        assertTrue(deathNote.writeDeathCause("karting accident"));
        assertEquals("karting accident", deathNote.getDeathCause(otherHuman));
        waitSomeTime(100);
        assertFalse(deathNote.writeDeathCause("ate without youtube"));
        assertEquals("karting accident", deathNote.getDeathCause(otherHuman));
    }

    @Test
    public void testDeathDetails() {
        assertThrows(IllegalStateException.class, () -> deathNote.writeDetails("sas"));
        assertThrows(IllegalStateException.class, () -> deathNote.writeDetails(null));
        assertThrows(IllegalStateException.class, () -> deathNote.writeDetails(""));
        assertThrows(IllegalStateException.class, () -> deathNote.writeDetails(" "));
        deathNote.writeName(human);
        assertEquals("", deathNote.getDeathDetails(human));
        assertTrue(deathNote.writeDetails("ran for too long"));
        assertEquals("ran for too long", deathNote.getDeathDetails(human));
        deathNote.writeName(otherHuman);
        waitSomeTime(2000);
        assertFalse(deathNote.writeDetails("3 secs with no zaza"));
        assertEquals("", deathNote.getDeathDetails(otherHuman));
    }

    private void waitSomeTime(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}