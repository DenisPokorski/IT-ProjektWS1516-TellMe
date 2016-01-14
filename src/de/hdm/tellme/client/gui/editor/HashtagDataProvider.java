package de.hdm.tellme.client.gui.editor;

import java.util.List;
import java.util.Vector;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ListDataProvider;

import de.hdm.tellme.client.TellMe;
import de.hdm.tellme.shared.EditorService;
import de.hdm.tellme.shared.EditorServiceAsync;
import de.hdm.tellme.shared.bo.Hashtag;

/**
 * 
 * Die Klasse <class>HashtagDataProvider</class> ist nötig, dass die Daten die
 * für ein Hashtag benötigt wird.
 * 
 * @author Alex
 *
 */
public class HashtagDataProvider {

	private final EditorServiceAsync _asyncObj = GWT
			.create(EditorService.class);

	private ListDataProvider<HashtagZelle.ZellenObjekt> dataProvider = new ListDataProvider<HashtagZelle.ZellenObjekt>();

	private static Vector<Hashtag> hashTagListeTemp = null;
	private List<HashtagZelle.ZellenObjekt> dataList = dataProvider.getList();

	private static HashtagDataProvider instanz = null;

	public static HashtagDataProvider gib() {
		if (instanz == null)
			instanz = new HashtagDataProvider();
		return instanz;
	}

	private HashtagDataProvider() {
		holeHashtagListe();

	}

	/**
	 * Die Methode holeHashtagListe() wird benötigt um eine bestimmte Selektion
	 * von Hashtag-Objekten zu erhalten.
	 * 
	 * TODO richtig?
	 */
	private void holeHashtagListe() {

		if (dataList != null) {
			dataList.clear();
		}
		dataList = dataProvider.getList();

		_asyncObj.gibAlleHashtags(new AsyncCallback<Vector<Hashtag>>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO
			}

			@Override
			public void onSuccess(Vector<Hashtag> hashTagListe) {

				hashTagListeTemp = hashTagListe;
				int NutzerId = TellMe.eingeloggterBenutzer.getUser().getId();

				_asyncObj.getAlleAbonniertenHashtagsfuerAbonehmer(NutzerId,
						new AsyncCallback<Vector<Integer>>() {

							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(Vector<Integer> aboListe) {
								for (int i = 0; i <= hashTagListeTemp.size(); i++) {
									HashtagZelle.ZellenObjekt nah = new HashtagZelle().new ZellenObjekt();
									nah.hashtag = hashTagListeTemp.get(i);
									nah.aboniert = false;

									if (aboListe.contains(nah.hashtag.getId()))
										nah.aboniert = true;
									dataList.add(nah);
								}

							}

						});
			}
		});

	}

	/**
	 * Die statische Methode wird über Vector<Hashtag> gebeHashtagListe()
	 * aufgerufen. Sie überprüft, dass nur eine Instanz von gebeHashtagListe
	 * besteht.
	 */
	public static Vector<Hashtag> gebeHashtagListe() {
		return hashTagListeTemp;
	}

	/**
	 * TODO
	 * 
	 * @param cellList
	 */
	public void addDataDisplay(CellList<HashtagZelle.ZellenObjekt> cellList) {
		dataProvider.addDataDisplay(cellList);
	}

	/**
	 * TODO
	 */
	public void refreshDisplays() {
		dataProvider.refresh();
	}

	/**
	 * Diese Methode sorgt dafür, dass Hashtag gespeichert bzw. aktualisiert
	 * wird. Hierfür wird ein RPC durchgeführt.
	 * 
	 * @param hashtag
	 */
	public void hashtagSpeichern(Hashtag hashtag) {
		/**
		 * Hier wird die RPC-Methode ausgeführt, die das Hashtag-Objekt in der
		 * Datenbank aktualisiert.
		 */
		_asyncObj.hashtagAktualisieren(hashtag, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fehler, bitte wenden Sie sich an den Systemadministrator");
			}

			/**
			 * Nach erfolgreichem RPC wird das folgende Code-Fragment
			 * ausgeführt.
			 */

			@Override
			public void onSuccess(Void resultListe) {
				holeHashtagListe();
				RootPanel.get("content_right").clear();
				Window.alert("Der Hashtag wurde erfolgreich angelegt.");

			}
		});

	}

	/**
	 * Diese Methode sorgt dafür, dass ein Hashtag entfernt wird. Hierfür wird
	 * ein RPC durchgeführt.
	 * 
	 * @param hashtag
	 */
	public void hashtagEntfernen(Hashtag hashtag) {
		/**
		 * Hier wird die RPC-Methode ausgeführt, die das Hashtag-Objekt aus der
		 * Datenbank entfernt.
		 */
		_asyncObj.hashtagEntfernen(hashtag, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fehler, bitte wenden Sie sich an den Systemadministrator");
			}

			/**
			 * Nach erfolgreichem RPC wird das folgende Code-Fragment
			 * ausgeführt.
			 */

			@Override
			public void onSuccess(Void resultListe) {
				holeHashtagListe();
				RootPanel.get("content_right").clear();
				Window.alert("Der Hashtag wurde erfolgreich entfernt.");

			}
		});

	}

	/**
	 * Diese Methode sorgt dafür, dass ein Hashtag erstellt wird. Hierfür wird
	 * ein RPC durchgeführt.
	 * 
	 * @param hashtag
	 */
	public void hashtagErstellen(Hashtag hashtag) {

		/**
		 * Hier wird die RPC-Methode ausgeführt, die das neue Hashtag-Objekt in
		 * der Datenbank erstellt.
		 */
		_asyncObj.hashtagErstellen(hashtag, new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fehler, bitte wenden Sie sich an den Systemadministrator");
			}

			/**
			 * Nach erfolgreichem RPC wird das folgende Code-Fragment
			 * ausgeführt.
			 */
			@Override
			public void onSuccess(Void resultListe) {
				holeHashtagListe();
				RootPanel.get("content_right").clear();
				Window.alert("Der Hashtag wurde erfolgreich erstellt.");
				RootPanel.get("content_right").add(
						new HashtagFormular().gibBeschreibungHtVerwaltung());
				RootPanel.get("content_right").add(
						new HashtagFormular().gibAnlegenFormular());

			}
		});

	}

	/**
	 * Diese Methode sorgt dafür, dass ein Hashtag von dem eingeloggten Nutzer
	 * abonniert wird. Hierfür wird ein RPC durchgeführt.
	 * 
	 * @param hashtag
	 */
	public void abonieren(Hashtag hashtag) {

		int NutzerId = TellMe.eingeloggterBenutzer.getUser().getId();
		int HashtagId = hashtag.getId();

		/**
		 * Hier wird die RPC-Methode ausgeführt, die das neue
		 * HashtagAbonnement-Objekt in der Datenbank erstellt.
		 */
		_asyncObj.hashtagAboErstellen(NutzerId, HashtagId,
				new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Fehler, bitte wenden Sie sich an den Systemadministrator");
					}

					/**
					 * Nach erfolgreichem RPC wird das folgende Code-Fragment
					 * ausgeführt.
					 */
					@Override
					public void onSuccess(Void resultListe) {
						holeHashtagListe();
						RootPanel.get("content_right").clear();
						RootPanel.get("content_right").add(
								new HashtagFormular().gibBeschreibungHtAbo());
						Window.alert("Das Abo wurde erfolgreich erstellt.");

					}
				});

	}

	/**
	 * Diese Methode sorgt dafür, dass ein Hashtagabonnement von dem
	 * eingeloggten Nutzer gelöscht wird. Hierfür wird ein RPC durchgeführt.
	 * 
	 * @param hashtag
	 */
	public void deabonieren(Hashtag hashtag) {

		int NutzerId = TellMe.eingeloggterBenutzer.getUser().getId();
		int HashtagId = hashtag.getId();

		/**
		 * Hier wird die RPC-Methode ausgeführt, die das neue
		 * Hashtagabonnement-Objekt aus der Datenbank löscht.
		 */
		_asyncObj.hashtagAboEntfernen(NutzerId, HashtagId,
				new AsyncCallback<Void>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert("Fehler, bitte wenden Sie sich an den Systemadministrator");
					}

					/**
					 * Nach erfolgreichem RPC wird das folgende Code-Fragment
					 * ausgeführt.
					 */
					@Override
					public void onSuccess(Void resultListe) {
						holeHashtagListe();
						RootPanel.get("content_right").clear();
						RootPanel.get("content_right").add(
								new HashtagFormular().gibBeschreibungHtAbo());
						Window.alert("Das Abo wurde erfolgreich entfernt.");

					}
				});
	}

}
