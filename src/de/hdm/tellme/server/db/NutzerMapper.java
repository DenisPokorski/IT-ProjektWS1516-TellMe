package de.hdm.tellme.server.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import de.hdm.tellme.shared.bo.Hashtag;
import de.hdm.tellme.shared.bo.Nutzer;
import de.hdm.tellme.shared.bo.Nutzer.eStatus;

/**
 * Mapper-Klasse, die Nutzer-Objekte in der Datenbank abbildet. Diese enthält
 * Methoden zum Anlegen, Aktualisieren, Entfernen und Suchen von Objekten.
 * 
 * @author Nicole Reum
 */

public class NutzerMapper {
	private static NutzerMapper nutzerMapper = null;

	protected NutzerMapper() {

	}

	/**
	 * Die statische Methode wird über NutzerMapper nutzerMapper aufgerufen.
	 * Diese überprüft, dass nur eine Instanz von NutzerMapper besteht.
	 */

	public static NutzerMapper nutzerMapper() {
		if (nutzerMapper == null) {
			nutzerMapper = new NutzerMapper();
		}
		return nutzerMapper;
	}

	/**
	 * Die Methode anlegen stellt eine Verbindung zur Datenbank her. Dazu wird
	 * die Methode "connection()" aus der Klasse DatenbankVerbindung dem Objekt
	 * con übergeben. Im Anschluss wird im "try-Block" ein Statement erstellt.
	 * Nun legen wir einen neuen String an, der das SQL-Statement mit
	 * dynamischen Nutzerdaten beinhaltet. Jetzt wird über die Methode
	 * "state.executeUpdate(sqlquery);" ausgeführt und der SQL String an die
	 * Datenbank übergeben. Sollte der "try-Block" Fehler aufweisen, wird der
	 * "catch-Block" mit einer entsprechenden Fehlermeldung (Exception)
	 * ausgeführt.
	 */

	public int anlegen(Nutzer n) {
		int ergebnis = -1;
		Connection con = DatenbankVerbindung.connection();
		try {

			PreparedStatement prepState = con
					.prepareStatement(
							"INSERT INTO Nutzer (Vorname, Nachname, Mailadresse, Status) VALUES (?,?,?,?) ;",
							Statement.RETURN_GENERATED_KEYS);
			prepState.setString(1, n.getVorname());
			prepState.setString(2, n.getNachname());
			prepState.setString(3, n.getMailadresse());
			prepState.setInt(4, 1);
			prepState.executeUpdate();
			ResultSet rs = prepState.getGeneratedKeys();
			if (rs.next()) {
				ergebnis = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ergebnis;
	}

	public Nutzer suchenMitEmailAdresse(String mailAdresse) {
		Connection con = DatenbankVerbindung.connection();
		Nutzer n = new Nutzer();
		try {
			Statement state = con.createStatement();
			String sqlquery = "SELECT * FROM Nutzer WHERE Mailadresse = '"
					+ mailAdresse + "'";
			ResultSet rs = state.executeQuery(sqlquery);
			while (rs.next()) {
				n.setId(rs.getInt("Id"));
				n.setVorname(rs.getString("Vorname"));
				n.setNachname(rs.getString("Nachname"));
				n.setMailadresse(rs.getString("Mailadresse"));
				int status = rs.getInt("Status");
				n.setStatus(Nutzer.eStatus.values()[status]);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return n;
	}

	/**
	 * Die Methode aktualisieren stellt eine Verbindung zur Datenbank her. Dazu
	 * wird die Methode "connection()" aus der Klasse DatenbankVerbindung dem
	 * Objekt con übergeben. Im Anschluss wird im "try-Block" ein Statement
	 * erstellt. Nun legen wir einen neuen String an, der das SQL-Statement mit
	 * dynamischen Nutzerdaten beinhaltet. Jetzt wird über die Methode
	 * "state.executeUpdate(sqlquery);" ausgeführt und der SQL String an die
	 * Datenbank übergeben. Sollte der "try-Block" Fehler aufweisen, wird der
	 * "catch-Block" mit einer entsprechenden Fehlermeldung (Exception)
	 * ausgeführt.
	 */

	public void aktualisieren(Nutzer n) {
		Connection con = DatenbankVerbindung.connection();
		try {
			Statement state = con.createStatement();

			String sqlquery = "UPDATE Nutzer SET Vorname = '" + n.getVorname()
					+ "', Nachname= '" + n.getNachname() + "', "
					+ "Mailadresse= '" + n.getMailadresse() + "' "
					+ "WHERE Id = '" + n.getId() + "';";
			state.executeUpdate(sqlquery);

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	/**
	 * Die Methode entfernen stellt eine Verbindung zur Datenbank her. Dazu wird
	 * die Methode "connection()" aus der Klasse DatenbankVerbindung dem Objekt
	 * con übergeben. Im Anschluss wird im "try-Block" ein Statement erstellt.
	 * Nun legen wir einen neuen String an, der das SQL-Statement mit
	 * dynamischen Nutzerdaten beinhaltet (Id). Jetzt wird über die Methode
	 * "state.executeUpdate(sqlquery);" ausgeführt und der SQL String an die
	 * Datenbank übergeben. Sollte der "try-Block" Fehler aufweisen, wird der
	 * "catch-Block" mit einer entsprechenden Fehlermeldung (Exception)
	 * ausgeführt.
	 */

	public void entfernen(Nutzer n) {
		Connection con = DatenbankVerbindung.connection();
		try {
			Statement state = con.createStatement();
			state.executeUpdate("UPDATE Nutzer SET Status = '"
					+ eStatus.inaktiv.ordinal() + "' WHERE Id = '" + n.getId()
					+ "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Die Methode suchenNutzerIdMitMailadresse stellt eine Verbindung zur
	 * Datenbank her. Dazu wird die Methode "connection()" aus der Klasse
	 * DatenbankVerbindung dem Objekt con übergeben. Anschließend wird ein neues
	 * Objekt von dem Typ Nutzer erstellt (na). Im Anschluss wird im "try-Block"
	 * ein Statement erstellt. Nun legen wir über ResultSet fest, dass der
	 * Nutzer über die Mailadresse gesucht werden soll. Danach wird für jeder
	 * Eintrag in das Objekt na geschrieben. Jetzt wird über die Methode
	 * "state.executeUpdate(sqlquery);" ausgeführt und der SQL String an die
	 * Datenbank übergeben. Sollte der "try-Block" Fehler aufweisen, wird der
	 * "catch-Block" mit einer entsprechenden Fehlermeldung (Exception)
	 * ausgeführt. Über "return" wird der Nutzer mit allen Attributen
	 * ausgegeben.
	 */
	/**
	 * TODO
	 * 
	 * @param meineid
	 * @return
	 */
	public Vector<Nutzer> alleNutzerAusserMeineId(int meineid) {

		Vector<Nutzer> alleNutzerAusserMeinNutzerListe = new Vector<Nutzer>();

		Connection con = DatenbankVerbindung.connection();
		try {
			Statement state = con.createStatement();
			ResultSet rs = state
					.executeQuery("SELECT * From Nutzer WHERE Status = '"
							+ eStatus.aktiv.ordinal() + "'");

			while (rs.next()) {

				if ((rs.getInt("Id")) == meineid) {
				} else {
					Nutzer n = new Nutzer();
					n.setId(rs.getInt("Id"));
					n.setVorname(rs.getString("Vorname"));
					n.setNachname(rs.getString("Nachname"));
					n.setMailadresse(rs.getString("Mailadresse"));
					alleNutzerAusserMeinNutzerListe.addElement(n);

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// Ergebnisvektor zur�ckgeben
		return alleNutzerAusserMeinNutzerListe;
	}

	/**
	 * TODO
	 * 
	 * @param meineid
	 * @return
	 */
	public Vector<Nutzer> alleNutzer(int meineid) {

		Vector<Nutzer> alleNutzerListe = new Vector<Nutzer>();

		Connection con = DatenbankVerbindung.connection();
		try {
			Statement state = con.createStatement();
			ResultSet rs = state
					.executeQuery("SELECT * From Nutzer WHERE Status = '"
							+ eStatus.aktiv.ordinal() + "'");

			while (rs.next()) {

				Nutzer n = new Nutzer();
				n.setId(rs.getInt("Id"));
				n.setVorname(rs.getString("Vorname"));
				n.setNachname(rs.getString("Nachname"));
				n.setMailadresse(rs.getString("Mailadresse"));
				alleNutzerListe.addElement(n);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// Ergebnisvektor zur�ckgeben
		return alleNutzerListe;
	}

	/**
	 * TODO
	 * 
	 * @param meineId
	 * @return
	 */
	public Vector<Integer> alleAbonniertenNutzer(int meineId) {
		Vector<Integer> alleAbonniertenNutzer = new Vector<Integer>();
		Connection con = DatenbankVerbindung.connection();
		try {
			Statement state = con.createStatement();
			ResultSet rs = state
					.executeQuery("SELECT * FROM AbonnentBenutzer JOIN Nutzer ON AbonnentBenutzer.VonId = Nutzer.Id WHERE VonId = '"
							+ meineId
							+ "' AND Nutzer.Status = '"
							+ eStatus.aktiv.ordinal() + "';");
			while (rs.next()) {
				alleAbonniertenNutzer.add(rs.getInt("NachId"));

			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return alleAbonniertenNutzer;
	}

	/**
	 * TODO
	 * 
	 * @param hashtag
	 */
	public void alleHashtagZuordnungLoeschen(Hashtag hashtag) {
		Connection con = DatenbankVerbindung.connection();
		try {
			Statement state = con.createStatement();
			state.execute("DELETE FROM NutzerHashtag WHERE HashtagId = '"
					+ hashtag.getId() + "';");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * TODO
	 * 
	 * @param id
	 */
	public void setzeNutzerAktiv(int id) {
		Connection con = DatenbankVerbindung.connection();
		try {
			Statement state = con.createStatement();

			state.executeUpdate("UPDATE Nutzer SET Status = '"
					+ eStatus.aktiv.ordinal() + "' WHERE Id = '" + id + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}