package org.andromda.cartridges.junit.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.andromda.utils.StringUtilsHelper;
import org.andromda.metafacades.uml.ActionStateFacade;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.CoppetecPackageFacade;
import org.andromda.metafacades.uml.PseudostateFacade;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.junit.metafacades.JUnitProcessDefinition.
 * 
 * @see org.andromda.cartridges.junit.metafacades.JUnitProcessDefinition
 */
public class JUnitProcessDefinitionLogicImpl extends JUnitProcessDefinitionLogic
{
	/**
	 * @see org.andromda.core.metafacade.MetafacadeBaseModular#insertModuleNameIntoOutlet(java.lang.String)
	 */
	public String insertModuleNameIntoOutlet(String outlet)
	{
		return ((CoppetecPackageFacade) (getPackage())).replaceOutletWithTestModuleName(outlet);
	}

	/**
	 * @param metaObject
	 * @param context
	 */
	public JUnitProcessDefinitionLogicImpl(Object metaObject, String context)
	{
		super(metaObject, context);
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitProcessDefinition#getNodes()
	 */
	protected java.util.List handleGetNodes()
	{
		final List states = new ArrayList();

		final ActivityGraphFacade graph = this.getFirstActivityGraph();

		if (graph != null)
		{
			final Collection actionStates = graph.getActionStates();
			for (final Iterator actionStateIterator = actionStates.iterator(); actionStateIterator.hasNext();)
			{
				final ActionStateFacade state = (ActionStateFacade) actionStateIterator.next();

				if (state instanceof JUnitNode)
				{
					states.add(state);
				}
			}
		}

		return states;
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitProcessDefinitionLogic#handleGetSwimlanes()
	 */
	protected java.util.List handleGetSwimlanes()
	{
		final List swimlanes = new ArrayList();

		final ActivityGraphFacade graph = this.getFirstActivityGraph();
		if (graph != null)
		{
			swimlanes.addAll(graph.getPartitions());
		}

		return swimlanes;
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitProcessDefinitionLogic#handleGetDecisions()
	 */
	protected java.util.List handleGetDecisions()
	{
		final List decisions = new ArrayList();

		final ActivityGraphFacade graph = this.getFirstActivityGraph();
		if (graph != null)
		{
			final Collection pseudostates = graph.getPseudostates();
			for (final Iterator pseudostateIterator = pseudostates.iterator(); pseudostateIterator.hasNext();)
			{
				final PseudostateFacade pseudostate = (PseudostateFacade) pseudostateIterator.next();
				if (pseudostate.isDecisionPoint())
				{
					decisions.add(pseudostate);
				}
			}
		}

		return decisions;
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitProcessDefinitionLogic#handleGetStartState()
	 */
	protected java.lang.Object handleGetStartState()
	{
		Object startState = null;

		final ActivityGraphFacade graph = this.getFirstActivityGraph();
		if (graph != null)
		{
			startState = graph.getInitialState();
		}

		return startState;
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitProcessDefinitionLogic#handleGetEndStates()
	 */
	protected java.util.List handleGetEndStates()
	{
		final List endStates = new ArrayList();

		final ActivityGraphFacade graph = this.getFirstActivityGraph();
		if (graph != null)
		{
			endStates.addAll(graph.getFinalStates());
		}

		return endStates;
	}
}
