package ilisi;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import javax.swing.*;

public class Vendeur extends Agent {
    private JFrame frame;
    private JButton startAuctionButton;

    @Override
    protected void setup() {
        // Création de l'interface graphique
        frame = new JFrame("Agent Vendeur");
        startAuctionButton = new JButton("Lancer l'enchère");
        frame.add(startAuctionButton);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        startAuctionButton.addActionListener(e -> lancerEnchere());
        System.out.println("Agent Vendeur démarré : " + getLocalName());
    }

    private void lancerEnchere() {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(getAID("CommissairePriseur")); // Nom de l'agent Commissaire-Priseur
        msg.setContent("Organiser une enchère pour un article artisanal");
        send(msg);
        JOptionPane.showMessageDialog(frame, "Demande envoyée au Commissaire-Priseur !");
        System.out.println("Demande envoyée au Commissaire-Priseur.");
    }
}
