package org.andromda.cartridges.ejb.metafacades;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.andromda.core.cartridge.template.ModelElement;
import org.andromda.metafacades.uml.CoppetecPackageFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.PackageFacade;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb.metafacades.CoppetecEJBPackage.
 *
 * @see org.andromda.cartridges.ejb.metafacades.CoppetecEJBPackage
 */
public class CoppetecEJBPackageLogicImpl
    extends CoppetecEJBPackageLogic
{

        public String insertModuleNameIntoOutlet(String outlet){
        	return replaceOutletWithServiceModuleName(outlet); 
        }
    
        public CoppetecEJBPackageLogicImpl (Object metaObject, String context)
        {
        	super (metaObject, context);
        }
    
       /* public java.util.Collection handleGetMessageDrivenBeans(){
        	System.out.println("Chamei");
        	
        	List beans = new ArrayList();
        	
        	Iterator it = getOwnedElements().iterator();
        	
        	while (it.hasNext()){
        		ModelElementFacade element = (ModelElementFacade)it.next();
        		
        		if(element instanceof CoppetecPackageFacade){
        			CoppetecPackageFacade pacote = (CoppetecPackageFacade) element;
        			System.out.println(pacote.getName());
        			beans.addAll(pacote.getMessageDrivenBeans());
        		}
        		else if(element.hasStereotype("MessageDrivenBean")){
        			beans.add(element);
        		}
        	}
        	
        	return beans;
        }

        public java.util.Collection handleGetSessions(){
        	System.out.println("Chamei");
        	List sessions = new ArrayList();
        	
        	Iterator it = getOwnedElements().iterator();
        	
        	while (it.hasNext()){
        		ModelElementFacade element = (ModelElementFacade)it.next();
        		
        		if(element instanceof CoppetecPackageFacade){
        			CoppetecPackageFacade pacote = (CoppetecPackageFacade) element;
        			System.out.println(pacote.getName());
        			sessions.addAll(pacote.getSessions());
        		}
        		else if(element.hasStereotype(UMLProfile.STEREOTYPE_SERVICE)){
        			sessions.add(element);
        		}
        	}
        	
        	return sessions;
        }*/
}