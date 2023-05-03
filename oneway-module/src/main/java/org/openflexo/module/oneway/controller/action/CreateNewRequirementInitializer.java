/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Formose prototype, a component of the software infrastructure 
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

import javax.swing.Icon;

import org.openflexo.components.wizard.Wizard;
import org.openflexo.components.wizard.WizardDialog;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.FlexoActionRunnable;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.gina.controller.FIBController.Status;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.module.oneway.OWIconLibrary;
import org.openflexo.module.oneway.model.action.CreateNewRequirement;
import org.openflexo.view.controller.ActionInitializer;
import org.openflexo.view.controller.ControllerActionInitializer;

/**
 * @author sylvain
 */
public class CreateNewRequirementInitializer extends ActionInitializer<CreateNewRequirement, FlexoConceptInstance, FlexoObject> {

	public CreateNewRequirementInitializer(final ControllerActionInitializer controllerActionInitializer) {
		super(CreateNewRequirement.ACTION_TYPE, controllerActionInitializer);
	}

	@Override
	protected FlexoActionRunnable<CreateNewRequirement, FlexoConceptInstance, FlexoObject> getDefaultInitializer() {
		return (e, action) -> {
			Wizard wizard = new CreateNewRequirementWizard(action, getController());
			WizardDialog dialog = new WizardDialog(wizard, getController());
			dialog.showDialog();
			if (dialog.getStatus() != Status.VALIDATED) {
				// Operation cancelled
				return false;
			}
			return true;
		};
	}

	@Override
	protected FlexoActionRunnable<CreateNewRequirement, FlexoConceptInstance, FlexoObject> getDefaultFinalizer() {
		return (e, action) -> {
			// ((FMSController)
			// getController()).getFormosePerspective().getProjectBrowser().getFIBController().getPropertyChangeSupport()
			// .firePropertyChange("getChildren(FlexoConceptInstance)", false, true);

			// System.out.println("Selecting requirement " + action.getNewRequirement());

			if (action.getSelectAfterCreation()) {
				getController().getSelectionManager().setSelectedObject(action.getNewRequirement());
			}
			return true;
		};
	}

	@Override
	protected Icon getEnabledIcon(FlexoActionFactory actionType) {
		return IconFactory.getImageIcon(OWIconLibrary.REQUIREMENT_ICON, IconLibrary.NEW_MARKER);
	}

}
