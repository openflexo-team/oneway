/**
 * 
 * Copyright (c) 2014-2015, Openflexo
 * 
 * This file is part of Oneway prototype, a component of the software infrastructure developed at Openflexo.
 * 
 * 
 * Openflexo is dual-licensed under the European Union Public License (EUPL, either version 1.1 of the License, or any later version ),
 * which is available at https://joinup.ec.europa.eu/software/page/eupl/licence-eupl and the GNU General Public License (GPL, either version
 * 3 of the License, or any later version), which is available at http://www.gnu.org/licenses/gpl.html .
 * 
 * You can redistribute it and/or modify under the terms of either of these licenses
 * 
 * If you choose to redistribute it and/or modify under the terms of the GNU GPL, you must include the following additional permission.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or combining it with software containing parts covered by the terms of EPL
 * 1.0, the licensors of this Program grant you additional permission to convey the resulting work. *
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * See http://www.openflexo.org/license.html for details.
 * 
 * 
 * Please contact Openflexo (openflexo-contacts@openflexo.org) or visit www.openflexo.org if you need additional information.
 * 
 */

package org.openflexo.module.oneway.model;

import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.nature.ProjectNature;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.module.oneway.OWConstants;
import org.openflexo.pamela.annotations.Getter;
import org.openflexo.pamela.annotations.ImplementationClass;
import org.openflexo.pamela.annotations.ModelEntity;
import org.openflexo.pamela.annotations.PropertyIdentifier;
import org.openflexo.pamela.annotations.Setter;
import org.openflexo.pamela.annotations.XMLElement;

/**
 * This class is used to interpret a {@link FlexoProject} as a Oneway project <br>
 *
 * 
 * @author sylvain
 */
@ModelEntity
@XMLElement
@ImplementationClass(OnewayProjectNature.OnewayProjectNatureImpl.class)
public interface OnewayProjectNature extends ProjectNature<OnewayProjectNature> {

	@PropertyIdentifier(type = OnewayInstance.class)
	public static final String ONEWAY_INSTANCE = "onewayInstance";

	@Getter(value = ONEWAY_INSTANCE, inverse = OnewayInstance.NATURE)
	@XMLElement
	public OnewayInstance getOnewayInstance();

	@Setter(ONEWAY_INSTANCE)
	public void setOnewayInstance(OnewayInstance onewayVirtualModelInstance);

	public FMLRTVirtualModelInstance getOnewayView();

	public VirtualModel getOnewayViewPoint();

	public VirtualModel getDocumentLibraryVirtualModel();

	public VirtualModel getAbstractDocumentVirtualModel();

	public FlexoConcept getReferenceConcept();

	public VirtualModel getRequirementsVirtualModel();

	public FlexoConcept getElementConcept();

	public FlexoConcept getRequirementConcept();

	public VirtualModel getDocumentAnnotationsVirtualModel();

	public FlexoConcept getElementReferenceConcept();

	public FlexoConcept getRequirementReferenceConcept();

	public FMLRTVirtualModelInstance getRequirementsVirtualModelInstance();

	public FMLRTVirtualModelInstance getDocumentAnnotationsVirtualModelInstance();
	
	//---------Ajout @Valery------------------------------
	public FMLRTVirtualModelInstance getPropertiesVirtualModelInstance();
	public VirtualModel getPropertiesVirtualModel();
	
	public FMLRTVirtualModelInstance getProcessPropertiesVirtualModelInstance();
	public VirtualModel getProcessPropertiesVirtualModel();
	
	public FlexoConcept getPropertyConcept();
	public FlexoConcept getPropertyReferenceConcept();
	
	//----------------------------------------------------

	public abstract class OnewayProjectNatureImpl extends ProjectNatureImpl<OnewayProjectNature> implements OnewayProjectNature {
		private static final Logger logger = Logger.getLogger(OnewayProjectNature.class.getPackage().getName());

		private VirtualModel onewayViewpoint;
		private VirtualModel documentLibraryVirtualModel;

		@Override
		public VirtualModel getOnewayViewPoint() {
			if (onewayViewpoint == null && getServiceManager() != null) {
				try {
					onewayViewpoint = getServiceManager().getVirtualModelLibrary().getVirtualModel(OWConstants.ONEWAY_ROOT_MODEL_URI);
				} catch (FileNotFoundException | ResourceLoadingCancelledException | FlexoException e) {
					e.printStackTrace();
				}
			}
			return onewayViewpoint;
		}

		public FMLRTVirtualModelInstance getOnewayView() {
			return getOnewayInstance().getAccessedVirtualModelInstance();
		}

		@Override
		public VirtualModel getDocumentLibraryVirtualModel() {
			if (documentLibraryVirtualModel == null && getServiceManager() != null) {
				try {
					documentLibraryVirtualModel = getServiceManager().getVirtualModelLibrary()
							.getVirtualModel(OWConstants.DOCUMENT_LIBRARY_VIEWPOINT_URI);
				} catch (FileNotFoundException | ResourceLoadingCancelledException | FlexoException e) {
					e.printStackTrace();
				}
			}
			return documentLibraryVirtualModel;
		}

		@Override
		public VirtualModel getAbstractDocumentVirtualModel() {
			if (getDocumentLibraryVirtualModel() != null) {
				return getDocumentLibraryVirtualModel().getVirtualModelNamed(OWConstants.ABSTRACT_DOCUMENT_VM_NAME);
			}
			return null;
		}

		@Override
		public FlexoConcept getReferenceConcept() {
			return getAbstractDocumentVirtualModel().getFlexoConcept(OWConstants.REFERENCE_CONCEPT_NAME);
		}

		@Override
		public VirtualModel getRequirementsVirtualModel() {
			return getOnewayViewPoint().getVirtualModelNamed(OWConstants.REQUIREMENTS_VM_NAME);
		}
		
				
		@Override
		public FlexoConcept getElementConcept() {
			return getRequirementsVirtualModel().getFlexoConcept(OWConstants.ELEMENT_CONCEPT_NAME);
		}

		@Override
		public FlexoConcept getRequirementConcept() {
			return getRequirementsVirtualModel().getFlexoConcept(OWConstants.REQUIREMENT_CONCEPT_NAME);
		}

		// Concepts declared in DocumentAnnotation-Methodology

		@Override
		public VirtualModel getDocumentAnnotationsVirtualModel() {
			return getOnewayViewPoint().getVirtualModelNamed(OWConstants.DOCUMENT_ANNOTATIONS_VM_NAME);
		}

		@Override
		public FlexoConcept getElementReferenceConcept() {
			return getDocumentAnnotationsVirtualModel().getFlexoConcept(OWConstants.ELEMENT_REFERENCE_CONCEPT_NAME);
		}

		@Override
		public FlexoConcept getRequirementReferenceConcept() {
			return getDocumentAnnotationsVirtualModel().getFlexoConcept(OWConstants.REQUIREMENT_REFERENCE_CONCEPT_NAME);
		}

		@Override
		public FMLRTVirtualModelInstance getDocumentAnnotationsVirtualModelInstance() {
			if (getOnewayView() != null) {
				if (getOnewayView().getVirtualModelInstancesForVirtualModel(getDocumentAnnotationsVirtualModel()).size() > 0) {
					return (FMLRTVirtualModelInstance) getOnewayView()
							.getVirtualModelInstancesForVirtualModel(getDocumentAnnotationsVirtualModel()).get(0);
				}
			}
			return null;
		}

		@Override
		public FMLRTVirtualModelInstance getRequirementsVirtualModelInstance() {
			if (getOnewayView() != null) {
				if (getOnewayView().getVirtualModelInstancesForVirtualModel(getRequirementsVirtualModel()).size() > 0) {
					return (FMLRTVirtualModelInstance) getOnewayView().getVirtualModelInstancesForVirtualModel(getRequirementsVirtualModel())
							.get(0);
				}
			}
			return null;
		}
		
		
		//------------Ajout @Valery-------------------------------
		//------------------Valery
				@Override
				public VirtualModel getPropertiesVirtualModel() {
					return getOnewayViewPoint().getVirtualModelNamed(OWConstants.PROPERTIES_VM_URI);
				}
				
				@Override
				public FMLRTVirtualModelInstance getPropertiesVirtualModelInstance() {
					if (getOnewayView() != null) {
						if (getOnewayView().getVirtualModelInstancesForVirtualModel(getPropertiesVirtualModel()).size() > 0) {
							return (FMLRTVirtualModelInstance) getOnewayView().getVirtualModelInstancesForVirtualModel(getPropertiesVirtualModel())
									.get(0);
						}
					}
					return null;
				}
				
				@Override
				public VirtualModel getProcessPropertiesVirtualModel() {
					//return getOnewayViewPoint().getVirtualModelNamed(OWConstants.REQUIREMENTS_VM_NAME);
					return getOnewayViewPoint().getVirtualModelNamed(OWConstants.PROCESS_PROPERTIES_VM_NAME);
					
					
				}
				
				@Override
				public FMLRTVirtualModelInstance getProcessPropertiesVirtualModelInstance() {
					if (getOnewayView() != null) {
						if (getOnewayView().getVirtualModelInstancesForVirtualModel(getProcessPropertiesVirtualModel()).size() > 0) {
							return (FMLRTVirtualModelInstance) getOnewayView().getVirtualModelInstancesForVirtualModel(getProcessPropertiesVirtualModel())
									.get(0);
						}
					}
					return null;
				}
				
				
				
				

	}
}
