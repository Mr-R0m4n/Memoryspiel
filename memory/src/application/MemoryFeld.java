package application;

import java.util.Arrays;
import java.util.Collections;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class MemoryFeld {
	class TimerHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			karteSchliessen();
		}
	}

	class SchummelnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			for (MemoryKarte karte : karten) {
				if (karte.isNochImSpiel() == true) {
					karte.vorderseiteZeigen();
				}
			}
			Timeline zeitfenster = new Timeline(new KeyFrame(Duration.millis(2000), new SchummelZeit()));
			zeitfenster.play();

		}
	}

	class SchummelZeit implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent arg0) {
			for (MemoryKarte karte : karten) {
				if (karte.isNochImSpiel() == true) {
					karte.rueckseiteZeigen(false);
				}
			}
		}
	}

	private MemoryKarte[] karten;

	private String[] bilder = { "grafiken/apfel.jpg", "grafiken/birne.jpg", "grafiken/blume.jpg", "grafiken/blume2.jpg",
			"grafiken/ente.jpg", "grafiken/fisch.jpg", "grafiken/fuchs.jpg", "grafiken/igel.jpg",
			"grafiken/kaenguruh.jpg", "grafiken/katze.jpg", "grafiken/kuh.jpg", "grafiken/maus1.jpg",
			"grafiken/maus2.jpg", "grafiken/maus3.jpg", "grafiken/melone.jpg", "grafiken/pilz.jpg",
			"grafiken/ronny.jpg", "grafiken/schmetterling.jpg", "grafiken/sonne.jpg", "grafiken/wolke.jpg",
			"grafiken/maus4.jpg" };

	public int menschPunkte, computerPunkte;
	private Label menschPunkteLabel, computerPunkteLabel, aktuellerSpieler;
	private Button schummeln;
	private int umgedrehteKarten;
	private MemoryKarte[] paar;
	private int spieler;
	private int[][] gemerkteKarten;
	private int spielstaerke;
	private Timeline timer;

	public MemoryFeld() {
		karten = new MemoryKarte[42];
		paar = new MemoryKarte[2];
		gemerkteKarten = new int[2][21];
		menschPunkte = 0;
		computerPunkte = 0;
		umgedrehteKarten = 0;
		spieler = 0;
		spielstaerke = 10;

		for (int aussen = 0; aussen < 2; aussen++)
			for (int innen = 0; innen < 21; innen++)
				gemerkteKarten[aussen][innen] = -1;
	}

	public FlowPane initGUI(FlowPane feld) {
		kartenZeichnen(feld);
		menschPunkteLabel = new Label();
		computerPunkteLabel = new Label();
		menschPunkteLabel.setText(Integer.toString(menschPunkte));
		computerPunkteLabel.setText(Integer.toString(computerPunkte));
		aktuellerSpieler = new Label();
		aktuellerSpieler.setText("Der Mensch");
		schummeln = new Button("Schummeln");
		SchummelnHandler schummler = new SchummelnHandler();
		schummeln.setOnAction(schummler);
		GridPane tempGrid = new GridPane();
		tempGrid.add(new Label("Mensch: "), 0, 0);
		tempGrid.add(menschPunkteLabel, 1, 0);
		tempGrid.add(new Label("Computer: "), 0, 1);
		tempGrid.add(computerPunkteLabel, 1, 1);
		tempGrid.add(new Label("Es zieht: "), 0, 2);
		tempGrid.add(aktuellerSpieler, 1, 2);
		tempGrid.add(schummeln, 1, 3);
		feld.getChildren().add(tempGrid);
		return feld;
	}

	private void kartenZeichnen(FlowPane feld) {
		int count = 0;
		for (int i = 0; i <= 41; i++) {
			karten[i] = new MemoryKarte(bilder[count], count, this);
			if ((i + 1) % 2 == 0)
				count++;
		}
		Collections.shuffle(Arrays.asList(karten));
		for (int i = 0; i <= 41; i++) {
			feld.getChildren().add(karten[i]);
			karten[i].setBildPos(i);
		}
	}

	public void karteOeffnen(MemoryKarte karte) {
		int kartenID, kartenPos;
		paar[umgedrehteKarten] = karte;
		kartenID = karte.getBildID();
		kartenPos = karte.getBildPos();
		if ((gemerkteKarten[0][kartenID] == -1))
			gemerkteKarten[0][kartenID] = kartenPos;
		else if (gemerkteKarten[0][kartenID] != kartenPos)
			gemerkteKarten[1][kartenID] = kartenPos;
		umgedrehteKarten++;
		if (umgedrehteKarten == 1) {
			schummeln.setDisable(true);
		}
		if (umgedrehteKarten == 2) {
			paarPruefen(kartenID);
			timer = new Timeline(new KeyFrame(Duration.millis(2000), new TimerHandler()));
			timer.play();
		}
		if (computerPunkte + menschPunkte == 21) {
			timer.stop();
			gewinnerDialog();
		}
	}

	private void karteSchliessen() {
		boolean raus = false;
		if (paar[0].getBildID() == paar[1].getBildID()) {
			schummeln.setDisable(false);
			raus = true;
		}
		paar[0].rueckseiteZeigen(raus);
		paar[1].rueckseiteZeigen(raus);
		umgedrehteKarten = 0;
		if (raus == false)
			spielerWechseln();
		else if (spieler == 1)
			computerZug();
	}

	private void paarPruefen(int kartenID) {
		if (paar[0].getBildID() == paar[1].getBildID()) {
			paarGefunden();
			gemerkteKarten[0][kartenID] = -2;
			gemerkteKarten[1][kartenID] = -2;
		}
	}

	private void paarGefunden() {
		if (spieler == 0) {
			menschPunkte++;
			menschPunkteLabel.setText(Integer.toString(menschPunkte));
		} else {
			computerPunkte++;
			computerPunkteLabel.setText(Integer.toString(computerPunkte));
		}
	}

	private void spielerWechseln() {
		if (spieler == 0) {
			aktuellerSpieler.setText("Der Computer");
			schummeln.setDisable(true);
			spieler = 1;
			computerZug();
		} else {
			aktuellerSpieler.setText("Der Mensch");
			schummeln.setDisable(false);
			spieler = 0;
		}

	}

	private void computerZug() {
		int kartenZaehler = 0;
		int zufall = 0;
		boolean treffer = false;
		if ((int) (Math.random() * spielstaerke) == 0) {
			while ((kartenZaehler < 21) && (treffer == false)) {
				if ((gemerkteKarten[0][kartenZaehler] >= 0) && (gemerkteKarten[1][kartenZaehler] >= 0)) {
					treffer = true;
					karten[gemerkteKarten[0][kartenZaehler]].vorderseiteZeigen();
					karteOeffnen(karten[gemerkteKarten[0][kartenZaehler]]);
					karten[gemerkteKarten[1][kartenZaehler]].vorderseiteZeigen();
					karteOeffnen(karten[gemerkteKarten[1][kartenZaehler]]);
				}
				kartenZaehler++;
			}
		}
		if (treffer == false) {
			do {
				zufall = (int) (Math.random() * karten.length);
			} while (karten[zufall].isNochImSpiel() == false);
			karten[zufall].vorderseiteZeigen();
			karteOeffnen(karten[zufall]);

			do {
				zufall = (int) (Math.random() * karten.length);
			} while ((karten[zufall].isNochImSpiel() == false) || (karten[zufall].isUmgedreht() == true));
			karten[zufall].vorderseiteZeigen();
			karteOeffnen(karten[zufall]);
		}
	}

	public boolean zugErlaubt() {
		boolean erlaubt = true;
		if (spieler == 1)
			erlaubt = false;
		if (umgedrehteKarten == 2)
			erlaubt = false;
		return erlaubt;
	}

	private void gewinnerDialog() {
		Alert dialog = new Alert(AlertType.INFORMATION,
				"Ihre Punkte: " + menschPunkte + "\nComputer Punkte: " + computerPunkte, ButtonType.OK);
		dialog.setTitle("Spielende");
		if (menschPunkte > computerPunkte)
			dialog.setHeaderText("Sie haben Gewonnen");
		else
			dialog.setHeaderText("Der Computer hat Gewonnen");
		dialog.setOnHidden(new EventHandler<DialogEvent>() {
			@Override
			public void handle(DialogEvent arg0) {
				Platform.exit();
			}
		});
	}
}