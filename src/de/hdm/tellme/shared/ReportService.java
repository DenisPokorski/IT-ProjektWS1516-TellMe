package de.hdm.tellme.shared;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.hdm.tellme.shared.bo.Hashtag;
import de.hdm.tellme.shared.bo.Nachricht;
import de.hdm.tellme.shared.bo.Nutzer;
/**
 * Synnchrone Schnittstelle für eine RPC-fähige Klasse zur Ausagbe des Reports.
 * 
 * TODO 
 * 
 *
 */

@RemoteServiceRelativePath("reportservice")
public interface ReportService extends RemoteService {


	Vector<Nutzer> report2GenerierenListe(int i);

	Vector<Hashtag> report3Generieren(int i);

	Vector<Nachricht> report1_3Generieren(int i);

	Vector<Nachricht> report1_2Generieren(Timestamp vonDatum, Timestamp bisDatum);

//	Vector<Nutzer> report1_1Generieren(int nutzerId, Date vD, Date bD);

	Vector<Nachricht> report1_4Generieren();

	Vector<Nachricht> report1_1Generieren(int nutzerId, Date vonDatum,
			Date bisDatum);


 
 
 
	 
}
