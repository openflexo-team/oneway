/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.module.oneway;

import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.foundation.fml.FMLTechnologyAdapter;
import org.openflexo.foundation.fml.rm.VirtualModelResource;
import org.openflexo.foundation.fml.rt.FMLRTTechnologyAdapter;
import org.openflexo.foundation.task.Progress;
import org.openflexo.foundation.technologyadapter.TechnologyAdapterService;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.module.FlexoModule;
import org.openflexo.module.Module;
import org.openflexo.module.oneway.controller.OWController;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.view.controller.FlexoController;

/**
 * A module dedicated to {@link OnewayProject} edition
 * 
 * @author sylvain
 *
 */
public class OWModule extends FlexoModule<OWModule> {

	public static final String OW_MODULE_SHORT_NAME = "OW";
	public static final String OW_MODULE_NAME = "OnewayPropertiesEditor";
	private static final Logger logger = Logger.getLogger(OWModule.class.getPackage().getName());

	public OWModule(ApplicationContext applicationContext) {
		super(applicationContext);
		Progress.progress(FlexoLocalization.getMainLocalizer().localizedForKey("build_editor"));
	}

	@Override
	public Module<OWModule> getModule() {
		return OnewayEditor.INSTANCE;
	}

	@Override
	public String getLocalizationDirectory() {
		return "FlexoLocalization/Oneway";
	}

	@Override
	public void initModule() {
		super.initModule();
		TechnologyAdapterService taService = getApplicationContext().getTechnologyAdapterService();
		taService.activateTechnologyAdapter(taService.getTechnologyAdapter(FMLTechnologyAdapter.class), true);
		taService.activateTechnologyAdapter(taService.getTechnologyAdapter(FMLRTTechnologyAdapter.class), true);
		taService.activateTechnologyAdapter(taService.getTechnologyAdapter(DiagramTechnologyAdapter.class), true);
		Progress.progress(getLocales().localizedForKey("load_oneway_viewpoint"));
		initOnewayViewpoint();
	}

	/**
	 * Create a binded carto editor controller.
	 *
	 * @return a freshly created CEController.
	 */
	@Override
	protected FlexoController createControllerForModule() {
		return new OWController(this);
	}

	@Override
	public boolean close() {
		if (getApplicationContext().getResourceManager().getUnsavedResources().size() == 0) {
			return super.close();
		}
		else {
			return getFlexoController().reviewModifiedResources() && super.close();
		}
	}

	private void initOnewayViewpoint() {
		VirtualModelResource fmsVirtualModelResource = getApplicationContext().getVirtualModelLibrary()
				.getVirtualModelResource(OWConstants.ONEWAY_ROOT_MODEL_URI);
		if (fmsVirtualModelResource == null) {
			logger.severe("Cannot find Oneway viewpoint !!!!");
			System.out.println("RCs=" + getApplicationContext().getResourceCenterService().getResourceCenters());
			// JarResourceCenter.addNamedJarFromClassPath(getApplicationContext().getResourceCenterService(),
			// "fr/lacl/oneway_rc/1.0/oneway_rc-1.0");
		}
	}
}
