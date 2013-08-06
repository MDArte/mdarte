package org.andromda.cartridges.ejb.metafacades;

import org.andromda.metafacades.uml.CoppetecPackageFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb.metafacades.CoppetecAbstractServiceFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.CoppetecAbstractServiceFacade
 */
public class CoppetecAbstractServiceFacadeLogicImpl
    extends CoppetecAbstractServiceFacadeLogic
{

        public String insertModuleNameIntoOutlet(String outlet){
        	return ((CoppetecPackageFacade)(getPackage())).replaceOutletWithServiceModuleName(outlet);
    }
    
    public CoppetecAbstractServiceFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }
}