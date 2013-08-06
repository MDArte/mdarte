package org.andromda.cartridges.junit.metafacades;

import java.util.Collection;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.CoppetecPackageFacade;
import org.andromda.metafacades.uml.PseudostateFacade;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.junit.metafacades.JUnitActivityGraph.
 * 
 * @see org.andromda.cartridges.junit.metafacades.JUnitActivityGraph
 */
public class JUnitActivityGraphLogicImpl extends JUnitActivityGraphLogic
{
	/**
	 * @see org.andromda.core.metafacade.MetafacadeBaseModular#insertModuleNameIntoOutlet(java.lang.String)
	 */
	public String insertModuleNameIntoOutlet(String outlet)
	{
		return ((CoppetecPackageFacade) (getUseCase().getPackage())).replaceOutletWithTestModuleName(outlet);
	}

	/**
	 * @param metaObject
	 * @param context
	 */
	public JUnitActivityGraphLogicImpl(Object metaObject, String context)
	{
		super(metaObject, context);
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitActivityGraphLogic#handleIsContainedInProcess()
	 */
	protected boolean handleIsContainedInProcess()
	{
		return getUseCase() instanceof JUnitProcessDefinition;
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitActivityGraphLogic#handleGetFirstAction()
	 */
	protected Object handleGetFirstAction()
	{
		Object firstAction = null;
		final Collection initialStates = getInitialStates();
		if (!initialStates.isEmpty())
		{
			final PseudostateFacade initialState = (PseudostateFacade) initialStates.iterator().next();
			final Collection outgoing = initialState.getOutgoing();
			firstAction = (outgoing.isEmpty()) ? null : outgoing.iterator().next();
		}
		return firstAction;
	}
}
