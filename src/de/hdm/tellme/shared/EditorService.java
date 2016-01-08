package de.hdm.tellme.shared;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.tellme.shared.bo.Hashtag;
import de.hdm.tellme.shared.bo.Nachricht;
import de.hdm.tellme.shared.bo.Nutzer;
import de.hdm.tellme.shared.bo.Unterhaltung;

@RemoteServiceRelativePath("editorservice")
public interface EditorService extends RemoteService {

	void nutzerAktualisieren(Nutzer n);

	void nutzerLoeschen(Nutzer n);

	Vector<Nutzer> getAlleNutzerAußerMeineId(int meineId);

	Vector<Integer> getAlleAbonniertenHashtagsfuerAbonehmer(int meineId);

	void hashtagAboErstellen(int nutzerId, int hashtagId);

	void nutzerAbonnementErstellen(int i, Nutzer _nutzer);

	Vector<Integer> holeAlleAbonniertenNutzer(int meineId);

	void nutzerAbonnementLoeschen(int id, Nutzer _nutzerDeabonieren);

	void hashtagErstellen(Hashtag hashtag);

	void hashtagEntfernen(Hashtag hashtag);

	void hashtagAboEntfernen(int nutzerId, int hashtagId);


	boolean unterhaltung_loeschen(int unterhaltungsID);

	Vector<Unterhaltung> alleUnterhaltungenFuerAktivenTeilnehmerOhneNachrichten(int teilnehmerID);

	boolean unterhaltungStarten(Nachricht ersteNachricht, Vector<Nutzer> teilnehmer);


	Vector<Unterhaltung> meineUnterhaltungenMitSichtbarkeit(int meineId);

	Vector<Nachricht> ladeAlleNachrichtenZuUnterhaltung(int UnterhaltungsID);

	Vector<Unterhaltung> oeffentlicheUnterhaltungenAbonnierterNutzer(int meineId);

	Vector<Unterhaltung> alleUnterhaltungenVonAbonniertemHashtagUeberNutzerId(int nutzerId);

	boolean NachrichtLoeschen(Nachricht n);


	Vector<Unterhaltung> getAlleRelevantenUnterhaltungen(int UserID);

	Vector<Hashtag> gibAlleHashtags();

	void hashtagAktualisieren(Hashtag hashtag);

	void erstellenHashtagAbo(int NutzerId, int HashtagId);

	Vector<Hashtag> getAbonnierteHashtags(int aboNehmerID);

	Vector<Unterhaltung> getAlleSichtbarenUnterhaltungenFuerTeilnehmer(
			int aktiverTeilnehmerID);

	boolean NachrichtAktualisieren(Nachricht original, Nachricht neu);

	boolean unterhaltungBeantworten(Nachricht antwortNachricht, Unterhaltung unterhaltung);

	Vector<Nutzer> getAlleNutzer(boolean zwingeNeuladen);

	boolean UnterhaltungVerlassen(Unterhaltung u, int nutzerId);



}
