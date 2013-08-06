package org.andromda.cartridges.junit.metafacades;

import java.util.Iterator;

import org.andromda.metafacades.uml.ActivityGraphFacade;
import org.andromda.metafacades.uml.CoppetecPackageFacade;
import org.andromda.metafacades.uml.StateMachineFacade;
import org.andromda.metafacades.uml.UseCaseFacade;
import org.andromda.utils.StringUtilsHelper;
import org.apache.commons.lang.StringUtils;

/**
 * MetafacadeLogic implementation for
 * org.andromda.cartridges.junit.metafacades.JUnitPseudostate.
 * 
 * @see org.andromda.cartridges.junit.metafacades.JUnitPseudostate
 */
public class JUnitPseudostateLogicImpl extends JUnitPseudostateLogic
{
	/**
	 * @see org.andromda.core.metafacade.MetafacadeBaseModular#insertModuleNameIntoOutlet(java.lang.String)
	 */
	public String insertModuleNameIntoOutlet(String outlet)
	{
		final StateMachineFacade stateMachine = this.getStateMachine();
		if (stateMachine instanceof ActivityGraphFacade)
		{
			final UseCaseFacade useCase = ((ActivityGraphFacade) stateMachine).getUseCase();
			if (useCase != null)
			{
				outlet = ((CoppetecPackageFacade) (useCase.getPackage())).replaceOutletWithTestModuleName(outlet);
			}
		}

		return outlet;
	}

	/**
	 * @param metaObject
	 * @param context
	 */
	public JUnitPseudostateLogicImpl(Object metaObject, String context)
	{
		super(metaObject, context);
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitPseudostateLogic#handleGetSwimlane()
	 */
	protected java.lang.Object handleGetSwimlane()
	{
		return this.getPartition();
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitPseudostateLogic#handleGetDecisionPackageName()
	 */
	protected String handleGetDecisionPackageName()
	{
		String packageName = null;

		final StateMachineFacade stateMachine = this.getStateMachine();
		if (stateMachine instanceof ActivityGraphFacade)
		{
			final UseCaseFacade useCase = ((ActivityGraphFacade) stateMachine).getUseCase();
			if (useCase != null)
			{
				packageName = useCase.getPackageName();
			}
		}

		return packageName;
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitPseudostateLogic#handleGetDecisionClassName()
	 */
	protected java.lang.String handleGetDecisionClassName()
	{
		return StringUtilsHelper.upperCamelCaseName(this.getName());
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitPseudostateLogic#handleGetDecisionFullPath()
	 */
	protected java.lang.String handleGetDecisionFullPath()
	{
		return StringUtils.replace(this.getClazz(), ".", "/");
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitPseudostateLogic#handleGetClassName()
	 */
	protected java.lang.String handleGetClassName()
	{
		return StringUtilsHelper.upperCamelCaseName(getName());
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitPseudostateLogic#handleGetClazz()
	 */
	protected java.lang.String handleGetClazz()
	{
		String decisionHandlerClass = null;

		if (this.isDecisionPoint())
		{
			final StringBuffer clazzBuffer = new StringBuffer();
			if (StringUtils.isNotBlank(this.getDecisionPackageName()))
			{
				clazzBuffer.append(this.getDecisionPackageName());
				clazzBuffer.append('.');
			}
			clazzBuffer.append(this.getDecisionClassName());
			decisionHandlerClass = clazzBuffer.toString();
		}

		return decisionHandlerClass;
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitPseudostateLogic#handleGetDecisionTransionsNumber()
	 */
	protected Integer handleGetDecisionTransionsNumber()
	{
		return Integer.valueOf(this.getOutgoing().size());
	}

	/**
	 * @see org.andromda.cartridges.junit.metafacades.JUnitPseudostate#handleHasUniqueName()
	 */
	protected boolean handleHasUniqueName()
	{
		if (getStateMachine() instanceof ActivityGraphFacade && ((ActivityGraphFacade)this.getStateMachine()).getUseCase() instanceof JUnitProcessDefinition)
		{
			JUnitProcessDefinition jUnitProcessDefinition = (JUnitProcessDefinition) ((ActivityGraphFacade)this.getStateMachine()).getUseCase();

			if (StringUtilsHelper.lowerCamelCaseName(jUnitProcessDefinition.getName()).equals(StringUtilsHelper.lowerCamelCaseName(this.getName())))
				return false;

			for (final Iterator pseudostateIterator = jUnitProcessDefinition.getDecisions().iterator(); pseudostateIterator.hasNext(); )
			{
				JUnitPseudostate pseudostate = (JUnitPseudostate) pseudostateIterator.next();

				if (pseudostate.getId().equals(this.getId()))
					continue;
				
				if (pseudostate.isDecisionPoint() && this.isDecisionPoint())
				{
					if (StringUtilsHelper.lowerCamelCaseName(pseudostate.getName()).equals(StringUtilsHelper.lowerCamelCaseName(this.getName())))
						return false;
				}
			}
		}

		return true;
	}
}
