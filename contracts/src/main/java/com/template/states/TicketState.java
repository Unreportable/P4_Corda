package com.template.states;

import com.template.contracts.TicketContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.serialization.ConstructorForDeserialization;
import net.corda.core.serialization.CordaSerializable;

import java.util.Arrays;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(TicketContract.class)
@CordaSerializable
public class TicketState implements ContractState {

    private final Party department;
    private final Party driver;
    private final String violation;
    private final String fine;

    @ConstructorForDeserialization
    public TicketState(Party department, Party driver, String violation, String fine) {
        this.department = department;
        this.driver = driver;
        this.violation = violation;
        this.fine = fine;
    }

    public Party getDepartment() { return department; }

    public Party getDriver() {
        return driver;
    }

    public String getViolation() { return violation; }

    public String getFine() {
        return fine;
    }

    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(department, driver);
    }

    @Override
    public String toString() {
        return "Ticket: [" +
                "GIBDD=" + department.getName().getOrganisation() +
                ", driver=" + driver.getName().getOrganisation() +
                ", violation=" + violation +
                ", fine=" + fine +
                ']';
    }
}