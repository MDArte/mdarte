package org.andromda.cartridges.junit.metafacades;

import org.andromda.cartridges.junit.JUnitProfile;




/**
 * MetafacadeLogic implementation for org.andromda.cartridges.junit.metafacades.JUnitSwimlane.
 *
 * @see org.andromda.cartridges.junit.metafacades.JUnitSwimlane
 */
public class JUnitSwimlaneLogicImpl
	extends JUnitSwimlaneLogic
{
	public JUnitSwimlaneLogicImpl (Object metaObject, String context)
	{
		super (metaObject, context);
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitSwimlane#isContainedInProcess()
	 */
	protected boolean handleIsContainedInProcess()
	{
		return this.getActivityGraph().getUseCase() instanceof JUnitProcessDefinition;
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitSwimlaneLogic#handleGetPassword()
	 */
	protected String handleGetPassword()
	{
		return (String) this.findTaggedValue(JUnitProfile.TAGGEDVALUE_PASSWORD);
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitSwimlaneLogic#handleGetProjectName()
	 */
	protected String handleGetProjectName()
	{
		return (String) this.findTaggedValue(JUnitProfile.TAGGEDVALUE_PROJECT_NAME);
	}
}