package org.andromda.cartridges.ejb.metafacades;

import org.andromda.metafacades.uml.CoppetecPackageFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb.metafacades.CoppetecMessageDrivenBeanClientFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.CoppetecMessageDrivenBeanClientFacade
 */
public class CoppetecMessageDrivenBeanClientFacadeLogicImpl
    extends CoppetecMessageDrivenBeanClientFacadeLogic
{

	public String insertModuleNameIntoOutlet(String outlet){
		return ((CoppetecPackageFacade)(getPackage())).replaceOutletWithServiceModuleName(outlet); 
	}
    
    public CoppetecMessageDrivenBeanClientFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
}