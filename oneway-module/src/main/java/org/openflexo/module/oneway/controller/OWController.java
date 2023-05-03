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

package org.openflexo.module.oneway.controller;

import javax.swing.ImageIcon;

import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionFactory;
import org.openflexo.foundation.fml.rt.action.FlexoBehaviourAction;
import org.openflexo.icon.IconLibrary;
import org.openflexo.module.FlexoModule.WelcomePanel;
import org.openflexo.module.oneway.OWConstants;
import org.openflexo.module.oneway.OWIconLibrary;
import org.openflexo.module.oneway.OWModule;
import org.openflexo.module.oneway.controller.action.OWControllerActionInitializer;
import org.openflexo.module.oneway.menu.OWMenuBar;
import org.openflexo.module.oneway.model.OnewayProjectNature;
import org.openflexo.module.oneway.view.ConvertToOnewayProjectView;
import org.openflexo.module.oneway.view.OnewayWelcomePanelModuleView;
import org.openflexo.selection.MouseSelectionManager;
import org.openflexo.technologyadapter.diagram.DiagramTechnologyAdapter;
import org.openflexo.technologyadapter.diagram.controller.DiagramTechnologyAdapterController;
import org.openflexo.technologyadapter.diagram.gui.DiagramIconLibrary;
import org.openflexo.technologyadapter.docx.gui.DocXIconLibrary;
import org.openflexo.technologyadapter.docx.nature.FMLControlledDocXVirtualModelInstanceNature;
import org.openflexo.view.FlexoMainPane;
import org.openflexo.view.ModuleView;
import org.openflexo.view.controller.ControllerActionInitializer;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;
import org.openflexo.view.menu.FlexoMenuBar;

/**
 * Oneway-module specific FlexoController.
 * 
 * @author sylvain
 */
public class OWController extends FlexoController {

	private OnewayPerspective onewayPerspective;
	private DocumentAnnotationPerspective documentAnnotationPerspective;
	private BPMNPerspective bpmnPerspective;
	private PropertiesPerspective propertiesPerspective;

	private DiagramTechnologyAdapterController diagramTAC = null;

	public OWController(OWModule module) {
		super(module);
	}

	public OnewayProjectNature getOnewayNature() {
		if (getProject() != null) {
			return getProject().getNature(OnewayProjectNature.class);
		}
		return null;
	}

	@Override
	public FlexoObject getDefaultObjectToSelect(FlexoProject<?> project) {
		if (project != null && project.hasNature(OnewayProjectNature.class)) {
			return project.getNature(OnewayProjectNature.class);
		}
		return project;
	}

	@Override
	protected void initializePerspectives() {
		this.addToPerspectives(onewayPerspective = new MainPerspective(this));
		this.addToPerspectives(documentAnnotationPerspective = new DocumentAnnotationPerspective(this));
		this.addToPerspectives(bpmnPerspective = new BPMNPerspective(this));
		this.addToPerspectives(propertiesPerspective = new PropertiesPerspective(this));
	}

	@Override
	protected MouseSelectionManager createSelectionManager() {
		return new OWSelectionManager(this);
	}

	@Override
	protected FlexoMenuBar createNewMenuBar() {
		return new OWMenuBar(this);
	}

	@Override
	protected FlexoMainPane createMainPane() {
		return new FlexoMainPane(this);
	}

	@Override
	protected void updateEditor(final FlexoEditor from, final FlexoEditor to) {
		super.updateEditor(from, to);
		FlexoProject<?> project = (to != null ? to.getProject() : null);
		if (onewayPerspective != null) {
			onewayPerspective.setProject(project);
		}
	}

	@Override
	public ImageIcon iconForObject(final Object object) {
		if (object instanceof OnewayProjectNature) {
			return OWIconLibrary.OW_SMALL_ICON;
		}

		if (getOnewayNature() != null) {
			if (object == getOnewayNature().getOnewayInstance().getAccessedVirtualModelInstance()) {
				return OWIconLibrary.OW_SMALL_ICON;
			}
		}

		if (object instanceof FMLRTVirtualModelInstance) {
			VirtualModel type = ((FMLRTVirtualModelInstance) object).getVirtualModel();
			if (type != null) {
				if (type.getName().equals(OWConstants.DOCUMENT_ANNOTATIONS_VM_NAME)) {
					return OWIconLibrary.DOC_LIBRARY_ICON;
				}
				else if (type.getName().equals(OWConstants.REQUIREMENTS_VM_NAME)) {
					return OWIconLibrary.OW_SMALL_ICON;
				}
				else if (type.getName().equals(OWConstants.DOCUMENT_LIBRARY_VM_NAME)) {
					return OWIconLibrary.DOC_LIBRARY_ICON;
				}
				else if (type.getName().equals(OWConstants.PROCESS_DEFINITION_VM_NAME)) {
					return OWIconLibrary.BPMN_ICON;
				}
				else if (type.getURI().equals(OWConstants.BPMN_DIAGRAM_URI)) {
					return DiagramIconLibrary.DIAGRAM_ICON;
				}
				// FML-controlled document
				else if (((FMLRTVirtualModelInstance) object).hasNature(FMLControlledDocXVirtualModelInstanceNature.INSTANCE)) {
					return DocXIconLibrary.DOCX_FILE_ICON;
				}
			}

			return super.iconForObject(object);
		}

		if (object instanceof FlexoConceptInstance) {
			FlexoConcept type = ((FlexoConceptInstance) object).getFlexoConcept();
			if (type != null) {
				if (type.getName().equals(OWConstants.ELEMENT_CONCEPT_NAME)) {
					return OWIconLibrary.ELEMENT_ICON;
				}
				else if (type.getName().equals(OWConstants.REQUIREMENT_CONCEPT_NAME)) {
					return OWIconLibrary.REQUIREMENT_ICON;
				}
				else if (type.getName().equals(OWConstants.REFERENCE_CONCEPT_NAME)) {
					return DocXIconLibrary.FRAGMENT_ICON;
				}
				else if (type.getName().equals("DocXReference")) {
					return DocXIconLibrary.FRAGMENT_ICON;
				}
				else if (type.getName().equals(OWConstants.UNCLASSIFIED_CONCEPT_NAME)) {
					return IconLibrary.FOLDER_ICON;
				}
				else if (type.getName().equals(OWConstants.ATOMIC_PROPOSITION_CONCEPT_NAME)) {
					return OWIconLibrary.ATOMIC_PROPOSITION_ICON;
				}
				else if (type.getName().equals(OWConstants.PROPERTY_CONCEPT_NAME)) {
					return OWIconLibrary.PROPERTIES_ICON;
				}
			}
		}
		return super.iconForObject(object);
	}

	@Override
	public ControllerActionInitializer createControllerActionInitializer() {
		return new OWControllerActionInitializer(this);
	}

	/**
	 * Helper functio to ease access to DiagramTAController
	 * 
	 * @return
	 */
	public DiagramTechnologyAdapterController getDiagramTAC() {
		if (diagramTAC == null) {
			DiagramTechnologyAdapter diagramTA = this.getTechnologyAdapter(DiagramTechnologyAdapter.class);
			diagramTAC = (DiagramTechnologyAdapterController) getTechnologyAdapterController(diagramTA);
		}
		return this.diagramTAC;
	}

	@Override
	public void willExecute(FlexoBehaviourAction<?, ?, ?> action) {
		super.willExecute(action);
	}

	@Override
	public void hasExecuted(FlexoBehaviourAction<?, ?, ?> action) {
		super.hasExecuted(action);

		// System.out.println("On vient d'executer " + action);
		/*if (action.getFlexoBehaviour() instanceof ActionScheme) {
			if (action.getFlexoBehaviour().getName().equals("refine")) {
				action.getFlexoConceptInstance().getPropertyChangeSupport().firePropertyChange("childrenElements", false, true);
				onewayPerspective.getProjectBrowser().getFIBController().getPropertyChangeSupport()
						.firePropertyChange("getChildren(FlexoConceptInstance)", false, true);
			}
			else if (action.getFlexoBehaviour().getName().equals("createRequirement")) {
				action.getFlexoConceptInstance().getPropertyChangeSupport().firePropertyChange("goals", false, true);
				onewayPerspective.getProjectBrowser().getFIBController().getPropertyChangeSupport()
						.firePropertyChange("getChildren(FlexoConceptInstance)", false, true);
			}
			else if (action.getFlexoBehaviour().getName().equals("createChildrenElement")) {
				action.getFlexoConceptInstance().getPropertyChangeSupport().firePropertyChange("childrenElements", false, true);
				onewayPerspective.getProjectBrowser().getFIBController().getPropertyChangeSupport()
						.firePropertyChange("getChildren(FlexoConceptInstance)", false, true);
			}
		}*/
	}

	/*@Override
	public void objectWasDoubleClicked(Object object) {
		if (object instanceof FlexoConceptInstance) {
			FlexoConceptInstance fci = (FlexoConceptInstance) object;
			if (fci.getFlexoConcept() == getOnewayProject().getElementConcept()) {
				System.out.println("Double-clicking on element");
				NavigationScheme navigationScheme = getOnewayProject().getElementConcept().getNavigationSchemes().get(0);
				NavigationSchemeActionFactory actionType = new NavigationSchemeActionFactory(navigationScheme, fci);
				NavigationSchemeAction navigationSchemeAction = actionType.makeNewAction(fci.getVirtualModelInstance(), null, getEditor());
				navigationSchemeAction.doAction();
			}
		}
		super.objectWasDoubleClicked(object);
	}*/

	public OnewayPerspective getOnewayPerspective() {
		return onewayPerspective;
	}

	public DocumentAnnotationPerspective getDocumentAnnotationPerspective() {
		return documentAnnotationPerspective;
	}

	public BPMNPerspective getBPMNPerspective() {
		return bpmnPerspective;
	}

	@Override
	public ModuleView<?> makeWelcomePanel(WelcomePanel<?> welcomePanel, FlexoPerspective perspective) {
		return new OnewayWelcomePanelModuleView((WelcomePanel<OWModule>) welcomePanel, this, perspective);
	}

	@Override
	public ModuleView<?> makeDefaultProjectView(FlexoProject<?> project, FlexoPerspective perspective) {
		return new ConvertToOnewayProjectView(project, this, perspective);
	}

	/**
	 * Return ElementReference instance matching supplied Element instance
	 * 
	 * @param element
	 *            an instance of of Element concept
	 * @return an instance of ElementReference concept
	 */
	public FlexoConceptInstance getElementReference(FlexoConceptInstance element) {

		VirtualModel docAnnotationVM = getOnewayNature().getDocumentAnnotationsVirtualModel();

		ActionScheme getElementReference = (ActionScheme) docAnnotationVM.getDeclaredFlexoBehaviour("getElementReference",
				getOnewayNature().getElementConcept().getInstanceType());

		ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(getElementReference,
				getOnewayNature().getDocumentAnnotationsVirtualModelInstance());
		ActionSchemeAction action = actionType.makeNewAction(getOnewayNature().getDocumentAnnotationsVirtualModelInstance(), null,
				getEditor());
		action.setParameterValue(getElementReference.getParameter("element"), element);
		action.doAction();

		return (FlexoConceptInstance) action.getReturnedValue();

	}

	/**
	 * Return RequirementReference instance matching supplied Requirement instance
	 * 
	 * @param element
	 *            an instance of of Requirement concept
	 * @return an instance of RequirementReference concept
	 */
	public FlexoConceptInstance getRequirementReference(FlexoConceptInstance requirement) {

		VirtualModel docAnnotationVM = getOnewayNature().getDocumentAnnotationsVirtualModel();

		ActionScheme getRequirementReference = (ActionScheme) docAnnotationVM.getDeclaredFlexoBehaviour("getRequirementReference",
				getOnewayNature().getRequirementConcept().getInstanceType());

		ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(getRequirementReference,
				getOnewayNature().getDocumentAnnotationsVirtualModelInstance());
		ActionSchemeAction action = actionType.makeNewAction(getOnewayNature().getDocumentAnnotationsVirtualModelInstance(), null,
				getEditor());
		action.setParameterValue(getRequirementReference.getParameter("requirement"), requirement);
		action.doAction();

		return (FlexoConceptInstance) action.getReturnedValue();
	}

	public PropertiesPerspective getPropertiesPerspective() {
		return propertiesPerspective;
	}

}
