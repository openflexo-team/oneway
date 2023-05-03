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

package org.openflexo.module.oneway.model.action;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.openflexo.ApplicationContext;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.action.LongRunningAction;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.resource.DirectoryResourceCenter;
import org.openflexo.foundation.resource.FileSystemBasedResourceCenter;
import org.openflexo.foundation.resource.FlexoResourceCenter;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.module.oneway.OWConstants;
import org.openflexo.module.oneway.OnewayEditor;
import org.openflexo.technologyadapter.emf.rm.EMFModelResource;

/**
 * @author sylvain
 */
public class ImportBPMN extends OWAction<ImportBPMN, FlexoConceptInstance, FlexoObject> implements LongRunningAction {

	public static final ImportDocXDocumentActionType ACTION_TYPE = new ImportDocXDocumentActionType();

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, FlexoConceptInstance.class);
	}

	public static final class ImportDocXDocumentActionType extends FlexoActionFactory<ImportBPMN, FlexoConceptInstance, FlexoObject> {

		private ImportDocXDocumentActionType() {
			super("import_BPMN_file", FlexoActionFactory.defaultGroup, FlexoActionFactory.NORMAL_ACTION_TYPE);
		}

		@Override
		public ImportBPMN makeNewAction(final FlexoConceptInstance focusedObject, final Vector<FlexoObject> globalSelection,
				final FlexoEditor editor) {
			return new ImportBPMN(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(final FlexoConceptInstance element, final Vector<FlexoObject> globalSelection) {
			return element.getFlexoConcept().getName().equals(OWConstants.ELEMENT_CONCEPT_NAME);
		}

		@Override
		public boolean isEnabledForSelection(final FlexoConceptInstance element, final Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(element, globalSelection);
		}
	}

	// ====================
	// FIELDS
	// ====================
	private File bpmnFile;
	private String bpmnName;
	private String bpmnDescription;
	private FMLRTVirtualModelInstance requirementsVMI;

	private FMLRTVirtualModelInstance newDocumentVMI;

	public ImportBPMN(final FlexoConceptInstance focusedObject, final Vector<FlexoObject> globalSelection, final FlexoEditor editor) {
		super(ACTION_TYPE, focusedObject, globalSelection, editor);
		try {
			requirementsVMI = getFocusedObject().execute("container");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FMLRTVirtualModelInstance getRequirementsVMI() {
		return requirementsVMI;
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
	protected void doAction(final Object context) throws FlexoException {

		if (getBPMNFile() != null) {

			// A more compact syntax ;-)
			try {
				newDocumentVMI = getFocusedObject().execute(
						"container.container.importBPMNProcess({$element},{$resource},{$name},{$description})", getFocusedObject(),
						getBPMNResource(), getBPMNName(), getBPMNDescription());
			} catch (Exception e) {
				e.printStackTrace();
				throw new FlexoException(e);
			}

			/*FlexoConceptInstance element = getFocusedObject();
			
			FMLRTVirtualModelInstance requirementsVMI = (FMLRTVirtualModelInstance) element.getOwningVirtualModelInstance();
			FMLRTVirtualModelInstance onewayVMI = (FMLRTVirtualModelInstance) requirementsVMI.getContainerVirtualModelInstance();
			VirtualModel requirementsVirtualModel = requirementsVMI.getVirtualModel();
			VirtualModel onewayVirtualModel = requirementsVirtualModel.getOwner();
			
			ActionScheme importBPMNProcess = (ActionScheme) onewayVirtualModel.getFlexoBehaviour("importBPMNProcess",
					getFocusedObject().getFlexoConcept().getInstanceType(), EMFModelResource.class, String.class, String.class);
			ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(importBPMNProcess, onewayVMI);
			
			ActionSchemeAction action = actionType.makeNewEmbeddedAction(onewayVMI, null, this);
			action.setParameterValue(importBPMNProcess.getParameter("element"), element);
			action.setParameterValue(importBPMNProcess.getParameter("bpmnName"), getBPMNName());
			if (getBPMNDescription() != null) {
				action.setParameterValue(importBPMNProcess.getParameter("bpmnDescription"), getBPMNDescription());
			}
			
			try {
				action.setParameterValue(importBPMNProcess.getParameter("bpmnDocumentResource"), getBPMNResource());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			action.doAction();
			
			newDocumentVMI = (FMLRTVirtualModelInstance) action.getReturnedValue();*/
		}

	}

	public FMLRTVirtualModelInstance getNewDocumentVMI() {
		return newDocumentVMI;
	}

	private EMFModelResource getBPMNResource() throws IOException {
		if (getBPMNFile() != null) {
			EMFModelResource bpmnResource = null;
			for (FlexoResourceCenter<?> rc : getServiceManager().getResourceCenterService().getResourceCenters()) {
				if (rc instanceof FileSystemBasedResourceCenter) {
					FileSystemBasedResourceCenter fsRC = (FileSystemBasedResourceCenter) rc;
					bpmnResource = fsRC.getResource(getBPMNFile(), EMFModelResource.class);
					if (bpmnResource != null) {
						break;
					}
				}
			}

			if (bpmnResource == null) {
				File newResourceCenterDir = getBPMNFile().getParentFile();
				DirectoryResourceCenter newResourceCenter = DirectoryResourceCenter
						.instanciateNewDirectoryResourceCenter(newResourceCenterDir, getServiceManager().getResourceCenterService());
				getServiceManager().getResourceCenterService().addToResourceCenters(newResourceCenter);
				bpmnResource = newResourceCenter.getResource(getBPMNFile(), EMFModelResource.class);
			}

			return bpmnResource;
		}
		return null;

	}

	@Override
	public boolean isValid() {
		if (!super.isValid()) {
			return false;
		}
		System.out.println("getBPMNFile()=" + getBPMNFile());
		if (getBPMNFile() == null || !(getBPMNFile().exists())) {
			return false;
		}
		return true;
	}

	// ====================
	// GETTERS AND SETTERS
	// ====================

	public String getBPMNName() {
		if (bpmnName == null && getBPMNFile() != null) {
			return getBPMNFile().getName();
		}
		return bpmnName;
	}

	public void setBPMNName(String bpmnName) {
		if ((bpmnName == null && this.bpmnName != null) || (bpmnName != null && !bpmnName.equals(this.bpmnName))) {
			String oldValue = this.bpmnName;
			this.bpmnName = bpmnName;
			getPropertyChangeSupport().firePropertyChange("BPMNName", oldValue, bpmnName);
		}
	}

	public File getBPMNFile() {
		return bpmnFile;
	}

	public void setBPMNFile(File bpmnFile) {

		System.out.println("Hop, set bpmnFile=" + bpmnFile);

		if ((bpmnFile == null && this.bpmnFile != null) || (bpmnFile != null && !bpmnFile.equals(this.bpmnFile))) {
			File oldValue = this.bpmnFile;
			this.bpmnFile = bpmnFile;
			getPropertyChangeSupport().firePropertyChange("BPMNFile", oldValue, bpmnFile);
			getPropertyChangeSupport().firePropertyChange("BPMNName", null, getBPMNName());
		}
	}

	public String getBPMNDescription() {
		return bpmnDescription;
	}

	public void setBPMNDescription(String description) {
		if ((description == null && this.bpmnDescription != null) || (description != null && !description.equals(this.bpmnDescription))) {
			String oldValue = this.bpmnDescription;
			this.bpmnDescription = description;
			getPropertyChangeSupport().firePropertyChange("BPMNDescription", oldValue, description);
		}
	}

	@Override
	public int getExpectedProgressSteps() {
		return 10;
	}

}
