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
import org.openflexo.foundation.fml.FlexoConcept;
import org.openflexo.foundation.fml.VirtualModel;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.localization.LocalizedDelegate;
import org.openflexo.module.oneway.OWConstants;
import org.openflexo.module.oneway.OnewayEditor;

/**
 * @author sylvain
 */
public class CreateProperty extends OWAction<CreateProperty, FMLRTVirtualModelInstance, FlexoObject> implements Bindable {

	public static final FlexoActionFactory<CreateProperty, FMLRTVirtualModelInstance, FlexoObject> ACTION_TYPE = new FlexoActionFactory<CreateProperty, FMLRTVirtualModelInstance, FlexoObject>(
			"create_property", FlexoActionFactory.defaultGroup, FlexoActionFactory.ADD_ACTION_TYPE) {

		@Override
		public CreateProperty makeNewAction(final FMLRTVirtualModelInstance focusedObject, final Vector<FlexoObject> globalSelection,
				final FlexoEditor editor) {
			return new CreateProperty(focusedObject, globalSelection, editor);
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

	public enum ScopeType {
		Globally, Before, After, Between, AfterUntil
	}

	public enum PatternType {
		Absence, Universality, Existence, BoundedExistence, Precedence, Response, ChainPrecedence, ChainResponse
	}

	public enum BounderType {
		AtLeast, Exactly, AtMost
	}

	private String name;
	private String description;
	private FlexoConceptInstance requirement;

	// Scope
	private ScopeType scopeType;
	private DataBinding<Boolean> scopeExpression1;
	private DataBinding<Boolean> scopeExpression2;

	// Pattern
	private PatternType patternType;
	private BounderType bounderType;
	private int counter;
	private DataBinding<Boolean> patternExpression1;
	private DataBinding<Boolean> patternExpression2;

	private FlexoConceptInstance newProperty;
	private VirtualModel processVM;
	private FMLRTVirtualModelInstance requirementsVMI;

	public CreateProperty(final FMLRTVirtualModelInstance focusedObject, final Vector<FlexoObject> globalSelection,
			final FlexoEditor editor) {
		super(ACTION_TYPE, focusedObject, globalSelection, editor);
		try {
			processVM = getFocusedObject().execute("processVM");
			requirementsVMI = getFocusedObject().execute("requirements");
		} catch (Exception e) {
			e.printStackTrace();
		}

		scopeType = ScopeType.Globally;
		scopeExpression1 = new DataBinding<Boolean>(this, Boolean.class, BindingDefinitionType.GET);
		scopeExpression1.setBindingName("scopeExpression1");
		scopeExpression2 = new DataBinding<Boolean>(this, Boolean.class, BindingDefinitionType.GET);
		scopeExpression2.setBindingName("scopeExpression2");

		patternType = PatternType.Existence;
		bounderType = BounderType.AtLeast;
		counter = 1;
		patternExpression1 = new DataBinding<Boolean>(this, Boolean.class, BindingDefinitionType.GET);
		patternExpression1.setBindingName("patternExpression1");
		patternExpression2 = new DataBinding<Boolean>(this, Boolean.class, BindingDefinitionType.GET);
		patternExpression2.setBindingName("patternExpression2");
	}

	public VirtualModel getProcessVirtualModel() {
		return processVM;
	}

	public FMLRTVirtualModelInstance getRequirementsVMI() {
		return requirementsVMI;
	}

	public FlexoConcept getRequirementType() {
		return getRequirementsVMI().getVirtualModel().getFlexoConcept(OWConstants.REQUIREMENT_CONCEPT_NAME);
	}

	@Override
	public BindingModel getBindingModel() {
		if (getProcessVirtualModel() != null) {
			return getProcessVirtualModel().getBindingModel();
		}
		return null;
	}

	@Override
	public BindingFactory getBindingFactory() {
		if (getProcessVirtualModel() != null) {
			return getProcessVirtualModel().getBindingFactory();
		}
		return null;
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

		System.out.println("Creons une property pour " + getFocusedObject());

		FlexoConceptInstance scope = makeScope();
		FlexoConceptInstance pattern = makePattern();

		try {
			newProperty = getFocusedObject().execute("this.createProperty({$name},{$description},{$requirement},{$scope},{$pattern})",
					getName(), getDescription(), getRequirement(), scope, pattern);
		} catch (Exception e) {
			throw new FlexoException(e);
		}

	}

	private FlexoConceptInstance makeScope() {
		try {
			switch (getScopeType()) {
				case Globally:
					return getFocusedObject().execute("this.createGloballyScope()");
				case Before:
					return getFocusedObject().execute("this.createBeforeScope({$expression})",
							makeBooleanExpression(getScopeExpression1()));
				case After:
					return getFocusedObject().execute("this.createAfterScope({$expression})", makeBooleanExpression(getScopeExpression1()));
				case Between:
					return getFocusedObject().execute("this.createBetweenScope({$expression1},{$expression2})",
							makeBooleanExpression(getScopeExpression1()), makeBooleanExpression(getScopeExpression2()));
				case AfterUntil:
					return getFocusedObject().execute("this.createAfterUntilScope({$expression1},{$expression2})",
							makeBooleanExpression(getScopeExpression1()), makeBooleanExpression(getScopeExpression2()));
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private FlexoConceptInstance makePattern() {
		try {
			switch (getPatternType()) {
				case Absence:
					return getFocusedObject().execute("this.createAbsencePattern({$expression})",
							makeBooleanExpression(getPatternExpression1()));
				case Universality:
					return getFocusedObject().execute("this.createUniversalityPattern({$expression})",
							makeBooleanExpression(getPatternExpression1()));
				case Existence:
					return getFocusedObject().execute("this.createExistencePattern({$expression})",
							makeBooleanExpression(getPatternExpression1()));
				case BoundedExistence:
					return getFocusedObject().execute("this.createBoundedExistencePattern({$bounderType},{$counter},{$expression})",
							getBounderType().name(), getCounter(), makeBooleanExpression(getPatternExpression1()));
				case Precedence:
					return getFocusedObject().execute("this.createPrecedencePattern({$expression1},{$expression2})",
							makeBooleanExpression(getPatternExpression1()), makeBooleanExpression(getPatternExpression2()));
				case Response:
					return getFocusedObject().execute("this.createResponsePattern({$expression1},{$expression2})",
							makeBooleanExpression(getPatternExpression1()), makeBooleanExpression(getPatternExpression2()));
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private FlexoConceptInstance makeBooleanExpression(DataBinding<Boolean> expression) {
		try {
			return getFocusedObject().execute("this.createBooleanExpression({$expression})", expression);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public FlexoConceptInstance getNewProperty() {
		return newProperty;
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

	public FlexoConceptInstance getRequirement() {
		return requirement;
	}

	public void setRequirement(FlexoConceptInstance requirement) {
		if ((requirement == null && this.requirement != null) || (requirement != null && !requirement.equals(this.requirement))) {
			FlexoConceptInstance oldValue = this.requirement;
			this.requirement = requirement;
			getPropertyChangeSupport().firePropertyChange("requirement", oldValue, requirement);
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

	public ScopeType getScopeType() {
		return scopeType;
	}

	public void setScopeType(ScopeType scopeType) {
		if (scopeType != this.scopeType) {
			ScopeType oldValue = this.scopeType;
			this.scopeType = scopeType;
			getPropertyChangeSupport().firePropertyChange("scopeType", oldValue, scopeType);
		}
	}

	public DataBinding<Boolean> getScopeExpression1() {
		return scopeExpression1;
	}

	public void setScopeExpression1(DataBinding<Boolean> scopeExpression1) {
		if (scopeExpression1 != null) {
			scopeExpression1.setOwner(this);
			scopeExpression1.setBindingName("scopeExpression1");
			scopeExpression1.setDeclaredType(Boolean.class);
			scopeExpression1.setBindingDefinitionType(BindingDefinitionType.GET);
		}
		this.scopeExpression1 = scopeExpression1;
		getPropertyChangeSupport().firePropertyChange(scopeExpression1.getBindingName(), false, true);
	}

	public DataBinding<Boolean> getScopeExpression2() {
		return scopeExpression2;
	}

	public void setScopeExpression2(DataBinding<Boolean> scopeExpression2) {
		if (scopeExpression2 != null) {
			scopeExpression2.setOwner(this);
			scopeExpression2.setBindingName("scopeExpression2");
			scopeExpression2.setDeclaredType(Boolean.class);
			scopeExpression2.setBindingDefinitionType(BindingDefinitionType.GET);
		}
		this.scopeExpression2 = scopeExpression2;
		getPropertyChangeSupport().firePropertyChange(scopeExpression2.getBindingName(), false, true);
	}

	public PatternType getPatternType() {
		return patternType;
	}

	public void setPatternType(PatternType patternType) {
		if (patternType != this.patternType) {
			PatternType oldValue = this.patternType;
			this.patternType = patternType;
			getPropertyChangeSupport().firePropertyChange("patternType", oldValue, patternType);
		}
	}

	public BounderType getBounderType() {
		return bounderType;
	}

	public void setBounderType(BounderType bounderType) {
		if (bounderType != this.bounderType) {
			BounderType oldValue = this.bounderType;
			this.bounderType = bounderType;
			getPropertyChangeSupport().firePropertyChange("bounderType", oldValue, bounderType);
		}
	}

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		if (counter != this.counter) {
			int oldValue = this.counter;
			this.counter = counter;
			getPropertyChangeSupport().firePropertyChange("counter", oldValue, counter);
		}
	}

	public DataBinding<Boolean> getPatternExpression1() {
		return patternExpression1;
	}

	public void setPatternExpression1(DataBinding<Boolean> patternExpression1) {
		if (patternExpression1 != null) {
			patternExpression1.setOwner(this);
			patternExpression1.setBindingName("patternExpression1");
			patternExpression1.setDeclaredType(Boolean.class);
			patternExpression1.setBindingDefinitionType(BindingDefinitionType.GET);
		}
		this.patternExpression1 = patternExpression1;
		getPropertyChangeSupport().firePropertyChange(patternExpression1.getBindingName(), false, true);
	}

	public DataBinding<Boolean> getPatternExpression2() {
		return patternExpression2;
	}

	public void setPatternExpression2(DataBinding<Boolean> patternExpression2) {
		if (patternExpression2 != null) {
			patternExpression2.setOwner(this);
			patternExpression2.setBindingName("patternExpression2");
			patternExpression2.setDeclaredType(Boolean.class);
			patternExpression2.setBindingDefinitionType(BindingDefinitionType.GET);
		}
		this.patternExpression2 = patternExpression2;
		getPropertyChangeSupport().firePropertyChange(patternExpression2.getBindingName(), false, true);
	}

	@Override
	public void notifiedBindingChanged(DataBinding<?> dataBinding) {
		System.out.println("**** notifiedBindingChanged for " + dataBinding.getBindingName() + " values " + dataBinding);
		getPropertyChangeSupport().firePropertyChange(dataBinding.getBindingName(), false, true);
	}

	@Override
	public void notifiedBindingDecoded(DataBinding<?> dataBinding) {
		System.out.println("**** notifiedBindingDecoded for " + dataBinding.getBindingName() + " values " + dataBinding);
		getPropertyChangeSupport().firePropertyChange(dataBinding.getBindingName(), false, true);
	}

}
