package ilisi;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialisation de la plateforme JADE
            Runtime runtime = Runtime.instance();
            Profile profile = new ProfileImpl();
            profile.setParameter(Profile.MAIN_HOST, "localhost");
            ContainerController container = runtime.createMainContainer(profile);

            System.out.println("Plateforme JADE démarrée...");

            // Démarrage de l'agent Commissaire-Priseur
            AgentController commissaire = container.createNewAgent("CommissairePriseur", "ilisi.CommissairePriseur", null);
            commissaire.start();

            // Démarrage de l'agent Vendeur
            AgentController vendeur = container.createNewAgent("Vendeur", "ilisi.Vendeur", null);
            vendeur.start();

            // Démarrage des agents Acheteurs
            AgentController acheteur1 = container.createNewAgent("Acheteur1", "ilisi.Acheteur", null);
            acheteur1.start();

            AgentController acheteur2 = container.createNewAgent("Acheteur2", "ilisi.Acheteur", null);
            acheteur2.start();

            AgentController acheteur3 = container.createNewAgent("Acheteur3", "ilisi.Acheteur", null);
            acheteur3.start();

       
            System.out.println("Agents créés et démarrés avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
