package ilisi;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import javax.swing.*;
import java.awt.*;

public class Vendeur extends Agent {
    private JFrame frame;
    private JButton startAuctionButton;

    @Override
    protected void setup() {
        frame = new JFrame("Agent Vendeur");
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 20)); 
        startAuctionButton = new JButton("Lancer l'enchère");

        startAuctionButton.setFont(new Font("Arial", Font.BOLD, 14));
        startAuctionButton.setBackground(new Color(34, 153, 84)); 
        startAuctionButton.setForeground(Color.WHITE); 
        startAuctionButton.setFocusPainted(false); 

        frame.add(startAuctionButton);

        frame.setSize(350, 250); 
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false); 
        frame.setVisible(true);

        startAuctionButton.addActionListener(e -> lancerEnchere());

        System.out.println("Agent Vendeur démarré : " + getLocalName());
    }

    private void lancerEnchere() {
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(getAID("CommissairePriseur")); 
        msg.setContent("Organiser une enchère pour un article artisanal");
        send(msg);

        JOptionPane.showMessageDialog(frame, "Demande envoyée au Commissaire-Priseur !", "Succès", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("Demande envoyée au Commissaire-Priseur.");
    }
}
