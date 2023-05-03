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

package org.openflexo.module.oneway.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import org.openflexo.connie.exception.InvalidBindingException;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.doc.TextSelection;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionFactory;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.view.GinaViewFactory;
import org.openflexo.module.oneway.model.OnewayProjectNature;
import org.openflexo.module.oneway.model.action.CreateAtomicProposition;
import org.openflexo.module.oneway.model.action.CreateNewElement;
import org.openflexo.module.oneway.model.action.CreateNewRequirement;
import org.openflexo.module.oneway.model.action.CreateProperty;
import org.openflexo.module.oneway.model.action.ImportBPMN;
import org.openflexo.module.oneway.model.action.ImportDocXDocument;
import org.openflexo.technologyadapter.docx.DocXTechnologyAdapter;
import org.openflexo.technologyadapter.docx.model.DocXDocument;
import org.openflexo.technologyadapter.gina.controller.FMLControlledFIBController;
import org.openflexo.view.controller.FlexoController;

/**
 * Represents the controller of a FIBComponent in Oneway context
 * 
 * 
 * @author sylvain
 * 
 * @param <T>
 */
public class OWFIBController extends FMLControlledFIBController {

	private static final Logger logger = Logger.getLogger(OWFIBController.class.getPackage().getName());

	public OWFIBController(FIBComponent component, GinaViewFactory<?> viewFactory) {
		super(component, viewFactory);
		// Default parent localizer is the main localizer
		// setParentLocalizer(FlexoLocalization.getMainLocalizer());
	}

	public OWFIBController(FIBComponent component, GinaViewFactory<?> viewFactory, FlexoController controller) {
		super(component, viewFactory, controller);
		// Default parent localizer is the main localizer
		// setParentLocalizer(FlexoLocalization.getMainLocalizer());
	}

	@Override
	public OWController getFlexoController() {
		return (OWController) super.getFlexoController();
	}

	@Override
	public void setFlexoController(FlexoController aController) {
		super.setFlexoController(aController);
		setParentLocalizer(aController.getModuleLocales());
	}

	public OnewayProjectNature getOnewayNature() {
		if (getFlexoController() != null) {
			return getFlexoController().getOnewayNature();
		}
		return null;
	}

	public FlexoConceptInstance createChildrenElement(FlexoConceptInstance element) {

		System.out.println("Nouvel element avec " + element);
		CreateNewElement action = CreateNewElement.ACTION_TYPE.makeNewAction(element, null, getFlexoController().getEditor());
		action.setSelectAfterCreation(false);
		action.doAction();
		return action.getNewElement();
	}

	public FlexoConceptInstance createRequirement(FlexoConceptInstance element) {

		System.out.println("Nouveau requirement avec " + element);
		CreateNewRequirement action = CreateNewRequirement.ACTION_TYPE.makeNewAction(element, null, getFlexoController().getEditor());
		action.setSelectAfterCreation(false);
		action.doAction();
		return action.getNewRequirement();
	}

	public FlexoConceptInstance identifyRequirement() {

		System.out.println("TODO: identifyRequirement()");
		return null;

		/*IdentifyRequirement action = IdentifyRequirement.ACTION_TYPE.makeNewAction(getFormoseNature(), null,
				getFlexoController().getEditor());
		action.doAction();
		return action.getNewIdentifiedRequirement();*/
	}

	public FlexoConceptInstance identifyRequirement(FlexoConceptInstance identifiedFragment) {

		System.out.println("TODO: identifyRequirement() with " + identifiedFragment);
		return null;

		/*IdentifyRequirement action = IdentifyRequirement.ACTION_TYPE.makeNewAction(getFormoseNature(), null,
				getFlexoController().getEditor());
		action.doAction();
		return action.getNewIdentifiedRequirement();*/
	}

	public FlexoConceptInstance identifyRequirement(TextSelection<DocXDocument, DocXTechnologyAdapter> textSelection) {

		System.out.println("TODO: identifyRequirement() with " + textSelection);
		return null;

		/*IdentifyRequirement action = IdentifyRequirement.ACTION_TYPE.makeNewAction(getFormoseNature(), null,
				getFlexoController().getEditor());
		action.doAction();
		return action.getNewIdentifiedRequirement();*/
	}

	public void moveReference(FlexoConceptInstance reference, FlexoConceptInstance sourceReference,
			FlexoConceptInstance destinationReference) {
		// System.out.println("moveReference " + reference + " from " + sourceReference + " to " + destinationReference);

		VirtualModel docAnnotationVM = getFlexoController().getOnewayNature().getDocumentAnnotationsVirtualModel();
		FlexoConcept namedReference = docAnnotationVM.getFlexoConcept("NamedReference");

		ActionScheme moveReference = (ActionScheme) namedReference.getDeclaredFlexoBehaviour("moveReference",
				getFlexoController().getOnewayNature().getReferenceConcept().getInstanceType(), namedReference.getInstanceType());

		ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(moveReference, sourceReference);
		ActionSchemeAction action = actionType.makeNewAction(sourceReference, null, getEditor());
		action.setParameterValue(moveReference.getParameter("aReference"), reference);
		action.setParameterValue(moveReference.getParameter("namedReference"), destinationReference);

		// System.out.println("Et hop, on execute pour " + sourceReference);
		// System.out.println(moveReference.getFMLRepresentation());
		// System.out.println("avec reference=" + reference);
		// System.out.println("avec namedReference=" + destinationReference);
		action.doAction();

	}

	public void moveUnclassifiedReference(FlexoConceptInstance reference, FlexoConceptInstance unclassified,
			FlexoConceptInstance destinationReference) {
		// System.out.println("moveReference " + reference + " from " + unclassified + " to " + destinationReference);

		VirtualModel docAnnotationVM = getFlexoController().getOnewayNature().getDocumentAnnotationsVirtualModel();
		FlexoConcept unclassifiedConcept = docAnnotationVM.getFlexoConcept("Unclassified");
		FlexoConcept namedReference = docAnnotationVM.getFlexoConcept("NamedReference");

		ActionScheme moveReference = (ActionScheme) unclassifiedConcept.getDeclaredFlexoBehaviour("moveReference",
				getFlexoController().getOnewayNature().getReferenceConcept().getInstanceType(), namedReference.getInstanceType());

		ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(moveReference, unclassified);
		ActionSchemeAction action = actionType.makeNewAction(unclassified, null, getEditor());
		action.setParameterValue(moveReference.getParameter("aReference"), reference);
		action.setParameterValue(moveReference.getParameter("namedReference"), destinationReference);

		// System.out.println("Et hop, on execute pour " + unclassified);
		// System.out.println(moveReference.getFMLRepresentation());
		// System.out.println("avec reference=" + reference);
		// System.out.println("avec namedReference=" + destinationReference);
		action.doAction();

	}

	public FMLRTVirtualModelInstance importNewDocXDocument(FMLRTVirtualModelInstance documentLibrary) {

		System.out.println("importNewDocXDocument with " + documentLibrary);

		ImportDocXDocument action = ImportDocXDocument.ACTION_TYPE.makeNewAction(documentLibrary, null, getFlexoController().getEditor());
		action.doAction();
		return action.getNewDocumentVMI();
	}

	public void removeDocument(FMLRTVirtualModelInstance document) {

		System.out.println("removeDocument " + document);

		System.out.println("document.getContainerVirtualModelInstance()=" + document.getContainerVirtualModelInstance());

		try {
			if (document.getContainerVirtualModelInstance() != null) {
				document.getContainerVirtualModelInstance().execute("removeDocXDocument({$doc})", document);
			}
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

	public FMLRTVirtualModelInstance importBPMNProcess(FlexoConceptInstance element) {

		System.out.println("importBPMNProcess with " + element);

		ImportBPMN action = ImportBPMN.ACTION_TYPE.makeNewAction(element, null, getFlexoController().getEditor());
		action.doAction();
		return action.getNewDocumentVMI();
	}

	public FlexoConceptInstance createAtomicProposition(FMLRTVirtualModelInstance processProperties) {
		System.out.println("createAtomicProposition() for " + processProperties);

		CreateAtomicProposition action = CreateAtomicProposition.ACTION_TYPE.makeNewAction(processProperties, null,
				getFlexoController().getEditor());
		action.doAction();
		return action.getNewAtomicProposition();
	}

	public FlexoConceptInstance createProperty(FMLRTVirtualModelInstance processProperties) {
		System.out.println("createProperty() for " + processProperties);

		CreateProperty action = CreateProperty.ACTION_TYPE.makeNewAction(processProperties, null, getFlexoController().getEditor());
		action.doAction();
		return action.getNewProperty();
	}

}
