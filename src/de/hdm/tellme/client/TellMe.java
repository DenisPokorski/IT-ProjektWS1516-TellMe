package de.hdm.tellme.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import de.hdm.tellme.client.Schaukasten.NeuigkeitenEditor;
import de.hdm.tellme.client.Schaukasten.NutzerBearbeitenEditor;
import de.hdm.tellme.client.gui.editor.MenuBarEditor;
import de.hdm.tellme.shared.LoginInfo;
import de.hdm.tellme.shared.LoginService;
import de.hdm.tellme.shared.LoginServiceAsync;

/**
 * Das ist der Eingangspunkt-Klasse des Projekts <b>TellMe</b> In dieser Klasse
 * wird der Login und die MenuBar erstellt.
 * 
 * @author denispokorski
 *
 */
public class TellMe implements EntryPoint {

	/**
	 * Die Klasse implementiert das Interface <code> EntryPoint</code>. Dafür
	 * wird die Methode <code> pubic void onModuleLoad()</code>. Das ist das
	 * GWT-Gegenstück der <code>main()</code> einer normalen Java-Applikation
	 * 
	 *
	 */
	public TellMe() {

	}

	/**
	 * Damit die Klasse LoginInfo nur einmal während der Laufzeit des Programms
	 * bestehen kann, muss man sie als Singleton darstellen, dies geschieht
	 * durch die Referenz <code>static</code>.
	 */

	private static LoginInfo eingeloggterBenutzer = null;
	/**
	 * LoginService wird erstellt.
	 */
	LoginServiceAsync loginService = GWT.create(LoginService.class);
	

	MenuBarEditor menuBar = new MenuBarEditor();

	public static LoginInfo gibEingeloggterBenutzer() {
		return eingeloggterBenutzer;
	}

	public Widget ladeTellMe() {

		if (eingeloggterBenutzer.getUser().getVorname() == "undefined" || eingeloggterBenutzer.getUser().getNachname() == "undefined") {

			// Startseite anzeigen
			NutzerBearbeitenEditor.NutzerRuftTellMeErstesMalAuf = true;
			MenuBarEditor.setzeInhalt(new NutzerBearbeitenEditor().gibNutzerBearbeitenFormular());

		} else {

			RootPanel.get("header").add(menuBar);
			// Startseite anzeigen

			NeuigkeitenEditor nE = new NeuigkeitenEditor();
			MenuBarEditor.setzeInhalt(nE.gibFilterPanel(), nE);
		}
		return null;
	}

	@Override
	public void onModuleLoad() {

		/**
		 * Der Login-Status wird durch den LoginService überprüft.
		 */

		loginService.getNutzerInfo(new AsyncCallback<LoginInfo>() {

			@Override
			public void onSuccess(LoginInfo result) {
				eingeloggterBenutzer = result;

				/**
				 * Wenn der Nutzer eingeloggt ist, wird dem Nutzer die
				 * TellMe-Applikation angezeigt.
				 */
				if (eingeloggterBenutzer.isLoggedIn()) {
					ladeTellMe();
				} else {
					Window.Location.assign(eingeloggterBenutzer.getLoginUrl());
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				/**
				 * TODO
				 */
			}
		});

	}

}