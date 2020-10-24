package Prim.views;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import javax.annotation.Resource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.sceneController;

public class domandeController  implements model.ISceneController{

	sceneController sc;
	Integer numeroDomanda = 1;   //linea corrispondente alla domanda corrente nel file txt

    @FXML
    private Button back, next, menu, inserisci,done, riprova;
    
    @FXML
    private AnchorPane ap;
    
    @FXML
    private Text domanda;
    
    @FXML
    private TextField tentativo;

    @FXML
    private TextArea risposta;

    
  //funzioni che gestiscono il file delle domande
	 
    @FXML
    void controlla(ActionEvent event) throws IOException {
    	
		String prova = tentativo.getText();     
		prova = prova.trim();
		
		if(prova.length()!=0) {
			
		try {	
			
			InputStream input = Resource.class.getResourceAsStream("/domandePrim.txt");
			Scanner s = new Scanner (input);
			Integer line = numeroDomanda + 1;                      //a seconda di quale è la domanda si richiede allo scanner di leggere una diversa riga del file txt
			
			String riga = getLine(s,line);               //prende la riga delle risposte nel file txt
			
			Scanner scanner = new Scanner(riga);
			scanner.useDelimiter("/");
			
			if (numeroDomanda == 1) {             //se si è alla prima domanda si ha un'ulteriore controllo
				 try {
		  			 Integer.parseInt(prova);                   //se l'inserimento non è un numero, la risposta non è valida
		  		 } catch(NumberFormatException e) {
		  			 risposta.appendText("Inserimento non valido");
		  			 inserisci.setDisable(true);
		 		     riprova.setDisable(false);
		  			 scanner.close();
		  			 return;
		  		 }		
			}
			
			boolean found = false;
			while(scanner.hasNext() ) {
				String a = scanner.next();
				if(a.equals(prova)) {
					risposta.appendText("Corretto!"); 
					found = true;
				}
			}
		    if(!found) risposta.appendText("Sbagliato, riprova");
		    else if((found) && (line < 8)) {
		    	next.setDisable(false);
		    }
		    else  {
		    	next.setText("MENU");
		    	next.setDisable(false);
		    }
		    
		    inserisci.setDisable(true);
		    riprova.setDisable(false);
		    
           scanner.close();
           
		}    catch(Exception ex) {
			
	        System.out.println("Unable to open file 'risposte.txt'");        
	        
	        }
		}
    }
    
  String getLine(Scanner s, Integer line) {       //funzione che restituisce la riga da cui leggere le risposte
    	String riga = new String();
    	while(line > 0) {
    		    riga = s.nextLine();
				line = line-1;
    	}
    	return(riga);	
    }
    
    @FXML
    void clean(ActionEvent event) {         //pulisce l'interfaccia ogni volta che viene schiacciato il tasto riprova
    	risposta.clear();
    	tentativo.clear();
    	inserisci.setDisable(false);
    	riprova.setDisable(true);
    }
    
   
    
  //Funzioni che gestiscono lo scambio tra le pagine	 
    
    @FXML
	 void goBack(ActionEvent event) throws IOException {
	   if (numeroDomanda == 1) sc.goBack();          //se si è alla prima domanda si torna alle domande a risposta multipla
	   else {                   
	    numeroDomanda = numeroDomanda -2;                     //altrimenti viene caricata la domanda precedente
	    this.setQuestion();
	    next.setText(">>");
	   }
	   this.clean(event);
	  }
	    
	@FXML
	  void goNext(ActionEvent event) throws IOException {
		this.clean(event);
		if (numeroDomanda == 7) {
		this.gotoMenu(event);                  //se si è all'ultima scena l'utente viene mandato al menù principale
		}
		else {
	    numeroDomanda = numeroDomanda + 2;                     //altrimenti viene caricata la domanda successiva
	      this.setQuestion();
	    }
      }		 
	
	public void setQuestion() throws IOException {       //setta la domanda che deve essere caricata
		try {
		 InputStream input = Resource.class.getResourceAsStream("/domandePrim.txt");
		  Scanner scanner = new Scanner (input);
	      domanda.setText(getLine(scanner, numeroDomanda));
	      scanner.close();
	      next.setDisable(true);
		}catch (Exception e) {
			System.out.println("File not found");
		}
	}


	 void gotoMenu(ActionEvent event) throws IOException {    //riporta l'utente al menu se ha risposto correttamente all'ultima domanda
		 if(next.getText().equals("MENU")) {
	        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
	        sc.getProgetto().gotoMenu(window);
		 }
	  }
	
	@Override
	public void setSceneParent(sceneController parent) {
		sc = parent;	
	}
	
	
	 @FXML
		void initialize() throws IOException {	
	        
	  	  if (ap != null) {                              //se viene premuto il tasto invio, mette in azione di tasto inserisci 
	  	      ap.setOnKeyPressed(e -> {                   //e controlla la risposta
	  		    if (e.getCode() == KeyCode.ENTER) {
	  		        try {
	  		        inserisci.fire();
	  		        }catch (Exception error) {
	  		        	System.out.println(error.getMessage());
	  		        }
	  		    }
	  		});
	            }
	          
	     //FXML domande  
	  	    assert back != null : "fx:id=\"back\" was not injected: check your FXML file 'domande.fxml'.";
            assert next != null : "fx:id=\"next\" was not injected: check your FXML file 'domande.fxml'.";
            assert ap != null : "fx:id=\"ap\" was not injected: check your FXML file 'domande.fxml'.";
	        assert inserisci != null : "fx:id=\"inserisci\" was not injected: check your FXML file 'domande.fxml'.";  
	        assert tentativo != null : "fx:id=\"tentativo\" was not injected: check your FXML file 'domande.fxml'.";
	        assert risposta != null : "fx:id=\"risposta\" was not injected: check your FXML file 'domande.fxml'.";
	        assert riprova != null : "fx:id=\"riprova\" was not injected: check your FXML file 'domande.fxml'.";
	        assert domanda != null : "fx:id=\"domanda\" was not injected: check your FXML file 'domande.fxml'.";
	        if(numeroDomanda == 1) this.setQuestion();
	        
	    }

}
