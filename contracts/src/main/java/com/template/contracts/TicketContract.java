package com.template.contracts;

import com.template.states.TicketState;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.TypeOnlyCommandData;
import net.corda.core.transactions.LedgerTransaction;

import static net.corda.core.contracts.ContractsDSL.requireSingleCommand;
import static net.corda.core.contracts.ContractsDSL.requireThat;

// ************
// * Contract *
// ************
public class TicketContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String ID = "com.template.contracts.GradeContract";

    @Override
    public void verify(LedgerTransaction tx) {
        requireSingleCommand(tx.getCommands(), GiveTicket.class);
        requireThat(require -> {
            require.using("There cant be inputs", tx.getInputs().size() == 0);
            require.using("Output must be TicketState", (tx.getOutputs().get(0).getData()) instanceof TicketState);

            final TicketState output = (TicketState) tx.getOutputs().get(0).getData();

            require.using("Department must not be null", output.getDepartment() != null);
            require.using("Department must be a department", output.getDepartment().getName().getOrganisation().startsWith("Department"));

            require.using("Driver must not be null", output.getDriver() != null);
            require.using("Driver must be a driver", output.getDriver().getName().getOrganisation().startsWith("Driver"));

            require.using("Violation must not be null", (output.getViolation() != null && !output.getViolation().equals("")));
            require.using("Fine must  not be null", (output.getFine() != null && !output.getFine().equals("")));

            return null;
        });

    }

    // Used to indicate the transaction's intent.
    public static class GiveTicket extends TypeOnlyCommandData {
    }
}