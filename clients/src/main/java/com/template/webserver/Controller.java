package com.template.webserver;

import com.template.flows.Initiator;
import com.template.states.TicketState;
import net.corda.core.identity.Party;
import net.corda.core.messaging.CordaRPCOps;
import net.corda.core.transactions.SignedTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Define your API endpoints here.
 */
@RestController
@RequestMapping("/") // The paths for HTTP requests are relative to this base path.
public class Controller {
    private final CordaRPCOps proxy;
    private final static Logger logger = LoggerFactory.getLogger(Controller.class);

    public Controller(NodeRPCConnection rpc) {
        this.proxy = rpc.proxy;
    }

    @GetMapping(value = "giveTicket", produces = "text/plain")
    private String templateendpoint(@RequestParam String department,
                                    @RequestParam String driver,
                                    @RequestParam String violation,
                                    @RequestParam String fine) throws ExecutionException, InterruptedException {
        Party departmentName = proxy.partiesFromName(department, false).iterator().next();
        Party driverName = proxy.partiesFromName(driver, false).iterator().next();
        SignedTransaction signedTransaction = proxy.startFlowDynamic(Initiator.class,
                new TicketState(departmentName, driverName, violation, fine)).getReturnValue().get();
        return signedTransaction.getId().toString();

    }

    @GetMapping("gottenTickets")
    private String gottenTickets () {
        List<TicketState> ticketStates = proxy.vaultQuery(TicketState.class).getStates()
                .stream()
                .map(gradeStateStateAndRef -> gradeStateStateAndRef.getState().getData())
                //.filter(gradeState -> gradeState.getStudent().equals(proxy.nodeInfo().getLegalIdentities().get(0)))
                .collect(Collectors.toList());
        return ticketStates.toString();
    }
}