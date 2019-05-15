package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.template.contracts.TicketContract;
import com.template.states.TicketState;
import net.corda.core.flows.CollectSignaturesFlow;
import net.corda.core.flows.FinalityFlow;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.FlowSession;
import net.corda.core.flows.InitiatingFlow;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

import java.security.PublicKey;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class Initiator extends FlowLogic<SignedTransaction> {
    private final ProgressTracker progressTracker = new ProgressTracker(
            VERIFYING,
            CREATING,
            SUCCESS
    );


    private static final ProgressTracker.Step VERIFYING = new ProgressTracker.Step(
            "Verifying");
    private static final ProgressTracker.Step CREATING = new ProgressTracker.Step(
            "Generating and signing transaction proposal");
    private static final ProgressTracker.Step SUCCESS = new ProgressTracker.Step(
            "Recording completed transaction");

    private final TicketState ticketState;

    public Initiator(TicketState ticketState) {
        this.ticketState = ticketState;
    }

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        if (!getOurIdentity().equals(ticketState.getDepartment()))
            throw new IllegalArgumentException("Only specific department can give tickets");
        Party notary = getServiceHub().getNetworkParameters().getNotaries().get(0).getIdentity();
        Set<FlowSession> otherFlowSessions = new HashSet<>();
        otherFlowSessions.add(initiateFlow(ticketState.getDriver()));
        progressTracker.setCurrentStep(CREATING);
        List<PublicKey> publicKeys = ticketState.getParticipants()
                .stream()
                .map(AbstractParty::getOwningKey)
                .collect(Collectors.toList());
        TransactionBuilder transactionBuilder = new TransactionBuilder(notary)
                .addCommand(new TicketContract.GiveTicket(), publicKeys)
                .addOutputState(ticketState);

        SignedTransaction signedTransaction = getServiceHub().signInitialTransaction(transactionBuilder);

        progressTracker.setCurrentStep(VERIFYING);
        signedTransaction.getTx().toLedgerTransaction(getServiceHub()).verify();

        SignedTransaction fullySignedTx = subFlow(new CollectSignaturesFlow(signedTransaction, otherFlowSessions));
        progressTracker.setCurrentStep(SUCCESS);
        return subFlow(new FinalityFlow(fullySignedTx, otherFlowSessions));
    }
}
