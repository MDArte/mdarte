package org.andromda.cartridges.junit.metafacades;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.CoppetecPackageFacade;
import org.andromda.metafacades.uml.GuardFacade;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.junit.metafacades.JUnitTransition.
 *
 * @see org.andromda.cartridges.junit.metafacades.JUnitTransition
 */
public class JUnitTransitionLogicImpl
	extends JUnitTransitionLogic
{
	public JUnitTransitionLogicImpl (Object metaObject, String context)
	{
		super (metaObject, context);
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitTransition#isContainedInProcess()
	 */
	protected boolean handleIsContainedInProcess()
	{
		return this.getSource().getStateMachine() instanceof ActivityGraphFacade &&
			((ActivityGraphFacade)this.getSource().getStateMachine()).getUseCase() instanceof JUnitProcessDefinition;
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitTransition#getCondition()
	 */
	protected java.lang.String handleGetCondition()
	{
		String decision = null;

		final GuardFacade guard = this.getGuard();
		if (guard != null)
		{
			decision = guard.getBody();
		}

		return decision;
	}

}