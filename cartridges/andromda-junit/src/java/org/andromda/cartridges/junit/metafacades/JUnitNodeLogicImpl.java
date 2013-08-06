package org.andromda.cartridges.junit.metafacades;

import java.util.Iterator;

import org.andromda.cartridges.junit.JUnitProfile;
import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.utils.StringUtilsHelper;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.junit.metafacades.JUnitNode.
 *
 * @see org.andromda.cartridges.junit.metafacades.JUnitNode
 */
public class JUnitNodeLogicImpl
	extends JUnitNodeLogic
{
	public JUnitNodeLogicImpl (Object metaObject, String context)
	{
		super (metaObject, context);
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitNode#isContainedInProcess()
	 */
	protected boolean handleIsContainedInProcess()
	{
		return this.getStateMachine() instanceof ActivityGraphFacade
			&& ((ActivityGraphFacade)this.getStateMachine()).getUseCase() instanceof JUnitProcessDefinition;
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitNodeLogic#handleIsNode()
	 */
	protected boolean handleIsNode()
	{
		return true;
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitNodeLogic#handleIsIgnored()
	 */
	protected boolean handleIsIgnored()
	{
		String ignored = (String) this.findTaggedValue(JUnitProfile.TAGGEDVALUE_IGNORE);

		if (ignored == null)
		{
			return false;
		}

		return Boolean.valueOf(ignored).booleanValue();
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitNodeLogic#handleGetServiceName()
	 */
	protected String handleGetServiceName()
	{
		return (String) this.findTaggedValue(JUnitProfile.TAGGEDVALUE_SERVICE_NAME);
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitNodeLogic#handleGetServicePackage()
	 */
	protected String handleGetServicePackage()
	{
		return (String) this.findTaggedValue(JUnitProfile.TAGGEDVALUE_SERVICE_PACKAGE);
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitNodeLogic#handleGetUseCaseName()
	 */
	protected String handleGetUseCaseName()
	{
		return (String) this.findTaggedValue(JUnitProfile.TAGGEDVALUE_USE_CASE_NAME);
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitNodeLogic#handleGetUseCasePackage()
	 */
	protected String handleGetUseCasePackage()
	{
		return (String) this.findTaggedValue(JUnitProfile.TAGGEDVALUE_USE_CASE_PACKAGE);
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitNodeLogic#handleGetSwimlane()
	 */
	protected Object handleGetSwimlane()
	{
		return this.getPartition();
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitNodeLogic#handleHasUniqueName()
	 */
	protected boolean handleHasUniqueName()
	{
		if (getStateMachine() instanceof ActivityGraphFacade && ((ActivityGraphFacade)this.getStateMachine()).getUseCase() instanceof JUnitProcessDefinition)
		{
			JUnitProcessDefinition jUnitProcessDefinition = (JUnitProcessDefinition) ((ActivityGraphFacade)this.getStateMachine()).getUseCase();

			for (final Iterator nodeIterator = jUnitProcessDefinition.getNodes().iterator(); nodeIterator.hasNext(); )
			{
				JUnitNode node = (JUnitNode) nodeIterator.next();

				if (node.getId().equals(this.getId()))
					continue;

				if (StringUtilsHelper.lowerCamelCaseName(node.getName()).equals(StringUtilsHelper.lowerCamelCaseName(this.getName())))
					return false;
			}
		}

		return true;
	}
}