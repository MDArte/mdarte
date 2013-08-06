package org.andromda.cartridges.ejb.metafacades;

import org.andromda.cartridges.ejb.EJBProfile;
import org.andromda.metafacades.uml.CoppetecPackageFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.ejb.metafacades.MDArteEJBSessionFacade.
 *
 * @see org.andromda.cartridges.ejb.metafacades.MDArteEJBSessionFacade
 */
public class MDArteEJBSessionFacadeLogicImpl
    extends MDArteEJBSessionFacadeLogic
{
    public String insertModuleNameIntoOutlet(String outlet){
    	return ((CoppetecPackageFacade)(getPackage())).replaceOutletWithServiceModuleName(outlet); 
    }

    public MDArteEJBSessionFacadeLogicImpl (Object metaObject, String context)
    {
        super (metaObject, context);
    }

    /**
     * @see org.andromda.cartridges.ejb.metafacades.MDArteEJBSessionFacade#isOpenAccess()
     */
    protected boolean handleIsOpenAccess()
    {
    	boolean ret = false;

		if (this.getPackage() instanceof CoppetecPackageFacade)
        {
			CoppetecPackageFacade pacote = (CoppetecPackageFacade) this.getPackage();
            ret = pacote.isOpenAccess();
        }

    	return ret || this.hasStereotype(EJBProfile.STEREOTYPE_OPEN_ACCESS);
    }
}