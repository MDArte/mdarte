package org.andromda.cartridges.junit.metafacades;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.CoppetecPackageFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.junit.metafacades.JUnitStateVertex.
 *
 * @see org.andromda.cartridges.junit.metafacades.JUnitStateVertex
 */
public class JUnitStateVertexLogicImpl
	extends JUnitStateVertexLogic
{
	public JUnitStateVertexLogicImpl (Object metaObject, String context)
	{
		super (metaObject, context);
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitStateVertex#isContainedInProcess()
	 */
	protected boolean handleIsContainedInProcess()
	{
		return this.getStateMachine() instanceof ActivityGraphFacade
			&& ((ActivityGraphFacade)this.getStateMachine()).getUseCase() instanceof JUnitProcessDefinition;
	}
}