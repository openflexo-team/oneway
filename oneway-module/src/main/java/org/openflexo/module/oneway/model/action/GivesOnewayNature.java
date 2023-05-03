/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.module.oneway.model.action;

import java.io.FileNotFoundException;
import java.util.Vector;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.action.ModuleSpecificFlexoAction;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.CreationScheme;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.action.CreateBasicVirtualModelInstance;
import org.openflexo.foundation.fml.rt.rm.FMLRTVirtualModelInstanceResource;
import org.openflexo.foundation.nature.GivesNatureAction;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.module.oneway.OWConstants;
import org.openflexo.module.oneway.OWModule;
import org.openflexo.module.oneway.OnewayEditor;
import org.openflexo.module.oneway.model.OnewayInstance;
import org.openflexo.module.oneway.model.OnewayProjectNature;

/**
 * This action is called to gives Oneway nature to a project
 * 
 * @author sylvain
 */
public class GivesOnewayNature extends GivesNatureAction<GivesOnewayNature, OnewayProjectNature>
		implements ModuleSpecificFlexoAction<OWModule> {

	private static final Logger logger = Logger.getLogger(GivesOnewayNature.class.getPackage().getName());

	public static FlexoActionFactory<GivesOnewayNature, FlexoProject<?>, FlexoObject> actionType = new FlexoActionFactory<GivesOnewayNature, FlexoProject<?>, FlexoObject>(
			"gives_oneway_nature") {

		/**
		 * Factory method
		 */
		@Override
		public GivesOnewayNature makeNewAction(FlexoProject<?> focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
			return new GivesOnewayNature(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(FlexoProject<?> project, Vector<FlexoObject> globalSelection) {
			return true;
		}

		@Override
		public boolean isEnabledForSelection(FlexoProject<?> project, Vector<FlexoObject> globalSelection) {
			return project != null && !project.hasNature(OnewayProjectNature.class);
		}

	};

	static {
		FlexoObjectImpl.addActionForClass(GivesOnewayNature.actionType, FlexoProject.class);
	}

	GivesOnewayNature(FlexoProject<?> focusedObject, Vector<FlexoObject> globalSelection, FlexoEditor editor) {
		super(actionType, focusedObject, globalSelection, editor);
	}

	@Override
	public LocalizedDelegate getLocales() {
		if (getServiceManager() instanceof ApplicationContext) {
			return ((ApplicationContext) getServiceManager()).getModuleLoader().getModule(OnewayEditor.class).getLoadedModuleInstance()
					.getLocales();
		}
		return super.getLocales();
	}

	@Override
	public Class<OWModule> getFlexoModuleClass() {
		return OWModule.class;
	}

	public VirtualModel getOnewayVirtualModel() {
		if (getServiceManager() != null) {
			try {
				return getServiceManager().getVirtualModelLibrary().getVirtualModel(OWConstants.ONEWAY_ROOT_MODEL_URI);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ResourceLoadingCancelledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FlexoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public OnewayProjectNature makeNewNature() {

		System.out.println("On fait une nouvelle nature");

		OnewayProjectNature nature = getFocusedObject().getModelFactory().newInstance(OnewayProjectNature.class);

		OnewayInstance onewayVMI = retrieveOnewayInstance();

		System.out.println("OnewayVMI-1 " + onewayVMI);

		if (onewayVMI == null) {
			onewayVMI = makeOnewayInstance();
		}

		System.out.println("OnewayVMI-2 " + onewayVMI);

		nature.setOnewayInstance(onewayVMI);

		System.out.println("A la fin: " + nature.getOnewayInstance());

		return nature;
	}

	private OnewayInstance retrieveOnewayInstance() {

		VirtualModel onewayVirtualModel = getOnewayVirtualModel();
		if (onewayVirtualModel == null) {
			return null;
		}
		for (FMLRTVirtualModelInstanceResource viewResource : getFocusedObject().getVirtualModelInstanceRepository().getAllResources()) {
			if (viewResource.getVirtualModelResource() != null
					&& viewResource.getVirtualModelResource() == onewayVirtualModel.getResource()) {
				try {

					OnewayInstance newOnewayVMI = getFocusedObject().getModelFactory().newInstance(OnewayInstance.class);
					newOnewayVMI.setAccessedVirtualModelInstance(viewResource.getResourceData());
					return newOnewayVMI;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ResourceLoadingCancelledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FlexoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	private OnewayInstance makeOnewayInstance() {

		VirtualModel onewayVirtualModel = getOnewayVirtualModel();
		System.out.println("onewayVM=" + onewayVirtualModel);
		if (onewayVirtualModel == null) {
			return null;
		}

		CreateBasicVirtualModelInstance action = CreateBasicVirtualModelInstance.actionType
				.makeNewEmbeddedAction(getFocusedObject().getVirtualModelInstanceRepository().getRootFolder(), null, this);
		action.setNewVirtualModelInstanceName(OWConstants.ONEWAY_ROOT_VMI_NAME);
		action.setNewVirtualModelInstanceTitle(OWConstants.ONEWAY_ROOT_VMI_NAME);
		action.setVirtualModel(onewayVirtualModel);

		CreationScheme onewayViewCreationScheme = (CreationScheme) onewayVirtualModel
				.getFlexoBehaviour(OWConstants.ONEWAY_CREATION_SCHEME_NAME);

		action.setCreationScheme(onewayViewCreationScheme);
		action.setParameterValue(onewayViewCreationScheme.getParameter("projectName"), getFocusedObject().getProjectName());
		action.setParameterValue(onewayViewCreationScheme.getParameter("projectDescription"), getFocusedObject().getProjectDescription());
		action.doAction();
		FMLRTVirtualModelInstance onewayView = action.getNewVirtualModelInstance();

		OnewayInstance newOnewayVMI = getFocusedObject().getModelFactory().newInstance(OnewayInstance.class);
		newOnewayVMI.setAccessedVirtualModelInstance(onewayView);
		return newOnewayVMI;

	}

}
