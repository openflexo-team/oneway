/*
 * (c) Copyright 2013- Openflexo
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.openflexo.module.oneway.view;

import javax.swing.ImageIcon;

import org.openflexo.gina.model.FIBComponent;
import org.openflexo.gina.view.GinaViewFactory;
import org.openflexo.gina.view.widget.FIBBrowserWidget;
import org.openflexo.module.oneway.OWIconLibrary;
import org.openflexo.module.oneway.controller.OWFIBController;
import org.openflexo.module.oneway.model.OnewayProjectNature;
import org.openflexo.rm.Resource;
import org.openflexo.rm.ResourceLocator;
import org.openflexo.technologyadapter.gina.model.FMLFIBBindingFactory;
import org.openflexo.view.FIBModuleView;
import org.openflexo.view.controller.FlexoController;
import org.openflexo.view.controller.model.FlexoPerspective;

/**
 * Main view for Oneway
 */
@SuppressWarnings("serial")
public class OnewayProjectNatureModuleView extends FIBModuleView<OnewayProjectNature> {

	public static Resource FMS_PROJECT_MODULE_VIEW_FIB = ResourceLocator.locateResource("Fib/OWProjectNaturePanel.fib");

	private final FlexoPerspective perspective;

	public OnewayProjectNatureModuleView(OnewayProjectNature nature, FlexoController controller, FlexoPerspective perspective) {
		super(nature, controller, FMS_PROJECT_MODULE_VIEW_FIB, controller.getModule().getLocales());
		this.perspective = perspective;

		FIBBrowserWidget<?, ?> browserView = (FIBBrowserWidget<?, ?>) getFIBView("ElementBrowser");
		// System.out.println("Found browser: " + browserView);
		/*browser = retrieveFIBBrowserNamed((FIBContainer) getFIBComponent(), "ElementBrowser");
		if (browser != null) {
			bindFlexoActionsToBrowser(browser);
		}*/

		setDataObject(nature);

	}

	@Override
	public FlexoPerspective getPerspective() {
		return perspective;
	}

	@Override
	public OnewayProjectNature getDataObject() {
		return (OnewayProjectNature) super.getDataObject();
	}

	@Override
	public void initializeFIBComponent() {

		super.initializeFIBComponent();

		getFIBComponent().setBindingFactory(new FMLFIBBindingFactory(getDataObject().getOnewayViewPoint()));

		/*if (getDataObject() != null) {
			getFIBComponent().setBindingFactory(new FMLBindingFactory(getDataObject().getOnewayVirtualModel()));
		}*/

	}

	@Override
	public void setDataObject(Object dataObject) {

		System.out.println("------> setDataObject with " + dataObject + " in " + this);

		/*if (getDataObject() != null) {
			getFIBComponent().setBindingFactory(new FMLBindingFactory(getDataObject().getOnewayViewPoint()));
		}*/

		if (dataObject instanceof OnewayProjectNature && getFIBController() != null) {
			System.out.println("onewayInstance=" + ((OnewayProjectNature) dataObject).getOnewayInstance());
			System.out.println("onewayView=" + ((OnewayProjectNature) dataObject).getOnewayInstance().getAccessedVirtualModelInstance());
			getFIBController().setVariableValue("onewayView",
					((OnewayProjectNature) dataObject).getOnewayInstance().getAccessedVirtualModelInstance());
		}

		super.setDataObject(dataObject);
	}

	public static class OnewayProjectNatureModuleViewFIBController extends OWFIBController {
		public OnewayProjectNatureModuleViewFIBController(FIBComponent component, GinaViewFactory<?> viewFactory) {
			super(component, viewFactory);
		}

		public OnewayProjectNatureModuleViewFIBController(FIBComponent component, GinaViewFactory<?> viewFactory,
				FlexoController controller) {
			super(component, viewFactory, controller);
		}

		public ImageIcon getProjectIcon() {
			return OWIconLibrary.OW_BIG_ICON;
		}

	}

}
