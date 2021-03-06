/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Model;
import it.polito.tdp.PremierLeague.model.Player;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnTopPlayer"
    private Button btnTopPlayer; // Value injected by FXMLLoader

    @FXML // fx:id="btnDreamTeam"
    private Button btnDreamTeam; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="txtGoals"
    private TextField txtGoals; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	try {
    		
    		double x = Double.parseDouble(txtGoals.getText());
    		this.model.creaGrafo(x);
    	} catch(NumberFormatException nbe) {
    		
    		txtResult.appendText("Inserisci un numero nel campo Goal fatti(x)\n");
    	} catch(NullPointerException npe) {
    		
    		txtResult.appendText("Inserisci un numero nel campo Goal fatti(x)\n");
    	}

    }

    @FXML
    void doDreamTeam(ActionEvent event) {

    	int k = 0;
    	
    	try {
    		
    		k = Integer.parseInt(txtK.getText());
    	}
    	catch(NumberFormatException nfe) {
    		
    		txtResult.appendText("Inserisci un numero intero nel campo #Giocatori (k)\n");
    		return;
    	}
    	
    	List<Player> lista = model.dreamTeam(k);
    	
    	txtResult.clear();
    	txtResult.appendText("Dream Team:\n");
    	for(Player p : lista) {
    		
    		txtResult.appendText(p.getName()+"\n");
    	}
    	txtResult.appendText("Titolarità: "+this.model.getTitolaritaDT());
    	
    }

    @FXML
    void doTopPlayer(ActionEvent event) {
    	
    	List<Player> lista = this.model.cercaTopPlayer();
    	
    	boolean top = true;
    	txtResult.clear();
    	txtResult.appendText("Top player: ");
    	for(Player p : lista) {
    		
    		txtResult.appendText(p.getName()+"\n");
    		if (top) {
    			
    			txtResult.appendText("Giocatori battuti:\n");
    			top = false;
    		}
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnTopPlayer != null : "fx:id=\"btnTopPlayer\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDreamTeam != null : "fx:id=\"btnDreamTeam\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGoals != null : "fx:id=\"txtGoals\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
