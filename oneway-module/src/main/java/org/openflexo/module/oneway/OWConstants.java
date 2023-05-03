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

package org.openflexo.module.oneway;

/**
 * References all constants used in the scope of Oneway project
 * 
 * @author sylvain
 */
public class OWConstants {

	public static final String ONEWAY_RC_URI = "http://ensta-bretagne.fr/oneway";
	public static final String ONEWAY_ROOT_MODEL_URI = ONEWAY_RC_URI + "/Oneway.fml";

	public static final String ONEWAY_VM_NAME = "Oneway";
	public static final String ONEWAY_ROOT_VMI_NAME = "OnewayVMI";
	public static final String ONEWAY_CREATION_SCHEME_NAME = "init";

	public static final String DOCUMENT_LIBRARY_VIEWPOINT_URI = ONEWAY_RC_URI + "/DocumentLibrary.fml";

	public static final String REQUIREMENTS_VM_NAME = "Requirements";
	public static final String ELEMENT_CONCEPT_NAME = "Element";
	public static final String ELEMENT_IDENTIFIER_ROLE_NAME = "identifier";
	public static final String PARENT_ROLE_NAME = "parent";
	public static final String CHILDREN_ELEMENTS_ROLE_NAME = "childrenElements";
	public static final String REQUIREMENT_PROPERTY_NAME = "requirements";
	public static final String REQUIREMENT_CONCEPT_NAME = "Requirement";
	public static final String PROJECT_ELEMENT_ROLE_NAME = "projectElement";

	public static final String DOCUMENT_LIBRARY_VM_NAME = "DocumentLibrary";
	public static final String ABSTRACT_DOCUMENT_VM_NAME = "AbstractDocument";
	public static final String REFERENCE_GROUP_CONCEPT_NAME = "ReferenceGroup";
	public static final String REFERENCE_CONCEPT_NAME = "Reference";
	public static final String REFERENCES_ROLE_NAME = "references";
	public static final String NAME_ROLE_NAME = "name";
	public static final String DESCRIPTION_ROLE_NAME = "description";
	public static final String RAW_TEXT_ROLE_NAME = "rawText";

	public static final String DOCUMENT_ANNOTATIONS_VM_NAME = "DocumentAnnotations";
	public static final String UNCLASSIFIED_CONCEPT_NAME = "Unclassified";
	public static final String ELEMENT_REFERENCE_CONCEPT_NAME = "ElementReference";
	public static final String REQUIREMENT_REFERENCE_CONCEPT_NAME = "RequirementReference";

	public static final String PROCESS_DEFINITION_VM_NAME = "ProcessDefinition";

	public static final String BPMN_EDITOR_URI = "http://openflexo.org/modellers/resources/BPMN/FML/BPMNEditor.fml";
	public static final String BPMN_MODEL_URI = "http://openflexo.org/modellers/resources/BPMN/FML/BPMNEditor.fml/BPMNModel.fml";
	public static final String BPMN_DIAGRAM_URI = "http://openflexo.org/modellers/resources/BPMN/FML/BPMNEditor.fml/ProcessDiagram.fml";

	public static final String PROPERTIES_VM_URI = ONEWAY_ROOT_MODEL_URI + "/Properties.fml";
	public static final String PROCESS_PROPERTIES_VM_NAME = "ProcessProperties";
	public static final String PROCESS_PROPERTIES_VM_URI = PROPERTIES_VM_URI + "/" + PROCESS_PROPERTIES_VM_NAME + ".fml";
	public static final String ATOMIC_PROPOSITION_CONCEPT_NAME = "AtomicProposition";
	public static final String PROPERTY_CONCEPT_NAME = "Property";

	private OWConstants() {
		// Only constants, prevent possible instantiation.
	}
}
