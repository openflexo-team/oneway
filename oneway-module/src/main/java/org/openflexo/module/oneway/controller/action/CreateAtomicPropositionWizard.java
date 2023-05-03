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
import java.util.logging.Logger;

import org.openflexo.ApplicationContext;
import org.openflexo.components.wizard.FlexoActionWizard;
import org.openflexo.components.wizard.WizardStep;
import org.openflexo.connie.DataBinding;
import org.openflexo.gina.annotation.FIBPanel;
import org.openflexo.icon.IconFactory;
import org.openflexo.icon.IconLibrary;
import org.openflexo.module.oneway.OWIconLibrary;
import org.openflexo.module.oneway.model.action.CreateAtomicProposition;
import org.openflexo.toolbox.StringUtils;
import org.openflexo.view.controller.FlexoController;

public class CreateAtomicPropositionWizard extends FlexoActionWizard<CreateAtomicProposition> {

	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(CreateAtomicPropositionWizard.class.getPackage().getName());

	private final ConfigureAtomicProposition configuration;

	private static final Dimension DIMENSIONS = new Dimension(600, 400);

	public CreateAtomicPropositionWizard(CreateAtomicProposition action, FlexoController controller) {
		super(action, controller);
		addStep(configuration = new ConfigureAtomicProposition());
	}

	@Override
	public Dimension getPreferredSize() {
		return DIMENSIONS;
	}

	@Override
	public String getWizardTitle() {
		return getAction().getLocales().localizedForKey("create_atomic_proposition");
	}

	@Override
	public Image getDefaultPageImage() {
		return IconFactory.getImageIcon(OWIconLibrary.ATOMIC_PROPOSITION_BIG_ICON, IconLibrary.BIG_NEW_MARKER).getImage();
	}

	public ConfigureAtomicProposition getConfiguration() {
		return configuration;
	}

	@FIBPanel("Fib/Wizard/ConfigureAtomicProposition.fib")
	public class ConfigureAtomicProposition extends WizardStep {

		public ApplicationContext getServiceManager() {
			return getController().getApplicationContext();
		}

		public CreateAtomicProposition getAction() {
			return CreateAtomicPropositionWizard.this.getAction();
		}

		@Override
		public String getTitle() {
			return getAction().getLocales().localizedForKey("configure_new_atomic_proposition");
		}

		@Override
		public boolean isValid() {

			if (StringUtils.isEmpty(getName())) {
				setIssueMessage(getAction().getLocales().localizedForKey("you_should_provide_a_name_for_the_new_atomic_proposition"),
						IssueMessageType.ERROR);
				return false;
			}

			if (StringUtils.isEmpty(getDescription())) {
				setIssueMessage(getAction().getLocales().localizedForKey("it_is_recommanded_to_describe_atomic_proposition"),
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
				getPropertyChangeSupport().firePropertyChange("identifier", null, getIdentifier());
				checkValidity();
			}
		}

		public String getIdentifier() {
			return getAction().getIdentifier();
		}

		public void setIdentifier(String identifier) {
			if (!identifier.equals(getIdentifier())) {
				String oldValue = getIdentifier();
				getAction().setIdentifier(identifier);
				getPropertyChangeSupport().firePropertyChange("identifier", oldValue, identifier);
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

		public DataBinding<?> getExpression() {
			return getAction().getExpression();
		}

	}

}
