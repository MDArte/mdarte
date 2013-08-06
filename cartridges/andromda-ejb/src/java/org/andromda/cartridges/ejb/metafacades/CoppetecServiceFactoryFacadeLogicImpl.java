package org.andromda.cartridges.ejb.metafacades;

import java.util.ArrayList;
import java.util.Collection;

import org.andromda.metafacades.uml.CoppetecPackageFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.UMLProfile;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb.metafacades.CoppetecServiceFactoryFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.CoppetecServiceFactoryFacade
 */
public class CoppetecServiceFactoryFacadeLogicImpl
    extends CoppetecServiceFactoryFacadeLogic
{

        public String insertModuleNameIntoOutlet(String outlet){
        	return ((CoppetecPackageFacade)(getPackage())).replaceOutletWithServiceModuleName(outlet);
    }
    
    public CoppetecServiceFactoryFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.CoppetecServiceFactoryFacade#getServicos()
     */
    protected java.util.Collection handleGetServicos()
    {
        Collection servicos = new ArrayList();
        
        java.util.Iterator specializations = getAllSpecializations().iterator();
        
        while(specializations.hasNext()){
        	ModelElementFacade element = (ModelElementFacade)specializations.next(); 
        	
        	if(element.hasStereotype(UMLProfile.STEREOTYPE_SERVICE)){
        		servicos.add(element);
        	}
        }
        
        return servicos;
        
        
    }

}