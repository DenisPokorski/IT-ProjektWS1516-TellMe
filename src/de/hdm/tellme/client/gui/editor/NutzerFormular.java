package de.hdm.tellme.client.gui.editor;

/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

import de.hdm.tellme.shared.bo.Nutzer;

/**
 * A form used for editing contacts.
 */

public class NutzerFormular extends Composite {
	private Nutzer nutzer = null;
	Button btnAbonieren = new Button("abonieren");
	Button btnDeabonieren = new Button("deabonieren");

	public NutzerFormular() {
		// Handle events.
		btnAbonieren.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (nutzer == null) {
					return;
				}
				NutzerDataProvider.gib().abonieren(nutzer);
			}
		});
		// Handle events.
		btnDeabonieren.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (nutzer == null) {
					return;
				}
				NutzerDataProvider.gib().deabonieren(nutzer);
			}
		});
	}

	public VerticalPanel gibFormular() {
		VerticalPanel vpForm = new VerticalPanel();

		HorizontalPanel hpForm = new HorizontalPanel();
		btnAbonieren.setStylePrimaryName("btnPositiv");
		btnDeabonieren.setStylePrimaryName("btnNegativ");

		
		Label selctionLabel = new Label("Ausgewählter Nutzer: "
				+ nutzer.getVorname());
		selctionLabel.setStylePrimaryName("selectionLabel");
		vpForm.add(selctionLabel);
		
		hpForm.add(btnAbonieren);
		hpForm.add(btnDeabonieren);

		vpForm.add(hpForm);

		return vpForm;
	}
	
	public VerticalPanel gibBeschreibung(){
		VerticalPanel vpForm = new VerticalPanel();

		HTML headline = new HTML(" <div class='" + "subline"
				+ "'><h2>Nuterabo(s) verwalten:</h2></div> ");
		vpForm.add(headline);

		HTML subtext = new HTML(
				" <div class='"
						+ "subtext"
						+ "'><h4>Hier können Sie andere Nutzer abonieren. Wenn Sie dies getan haben, werden alle öffentlichen und privaten nachrichten in den Sie als Empfänger asugewählt sind, in dem Menüpunkt Neuigkeiten angezeigt. <br /> Zudem können Sie bereits vorhandene abonnoments löschen. </h4></div> ");
		vpForm.add(subtext);

		
		return vpForm;
	}

	public void setzeNutzerAbo(NutzerZelle.ZellenObjekt ZellenObjekt) {
		this.nutzer = ZellenObjekt.nutzer;
		if (ZellenObjekt.aboniert) {
			btnAbonieren.setEnabled(false);
			btnDeabonieren.setEnabled(true);
		} else {
			btnAbonieren.setEnabled(true);
			btnDeabonieren.setEnabled(false);
		}

	}
}