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

package org.openflexo.module.oneway.widget;

import java.io.FileNotFoundException;

import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.resource.ResourceLoadingCancelledException;
import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.model.FIBContainer;
import org.openflexo.gina.model.widget.FIBBrowser;
import org.openflexo.gina.swing.view.SwingViewFactory;
import org.openflexo.gina.swing.view.widget.JFIBBrowserWidget;
import org.openflexo.module.oneway.OWConstants;
import org.openflexo.module.oneway.controller.OWController;
import org.openflexo.module.oneway.controller.OWFIBController;
import org.openflexo.module.oneway.model.OnewayProjectNature;
import org.openflexo.rm.Resource;
import org.openflexo.technologyadapter.gina.model.FMLFIBBindingFactory;
import org.openflexo.view.FIBBrowserView;
import org.openflexo.view.controller.FlexoController;

/**
 * Abstract implementation of a browser of oneway project
 * 
 * @author sylvain
 */
@SuppressWarnings("serial")
public abstract class AbstractOnewayProjectBrowser extends FIBBrowserView<OnewayProjectNature> {

	private FIBBrowser browser;

	public AbstractOnewayProjectBrowser(final FlexoController controller, Resource fibResource) {
		super(controller.getProject() != null ? controller.getProject().getNature(OnewayProjectNature.class) : null, controller,
				fibResource, controller.getModuleLocales());
		getFIBController().setBrowser(this);

	}

	/*public void refresh() {
		System.out.println("Refresh du browser " + this);
		getFIBBrowserWidget().updateBrowser();
	}*/

	@Override
	public OnewayProjectBrowserFIBController getFIBController() {
		return (OnewayProjectBrowserFIBController) super.getFIBController();
	}

	@Override
	public OWController getFlexoController() {
		return (OWController) super.getFlexoController();
	}

	private VirtualModel onewayViewpoint;

	public VirtualModel getOnewayViewPoint() {
		if (onewayViewpoint == null && getFlexoController() != null) {
			try {
				onewayViewpoint = getFlexoController().getApplicationContext().getVirtualModelLibrary()
						.getVirtualModel(OWConstants.ONEWAY_ROOT_MODEL_URI);
			} catch (FileNotFoundException | ResourceLoadingCancelledException | FlexoException e) {
				e.printStackTrace();
			}
		}
		return onewayViewpoint;
	}

	@Override
	public void initializeFIBComponent() {

		super.initializeFIBComponent();

		getFIBComponent().setBindingFactory(new FMLFIBBindingFactory(getOnewayViewPoint()));

		browser = retrieveFIBBrowserNamed((FIBContainer) getFIBComponent(), "OnewayProjectBrowser");
		if (browser != null) {
			bindFlexoActionsToBrowser(browser);
		}
	}

	public FIBBrowser getBrowser() {
		return browser;
	}

	public JFIBBrowserWidget<?> getFIBBrowserWidget() {
		return (JFIBBrowserWidget<?>) getFIBController().viewForComponent(retrieveFIBBrowser((FIBContainer) getFIBComponent()));
	}

	/*@Override
	public void setDataObject(Object dataObject) {
	
		// System.out.println("setDataObject with " + dataObject + " in " + this);
	
		if (getDataObject() != null) {
			getFIBComponent().setBindingFactory(new FMLBindingFactory(getDataObject().getOnewayViewPoint()));
		}
		super.setDataObject(dataObject);
	
		if (getDataObject() != null && getFIBController() != null) {
			// System.out.println("onewayView=" + getDataObject().getOnewayInstance().getAccessedVirtualModelInstance());
			getFIBController().setVariableValue("onewayView", getDataObject().getOnewayInstance().getAccessedVirtualModelInstance());
		}
	
	}*/

	private Object selectedElement;

	public Object getSelectedElement() {
		return selectedElement;
	}

	public void setSelectedElement(Object selected) {
		selectedElement = selected;
	}

	public static class OnewayProjectBrowserFIBController extends OWFIBController {
		protected AbstractOnewayProjectBrowser browser;

		public OnewayProjectBrowserFIBController(FIBComponent component) {
			super(component, SwingViewFactory.INSTANCE);
			// Default parent localizer is the main localizer
			// setParentLocalizer(FlexoLocalization.getMainLocalizer());
		}

		protected void setBrowser(AbstractOnewayProjectBrowser browser) {
			this.browser = browser;
		}

		public Object getSelectedElement() {
			if (browser != null) {
				return browser.getSelectedElement();
			}
			return null;
		}

		public void setSelectedElement(Object selected) {
			if (browser != null) {
				browser.setSelectedElement(selected);
			}
		}

	}

}
