package de.hdm.tellme.client.gui.editor;

import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;

import de.hdm.tellme.client.TellMe;
import de.hdm.tellme.shared.EditorService;
import de.hdm.tellme.shared.EditorServiceAsync;
import de.hdm.tellme.shared.bo.BusinessObject.eSichtbarkeit;
import de.hdm.tellme.shared.bo.Hashtag;
import de.hdm.tellme.shared.bo.Nachricht;
import de.hdm.tellme.shared.bo.Nutzer;
import de.hdm.tellme.shared.bo.Unterhaltung;

public class NeuigkeitenNachrichtDialogbox {

	public enum eNachrichtenmodus {
		NeueNachricht, AntwortNachricht, BearbeitenNachricht;
	}

	final DialogBox db = new DialogBox();
	TextArea textArea = new TextArea();
	private final EditorServiceAsync asyncObj = GWT.create(EditorService.class);
	eNachrichtenmodus NachrichtenModus = null;

	// Ausgelagert, da sie durch einen async call gefüllt werden
	final MultiWordSuggestOracle suggestOracleEmpfaenger = new MultiWordSuggestOracle();
	Vector<Nutzer> moeglicheEmpfaenger = new Vector<Nutzer>();
	Vector<Nutzer> AusgewaehlteEmpfaenger = new Vector<Nutzer>();

	final MultiWordSuggestOracle suggestOracleHashtags = new MultiWordSuggestOracle();
	Vector<Hashtag> moeglicheHashtags = new Vector<Hashtag>();
	Vector<Hashtag> AusgewaehlteHashtags = new Vector<Hashtag>();

	// Panel für Nachricht Empfänger Dialog Box
	final FlowPanel fpAusgewaehlteEmpfanger = new FlowPanel();
	HorizontalPanel hpEmpfaenger = new HorizontalPanel();

	// Panel für Nachricht Hashtag Dialog Box
	HorizontalPanel hpHashtags = new HorizontalPanel();
	final FlowPanel fpAusgewaehlteHashtags = new FlowPanel();

	// Elemente, die von jedem Nachrichtemodus unterschiedlich befüllt werden
	private String boxTitel;
	private String textAreaInhalt = "";
	private String textFunktionsbutton = "";

	private Unterhaltung antwortUnterhaltung;
	private Nachricht originalNachricht;

	public DialogBox getNeueNachrichtDialogbox() {
		NachrichtenModus = eNachrichtenmodus.NeueNachricht;
		boxTitel = "Neue Nachricht verfassen";
		textFunktionsbutton = "Senden";

		return gibDialogBox();
	}

	public DialogBox getAntwortNachrichtDialogbox(Unterhaltung u) {
		// TODO: Unterhaltung, der die Nachricht angehängt werden soll. Noch
		// Implementeiren
		NachrichtenModus = eNachrichtenmodus.AntwortNachricht;
		boxTitel = "Antwort verfassen";
		textFunktionsbutton = "Senden";

		antwortUnterhaltung = u;
		
		return gibDialogBox();
	}

	public DialogBox getNachrichtBearbeitenDialogbox(Nachricht _nachrichtZumBearbeiten) {
		NachrichtenModus = eNachrichtenmodus.BearbeitenNachricht;
		boxTitel = "Nachricht bearbeiten";
		textFunktionsbutton = "Speichern";

		originalNachricht = _nachrichtZumBearbeiten;
		textAreaInhalt = _nachrichtZumBearbeiten.getText();

		// Fuelle HashtagPanel
		for (Hashtag zuHinzuzufuegenderHashtag : _nachrichtZumBearbeiten.getVerknuepfteHashtags()) {
			AusgewaehlteHashtags.addElement(zuHinzuzufuegenderHashtag);
			final Button btnLoescheHashtag = new Button("#" + zuHinzuzufuegenderHashtag.getSchlagwort() + "(X)");
			btnLoescheHashtag.setStylePrimaryName("ButtonX-Nachrichtenverwaltung");

			btnLoescheHashtag.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					for (Hashtag hashtag : AusgewaehlteHashtags) {
						if (btnLoescheHashtag.getText().equals("#" + hashtag.getSchlagwort() + "(X)")) {
							AusgewaehlteHashtags.remove(hashtag);
							btnLoescheHashtag.removeFromParent();
							continue;
						}
					}
				}
			});

			fpAusgewaehlteHashtags.add(btnLoescheHashtag);

		}

		return gibDialogBox();
	}

	private DialogBox gibDialogBox() {
		ladVorschlagListen();

		// Dialog und FlowPanel definition
		db.setText(boxTitel);
		db.setAnimationEnabled(true);
		db.setGlassEnabled(true);
		

		FlowPanel fpDialog = new FlowPanel();
		// fpDialog.setHeight("500px");
		// fpDialog.setWidth("500px");
		
		//db.setWidth("100%");

		fpDialog.setWidth("100%");
		//fpDialog.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		Image btnAbbrechen = new Image("xbtn.png");
		btnAbbrechen.setStylePrimaryName("xbtn");
		btnAbbrechen.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				db.hide();
			}
		});
		fpDialog.add(btnAbbrechen);

		// ############################################## Empfaenger Panele
		// ##############################################
		if (NachrichtenModus == eNachrichtenmodus.NeueNachricht) {


			final SuggestBox EmpfaengerHinzufuegenSug = new SuggestBox(suggestOracleEmpfaenger);
			hpEmpfaenger.add(EmpfaengerHinzufuegenSug);

			final Button btnEmpfaengerHinzufuegen = new Button("+ Empfänger");
			btnEmpfaengerHinzufuegen.setStylePrimaryName("EmpfaengerPlusBtn");

			hpEmpfaenger.add(btnEmpfaengerHinzufuegen);

			btnEmpfaengerHinzufuegen.addClickHandler(new ClickHandler() {

				public void onClick(ClickEvent event) {
					Nutzer zuHinzuzufuegenderBenutzer = null;
					boolean bereitsHinzugefuegt = false;

					// Überprüfe, ob Text in Suggestionbox mit Text (Vorname
					// Nachname (Email)) eines Vorhandenen Benutzers
					// übereinstimmt. Falls ja setzte speichere Nutzerobjekt in
					// zuHinzuzufuegenderBenutzer
					for (Nutzer einzelnerNutzer : moeglicheEmpfaenger) {
						if (EmpfaengerHinzufuegenSug.getText().equals(gibVorschlageTextFuerNutzer(einzelnerNutzer))) {
							zuHinzuzufuegenderBenutzer = einzelnerNutzer;
							continue;
						}
					}

					// überprüfe, ob Empfänger schon vorhanden
					for (Nutzer nutzer : AusgewaehlteEmpfaenger) {
						if (zuHinzuzufuegenderBenutzer == nutzer)
							bereitsHinzugefuegt = true;

					}

					// Wenn Text Valide ist, füge einen Button zum Entfernen
					// hinzu.
					// Ansonsten Fehlermeldung ausgeben
					if (bereitsHinzugefuegt) {
						Window.alert("Der Empfänger kann nicht hinzugefügt werden, da dieser bereits hinzugefügt wurde.");

					} else if (zuHinzuzufuegenderBenutzer == null) {
						Window.alert("Kein vorhandener Benutzer ausgewaehlt.");
					} else {
						AusgewaehlteEmpfaenger.addElement(zuHinzuzufuegenderBenutzer);
						final Button btnLoescheEmpfaenger = new Button(gibVorschlageTextFuerNutzer(zuHinzuzufuegenderBenutzer) + "(X)");
						btnLoescheEmpfaenger.setStylePrimaryName("ButtonX-Nachrichtenverwaltung");


						btnLoescheEmpfaenger.addClickHandler(new ClickHandler() {
							public void onClick(ClickEvent event) {
								// Wenn ein Benutzer in der
								// AusgewaehlteEmpfaenger
								// Liste mit Vorschlagtext des Entfernenbutton,
								// entferne button von Panel und Nutzer von
								// Liste
								// der Ausgewählten Empfänger
								for (Nutzer nutzer : AusgewaehlteEmpfaenger) {
									if (btnLoescheEmpfaenger.getText().equals(gibVorschlageTextFuerNutzer(nutzer) + "(X)")) {
										AusgewaehlteEmpfaenger.remove(nutzer);
										btnLoescheEmpfaenger.removeFromParent();
										continue;
									}
								}

							}
						});

						fpAusgewaehlteEmpfanger.add(btnLoescheEmpfaenger);
					}

					EmpfaengerHinzufuegenSug.setText("");

				}
			});
			fpDialog.add(hpEmpfaenger);
			fpDialog.add(fpAusgewaehlteEmpfanger);
		}

		// ############################################## Textarea
		// ##############################################
		textArea.setWidth("98.4%");
		textArea.setVisibleLines(10);
		textArea.setText(textAreaInhalt);
		textArea.setStylePrimaryName("dialogtextArea");
		textArea.getElement().setAttribute("maxlength", "255");

		// maxlength="50"
		fpDialog.add(textArea);

		// ############################################## Hashtag Panele
		// ##############################################
		final SuggestBox HastagHinzufuegenSug = new SuggestBox(suggestOracleHashtags);
		hpHashtags.add(HastagHinzufuegenSug);

		final Button btnHashtagHinzufuegen = new Button("+ Hashtag");
		hpHashtags.add(btnHashtagHinzufuegen);

		btnHashtagHinzufuegen.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				Hashtag zuHinzuzufuegenderHashtag = null;
				boolean bereitsHinzugefuegt = false;
				// Überprüfe, ob Text in Suggestionbox mit Text eines
				// Vorhandenen Hashtags
				// übereinstimmt. Falls ja setzte speichere Hastag in
				// zuHinzuzufuegenderHashtag
				for (Hashtag hashtag : moeglicheHashtags) {
					if (HastagHinzufuegenSug.getText().equals("#" + hashtag.getSchlagwort())) {
						zuHinzuzufuegenderHashtag = hashtag;
						continue;
					}
				}

				// überprüfe, ob Hashtag schon vorhanden
				for (Hashtag hashtag2 : AusgewaehlteHashtags) {
					if (zuHinzuzufuegenderHashtag == hashtag2)
						bereitsHinzugefuegt = true;
				}

				// Wenn Text Valide ist, füge einen Button zum Entfernen hinzu.
				// Ansonsten Fehlermeldung ausgeben
				if (bereitsHinzugefuegt) {
					Window.alert("Der Hashtag kann nicht hinzugefügt werden, da dieser bereits hinzugefügt wurde.");
				} else if (zuHinzuzufuegenderHashtag == null) {
					Window.alert("Kein vorhandener Hashtag ausgewaehlt.");
				} else {
					AusgewaehlteHashtags.addElement(zuHinzuzufuegenderHashtag);
					final Button btnLoescheHashtag = new Button("#" + zuHinzuzufuegenderHashtag.getSchlagwort() + "(X)");
					btnLoescheHashtag.setStylePrimaryName("ButtonX-Hashtagverwaltung");


					btnLoescheHashtag.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							// Wenn ein Hashtag in der AusgewaehlteHashtag
							// Liste mit Text des Entfernenbutton,
							// entferne button von Panel und Hashtag von Liste
							// der Ausgewählten Hastags
							for (Hashtag hashtag : AusgewaehlteHashtags) {
								if (btnLoescheHashtag.getText().equals("#" + hashtag.getSchlagwort() + "(X)")) {
									AusgewaehlteHashtags.remove(hashtag);
									btnLoescheHashtag.removeFromParent();
									continue;
								}
							}
						}
					});

					fpAusgewaehlteHashtags.add(btnLoescheHashtag);
				}
				HastagHinzufuegenSug.setText("");
			}
		});
		fpDialog.add(hpHashtags);
		fpDialog.add(fpAusgewaehlteHashtags);

		HorizontalPanel hpButtons = new HorizontalPanel();
		hpButtons.setWidth("100%");
		hpButtons.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

		Button btnFunktionsbutton = new Button(textFunktionsbutton);
		btnHashtagHinzufuegen.setStylePrimaryName("HashtagPlusBtn");
		
		btnFunktionsbutton.setStylePrimaryName("NeuigkeitenDialogboxSendenBtn");
		btnFunktionsbutton.addClickHandler(new ClickHandler() {
			
			
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				switch (NachrichtenModus) {
				case NeueNachricht:
				default:
					if (textArea.getValue() == "") {
						Window.alert("Bitte geben Sie einen Text ein um die Nachricht versenden zu können.");

					} else {
						Nachricht neueNachricht = new Nachricht();
						// neueNachricht.setErstellungsDatum(erstellungsDatum);
						// wird
						// im
						// Mapper direkt gesetzt
						// neueNachricht.setId(id); wird für anlegen nicht
						// benötigt
						neueNachricht.setSender(TellMe.gibEingeloggterBenutzer().getUser());
						neueNachricht.setSenderId(TellMe.gibEingeloggterBenutzer().getUser().getId());
						neueNachricht.setSichtbarkeit(eSichtbarkeit.Sichtbar.ordinal());

						neueNachricht.setText(textArea.getText());
						neueNachricht.setVerknuepfteHashtags(AusgewaehlteHashtags);

						// Füge eigene ID als Teilnehmer hinzu
						AusgewaehlteEmpfaenger.addElement(TellMe.gibEingeloggterBenutzer().getUser());
						
						
						

						asyncObj.unterhaltungStarten(neueNachricht, AusgewaehlteEmpfaenger, new AsyncCallback<Boolean>() {

							@Override
							public void onSuccess(Boolean result) {
								// TODO Auto-generated method stub
								if (result)
									Window.alert("Nachricht erfolgreich erstellt");
								else
									Window.alert("Fehler beim erstellen der Nachricht, bitte an Administrator wenden.");
							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								Window.alert("Fehler beim erstellen der Nachricht, bitte an Administrator wenden.");
							}
						});
					}
					break;

				case AntwortNachricht:
					if (textArea.getValue() == "") {
						Window.alert("Bitte geben Sie einen Text ein um die Unterhaltung zu beantworten.");
					} else {
						Nachricht antwortNachricht = new Nachricht();
						antwortNachricht.setSender(TellMe.gibEingeloggterBenutzer().getUser());
						antwortNachricht.setSichtbarkeit(1);
						antwortNachricht.setText(textArea.getValue());
						antwortNachricht.setVerknuepfteHashtags(AusgewaehlteHashtags);
						
						asyncObj.unterhaltungBeantworten(antwortNachricht, antwortUnterhaltung, new AsyncCallback<Boolean>() {
							
							@Override
							public void onSuccess(Boolean result) {
								if (result)
									Window.alert("Nachricht erfolgreich gesendet.");
								else
									Window.alert("Fehler beim erstellen der Nachricht, bitte an Administrator wenden.");
								
							}
							
							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Fehler beim erstellen der Nachricht, bitte an Administrator wenden.");
								
							}
						});
						
						//TODO: do somthng
					}
					break;
				case BearbeitenNachricht:
					if (textArea.getValue() == "") {
						Window.alert("Bitte geben Sie einen Text ein um die Nachricht speichern zu können.");
					} else {

						Nachricht bearbeiteteNachricht = new Nachricht();
						bearbeiteteNachricht.setErstellungsDatum(originalNachricht.getErstellungsDatum());
						bearbeiteteNachricht.setId(originalNachricht.getId());
						bearbeiteteNachricht.setSender(originalNachricht.getSender());
						bearbeiteteNachricht.setSichtbarkeit(originalNachricht.getSichtbarkeit());
						bearbeiteteNachricht.setText(textArea.getValue());
						bearbeiteteNachricht.setVerknuepfteHashtags(AusgewaehlteHashtags);
						asyncObj.NachrichtAktualisieren(originalNachricht, bearbeiteteNachricht, new AsyncCallback<Boolean>() {

							@Override
							public void onSuccess(Boolean result) {
								if (result)
									Window.alert("Nachricht erfolgreich aktualisiert");
								else
									Window.alert("Fehler beim bearbeiten der Nachricht, bitte an Administrator wenden.");

							}

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("Fehler beim bearbeiten der Nachricht, bitte an Administrator wenden.");

							}
						});
					}
					break;
				}
				db.hide();

			}
		});

		
		hpButtons.add(btnFunktionsbutton);
		//hpButtons.add(btnAbbrechen);
		fpDialog.add(hpButtons);

		db.setWidget(fpDialog);

		return db;
	}

	private void ladVorschlagListen() {
		asyncObj.getAlleNutzerAußerMeineId(TellMe.eingeloggterBenutzer.getUser().getId(), new AsyncCallback<Vector<Nutzer>>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Vector<Nutzer> resultListe) {
				moeglicheEmpfaenger = resultListe;
				for (Nutzer einzelnerUser : moeglicheEmpfaenger) {
					suggestOracleEmpfaenger.add(gibVorschlageTextFuerNutzer(einzelnerUser));
				}

			}
		});

		asyncObj.gibAlleHashtags(new AsyncCallback<Vector<Hashtag>>() {
			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Vector<Hashtag> resultListe) {
				moeglicheHashtags = resultListe;
				for (Hashtag hashtag : moeglicheHashtags) {
					suggestOracleHashtags.add("#" + hashtag.getSchlagwort());
				}
			}
		});
	}

	private String gibVorschlageTextFuerNutzer(Nutzer _nutzer) {
		return _nutzer.getVorname() + " " + _nutzer.getNachname() + " (" + _nutzer.getMailadresse() + ")";
	}

}