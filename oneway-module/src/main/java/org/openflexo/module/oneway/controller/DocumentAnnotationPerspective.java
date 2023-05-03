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

import javax.swing.ImageIcon;

import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoProject;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.module.oneway.OWIconLibrary;
import org.openflexo.module.oneway.model.OnewayProjectNature;
import org.openflexo.module.oneway.view.DocXRequirementModuleView;
import org.openflexo.module.oneway.view.DocumentAnnotationModuleView;
import org.openflexo.module.oneway.widget.AbstractOnewayProjectBrowser;
import org.openflexo.module.oneway.widget.DocumentAnnotationBrowser;
import org.openflexo.module.oneway.widget.ReferencesBrowser;
import org.openflexo.technologyadapter.docx.nature.FMLControlledDocXVirtualModelInstanceNature;
import org.openflexo.technologyadapter.gina.fml.FMLControlledFIBFlexoConceptInstanceNature;
import org.openflexo.technologyadapter.gina.view.FMLControlledFIBFlexoConceptInstanceModuleView;
import org.openflexo.view.ModuleView;

/**
 * Doc annotation perspective for Oneway module
 * 
 * @author sylvain
 */
public class DocumentAnnotationPerspective extends OnewayPerspective {

	private final ReferencesBrowser referencesBrowser;

	public DocumentAnnotationPerspective(final OWController controller) {
		super("document_annotation", controller);
		referencesBrowser = new ReferencesBrowser(controller) {
			@Override
			public void setSelectedElement(Object selected) {
				super.setSelectedElement(selected);
				/*if (selected instanceof FlexoConceptInstance && getFormoseProject().getReferenceConcept()
						.isAssignableFrom(((FlexoConceptInstance) selected).getFlexoConcept())) {
					// DocXFragment fragment = ((FlexoConceptInstance) selected).getFlexoActor(FMSConstants.FRAGMENT_ROLE_NAME);
					selectReferenceInDocumentEditor((FlexoConceptInstance) selected);
				}
				else {
					selectElementInDocumentEditor(null);
				}*/
				// System.out.println("Hop, on selectionne: " + selected);
			}
		};

		// setTopRightView(referencesBrowser);
		// controller.getControllerModel().setRightViewVisible(true);

	}

	@Override
	public OWController getController() {
		return (OWController) super.getController();
	}

	public void showReferencesBrowser() {
		setTopRightView(getReferencesBrowser());
		getController().getControllerModel().setRightViewVisible(true);
		getReferencesBrowser().setEnableSynchronization(true);
		getReferencesBrowser().setDataObject(getController().getOnewayNature());
	}

	public void hideReferencesBrowser() {
		getController().getControllerModel().setRightViewVisible(false);
		getReferencesBrowser().setEnableSynchronization(false);
	}

	public ReferencesBrowser getReferencesBrowser() {
		return referencesBrowser;
	}

	@Override
	public ImageIcon getActiveIcon() {
		return OWIconLibrary.DOC_LIBRARY_ICON;
	}

	@Override
	public ModuleView<?> createModuleViewForObject(FlexoObject object) {

		if (object instanceof OnewayProjectNature) {
			return new DocumentAnnotationModuleView((OnewayProjectNature) object, getController(), this);
		}

		if (object instanceof FMLRTVirtualModelInstance) {
			if (((FMLRTVirtualModelInstance) object).getVirtualModel().getName().equals("DocumentLibrary")) {
				return new DocumentAnnotationModuleView(getController().getOnewayNature(), getController(), this);
			}
			// FML-controlled document
			if (((FMLRTVirtualModelInstance) object).hasNature(FMLControlledDocXVirtualModelInstanceNature.INSTANCE)) {
				// DocXAdapterController docXTAC = ((FMSController) getController()).getDocXTAC();
				// return new FMLControlledDocXDocumentModuleView((FMLRTVirtualModelInstance) object, this);
				return new DocXRequirementModuleView((FMLRTVirtualModelInstance) object, this);
			}
		}

		if (object instanceof FlexoConceptInstance) {

			if (((FlexoConceptInstance) object).hasNature(FMLControlledFIBFlexoConceptInstanceNature.INSTANCE)) {

				return new FMLControlledFIBFlexoConceptInstanceModuleView((FlexoConceptInstance) object, getController(), this,
						getController().getModuleLocales()) {
					@Override
					public void willHide() {
						super.willHide();
						setBottomLeftView(null);
						hideReferencesBrowser();
					}

					@Override
					public void willShow() {
						super.willShow();
						// setBottomLeftView(browser);
						getController().getControllerModel().setLeftViewVisible(true);
						showReferencesBrowser();
					}

					@Override
					protected void updateAsNormalMode() {
						super.updateAsNormalMode();
						getController().getControllerModel().setLeftViewVisible(true);
						showReferencesBrowser();
					}
				};
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
			if (((FMLRTVirtualModelInstance) object).getVirtualModel().getName().equals("DocumentLibrary")) {
				return true;
			}
			// FML-controlled document
			if (((FMLRTVirtualModelInstance) object).hasNature(FMLControlledDocXVirtualModelInstanceNature.INSTANCE)) {
				return true;
			}
			return false;
		}
		if (object instanceof FlexoConceptInstance) {
			if (((FlexoConceptInstance) object).hasNature(FMLControlledFIBFlexoConceptInstanceNature.INSTANCE)) {
				return true;
			}
		}
		return super.hasModuleViewForObject(object);
	}

	@Override
	protected AbstractOnewayProjectBrowser makeOnewayProjectBrowser() {
		return new DocumentAnnotationBrowser(getController());
	}

	@Override
	public void setProject(FlexoProject<?> project) {
		super.setProject(project);
		referencesBrowser.setDataObject(project != null ? project.getNature(OnewayProjectNature.class) : null);
	}

}
