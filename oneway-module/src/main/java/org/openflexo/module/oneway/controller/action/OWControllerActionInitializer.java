/**
 * 
 * Copyright (c) 2014, Openflexo
 * 
 * This file is part of Oneway prototype, a component of the software infrastructure 
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

import org.openflexo.module.oneway.controller.OWController;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.view.controller.ControllerActionInitializer;

/**
 * @author sylvain
 */
public class OWControllerActionInitializer extends ControllerActionInitializer {

	public OWControllerActionInitializer(OWController controller) {
		super(controller);
	}

	@Override
	public void initializeActions() {

		super.initializeActions();

		this.getController().getModuleInspectorController().loadDirectory(ResourceLocator.locateResource("Inspectors/OW"),
				getController().getModuleLocales());

		new GivesOnewayNatureInitializer(this);

		new CreateNewElementInitializer(this);
		new CreateNewRequirementInitializer(this);

		new ImportDocXDocumentInitializer(this);

		new IdentifyElementInitializer(this);
		new IdentifyRequirementInitializer(this);
		new IdentifyTextFragmentInitializer(this);

		new ImportBPMNInitializer(this);

		new CreateAtomicPropositionInitializer(this);
		new CreatePropertyInitializer(this);
	}
}
