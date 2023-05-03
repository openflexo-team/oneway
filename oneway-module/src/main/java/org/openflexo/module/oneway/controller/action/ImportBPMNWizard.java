/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Freemodellingeditor, a component of the software infrastructure 
 * developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either 
 * version 1.1 of the License, or any later version ), which is available at 
 * https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 * and the GNU General Public License (GPL, either version 3 of the License, or any 
 * later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you
 * must include the following additional permission.
 *
 *          Additional permission under GNU GPL version 3 section 7
 *
 *          If you modify this Program, or any covered work, by linking or 
 *          combining it with software containing parts covered by the terms 
 *          of EPL 1.0, the licensors of this Program grant you additional permission
 *          to convey the resulting work. * 
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. 
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org)
 * or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.module.oneway.controller.action;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoActionWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.module.oneway.OWIconLibrary;
import org.openflexo.module.oneway.model.action.ImportBPMN;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class ImportBPMNWizard extends FlexoActionWizard<ImportBPMN> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(ImportBPMNWizard.class.getPackage().getName());

	private final ConfigureBPMNImport configureBPMNImport;

	private static final Dimension DIMENSIONS = new Dimension(600, 400);

	public ImportBPMNWizard(ImportBPMN action, FlexoController controller) {
		super(action, controller);
		addStep(configureBPMNImport = new ConfigureBPMNImport());
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSIONS;
	}

	@Override
	public String getWizardTitle() {
		return getAction().getLocales().localizedForKey("import_BPMN_file");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(OWIconLibrary.BPMN_BIG_ICON, IconLibrary.BIG_NEW_MARKER).getImage();
	}

	public ConfigureBPMNImport getConfigureBPMNImport() {
		return configureBPMNImport;
	}

	@FIBPanel("Fib/Wizard/ConfigureBPMNImport.fib")
	public class ConfigureBPMNImport extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public ImportBPMN getAction() {
			return ImportBPMNWizard.this.getAction();
		}

		public FlexoConceptInstance getElement() {
			return getAction().getFocusedObject();
		}

		public void setElement(FlexoConceptInstance element) {
			// read only
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("select_docx_file_to_import");
		}

		@Override
		public boolean isValid() {

			if (getBPMNFile() == null || !getBPMNFile().exists()) {
				setIssueMessage(getAction().getLocales().localizedForKey("you_must_choose_BPMN_file"), IssueMessageType.ERROR);
				return false;
			}
			if (StringUtils.isEmpty(getBPMNName())) {
				setIssueMessage(getAction().getLocales().localizedForKey("you_must_define_document_name"), IssueMessageType.ERROR);
				return false;
			}
			if (StringUtils.isEmpty(getBPMNDescription())) {
				setIssueMessage(getAction().getLocales().localizedForKey("it_is_recommanded_to_describe_document"),
						IssueMessageType.WARNING);
			}

			return true;

		}

		public String getBPMNName() {
			return getAction().getBPMNName();
		}

		public void setBPMNName(String bpmnName) {
			if (!bpmnName.equals(getBPMNName())) {
				String oldValue = getBPMNName();
				getAction().setBPMNName(bpmnName);
				getPropertyChangeSupport().firePropertyChange("BPMNName", oldValue, bpmnName);
				checkValidity();
			}
		}

		public File getBPMNFile() {
			return getAction().getBPMNFile();
		}

		public void setBPMNFile(File bpmnFile) {

			System.out.println("Hop ici, set bpmnFile=" + bpmnFile);

			if (!bpmnFile.equals(getBPMNFile())) {
				File oldValue = getBPMNFile();
				getAction().setBPMNFile(bpmnFile);
				getPropertyChangeSupport().firePropertyChange("BPMNFile", oldValue, bpmnFile);
				getPropertyChangeSupport().firePropertyChange("BPMNName", null, getBPMNName());
				checkValidity();
			}
		}

		public String getBPMNDescription() {
			return getAction().getBPMNDescription();
		}

		public void setBPMNDescription(String newDescription) {
			if (!newDescription.equals(getBPMNDescription())) {
				String oldValue = getBPMNDescription();
				getAction().setBPMNDescription(newDescription);
				getPropertyChangeSupport().firePropertyChange("BPMNDescription", oldValue, newDescription);
				checkValidity();
			}
		}

	}

}
