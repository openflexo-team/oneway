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

package org.openflexo.module.oneway.menu;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import org.openflexo.FlexoCst;
import org.openflexo.connie.exception.InvalidBindingException;
import org.openflexo.connie.exception.NullReferenceException;
import org.openflexo.connie.exception.TypeMismatchException;
import org.openflexo.foundation.fml.rt.FMLRTVirtualModelInstance;
import org.openflexo.foundation.fml.rt.FlexoConceptInstance;
import org.openflexo.icon.IconLibrary;
import org.openflexo.module.oneway.controller.OWController;
import org.openflexo.module.oneway.model.OnewayProjectNature;
import org.openflexo.view.menu.FileMenu;
import org.openflexo.view.menu.FlexoMenuItem;

/**
 * 'File' menu for Oneway Prototype
 *
 * @author sylvain
 */
@SuppressWarnings("serial")
public class OWFileMenu extends FileMenu {

	public OWFileMenu(OWController controller) {
		super(controller);
	}

	public OWController getOWController() {
		return (OWController) getController();
	}

	@Override
	public void addSpecificItems() {
		add(new SaveModifiedItem());
		addSeparator();
		add(new GenerateGPSLItem());
		addSeparator();
	}

	@Override
	public void quit() {
		if (getOWController().getApplicationContext().getResourceManager().getUnsavedResources().size() == 0) {
			super.quit();
		}
		else if (getOWController().reviewModifiedResources()) {
			super.quit();
		}

	}

	public class SaveModifiedItem extends FlexoMenuItem {
		public SaveModifiedItem() {
			super(new SaveModifiedAction(), "save", KeyStroke.getKeyStroke(KeyEvent.VK_S, FlexoCst.META_MASK), getController(), true);
			setIcon(IconLibrary.SAVE_ICON);
		}
	}

	public class SaveModifiedAction extends AbstractAction {
		public SaveModifiedAction() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			getOWController().reviewModifiedResources();
		}
	}


	public class GenerateGPSLItem extends FlexoMenuItem {
		public GenerateGPSLItem() {
			super(new GenerateGPSLAction(), "Générer le GPSL", null, getController(), true);
		}
	}

	public class GenerateGPSLAction extends AbstractAction {
		public GenerateGPSLAction() {
			super();
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			OnewayProjectNature onewayNature = getOWController().getOnewayNature();
			System.out.println("onewayNature="+onewayNature);
			FMLRTVirtualModelInstance onewayView = onewayNature.getOnewayView(); // correspond a OnewayVMI.fml.rt
			System.out.println("onewayView="+onewayView);
						
			//------------------@Valery-----------------------------
			System.out.println("Name VirtualModel="+onewayNature.getRequirementsVirtualModel().getName());
			System.out.println("Description VirtualModel="+onewayNature.getRequirementsVirtualModel().getDescription());
			System.out.println("RequirementReferenceConcept="+onewayNature.getRequirementReferenceConcept().toString());
			System.out.println("RequirementConcept="+onewayNature.getRequirementConcept().toString());
			//System.out.println("onewayNature="+onewayNature.getRequirementsVirtualModel().toString());
			
			//-----------------------------------------------------------------------------
			
			
			FMLRTVirtualModelInstance requirementsVirtualModelInstance = onewayNature.getRequirementsVirtualModelInstance();
			System.out.println("requirementsVirtualModelInstance="+requirementsVirtualModelInstance);
			
			FMLRTVirtualModelInstance propertiesVirtualModelInstance = onewayNature.getPropertiesVirtualModelInstance();
			System.out.println("propertiesVirtualModelInstance="+propertiesVirtualModelInstance);

			
			
			// A partir de la, 2 solutions
			// - Premiere solution : API java
			// - Deuxieme solution : API FML

			List<FlexoConceptInstance> elements;
			List<FlexoConceptInstance> requirements;
			
			List<FlexoConceptInstance> properties = new ArrayList<>();
			FlexoConceptInstance property;
			FlexoConceptInstance requirementOfProperty;
			List<FlexoConceptInstance> atomicPropositions;
			
			String fileName, directory;
			
			//FMLRTVirtualModelInstance processPropertiesVirtualModelInstance;
			
			
			try {
				
					
				directory="C:\\Users\\montheva\\Documents\\ONEWAY\\Implementation\\GpslFiles";
				
				fileName=directory+"/"+onewayView.execute("this.projectName")+".gpsl";
				
				
				
                //change the directory------------
				System.setProperty("user.dir", directory);
				
				System.out.println("\n========================");
				System.out.println("Chemin User = "+System.getProperty("user.dir"));
				System.out.println("========================\n");
				
				
				elements = requirementsVirtualModelInstance.execute("this.allElements");
				
				FlexoConceptInstance monElementASupprimer = null;
				
				//requirementsVirtualModelInstance.execute("this.removeElement({$element})",monElementASupprimer);
				
				// Elements and Requirement-------------------------------
				System.out.println("========================");
				System.out.println("=======Les Elements=====");
				System.out.println("elements="+elements);
				
				System.out.println("=======Les Exigences de chaque Element=====");
								
				for(FlexoConceptInstance e : elements)
				{
					System.out.println("-------------------------");
					requirements = e.execute("this.requirements");
					System.out.println("Element = "+e.getStringRepresentation());
					System.out.println("Requirements = "+requirements);
					//System.out.println("-------------------------");
					for(FlexoConceptInstance r : requirements)
					{
								
					  System.out.println("Nom = "+r.execute("this.name"));
					  System.out.println("Description = "+r.execute("this.description"));
					}  
				}
				
				
				System.out.println("==============================");
								
				System.out.println("========Les Propriétés========");
				propertiesVirtualModelInstance.getVirtualModelInstances(); 
				System.out.println("properties="+propertiesVirtualModelInstance.getVirtualModelInstances());
				
				for(FlexoConceptInstance pp : propertiesVirtualModelInstance.getVirtualModelInstances())
				{
					System.out.println("-------------------------");
					atomicPropositions = pp.execute("this.atomicPropositions");
					properties = pp.execute("this.properties");
					
					System.out.println("Atomic Propositions = "+atomicPropositions);
					System.out.println("Properties = "+properties);
					System.out.println("-------------------------");
					//for(FlexoConceptInstance p : properties)
					
					
					System.out.println("//----------------------------------------------------------");
					System.out.println("//-----------------project GPSL file------------------------");
					System.out.println("//----------------------------------------------------------");
					System.out.println("// this file contains all the properties defined for your projets \n\n");
					System.out.println("// the file structure is as follows :\n"+
					                   "// fror each property defined on the process execution, you have : \n"+
									   "// name : property name \n"+
					                   "// requirement : \n"+
									   "//   name : requirement description");
					System.out.println("//----------------------------------------------------------");
						
				    // List of atomic propositions--------------------------	
					FlexoConceptInstance ap;
					
					System.out.println("\n//--------List of Atomic propositions-----------------------");
					System.out.println("//---Number of Atomic propositions : "+ atomicPropositions.size());
					System.out.println("//----------------------------------------------------------");
					
					for(int i=0;i<atomicPropositions.size();i++)
					{
						ap=atomicPropositions.get(i);		
						System.out.println("\n//----------Atomic proposition N°:"+(i+1)+"-------------------");
						System.out.println("// Name : "+ap.execute("this.name"));
						System.out.println("// Description : "+ap.execute("this.description"));
						System.out.println("let "+ap.execute("this.name")+"="+ap.execute("this.expressionProperty"));
							
					} 
					
					// List of properties--------------------------	
					
					FlexoConceptInstance p;
					
					System.out.println("\n//-------------List of properties------------------------");
					System.out.println("//---Number of properties : "+ properties.size());
					System.out.println("//----------------------------------------------------------");
					
					for(int i=0;i<properties.size();i++)
					{
						p=properties.get(i);		
						System.out.println("\n//----------Property N°:"+(i+1)+"-------------------");
						System.out.println("// Name : "+p.execute("this.name"));
						System.out.println("// Requirement : \n"+
										   "//   "+p.execute("this.requirement.name")+
										   " : "+p.execute("this.requirement.description"));
						//System.out.println("Description = "+p.execute("this.description"));
						System.out.println("// GPSL : \n");
						System.out.println(p.execute("this.name")+" = "+p.execute("this.gpslRepresentation"));
						
						System.out.println("\n//------its atomic propositions -----------------");
						System.out.println("// AP : "+p.execute("this.scope"));
						System.out.println("// AP : "+p.execute("this.pattern"));
						
						
					}  
				}
				
				System.out.println("//-------------------------------------");
				System.out.println("//-End of file ");
				System.out.println("//-All porperties have been generated");
				System.out.println("//-Total : "+properties.size());
				System.out.println("//-------------------------------------");
				
				
				//------write to file-------------------------
				writeToFile(fileName,propertiesVirtualModelInstance);		
				
			} catch (TypeMismatchException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NullReferenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidBindingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//-----write to file------------------
		public void writeToFile(String fileName,FMLRTVirtualModelInstance propertiesVMI) throws IOException
		  {
			List<FlexoConceptInstance> properties = new ArrayList<>();
			FlexoConceptInstance property;
			//FlexoConceptInstance requirementOfProperty;
			List<FlexoConceptInstance> atomicPropositions;
			
			PrintWriter writer;
			writer=new PrintWriter(new FileWriter(fileName));
			
			//---------------------------------------------------------
	try {
			
			for(FlexoConceptInstance pp : propertiesVMI.getVirtualModelInstances())
			{
				//writer.println("-------------------------");
				atomicPropositions = pp.execute("this.atomicPropositions");
				properties = pp.execute("this.properties");
				
				//writer.println("Atomic Propositions = "+atomicPropositions);
				///writer.println("Properties = "+properties);
				//writer.println("-------------------------");
				//for(FlexoConceptInstance p : properties)
				
				
				writer.println("//----------------------------------------------------------");
				writer.println("//-----------------project GPSL file------------------------");
				writer.println("//----------------------------------------------------------");
				writer.println("// this file contains all the properties defined for your projcet \n\n");
				writer.println("// the file structure is as follows :\n"+
				                   "// fror each property defined on the process execution, you have : \n"+
								   "// name : property name \n"+
				                   "// requirement : \n"+
								   "//   name : requirement description");
				writer.println("//----------------------------------------------------------");
					
			    // List of atomic propositions--------------------------	
				FlexoConceptInstance ap;
				
				writer.println("\n//--------List of Atomic propositions-----------------------");
				writer.println("//---Number of Atomic propositions : "+ atomicPropositions.size());
				writer.println("//----------------------------------------------------------");
				
				for(int i=0;i<atomicPropositions.size();i++)
				{
					ap=atomicPropositions.get(i);		
					writer.println("\n//----------Atomic proposition N°:"+(i+1)+"-------------------");
					writer.println("// Name : "+ap.execute("this.name"));
					writer.println("// Description : "+ap.execute("this.description"));
					writer.println("\nlet "+ap.execute("this.name")+"="+ap.execute("this.expressionProperty"));
						
				} 
				
				// List of properties--------------------------	
				
				FlexoConceptInstance p;
				
				writer.println("\n//-------------List of properties------------------------");
				writer.println("//---Number of properties : "+ properties.size());
				writer.println("//----------------------------------------------------------");
				
				for(int i=0;i<properties.size();i++)
				{
					p=properties.get(i);		
					writer.println("\n//----------Property N°:"+(i+1)+"-------------------");
					writer.println("// Name : "+p.execute("this.name"));
					writer.println("// Requirement : \n"+
									   "//   "+p.execute("this.requirement.name")+
									   " : "+p.execute("this.requirement.description"));
					//writer.println("Description = "+p.execute("this.description"));
					writer.println("// GPSL : \n");
					writer.println(p.execute("this.name")+" = "+p.execute("this.gpslRepresentation"));
					
					//writer.println("\n//------its atomic propositions -----------------");
					//writer.println("// AP : "+p.execute("this.scope"));
					//writer.println("// AP : "+p.execute("this.pattern"));
					
					
				}  
			}
			
			writer.println("//-------------------------------------");
			writer.println("//-End of file ");
			writer.println("//-All porperties have been generated");
			writer.println("//-Total : "+properties.size());
			writer.println("//-------------------------------------");
			
	   //--------------------------------------------------------
			
			writer.close();
		  
		  
	} catch (TypeMismatchException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NullReferenceException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvocationTargetException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (InvalidBindingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }
 }

}
