/**
 * 
 * Copyright (c) 2014-2015, Openflexo
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

package org.openflexo.module.oneway.controller;

import java.lang.reflect.InvocationTargetException;

import javax.swing.ImageIcon;

import org.openflexo.connie.exception.InvalidBindingException;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.fml.rt.controller.view.VirtualModelInstanceView;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.VirtualModelInstance;
import org.openflexo.module.oneway.OWConstants;
import org.openflexo.module.oneway.OWIconLibrary;
import org.openflexo.module.oneway.model.OnewayProjectNature;
import org.openflexo.module.oneway.view.BPMNDiagramModuleView;
import org.openflexo.module.oneway.view.BPMNModuleView;
import org.openflexo.module.oneway.widget.AbstractOnewayProjectBrowser;
import org.openflexo.module.oneway.widget.BPMNBrowser;
import org.openflexo.module.oneway.widget.ProcessBrowser;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.controller.diagrameditor.FMLControlledDiagramEditor;
import org.openflexo.technologyadapter.diagram.fml.FMLControlledDiagramVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBFlexoConceptInstanceNature;
import org.openflexo.technologyadapter.gina.view.FMLControlledFIBFlexoConceptInstanceModuleView;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.FlexoController;

/**
 * Perspective dedicated to BPMN perspective
 * 
 * @author sylvain
 */
public class BPMNPerspective extends OnewayPerspective {

	private final ProcessBrowser processBrowser;

	public BPMNPerspective(final FlexoController controller) {
		super("bpmn", controller);
		processBrowser = new ProcessBrowser(controller);
	}

	@Override
	public ImageIcon getActiveIcon() {
		return OWIconLibrary.BPMN_ICON;
	}

	public void showProcessBrowser(FlexoConceptInstance process) {
		setBottomLeftView(getProcessBrowser());
		getProcessBrowser().setDataObject(process);
	}

	public void hideProcessBrowser() {
		setBottomLeftView(null);
	}

	public ProcessBrowser getProcessBrowser() {
		return processBrowser;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(FlexoObject object) {

		if (object instanceof OnewayProjectNature) {
			return new BPMNModuleView((OnewayProjectNature) object, getController(), this);
		}

		if (object instanceof VirtualModelInstance) {
			if (((VirtualModelInstance) object).getVirtualModel().getName().equals(OWConstants.PROCESS_DEFINITION_VM_NAME)) {
				System.out.println("Un process definition");
				try {
					VirtualModelInstance bpmnModel = ((VirtualModelInstance) object).execute("bpmnEditor.model");
					System.out.println("bpmnModel=" + bpmnModel);
					return new VirtualModelInstanceView(bpmnModel, getController(), null);

					/*if (bpmnModel.hasNature(FMLControlledFIBVirtualModelInstanceNature.INSTANCE)) {
						System.out.println("Hop");
						return new FMLControlledFIBVirtualModelInstanceModuleView(bpmnModel, getController(), this,
								getController().getModuleLocales());
					}*/

				} catch (TypeMismatchException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullReferenceException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidBindingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (((VirtualModelInstance) object).getVirtualModel().getURI().equals(OWConstants.BPMN_DIAGRAM_URI)) {
				DiagramTechnologyAdapterController diagramTAC = ((OWController) getController()).getDiagramTAC();
				FMLControlledDiagramEditor editor = new FMLControlledDiagramEditor((FMLRTVirtualModelInstance) object, false,
						getController(), diagramTAC.getToolFactory());
				return new BPMNDiagramModuleView(editor, this);
			}

			/*if (((FlexoConceptInstance) object).getFlexoConcept().getName().equals(FMSConstants.DIAGRAM_MAPPING_CONCEPT_NAME)) {
				FlexoConceptInstance diagramMapping = ((FlexoConceptInstance) object);
				FMLRTVirtualModelInstance diagramVMI = diagramMapping.getFlexoPropertyValue("goalModelingDiagram");
				if (diagramVMI != null && diagramVMI.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
					DiagramTechnologyAdapterController diagramTAC = ((FMSController) getController()).getDiagramTAC();
					FMLControlledDiagramEditor editor = new FMLControlledDiagramEditor(diagramVMI, false, getController(),
							diagramTAC.getToolFactory());
					return new BPMNDiagramModuleView(editor, this);
				}
			
			}*/

		}

		if (object instanceof FlexoConceptInstance) {

			/*if (((FlexoConceptInstance) object).getFlexoConcept().getName().equals(FMSConstants.DIAGRAM_MAPPING_CONCEPT_NAME)) {
				FlexoConceptInstance diagramMapping = ((FlexoConceptInstance) object);
				FMLRTVirtualModelInstance diagramVMI = diagramMapping.getFlexoPropertyValue("goalModelingDiagram");
				if (diagramVMI != null && diagramVMI.hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
					DiagramTechnologyAdapterController diagramTAC = ((FMSController) getController()).getDiagramTAC();
					FMLControlledDiagramEditor editor = new FMLControlledDiagramEditor(diagramVMI, false, getController(),
							diagramTAC.getToolFactory());
					return new BPMNDiagramModuleView(editor, this);
				}
			
			}*/
			System.out.println("TODO-2 !!!");

			if (((FlexoConceptInstance) object).hasNature(FMLControlledFIBFlexoConceptInstanceNature.INSTANCE)) {

				return new FMLControlledFIBFlexoConceptInstanceModuleView((FlexoConceptInstance) object, getController(), this,
						getController().getModuleLocales());
			}

		}

		// In all other cases...
		return super.createModuleViewForObject(object);

	}

	@Override
	public boolean hasModuleViewForObject(FlexoObject object) {
		if (object instanceof OnewayProjectNature) {
			return true;
		}
		if (object instanceof FMLRTVirtualModelInstance) {
			if (((FMLRTVirtualModelInstance) object).getVirtualModel().getName().equals(OWConstants.PROCESS_DEFINITION_VM_NAME)) {
				return true;
			}
			// FML-controlled diagram
			if (((FMLRTVirtualModelInstance) object).hasNature(FMLControlledDiagramVirtualModelInstanceNature.INSTANCE)) {
				return true;
			}
			return false;
		}
		if (object instanceof FlexoConceptInstance) {

			/*if (((FlexoConceptInstance) object).getFlexoConcept().getName().equals(OWConstants.DIAGRAM_MAPPING_CONCEPT_NAME)) {
				return true;
			}*/
			System.out.println("TODO-3 !!!");
			if (((FlexoConceptInstance) object).hasNature(FMLControlledFIBFlexoConceptInstanceNature.INSTANCE)) {
				return true;
			}
		}
		return super.hasModuleViewForObject(object);
	}

	@Override
	protected AbstractOnewayProjectBrowser makeOnewayProjectBrowser() {
		return new BPMNBrowser(getController());
	}
}
