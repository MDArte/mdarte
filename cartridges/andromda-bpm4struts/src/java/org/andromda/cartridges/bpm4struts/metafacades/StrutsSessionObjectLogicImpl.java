package org.andromda.cartridges.bpm4struts.metafacades;

import org.andromda.metafacades.uml.CoppetecPackageFacade;
import org.andromda.utils.StringUtilsHelper;


public class StrutsSessionObjectLogicImpl
    extends StrutsSessionObjectLogic
{
	public String insertModuleNameIntoOutlet(String outlet){
		return ((CoppetecPackageFacade)(getPackage())).replaceOutletWithWebModuleName(outlet); 
	}
	
	public StrutsSessionObjectLogicImpl(
        Object metaObject,
        String context)
    {
        super(metaObject, context);
    }

    protected java.lang.String handleGetSessionKey()
    {
        return StringUtilsHelper.lowerCamelCaseName(getName());
    }

    protected java.lang.String handleGetFullPath()
    {
        return '/' + getFullyQualifiedName().replace('.', '/');
    }

}
