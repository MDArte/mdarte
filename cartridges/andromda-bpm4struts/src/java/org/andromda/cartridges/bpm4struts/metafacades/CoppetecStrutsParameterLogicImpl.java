package org.andromda.cartridges.bpm4struts.metafacades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.bpm4struts.Bpm4StrutsProfile;
import org.andromda.cartridges.bpm4struts.Bpm4StrutsUtils;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.EventFacade;
import org.andromda.metafacades.uml.FrontEndActivityGraph;
import org.andromda.metafacades.uml.FrontEndController;
import org.andromda.metafacades.uml.FrontEndForward;
import org.andromda.metafacades.uml.TransitionFacade;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.andromda.utils.StringUtilsHelper;


/**
 * MetafacadeLogic implementation for org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsParameter.
 *
 * @see org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsParameter
 */
public class CoppetecStrutsParameterLogicImpl
extends CoppetecStrutsParameterLogic
{

	public CoppetecStrutsParameterLogicImpl (Object metaObject, String context)
	{
		super (metaObject, context);
	}

	/**
	 * @see org.andromda.cartridges.bpm4struts.metafacades.CoppetecStrutsParameter#isDoubleSelect()
	 */
	protected boolean handleIsDoubleSelect()
	{
		return "doubleselect".equals(this.getWidgetType());
	}
	
	protected boolean handleIsEnumEmptyValue()
	{
		Object value = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_ENUM_EMPTY_VALUE);
		
		if (value == null) return true;

		return Bpm4StrutsUtils.isTrue(String.valueOf(value));
	}

	protected String handleGetOnlineHelpValue()
	{
		final String crlf = "<br/>";
		final String format = getValidatorFormat();
		final StringBuffer buffer = new StringBuffer();

		final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
		buffer.append((value == null) ? "" : value);
		if (value != null) {
			buffer.append(crlf);
			buffer.append(crlf);
		}

		buffer.append(isRequired() ? "Este campo é obrigatório." : "Este campo é opcional.");
		buffer.append(crlf);

		if ("password".equals(getWidgetType()))
		{
			buffer.append("Este é um campo de senha, ele não exibirá os dados digitados, ")
			.append("cada caractere será mascarado usando um asterisco.");
			buffer.append(crlf);
		}

//		if (isCreditCardFormat(format))
//		{
//			buffer.append("O valor deste campo deve refletir um ")
//			.append("<a href=\"http://www.beachnet.com/~hstiles/cardtype.html\" target=\"_blank\">cartão de crédito</a>.");
//			buffer.append(crlf);
//		}

//		if (isDate())
//		{
//			String dateFormat = getDateFormat();
//			buffer.append("Este campo representa uma data e deve ser formatado na forma descrita aqui ")
//			.append("<a href=\"http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html\" ")
//			.append("target=\"_jdk\">");
//			buffer.append(dateFormat).append("</a>. ");
//
//			if (isStrictDateFormat()) buffer
//			.append("Este formato é estrito no sentido que o analisador não usará qualquer heurística para ")
//			.append("descobrir a data pretendida no caso da entrada não casar perfeitamente com o formato.");
//			else
//			{
//				buffer.append("Este formato é tolerante no sentido que o analisador tentará usar heurísticas para ")
//				.append("descobrir a data pretendida no caso da entrada não casar perfeitamente com o formato.");
//			}
//			buffer.append(crlf);
//			buffer.append("Um calendário foi disponibilizado para a seleção da data, ele converterá automaticamente a data ")
//			.append("para o formato apropriado.");
//			buffer.append(crlf);
//		}

//		if (this.isValidatorTime())
//		{
//			String dateFormat = getDateFormat();
//			buffer
//			.append("Este campo representa um horário e deve ser formatado na forma descrita aqui ")
//			.append("<a href=\"http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html\" ")
//			.append("target=\"_jdk\">");
//			buffer.append(dateFormat).append("</a>. ");
//		}

//		if (isEmailFormat(format))
//		{
//			buffer.append("O valor deste campo deve refletir um endereço de email.");
//			buffer.append(crlf);
//		}

		if (isMaxLengthFormat(format))
		{
			buffer.append("Este campo não deve conter mais que ");
			buffer.append(getMaxLengthValue(format));
			buffer.append(" caracteres.");
			buffer.append(crlf);
		}

		if (isMinLengthFormat(format))
		{
			buffer.append("Este campo deve conter pelo menos ");
			buffer.append(getMinLengthValue(format));
			buffer.append(" caracteres.");
			buffer.append(crlf);
		}

//		if (isPatternFormat(format))
//		{
//			buffer.append("O valor deve casar com esta ");
//			buffer.append(
//			"<a href=\"http://java.sun.com/j2se/1.4.2/docs/api/java/util/regex/Pattern.html\" target=\"_jdk\">");
//			buffer.append("expressão regular</a>: ");
//			buffer.append(getPatternValue(format));
//			buffer.append(crlf);
//		}

		if (isRangeFormat(format))
		{
			buffer.append("O valor deste campo deve estar na escala de ");
			buffer.append(getRangeStart(format));
			buffer.append(" a ");
			buffer.append(getRangeEnd(format));
			buffer.append(".");
			buffer.append(crlf);
		}

		final String validWhen = getValidWhen();
		if (validWhen != null)
		{
			buffer.append("Este campo só é válido sob condições específicas, mais concretamente a seguinte ")
			.append("expressão deve ser verdadeira: ").append(validWhen);
			buffer.append(crlf);
		}

		if (isReadOnly())
		{
			buffer.append("O valor deste campo não pode ser alterado, ele é somente-leitura.");
			buffer.append(crlf);
		}

//		if (isValidatorBoolean())
//		{
//			buffer.append("O valor deste campo deve refletir um valor do tipo ")
//			.append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
//			.append("target=\"_jdk\">boolean</a> (booleano).");
//			buffer.append(crlf);
//		}
//		else if (isValidatorByte())
//		{
//			buffer.append("O valor deste campo deve refletir um valor do tipo ")
//			.append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
//			.append("target=\"_jdk\">byte</a> (nº inteiro).");
//			buffer.append(crlf);
//		}
//		else if (isValidatorChar())
//		{
//			buffer.append("O valor deste campo deve refletir um valor do tipo ")
//			.append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
//			.append("target=\"_jdk\">character</a> (caractere).");
//			buffer.append(crlf);
//		}
//		else if (isValidatorDouble())
//		{
//			buffer.append("O valor deste campo deve refletir um valor do tipo ")
//			.append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
//			.append("target=\"_jdk\">double precision integer</a> (nº inteiro).");
//			buffer.append(crlf);
//		}
//		else if (isValidatorFloat())
//		{
//			buffer.append("O valor deste campo deve refletir um valor do tipo ")
//			.append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
//			.append("target=\"_jdk\">floating point</a> (nº real).");
//			buffer.append(crlf);
//		}
//		else if (isValidatorInteger())
//		{
//			buffer.append("O valor deste campo deve refletir um valor do tipo ")
//			.append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
//			.append("target=\"_jdk\">integer</a> (nº inteiro).");
//			buffer.append(crlf);
//		}
//		else if (isValidatorLong())
//		{
//			buffer.append("O valor deste campo deve refletir um valor do tipo ")
//			.append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
//			.append("target=\"_jdk\">long integer</a> (nº inteiro).");
//			buffer.append(crlf);
//		}
//		else if (isValidatorShort())
//		{
//			buffer.append("O valor deste campo deve refletir um valor do tipo ")
//			.append("<a href=\"http://java.sun.com/docs/books/tutorial/java/nutsandbolts/datatypes.html\" ")
//			.append("target=\"_jdk\">short integer</a> (nº inteiro).");
//			buffer.append(crlf);
//		}
//		else if (isValidatorUrl())
//		{
//			buffer.append("O valor deste campo deve refletir uma ")
//			.append("<a href=\"http://java.sun.com/j2se/1.4.2/docs/api/java/net/URL.html\" ")
//			.append("target=\"_jdk\">URL</a>.");
//			buffer.append(crlf);
//		}

		return StringUtilsHelper.toResourceMessage(buffer.toString());
	}

	protected String handleGetWidgetType()
	{
		Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE);
		final String fieldType = value == null ? null : value.toString();

		String widgetType = null;

		if (isActionParameter())
		{
			if (fieldType == null)
			{
				// no widget type has been specified
				final ClassifierFacade type = getType();
				if (type != null)
				{
					if (type.isFileType()) widgetType = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_FILE;
					else if (isValidatorBoolean()) widgetType = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_CHECKBOX;
					else if (isMultiple()) widgetType = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_SELECT;
					else widgetType = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_TEXT;
				}
			}
			else if(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_CUSTOM.equalsIgnoreCase(fieldType)) {

				  return Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_CUSTOM;
			}
			else if ("doubleselect".equalsIgnoreCase(fieldType))
			{
				widgetType = "doubleselect";
			}
			else if ("money".equalsIgnoreCase(fieldType))
			{
				widgetType = "money";
			}            
			else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_SELECT.equalsIgnoreCase(fieldType))
			{
				widgetType = "select";
			}
			else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_PASSWORD.equalsIgnoreCase(fieldType))
			{
				widgetType = "password";
			}
			else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_TEXTAREA.equalsIgnoreCase(fieldType))
			{
				widgetType = "textarea";
			}
			else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_HIDDEN.equalsIgnoreCase(fieldType))
			{
				widgetType = HIDDEN_INPUT_TYPE;
			}
			else if (fieldType.toLowerCase().startsWith(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_RADIO))
			{
				widgetType = "radio";
			}
			else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_CHECKBOX.equalsIgnoreCase(fieldType))
			{
				widgetType = "checkbox";
			}
			else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_PLAINTEXT.equalsIgnoreCase(fieldType))
			{
				widgetType = "plaintext";
			}
			else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_TEXT.equalsIgnoreCase(fieldType))
			{
				widgetType = "text";
			}
			else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_MULTIBOX.equalsIgnoreCase(fieldType))
			{
				widgetType = "multibox";
				/*
                if (getMultiboxPropertyName() != null)
                {
                    widgetType = "multibox";
                }
				 */
			}
			else if (Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_LINK.equalsIgnoreCase(fieldType))
			{
				final StrutsAction action = getStrutsAction();
				/* if (action != null)
                {
                    if (action.isTableLink())*/

				widgetType = "link";

				//}
			} else if(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_AUTOCOMPLETE.equalsIgnoreCase(fieldType))
			{
				widgetType = Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_AUTOCOMPLETE;
			}
			else
			{
				widgetType = (isMultiple()) ? "select" : "text";
			}
		}
		return widgetType;
	}

	private boolean isValidatorBoolean()
	{
		return UMLMetafacadeUtils.isType(this.getType(), UMLProfile.BOOLEAN_TYPE_NAME);
	}

	private boolean isGenericValidator()
	{
		return true;
	}
	
	
	protected boolean handleIsCustom() {

		  return Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_CUSTOM.equals(this.getWidgetType());
	}

	protected boolean handleIsSelectable()
	{
		if (super.handleIsSelectable()) return true;
		if (isMultibox() && getMultiboxPropertyName() == null) return true;
		return false;
	}

	protected boolean handleIsReport()
	{
		boolean isReport = false;
		final ClassifierFacade type = this.getType();
		if (type != null)
		{
			isReport = type.isCollectionType() || type.isArrayType();
			if (isReport)
			{
				isReport = this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_REPORT_NAME) != null;
			}
		}
		return isReport;
	}


	protected java.lang.String handleGetReportName()
	{
		return this.findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_REPORT_NAME).toString();
	}

	protected String handleGetReportType()
	{
		Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_REPORT_TYPE);
		final String reportType = value == null ? Bpm4StrutsProfile.TAGGEDVALUE_REPORT_TYPE_DEFAULT : value.toString();

		return reportType;
	}


	protected Object handleGetValueObject() {
		final EventFacade event = this.getEvent();
		Object valueObject = null;
		if (event != null)
		{
			final TransitionFacade transition = event.getTransition();
			if (transition instanceof FrontEndForward)
			{        	
				final FrontEndForward forward = (FrontEndForward)transition;              
				FrontEndActivityGraph activityGraph = forward.getFrontEndActivityGraph();
				FrontEndController controller = activityGraph.getController();
				java.util.Collection dependencies = controller.getSourceDependencies();
				for(java.util.Iterator it = dependencies.iterator(); it.hasNext();){
					DependencyFacade dependency = (DependencyFacade) it.next();
					if(dependency.getTargetElement().hasStereotype(Bpm4StrutsProfile.STEREOTYPE_VALUE_OBJECT)){                		
						valueObject = dependency.getTargetElement();
					}                	
				}
			}
		}        
		return valueObject;
	}


	protected boolean handleIsMoney() {
		return "money".equals(this.getWidgetType());
	}

	protected boolean handleIsAutocomplete()
	{
		return Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_AUTOCOMPLETE.equals(this.getWidgetType());
	}

	protected java.util.Collection handleGetValidatorTypes()
	{
		final Collection validatorTypesList = new ArrayList();

		ClassifierFacade type = getType();
		if (type != null)
		{
			final String format = getValidatorFormat();
			final boolean isRangeFormat = (format != null) && isRangeFormat(format);

			if (isRequired()) validatorTypesList.add("required");

			if (isValidatorByte()) validatorTypesList.add("byte");
			else if (isValidatorShort()) validatorTypesList.add("short");
			else if (isValidatorInteger() && !isPlainText()) validatorTypesList.add("integer");
			else if (isValidatorLong()) validatorTypesList.add("long");
			else if (!isMoney() && isValidatorFloat()) validatorTypesList.add("float");
			else if (isValidatorDate()) validatorTypesList.add("date");
			else if (isValidatorTime()) validatorTypesList.add("time");
			else if (isValidatorUrl()) validatorTypesList.add("url");

			if (isRangeFormat)
			{
				if (isValidatorInteger() || isValidatorShort() || isValidatorLong()) validatorTypesList.add("intRange");
				if (!isMoney() && isValidatorFloat()) validatorTypesList.add("floatRange");

			}

			if (format != null)
			{
				if (isValidatorString() && isEmailFormat(format)) validatorTypesList.add("email");
				else if (isValidatorString() && isCreditCardFormat(format)) validatorTypesList.add("creditCard");
				else
				{
					Collection formats = findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_FORMAT);
					for (final Iterator formatIterator = formats.iterator(); formatIterator.hasNext();)
					{
						String additionalFormat = String.valueOf(formatIterator.next());
						if (isMinLengthFormat(additionalFormat)) validatorTypesList.add("minlength");
						else if (isMaxLengthFormat(additionalFormat)) validatorTypesList.add("maxlength");
						else if (isPatternFormat(additionalFormat)) validatorTypesList.add("mask");
					}
				}
			}

			if (getValidWhen() != null)
			{
				validatorTypesList.add("validwhen");
			}
		}

		if (isGenericValidator()) validatorTypesList.add("genericValidator");

		// custom (paramterized) validators are allowed here
		Collection taggedValues = findTaggedValues(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_VALIDATORS);
		for (final Iterator iterator = taggedValues.iterator(); iterator.hasNext();)
		{
			String validator = String.valueOf(iterator.next());
			validatorTypesList.add(Bpm4StrutsUtils.parseValidatorName(validator));
		}

		return validatorTypesList;
	}

	/**
     * @return <code>true</code> if the type of this field is a character, <code>false</code> otherwise
     */
    private boolean isValidatorChar()
    {
        return UMLMetafacadeUtils.isType(this.getType(), Bpm4StrutsProfile.CHARACTER_TYPE_NAME);
    }

    /**
     * @return the lower limit for this field's value's range
     */
    private String getRangeStart(String format)
    {
        return getToken(format, 1, 3);
    }

    /**
     * @return the upper limit for this field's value's range
     */
    private String getRangeEnd(String format)
    {
        return getToken(format, 2, 3);
    }

    /**
	 * @return the minimum number of characters this field's value must consist of
	 */
	private String getMinLengthValue(String format)
	{
		return getToken(format, 1, 2);
	}

	/**
	 * @return the maximum number of characters this field's value must consist of
	 */
	private String getMaxLengthValue(String format)
	{
		return getToken(format, 1, 2);
	}

	/**
	 * @return the pattern this field's value must respect
	 */
	private String getPatternValue(String format)
	{
		return '^' + getToken(format, 1, 2) + '$';
	}

	/**
	 * @return the i-th space delimited token read from the argument String, where i does not exceed the specified limit
	 */
	private String getToken(String string,
			int index,
			int limit)
	{
		if (string == null) return null;

		final String[] tokens = string.split("[\\s]+", limit);
		return (index >= tokens.length) ? null : tokens[index];
	}
	protected boolean handleIsOldStruts()
    {
		
		CoppetecStrutsAction actionObject = null;

        final EventFacade event = getEvent();
        if (event != null)
        {
            final TransitionFacade transition = event.getTransition();
            if (transition instanceof CoppetecStrutsAction)
            {
                actionObject = (CoppetecStrutsAction)transition;
            }else if(transition instanceof CoppetecStrutsForward){
            	actionObject = (CoppetecStrutsAction)((CoppetecStrutsForward)transition).getActions().get(0);
            }
        }
        if(actionObject!=null){
        	return actionObject.isActionOldStruts();
        }
        return true;
    }
	protected boolean handleIsOnlineHelp() {
		if (getWidgetType().equals(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_TYPE_HIDDEN))
			return false;
		
		final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
		return value != null;
	}

	protected boolean handleIsMoneyDolar() {
		Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_MONEY);
		final String moneyType = value == null ? null : value.toString();
		
		if (moneyType != null && moneyType.equals("dolar"))
			return true;
		
		return false;
	}

	protected boolean handleIsMoneyReal() {
		Object value = findTaggedValue(Bpm4StrutsProfile.TAGGEDVALUE_INPUT_MONEY);
		final String moneyType = value == null ? null : value.toString();
		
		if (moneyType == null || moneyType.equals("real"))
			return true;
		
		return false;
	}

	protected String handleGetHintKey() {
		 return getMessageKey() + ".hint.key";
	}

	protected String handleGetHintValue() {
		
		final StringBuffer buffer = new StringBuffer();

		final String value = StringUtilsHelper.toResourceMessage(this.getDocumentation("", 64, false));
		buffer.append((value == null) ? "" : value);
		
		return StringUtilsHelper.toResourceMessage(buffer.toString());
	}
}