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
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setBackground(new Color(245, 245, 245)); 
        textArea.setForeground(new Color(44, 62, 80)); 
    
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Journal des enchères"));
        frame.add(scrollPane, BorderLayout.CENTER);
   
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(236, 240, 241)); 

        JButton startAuctionButton = new JButton("Nouvelle enchère");
        startAuctionButton.setFont(new Font("Arial", Font.BOLD, 14));
        startAuctionButton.setBackground(new Color(46, 204, 113)); 
        startAuctionButton.setForeground(Color.WHITE);
        startAuctionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startAuctionButton.setFocusPainted(false);

        startAuctionButton.addActionListener(e -> {
            if (!auctionInProgress) {
                lancerAppelOffres();
                auctionInProgress = true;
            } else {
                JOptionPane.showMessageDialog(frame, 
                    "Une enchère est déjà en cours.", 
                    "Erreur", 
                    JOptionPane.WARNING_MESSAGE);
            }
        });

        bottomPanel.add(startAuctionButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

      
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);

        
        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    switch (msg.getPerformative()) {
                        case ACLMessage.REQUEST:
                            afficherMessage("Requête reçue du Vendeur : " + msg.getContent());
                            if (!auctionInProgress) {
                                auctionInProgress = true;
                                lancerAppelOffres();
                            }
                            break;

                        case ACLMessage.PROPOSE:
                            traiterProposition(msg);
                            break;

                        default:
                            afficherMessage("Message non pris en charge : " + msg.getContent());
                            break;
                    }
                } else {
                    block();
                }
            }
        });
    }

    private void lancerAppelOffres() {
        afficherMessage("Appel d'offres envoyé aux acheteurs.");
        ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
        cfp.addReceiver(getAID("Acheteur1"));
        cfp.addReceiver(getAID("Acheteur2"));
        cfp.addReceiver(getAID("Acheteur3"));
        cfp.setContent("Appel d'offres pour un article artisanal");
        send(cfp);
    }

    private void traiterProposition(ACLMessage msg) {
        try {
            String bidder = msg.getSender().getLocalName();
            double price = Double.parseDouble(msg.getContent());
            offers.put(bidder, price);
            afficherMessage("Offre reçue de " + bidder + ": " + price);

            if (offers.size() >= 3) { 
                annoncerGagnant();
            }
        } catch (NumberFormatException e) {
            afficherMessage("Erreur : Offre non valide reçue.");
        }
    }

    private void annoncerGagnant() {
        if (offers.isEmpty()) {
            afficherMessage("Aucune offre reçue. Enchère terminée sans gagnant.");
            auctionInProgress = false;
            return;
        }

        Map.Entry<String, Double> bestOffer = offers.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);

        if (bestOffer != null) {
            String winner = bestOffer.getKey();
            double winningPrice = bestOffer.getValue();
            afficherMessage("Le gagnant est " + winner + " avec une offre de " + winningPrice + ".");

            ACLMessage winnerMsg = new ACLMessage(ACLMessage.INFORM);
            winnerMsg.addReceiver(getAID(winner));
            winnerMsg.setContent("Félicitations, vous avez remporté l'enchère avec une offre de " + winningPrice);
            send(winnerMsg);
        }

        auctionInProgress = false;
        offers.clear();
    }

    private void afficherMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            textArea.append(message + "\n");
            textArea.setCaretPosition(textArea.getDocument().getLength()); 
        });
    }
}
