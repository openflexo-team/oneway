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

import javax.swing.ImageIcon;

import org.openflexo.icon.IconMarker;
import org.openflexo.icon.ImageIconResource;
import org.openflexo.rm.ResourceLocator;

/**
 * Icon resources library in the context of Oneway prototype
 * 
 * @author sylvain
 *
 */
public class OWIconLibrary {

	public static final ImageIcon ONEWAY_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/OnewayLogo_128x128.png"));

	public static final ImageIcon OW_SMALL_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/OnewayLogo_16x16.png"));
	public static final ImageIcon OW_MEDIUM_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/OnewayLogo_32x32.png"));
	public static final ImageIcon OW_MEDIUM_ICON_HOVER = new ImageIconResource(
			ResourceLocator.locateResource("Icons/OnewayLogo_64x64.png"));
	public static final ImageIcon OW_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/OnewayLogo_64x64.png"));

	public static final ImageIcon BPMN_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/LogoBPMN.png"));
	public static final ImageIcon BPMN_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/BPMN_64x64.png"));
	public static final IconMarker BPMN_BIG_MARKER = new IconMarker(BPMN_BIG_ICON, 32, 0);

	public static final ImageIcon ELEMENT_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/ElementIcon_16x16.png"));
	public static final ImageIcon ELEMENT_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/ElementIcon_32x32.png"));

	public static final ImageIcon REQUIREMENT_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/RequirementIcon_16x16.png"));
	public static final ImageIcon REQUIREMENT_BIG_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/RequirementIcon_32x32.png"));

	public static final ImageIcon DOC_LIBRARY_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/DocLibrary_16x16.png"));
	public static final ImageIcon DOC_LIBRARY_BIG_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/DocLibrary_32x32.png"));
	public static final IconMarker DOC_BIG_MARKER = new IconMarker(DOC_LIBRARY_BIG_ICON, 32, 0);

	public static final ImageIcon PROPERTIES_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Check_16x16.png"));
	public static final ImageIcon PROPERTIES_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Check_32x32.png"));
	public static final IconMarker PROPERTIES_BIG_MARKER = new IconMarker(PROPERTIES_BIG_ICON, 32, -5);

	public static final ImageIcon ATOMIC_PROPOSITION_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/Check2_16x16.png"));
	public static final ImageIcon ATOMIC_PROPOSITION_BIG_ICON = new ImageIconResource(
			ResourceLocator.locateResource("Icons/Check2_32x32.png"));

	// public static final IconMarker FMS_MARKER = new IconMarker(new
	// ImageIconResource(ResourceLocator.locateResource("Icons/FMSMarker.png")),
	// 8, 0);
	// public static final IconMarker FMS_BIG_MARKER = new IconMarker(OW_MEDIUM_ICON, 32, 0);

	/*	
	
	
		public static final ImageIcon SYSML_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/SysML_16x16.png"));
		public static final ImageIcon SYSML_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/SysML_32x32.png"));
		public static final IconMarker SYSML_BIG_MARKER = new IconMarker(SYSML_BIG_ICON, 32, 0);
	
		public static final IconMarker DOMAIN_MODEL_BIG_MARKER = new IconMarker(OWLIconLibrary.ONTOLOGY_LIBRARY_BIG_ICON, 32, 0);
	
		public static final ImageIcon B_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/AtelierB_16x16.png"));
		public static final ImageIcon B_BIG_ICON = new ImageIconResource(ResourceLocator.locateResource("Icons/AtelierB_32x32.png"));
		public static final IconMarker B_BIG_MARKER = new IconMarker(B_BIG_ICON, 32, 0);*/

}
