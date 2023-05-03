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

import java.util.Vector;

import org.openflexo.ApplicationContext;
import org.openflexo.connie.Bindable;
import org.openflexo.connie.BindingFactory;
import org.openflexo.connie.BindingModel;
import org.openflexo.connie.DataBinding;
import org.openflexo.connie.DataBinding.BindingDefinitionType;
import org.openflexo.foundation.FlexoEditor;
import org.openflexo.foundation.FlexoException;
import org.openflexo.foundation.FlexoObject;
import org.openflexo.foundation.FlexoObject.FlexoObjectImpl;
import org.openflexo.foundation.action.FlexoActionFactory;
import org.openflexo.foundation.fml.ActionScheme;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.foundation.fml.rt.action.ActionSchemeAction;
import org.openflexo.foundation.fml.rt.action.ActionSchemeActionFactory;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.module.oneway.OWConstants;
import org.openflexo.module.oneway.OnewayEditor;
import org.openflexo.toolbox.JavaUtils;

/**
 * @author sylvain
 */
public class CreateAtomicProposition extends OWAction<CreateAtomicProposition, FMLRTVirtualModelInstance, FlexoObject> implements Bindable {

	public static final FlexoActionFactory<CreateAtomicProposition, FMLRTVirtualModelInstance, FlexoObject> ACTION_TYPE = new FlexoActionFactory<CreateAtomicProposition, FMLRTVirtualModelInstance, FlexoObject>(
			"create_atomic_proposition", FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

		@Override
		public CreateAtomicProposition makeNewAction(final FMLRTVirtualModelInstance focusedObject,
				final Vector<FlexoObject> globalSelection, final FlexoEditor editor) {
			return new CreateAtomicProposition(focusedObject, globalSelection, editor);
		}

		@Override
		public boolean isVisibleForSelection(final FMLRTVirtualModelInstance vmi, final Vector<FlexoObject> globalSelection) {
			return vmi != null && vmi.getVirtualModel() != null
					&& vmi.getVirtualModel().getName().equals(OWConstants.PROCESS_PROPERTIES_VM_NAME);
		}

		@Override
		public boolean isEnabledForSelection(final FMLRTVirtualModelInstance element, final Vector<FlexoObject> globalSelection) {
			return isVisibleForSelection(element, globalSelection);
		}
	};

	static {
		FlexoObjectImpl.addActionForClass(ACTION_TYPE, FMLRTVirtualModelInstance.class);
	}

	private String name;
	private String identifier;
	private String description;
	private DataBinding<?> expression;

	private FlexoConceptInstance newAtomicProposition;
	private VirtualModel processVM;

	public CreateAtomicProposition(final FMLRTVirtualModelInstance focusedObject, final Vector<FlexoObject> globalSelection,
			final FlexoEditor editor) {
		super(ACTION_TYPE, focusedObject, globalSelection, editor);
		try {
			processVM = getFocusedObject().execute("processVM");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("processVM=" + processVM);
		expression = new DataBinding<Object>(this, Object.class, BindingDefinitionType.GET);
	}

	public VirtualModel getProcessVirtualModel() {
		return processVM;
	}

	@Override
	public BindingModel getBindingModel() {
		return getProcessVirtualModel().getBindingModel();
	}

	@Override
	public BindingFactory getBindingFactory() {
		return getProcessVirtualModel().getBindingFactory();
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

		System.out.println("Creons une atomic proposition pour " + getFocusedObject() + " et " + getExpression());

		VirtualModel processPropertiesVM = getFocusedObject().getVirtualModel();

		ActionScheme createAtomicPropositionBehaviour = (ActionScheme) processPropertiesVM.getFlexoBehaviour("createNewAtomicProposition",
				String.class, String.class, String.class, DataBinding.class);
		ActionSchemeActionFactory actionType = new ActionSchemeActionFactory(createAtomicPropositionBehaviour, getFocusedObject());

		ActionSchemeAction action = actionType.makeNewEmbeddedAction(getFocusedObject(), null, this);
		action.setParameterValue(createAtomicPropositionBehaviour.getParameter("name"), getName());
		action.setParameterValue(createAtomicPropositionBehaviour.getParameter("identifier"), getIdentifier());
		action.setParameterValue(createAtomicPropositionBehaviour.getParameter("expression"), getExpression());
		if (getDescription() != null) {
			action.setParameterValue(createAtomicPropositionBehaviour.getParameter("description"), getDescription());
		}
		action.doAction();

		newAtomicProposition = (FlexoConceptInstance) action.getReturnedValue();

	}

	public FlexoConceptInstance getNewAtomicProposition() {
		return newAtomicProposition;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if ((name == null && this.name != null) || (name != null && !name.equals(this.name))) {
			String oldValue = this.name;
			this.name = name;
			getPropertyChangeSupport().firePropertyChange("name", oldValue, name);
		}
	}

	public String getIdentifier() {
		if (identifier == null && getName() != null) {
			return JavaUtils.getVariableName(getName());
		}
		return identifier;
	}

	public void setIdentifier(String identifier) {
		if ((identifier == null && this.identifier != null) || (identifier != null && !identifier.equals(this.identifier))) {
			String oldValue = this.identifier;
			this.identifier = identifier;
			getPropertyChangeSupport().firePropertyChange("identifier", oldValue, identifier);
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if ((description == null && this.description != null) || (description != null && !description.equals(this.description))) {
			String oldValue = this.description;
			this.description = description;
			getPropertyChangeSupport().firePropertyChange("description", oldValue, description);
		}
	}

	public DataBinding<?> getExpression() {
		return expression;
	}

	public void setExpression(DataBinding<?> expression) {
		if ((expression == null && this.expression != null) || (expression != null && !expression.equals(this.expression))) {
			DataBinding<?> oldValue = this.expression;
			this.expression = expression;
			getPropertyChangeSupport().firePropertyChange("expression", oldValue, expression);
		}
	}

	@Override
	public void notifiedBindingChanged(DataBinding<?> dataBinding) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifiedBindingDecoded(DataBinding<?> dataBinding) {
		// TODO Auto-generated method stub

	}

}
