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

package org.openflexo.module.oneway.controller.action;

import java.awt.Dimension;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoActionWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.connie.DataBinding;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.module.oneway.OWIconLibrary;
import org.openflexo.module.oneway.model.action.CreateProperty;
import org.openflexo.module.oneway.model.action.CreateProperty.BounderType;
import org.openflexo.module.oneway.model.action.CreateProperty.PatternType;
import org.openflexo.module.oneway.model.action.CreateProperty.ScopeType;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class CreatePropertyWizard extends FlexoActionWizard<CreateProperty> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreatePropertyWizard.class.getPackage().getName());

	private final ConfigureProperty step1;
	private final ConfigureScope step2;
	private final ConfigurePattern step3;

	private static final Dimension DIMENSIONS = new Dimension(800, 400);

	public CreatePropertyWizard(CreateProperty action, FlexoController controller) {
		super(action, controller);
		addStep(step1 = new ConfigureProperty());
		addStep(step2 = new ConfigureScope());
		addStep(step3 = new ConfigurePattern());
		action.getPropertyChangeSupport().addPropertyChangeListener(step2);
		action.getPropertyChangeSupport().addPropertyChangeListener(step3);
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSIONS;
	}

	@Override
	public String getWizardTitle() {
		return getAction().getLocales().localizedForKey("create_property");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(OWIconLibrary.PROPERTIES_BIG_ICON, IconLibrary.BIG_NEW_MARKER).getImage();
	}

	public ConfigureProperty getStep1() {
		return step1;
	}

	public ConfigureScope getStep2() {
		return step2;
	}

	@FIBPanel("Fib/Wizard/ConfigureProperty.fib")
	public class ConfigureProperty extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateProperty getAction() {
			return CreatePropertyWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("configure_new_property");
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getName())) {
				setIssueMessage(getAction().getLocales().localizedForKey("you_should_provide_a_name_for_the_new_property"),
						IssueMessageType.ERROR);
				return false;
			}

			if (StringUtils.isEmpty(getDescription())) {
				setIssueMessage(getAction().getLocales().localizedForKey("it_is_recommanded_to_describe_property"),
						IssueMessageType.WARNING);
			}

			if (getRequirement() == null) {
				setIssueMessage(getAction().getLocales().localizedForKey("it_is_recommanded_to_reference_a_requirement"),
						IssueMessageType.WARNING);
			}

			return true;

		}

		public String getName() {
			return getAction().getName();
		}

		public void setName(String name) {
			if (!name.equals(getName())) {
				String oldValue = getName();
				getAction().setName(name);
				getPropertyChangeSupport().firePropertyChange("name", oldValue, name);
				checkValidity();
			}
		}

		public FlexoConceptInstance getRequirement() {
			return getAction().getRequirement();
		}

		public void setRequirement(FlexoConceptInstance requirement) {
			if (requirement != getRequirement()) {
				FlexoConceptInstance oldValue = getRequirement();
				getAction().setRequirement(requirement);
				getPropertyChangeSupport().firePropertyChange("requirement", oldValue, requirement);
				checkValidity();
			}
		}

		public String getDescription() {
			return getAction().getDescription();
		}

		public void setDescription(String description) {
			if (!description.equals(getDescription())) {
				String oldValue = getDescription();
				getAction().setDescription(description);
				getPropertyChangeSupport().firePropertyChange("description", oldValue, description);
				checkValidity();
			}
		}

	}

	@FIBPanel("Fib/Wizard/ConfigureScope.fib")
	public class ConfigureScope extends WizardStep implements PropertyChangeListener {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateProperty getAction() {
			return CreatePropertyWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("choose_scope");
		}

		@Override
		public boolean isValid() {

			if (getScopeType() == null) {
				setIssueMessage(getAction().getLocales().localizedForKey("you_should_provide_a_scope"), IssueMessageType.ERROR);
			}

			switch (getScopeType()) {
				case Globally:
					break;
				case After:
				case Before:
					if (!getScopeExpression1().isSet()) {
						setIssueMessage(getAction().getLocales().localizedForKey("you_should_define_scope_expression"),
								IssueMessageType.ERROR);
						return false;
					}
					if (!getScopeExpression1().isValid()) {
						System.out.println("Pas bon: " + getScopeExpression1().invalidBindingReason());
						setIssueMessage("<html>" + getScopeExpression1().invalidBindingReason() + "</html>", IssueMessageType.ERROR);
						return false;
					}
					break;
				case Between:
				case AfterUntil:
					if (!getScopeExpression1().isSet()) {
						setIssueMessage(getAction().getLocales().localizedForKey("you_should_define_first_scope_expression"),
								IssueMessageType.ERROR);
						return false;
					}
					if (!getScopeExpression1().isValid()) {
						setIssueMessage(getScopeExpression1().invalidBindingReason(), IssueMessageType.ERROR);
						return false;
					}
					if (!getScopeExpression2().isSet()) {
						setIssueMessage(getAction().getLocales().localizedForKey("you_should_define_second_scope_expression"),
								IssueMessageType.ERROR);
						return false;
					}
					if (!getScopeExpression2().isValid()) {
						setIssueMessage(getScopeExpression2().invalidBindingReason(), IssueMessageType.ERROR);
						return false;
					}
					break;
			}

			return true;

		}

		public ScopeType getScopeType() {
			return getAction().getScopeType();
		}

		public void setScopeType(ScopeType scopeType) {
			if (scopeType != getScopeType()) {
				ScopeType oldValue = getScopeType();
				getAction().setScopeType(scopeType);
				getPropertyChangeSupport().firePropertyChange("scopeType", oldValue, scopeType);
				checkValidity();
			}
		}

		public DataBinding<?> getScopeExpression1() {
			return getAction().getScopeExpression1();
		}

		public DataBinding<?> getScopeExpression2() {
			return getAction().getScopeExpression2();
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			checkValidity();
		}
	}

	@FIBPanel("Fib/Wizard/ConfigurePattern.fib")
	public class ConfigurePattern extends WizardStep implements PropertyChangeListener {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateProperty getAction() {
			return CreatePropertyWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("choose_pattern");
		}

		@Override
		public boolean isValid() {

			if (getPatternType() == null) {
				setIssueMessage(getAction().getLocales().localizedForKey("you_should_provide_a_pattern"), IssueMessageType.ERROR);
				return false;
			}

			switch (getPatternType()) {
				case Existence:
				case Universality:
				case Absence:
					if (!getPatternExpression1().isSet()) {
						setIssueMessage(getAction().getLocales().localizedForKey("you_should_define_pattern_expression"),
								IssueMessageType.ERROR);
						return false;
					}
					if (!getPatternExpression1().isValid()) {
						setIssueMessage(getPatternExpression1().invalidBindingReason(), IssueMessageType.ERROR);
						return false;
					}
					break;
				case BoundedExistence:
					if (!getPatternExpression1().isSet()) {
						setIssueMessage(getAction().getLocales().localizedForKey("you_should_define_pattern_expression"),
								IssueMessageType.ERROR);
						return false;
					}
					if (!getPatternExpression1().isValid()) {
						setIssueMessage(getPatternExpression1().invalidBindingReason(), IssueMessageType.ERROR);
						return false;
					}
					if (getBounderType() == null) {
						setIssueMessage(getAction().getLocales().localizedForKey("you_should_provide_a_bounder_type"),
								IssueMessageType.ERROR);
						return false;
					}
					if (getCounter() < 1) {
						setIssueMessage(getAction().getLocales().localizedForKey("counter_should_be_greater_or_equals_to_1"),
								IssueMessageType.ERROR);
						return false;
					}
					break;
				case Precedence:
				case Response:
					if (!getPatternExpression1().isSet()) {
						setIssueMessage(getAction().getLocales().localizedForKey("you_should_define_first_pattern_expression"),
								IssueMessageType.ERROR);
						return false;
					}
					if (!getPatternExpression1().isValid()) {
						setIssueMessage(getPatternExpression1().invalidBindingReason(), IssueMessageType.ERROR);
						return false;
					}
					if (!getPatternExpression2().isSet()) {
						setIssueMessage(getAction().getLocales().localizedForKey("you_should_define_second_pattern_expression"),
								IssueMessageType.ERROR);
						return false;
					}
					if (!getPatternExpression2().isValid()) {
						setIssueMessage(getPatternExpression2().invalidBindingReason(), IssueMessageType.ERROR);
						return false;
					}
					break;
			}

			return true;

		}

		public PatternType getPatternType() {
			return getAction().getPatternType();
		}

		public void setPatternType(PatternType patternType) {
			if (patternType != getPatternType()) {
				PatternType oldValue = getPatternType();
				getAction().setPatternType(patternType);
				getPropertyChangeSupport().firePropertyChange("patternType", oldValue, patternType);
				checkValidity();
			}
		}

		public BounderType getBounderType() {
			return getAction().getBounderType();
		}

		public void setBounderType(BounderType bounderType) {
			if (bounderType != getBounderType()) {
				BounderType oldValue = getBounderType();
				getAction().setBounderType(bounderType);
				getPropertyChangeSupport().firePropertyChange("bounderType", oldValue, bounderType);
				checkValidity();
			}
		}

		public int getCounter() {
			return getAction().getCounter();
		}

		public void setCounter(int counter) {
			if (counter != getCounter()) {
				int oldValue = getCounter();
				getAction().setCounter(counter);
				getPropertyChangeSupport().firePropertyChange("counter", oldValue, counter);
				checkValidity();
			}
		}

		public DataBinding<?> getPatternExpression1() {
			return getAction().getPatternExpression1();
		}

		public DataBinding<?> getPatternExpression2() {
			return getAction().getPatternExpression2();
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			checkValidity();
		}
	}

}
