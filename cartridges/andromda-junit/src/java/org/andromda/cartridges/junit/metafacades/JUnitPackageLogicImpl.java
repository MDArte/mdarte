package org.andromda.cartridges.junit.metafacades;

import org.andromda.cartridges.junit.JUnitProperties;
import org.andromda.metafacades.uml.CoppetecUMLMetafacadeProperties;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.junit.metafacades.JUnitPackage.
 * 
 * @see org.andromda.cartridges.junit.metafacades.JUnitPackage
 */
public class JUnitPackageLogicImpl extends JUnitPackageLogic
{
	public String insertModuleNameIntoOutlet(String outlet)
	{
		return replaceOutletWithTestModuleName(outlet);
	}

	public JUnitPackageLogicImpl(Object metaObject, String context)
	{
		super(metaObject, context);
	}

	protected String handleGetContext()
	{
		return getConfiguredProperty(CoppetecUMLMetafacadeProperties.CONTEXTO_PRINCIPAL).toString()
			+ "/" + getConfiguredProperty(JUnitProperties.TEST_CONTEXT).toString()
			+ "/" + this.getTestModuleName();
	}
}
