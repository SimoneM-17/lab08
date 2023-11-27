package it.unibo.deathnote.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import it.unibo.deathnote.api.DeathNote;

public class DeathNoteImpl implements DeathNote {

    /**
     *
     */
    private static final int DEATH_CAUSE_TIME_LIMIT = 40;
    private static final int DEATH_DETAILS_TIME_LIMIT = 2000;
    private Map<String, Death> deathNote;
    private Death latestDeath;

    public DeathNoteImpl() {
        this.deathNote = new LinkedHashMap<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getRule(int ruleNumber) throws IllegalArgumentException{
        if (ruleNumber >= 1 && ruleNumber <= RULES.size()) {
            return DeathNote.RULES.get(ruleNumber - 1);
        } else {
            throw new IllegalArgumentException("invalid rule number");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeName(String name) throws NullPointerException{
        if (name == null || name.isBlank()) {
            throw new NullPointerException("the name to write should not be blank");
        } else {
            
            this.latestDeath = new Death();
            this.deathNote.put(name, this.latestDeath);
            latestDeath.startTimer();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDeathCause(String cause) throws IllegalStateException{
        return updateDeathInfo(DEATH_CAUSE_TIME_LIMIT, () -> latestDeath.SetCause(cause));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean writeDetails(String details) throws IllegalStateException{
        return updateDeathInfo(DEATH_DETAILS_TIME_LIMIT, () -> latestDeath.SetDetails(details));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathCause(String name) throws IllegalArgumentException{
        return getDeath(name).cause;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDeathDetails(String name) throws IllegalArgumentException{
        return getDeath(name).details;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNameWritten(String name) {
        return this.deathNote.containsKey(name);
    }

    private Death getDeath(final String name) {
        Death deadPerson = this.deathNote.get(name);
        if (deadPerson == null) {
            throw new IllegalArgumentException(name + " is not present in the DeathNote");
        }
        return deadPerson;
    }
    
    private boolean updateDeathInfo(int timeLimit, DeathOperation operation) {
        if (this.deathNote.isEmpty()) {
            throw new IllegalStateException("to write details there must be a name written");
        }
        if (latestDeath.getTimePassed() < timeLimit) {
            operation.call();
            return true;
        } else {
            return false;
        }
    }

    @FunctionalInterface
    private interface DeathOperation {
        void call();
    }

    private class Death {

        private static final String DEF_CAUSE = "heart attack";
        private String cause;
        private String details;
        private long initialTime;

        private int hash;

        private Death(){
            this.cause = DEF_CAUSE;
            this.details = "";
        };

        private void SetCause(String cause) {
            this.cause = cause;
        }

        private void SetDetails(String details) {
            this.details = details;
        }

        private long getTimePassed() {
            return System.currentTimeMillis() - this.initialTime;
        }

        private void startTimer() {
            this.initialTime = System.currentTimeMillis();
        }

        @Override
        public int hashCode() {
            if (hash == 0) {
                hash = Objects.hash(cause, details, initialTime);
            }
            return hash;
        }
    }
}
