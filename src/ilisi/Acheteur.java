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
        frame.setLayout(new GridBagLayout()); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); 

        JLabel label = new JLabel("Votre proposition :");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(44, 62, 80)); 

        offerField = new JTextField(10);
        offerField.setFont(new Font("Arial", Font.PLAIN, 14));

        sendButton = new JButton("Envoyer");
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        sendButton.setBackground(new Color(52, 152, 219)); 
        sendButton.setForeground(Color.WHITE); 
        sendButton.setFocusPainted(false); 
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); 

        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(label, gbc);

        gbc.gridx = 1;
        frame.add(offerField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(sendButton, gbc);

        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Action associée au bouton d'envoi
        sendButton.addActionListener(e -> envoyerProposition());

        System.out.println("Agent Acheteur démarré : " + getLocalName());
    }

    private void envoyerProposition() {
        String offerText = offerField.getText();
        try {
            double offer = Double.parseDouble(offerText); 
            ACLMessage propose = new ACLMessage(ACLMessage.PROPOSE);
            propose.addReceiver(getAID("CommissairePriseur")); 
            propose.setContent(String.valueOf(offer));
            send(propose);

            JOptionPane.showMessageDialog(frame, 
                    "Proposition envoyée : " + offer, 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Proposition envoyée : " + offer);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, 
                    "Veuillez entrer un nombre valide !", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
