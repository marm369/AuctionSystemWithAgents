package ilisi;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CommissairePriseur extends Agent {
    private boolean auctionInProgress = false;
    private Map<String, Double> offers = new HashMap<>();
    private JTextArea textArea;

    @Override
    protected void setup() {
        JFrame frame = new JFrame("Commissaire-Priseur");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        textArea = new JTextArea();
        textArea.setEditable(false);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);
        frame.setVisible(true);

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    switch (msg.getPerformative()) {
                        case ACLMessage.REQUEST:
                            textArea.append("Requête reçue du Vendeur : " + msg.getContent() + "\n");
                            if (!auctionInProgress) {
                                auctionInProgress = true;
                                lancerAppelOffres();
                            }
                            break;

                        case ACLMessage.PROPOSE:
                            try {
                                String bidder = msg.getSender().getLocalName();
                                double price = Double.parseDouble(msg.getContent());
                                offers.put(bidder, price);
                                textArea.append("Offre reçue de " + bidder + ": " + price + "\n");

                                if (offers.size() >= 3) { 
                                    annoncerGagnant();
                                }
                            } catch (NumberFormatException e) {
                                textArea.append("Erreur : Offre non valide reçue.\n");
                            }
                            break;

                        default:
                            textArea.append("Message non pris en charge : " + msg.getContent() + "\n");
                            break;
                    }
                } else {
                    block();
                }
            }
        });
    }

    private void lancerAppelOffres() {
        textArea.append("Appel d'offres envoyé aux acheteurs.\n");
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        cfp.addReceiver(getAID("Acheteur1"));
        cfp.addReceiver(getAID("Acheteur2"));
        cfp.addReceiver(getAID("Acheteur3"));
        cfp.setContent("Appel d'offres pour un article artisanal");
        send(cfp);
    }

    private void annoncerGagnant() {
        if (offers.isEmpty()) {
            textArea.append("Aucune offre reçue. Enchère terminée sans gagnant.\n");
            auctionInProgress = false;
            return;
        }

        Map.Entry<String, Double> bestOffer = offers.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (bestOffer != null) {
            String winner = bestOffer.getKey();
            double winningPrice = bestOffer.getValue();
            textArea.append("Le gagnant est " + winner + " avec une offre de " + winningPrice + ".\n");

            ACLMessage winnerMsg = new ACLMessage(ACLMessage.INFORM);
            winnerMsg.addReceiver(getAID(winner));
            winnerMsg.setContent("Félicitations, vous avez remporté l'enchère avec une offre de " + winningPrice);
            send(winnerMsg);
        }

        auctionInProgress = false;
        offers.clear();
    }
}
