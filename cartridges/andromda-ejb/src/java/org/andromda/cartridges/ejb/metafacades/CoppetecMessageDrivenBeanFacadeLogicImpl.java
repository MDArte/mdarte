package org.andromda.cartridges.ejb.metafacades;

import org.andromda.metafacades.uml.CoppetecPackageFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb.metafacades.CoppetecMessageDrivenBeanFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.CoppetecMessageDrivenBeanFacade
 */
public class CoppetecMessageDrivenBeanFacadeLogicImpl
    extends CoppetecMessageDrivenBeanFacadeLogic
{

	public String insertModuleNameIntoOutlet(String outlet){
		return ((CoppetecPackageFacade)(getPackage())).replaceOutletWithServiceModuleName(outlet); 
	}
    
    public CoppetecMessageDrivenBeanFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
}