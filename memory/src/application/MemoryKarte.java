package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class MemoryKarte extends Button{

	private int bildID;
	private ImageView bildVorne, bildHinten;
	
	private int bildPos;
	
	private boolean umgedreht;
	private boolean nochImSpiel;
	
	private MemoryFeld spielfeld;
	
	class KartenListener implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			if (nochImSpiel == false || (spielfeld.zugErlaubt() == false))
				return;
			if (umgedreht == false)
				vorderseiteZeigen();
				spielfeld.karteOeffnen(MemoryKarte.this);
		}
	}
	
	public MemoryKarte(String vorne, int bildID, MemoryFeld spielfeld) {
		bildVorne = new ImageView(getClass().getResource(vorne).toString());
		bildHinten = new ImageView(getClass().getResource("grafiken/back.jpg").toString());
		setGraphic(bildHinten);
		
		this.bildID = bildID;
		umgedreht = false;
		nochImSpiel = true;
		this.spielfeld = spielfeld;
		setOnAction(new KartenListener());
	}
	
	public void vorderseiteZeigen() {
		setGraphic(bildVorne);
		umgedreht = true;
	}
	
	public void rueckseiteZeigen(boolean rausnehmen) {
		if (rausnehmen == true) {
			setGraphic(new ImageView(getClass().getResource("grafiken/aufgedeckt.jpg").toString()));
			nochImSpiel = false;
		}
		else {
			setGraphic(bildHinten);
			umgedreht = false;
		}
	}

	public int getBildPos() {
		return bildPos;
	}

	public void setBildPos(int bildPos) {
		this.bildPos = bildPos;
	}

	public int getBildID() {
		return bildID;
	}

	public boolean isUmgedreht() {
		return umgedreht;
	}

	public boolean isNochImSpiel() {
		return nochImSpiel;
	}
}
