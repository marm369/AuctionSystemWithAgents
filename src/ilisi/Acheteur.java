package ilisi;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;

import javax.swing.*;
import java.awt.*;

public class Acheteur extends Agent {
    private JFrame frame;
    private JTextField offerField;
    private JButton sendButton;

    @Override
    protected void setup() {
        frame = new JFrame("Agent Acheteur - " + getLocalName());
        frame.setLayout(new FlowLayout());

        JLabel label = new JLabel("Votre proposition :");
        offerField = new JTextField(10);
        sendButton = new JButton("Envoyer");

        frame.add(label);
        frame.add(offerField);
        frame.add(sendButton);

        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        sendButton.addActionListener(e -> envoyerProposition());

        System.out.println("Agent Acheteur démarré : " + getLocalName());
    }

    private void envoyerProposition() {
        String offerText = offerField.getText();
        try {
            double offer = Double.parseDouble(offerText);
            ACLMessage propose = new ACLMessage(ACLMessage.PROPOSE);
            // Envoyer au commissaire-priseur
            propose.addReceiver(getAID("CommissairePriseur"));
            propose.setContent(String.valueOf(offer));
            send(propose);
            JOptionPane.showMessageDialog(frame, "Proposition envoyée : " + offer);
            System.out.println("Proposition envoyée : " + offer);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Veuillez entrer un nombre valide !");
        }
    }
}
