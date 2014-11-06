package org.andromda.metafacades.uml14;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.AttributeFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.andromda.metafacades.uml.DependencyFacade;
import org.andromda.metafacades.uml.FilteredCollection;
import org.andromda.metafacades.uml.GeneralizableElementFacade;
import org.andromda.metafacades.uml.ModelElementFacade;
import org.andromda.metafacades.uml.NameMasker;
import org.andromda.metafacades.uml.OperationFacade;
import org.andromda.metafacades.uml.ParameterFacade;
import org.andromda.metafacades.uml.TypeMappings;
import org.andromda.metafacades.uml.UMLMetafacadeProperties;
import org.andromda.metafacades.uml.UMLMetafacadeUtils;
import org.andromda.metafacades.uml.UMLProfile;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.omg.uml.foundation.core.Abstraction;
import org.omg.uml.foundation.core.Attribute;
import org.omg.uml.foundation.core.CorePackage;
import org.omg.uml.foundation.core.DataType;
import org.omg.uml.foundation.core.Interface;
import org.omg.uml.foundation.core.Operation;


/**
 * Metaclass facade implementation.
 */
public class ClassifierFacadeLogicImpl
    extends ClassifierFacadeLogic
{
    public ClassifierFacadeLogicImpl(
        org.omg.uml.foundation.core.Classifier metaObject,
        String context)
    {
        super(metaObject, context);
    }

    /**
     * Overridden to provide name masking.
     *
     * @see org.andromda.metafacades.uml.ModelElementFacade#getName()
     */
    protected String handleGetName()
    {
        final String nameMask =
            String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.CLASSIFIER_NAME_MASK));
        return NameMasker.mask(
            super.handleGetName(),
            nameMask);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperations()
     */
    protected java.util.Collection handleGetOperations()
    {
        return new FilteredCollection(metaObject.getFeature())
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof Operation;
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAssociationEnds()
     */
    protected java.util.Collection handleGetAssociationEnds()
    {
        return UML14MetafacadeUtils.getCorePackage().getAParticipantAssociation().getAssociation(metaObject);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isPrimitiveType()
     */
    protected boolean handleIsPrimitive()
    {
        // If this type has a wrapper then its a primitive,
        // otherwise it isn't
        return this.getWrapperMappings() != null &&
        this.getWrapperMappings().getMappings().containsFrom(this.getFullyQualifiedName());
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isArrayType()
     */
    protected boolean handleIsArrayType()
    {
        return this.getFullyQualifiedName(true).endsWith(this.getArraySuffix());
    }

    /**
     * Gets the array suffix from the configured metafacade properties.
     *
     * @return the array suffix.
     */
    private String getArraySuffix()
    {
        return String.valueOf(this.getConfiguredProperty(UMLMetafacadeProperties.ARRAY_NAME_SUFFIX));
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getWrapperName()
     */
    protected String handleGetWrapperName()
    {
        String wrapperName = null;
        if (this.getWrapperMappings() != null)
        {
            if (this.getWrapperMappings().getMappings().containsFrom(this.getFullyQualifiedName()))
            {
                wrapperName = this.getWrapperMappings().getTo(this.getFullyQualifiedName());
            }
        }
        return wrapperName;
    }
    
    protected  boolean handleIsWebServiceGeneralizationFormat(){
    	
    	boolean valido = true;
    	
    	if(this.hasStereotype("WebServiceData")){
    		for(GeneralizableElementFacade generalization = this.getGeneralization(); generalization != null;generalization = generalization.getGeneralization()){
    			if(!generalization.hasStereotype("WebServiceData")){
    				valido = false;
    				break;
    			}
    		}
    	}
    	
    	return valido;
    }

    /**
     * Gets the mappings from primitive types to wrapper types. Some languages have primitives (i.e., Java) and some
     * languages don't, so therefore this property is optional.
     *
     * @return the wrapper mappings
     */
    protected TypeMappings getWrapperMappings()
    {
        final String propertyName = UMLMetafacadeProperties.WRAPPER_MAPPINGS_URI;
        final Object property = this.getConfiguredProperty(propertyName);
        TypeMappings mappings = null;
        String uri;
        if (property instanceof String)
        {
            uri = (String)property;
            try
            {
                mappings = TypeMappings.getInstance(uri);
                this.setProperty(
                    propertyName,
                    mappings);
            }
            catch (final Throwable throwable)
            {
                final String errMsg = "Error getting '" + propertyName + "' --> '" + uri + "'";
                logger.error(
                    errMsg,
                    throwable);

                // don't throw the exception
            }
        }
        else
        {
            mappings = (TypeMappings)property;
        }
        return mappings;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isCollectionType()
     */
    protected boolean handleIsCollectionType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.COLLECTION_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isListType()
     */
    protected boolean handleIsListType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.LIST_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isSetType()
     */
    protected boolean handleIsSetType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.SET_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isBooleanType()
     */
    protected boolean handleIsBooleanType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.BOOLEAN_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDateType()
     */
    protected boolean handleIsDateType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.DATE_TYPE_NAME);
    }
    
    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isTimeType()
     */
    protected boolean handleIsTimeType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.TIME_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isFileType()
     */
    protected boolean handleIsFileType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.FILE_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isBlobType()
     */
    protected boolean handleIsBlobType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.BLOB_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isMapType()
     */
    public boolean handleIsMapType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.MAP_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isStringType()
     */
    protected boolean handleIsStringType()
    {
        return UMLMetafacadeUtils.isType(
            this,
            UMLProfile.STRING_TYPE_NAME);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes()
     */
    protected java.util.Collection handleGetAttributes()
    {
        return new FilteredCollection(metaObject.getFeature())
            {
                public boolean evaluate(final Object object)
                {
                    return object instanceof Attribute;
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAttributes(boolean)
     */
    protected Collection handleGetAttributes(boolean follow)
    {
        final Collection attributes = this.getAttributes();
        for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null && follow;
            superClass = (ClassifierFacade)superClass.getGeneralization())
        {
            attributes.addAll(
                new FilteredCollection(superClass.getAttributes(follow))
                {
                    public boolean evaluate(final Object object)
                    {
                        boolean valid = true;
                        for (final Iterator iterator = attributes.iterator(); iterator.hasNext();)
                        {
                            final AttributeFacade attribute = (AttributeFacade)iterator.next();
                            final AttributeFacade superAttribute = (AttributeFacade)object;
                            if (attribute.getName().equals(superAttribute.getName()))
                            {
                                valid = false;
                                break;
                            }
                        }
                        return valid;
                    }
                });
        }
        return attributes;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties()
     */
    protected java.util.Collection handleGetProperties()
    {
        final Collection properties = this.getAttributes();
        properties.addAll(this.getNavigableConnectingEnds());
        return properties;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties(boolean)
     */
    protected Collection handleGetProperties(boolean follow)
    {
        final Collection properties = this.getAttributes(follow);
        final Collection associations = this.getNavigableConnectingEnds();
        if (follow)
        {
            for (ClassifierFacade superClass = (ClassifierFacade)getGeneralization(); superClass != null && follow;
                superClass = (ClassifierFacade)superClass.getGeneralization())
            {
                // - filter duplicate association ends from each generalization.
                properties.addAll(
                    new FilteredCollection(superClass.getNavigableConnectingEnds())
                    {
                        public boolean evaluate(final Object object)
                        {
                            boolean valid = true;
                            for (final Iterator iterator = associations.iterator(); iterator.hasNext();)
                            {
                                final AssociationEndFacade association = (AssociationEndFacade)iterator.next();
                                final AssociationEndFacade superAssociation = (AssociationEndFacade)object;
                                if (association.getName().equals(superAssociation.getName()))
                                {
                                    valid = false;
                                    break;
                                }
                            }
                            return valid;
                        }
                    });
            }
        }
        properties.addAll(associations);
        return properties;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getOperationCallFromAttributes()
     */
    protected String handleGetOperationCallFromAttributes()
    {
        final StringBuffer call = new StringBuffer();
        String separator = "";
        call.append("(");
        for (final Iterator iterator = getAttributes().iterator(); iterator.hasNext();)
        {
            AttributeFacade attribute = (AttributeFacade)iterator.next();

            call.append(separator);
            String typeName = attribute.getType().getFullyQualifiedName();
            call.append(typeName);
            call.append(" ");
            call.append(attribute.getName());
            separator = ", ";
        }
        call.append(")");
        return call.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isAbstract()
     */
    protected boolean handleIsAbstract()
    {
        return this.metaObject.isAbstract();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getStaticAttributes()
     */
    protected Collection handleGetStaticAttributes()
    {
        return new FilteredCollection(this.getAttributes())
            {
                public boolean evaluate(Object object)
                {
                    return ((AttributeFacade)object).isStatic();
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getImplementedInterfaceList()
     */
    protected String handleGetImplementedInterfaceList()
    {
        final StringBuffer list = new StringBuffer();
        if (this.getAbstractions() != null)
        {
            for (final Iterator iterator = this.getAbstractions().iterator(); iterator.hasNext();)
            {
                DependencyFacade abstraction = (DependencyFacade)iterator.next();
                final ModelElementFacade element = abstraction.getTargetElement();
                list.append(element.getFullyQualifiedName());
                if (iterator.hasNext())
                {
                    list.append(", ");
                }
            }
        }
        return list.toString();
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInstanceAttributes()
     */
    protected java.util.Collection handleGetInstanceAttributes()
    {
        return new FilteredCollection(this.getAttributes())
            {
                public boolean evaluate(Object object)
                {
                    return !((AttributeFacade)object).isStatic();
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getAbstractions()
     */
    protected Collection handleGetAbstractions()
    {
        return new FilteredCollection(this.metaObject.getClientDependency())
            {
                public boolean evaluate(Object object)
                {
                    return object instanceof Abstraction;
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDatatype()
     */
    protected boolean handleIsDataType()
    {
        return DataType.class.isAssignableFrom(this.metaObject.getClass());
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isDatatype()
     */
    protected boolean handleIsInterface()
    {
        return Interface.class.isAssignableFrom(this.metaObject.getClass());
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNonArray()
     */
    protected Object handleGetNonArray()
    {
        ClassifierFacade nonArrayType = this;
        if (this.getFullyQualifiedName().indexOf(this.getArraySuffix()) != -1)
        {
            nonArrayType =
                (ClassifierFacade)this.getRootPackage().findModelElement(
                    StringUtils.replace(
                        this.getFullyQualifiedName(true),
                        this.getArraySuffix(),
                        ""));
        }
        return nonArrayType;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getArray()
     */
    protected Object handleGetArray()
    {
        ClassifierFacade arrayType = this;
        String name = this.getFullyQualifiedName(true);
        if (name.indexOf(this.getArraySuffix()) == -1)
        {
            name = name + this.getArraySuffix();
            arrayType = (ClassifierFacade)this.getRootPackage().findModelElement(name);
        }
        return arrayType;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#addAttribute(java.lang.String, java.lang.String,
            *      java.lang.String)
     */
    protected void handleAddAttribute(
        String name,
        String fullyQualifiedType,
        String visibility)
    {
        CorePackage corePackage = UML14MetafacadeUtils.getCorePackage();
        Attribute attribute = corePackage.getAttribute().createAttribute();
        attribute.setName(name);
        attribute.setVisibility(UML14MetafacadeUtils.getVisibilityKind(visibility));
        this.metaObject.getFeature().add(attribute);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isEnumeration()
     */
    protected boolean handleIsEnumeration()
    {
        return this.hasStereotype(UMLProfile.STEREOTYPE_ENUMERATION);
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getJavaNullString()
     */
    protected String handleGetJavaNullString()
    {
        String javaNullString;
        if (isPrimitive())
        {
            if (UMLMetafacadeUtils.isType(
                    this,
                    UMLProfile.BOOLEAN_TYPE_NAME))
            {
                javaNullString = "false";
            }
            else
            {
                javaNullString = "0";
            }
        }
        else
        {
            javaNullString = "null";
        }
        return javaNullString;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getStaticOperations()
     */
    protected Collection handleGetStaticOperations()
    {
        return new FilteredCollection(this.getOperations())
            {
                public boolean evaluate(Object object)
                {
                    return ((OperationFacade)object).isStatic();
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getInstanceOperations()
     */
    protected Collection handleGetInstanceOperations()
    {
        return new FilteredCollection(this.getOperations())
            {
                public boolean evaluate(Object object)
                {
                    return !((OperationFacade)object).isStatic();
                }
            };
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#findAttribute(java.lang.String)
     */
    protected AttributeFacade handleFindAttribute(final String name)
    {
        return (AttributeFacade)CollectionUtils.find(
            this.getAttributes(),
            new Predicate()
            {
                public boolean evaluate(Object object)
                {
                    final AttributeFacade attribute = (AttributeFacade)object;
                    return StringUtils.trimToEmpty(attribute.getName()).equals(name);
                }
            });
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getArrayName()
     */
    protected String handleGetArrayName()
    {
        return this.getName() + this.getArraySuffix();
    }

    /**
     * @see org.andromda.metafacades.uml14.ClassifierFacade#getFullyQualifiedArrayName()
     */
    protected String handleGetFullyQualifiedArrayName()
    {
        return this.getFullyQualifiedName() + this.getArraySuffix();
    }

    /**
     * Calculates the serial version UID of this classifier based on the
     * signature of the classifier (name, visibility, attributes and methods).
     * The algorithm is inspired by
     * {@link java.io.ObjectStreamClass#getSerialVersionUID()}.
     *
     * The value should be stable as long as the classifier remains unchanged
     * and should change as soon as there is any change in the signature of the
     * classifier.
     *
     * @return the serial version UID of this classifier.
     */
    private Long calculateDefaultSUID()
    {
        final StringBuffer buffer = new StringBuffer();

        // class name
        buffer.append(this.getName());

        // class modifiers (visibility)
        buffer.append(this.getVisibility());

        // generalizations
        for (final Iterator iterator = this.getAllGeneralizations().iterator(); iterator.hasNext();)
        {
            ClassifierFacade classifier = (ClassifierFacade)iterator.next();
            buffer.append(classifier.getName());
        }

        // declared fields
        for (final Iterator iterator = this.getAttributes().iterator(); iterator.hasNext();)
        {
            AttributeFacade attribute = (AttributeFacade)iterator.next();
            buffer.append(attribute.getName());
            buffer.append(attribute.getVisibility());
            buffer.append(attribute.getType());
        }

        // operations
        for (final Iterator iter = this.getOperations().iterator(); iter.hasNext();)
        {
            OperationFacade operation = (OperationFacade)iter.next();
            buffer.append(operation.getName());
            buffer.append(operation.getVisibility());
            buffer.append(operation.getReturnType());
            for (final Iterator iterator = operation.getParameters().iterator(); iterator.hasNext();)
            {
                final ParameterFacade parameter = (ParameterFacade)iterator.next();
                buffer.append(parameter.getName());
                buffer.append(parameter.getVisibility());
                buffer.append(parameter.getType());
            }
        }
        final String signature = buffer.toString();

        Long serialVersionUID = new Long(0L);
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] hashBytes = md.digest(signature.getBytes());

            long hash = 0;
            for (int ctr = Math.min(hashBytes.length, 8) - 1; ctr >= 0; ctr--)
            {
                hash = (hash << 8) | (hashBytes[ctr] & 0xFF);
            }
            serialVersionUID = new Long(hash);
        }
        catch (final NoSuchAlgorithmException exception)
        {
            final String errMsg = "Error performing ModelElementFacadeImpl.getSerialVersionUID";
            logger.error(errMsg, exception);
        }
        return serialVersionUID;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getSerialVersionUID()
     */
    protected Long handleGetSerialVersionUID()
    {
        Long serialVersionUID;
        final String serialVersionString = UML14MetafacadeUtils.getSerialVersionUID(this);
        if (serialVersionString != null)
        {
            serialVersionUID = Long.valueOf(serialVersionString);
        }
        else
        {
            serialVersionUID = calculateDefaultSUID();
        }
        return serialVersionUID;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getNavigableConnectingEnds()
     */
    protected Collection handleGetNavigableConnectingEnds()
    {
        final Collection connectingEnds = this.getAssociationEnds();
        CollectionUtils.transform(
            connectingEnds,
            new Transformer()
            {
                public Object transform(final Object object)
                {
                    return ((AssociationEndFacade)object).getOtherEnd();
                }
            });
        CollectionUtils.filter(
            connectingEnds,
            new Predicate()
            {
                public boolean evaluate(final Object object)
                {
                    return ((AssociationEndFacade)object).isNavigable();
                }
            });
        return connectingEnds;
    }

    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#isLeaf()
     */
    protected boolean handleIsLeaf()
    {
        return this.metaObject.isLeaf();
    }

    /**
     * Verifica se o nome da classe tem tamanho valido
     */
	protected boolean handleValidateFullyQualifiedNameSize() {

		// Verifica se a propriedade validateSizeClass está ativa (true)
		boolean validate;
		Object validateProperty = this.getConfiguredProperty(UMLMetafacadeProperties.VALIDATE_SIZE_CLASS);		
		
		if (validateProperty == null)
			validate = false;
		else
			validate = Boolean.parseBoolean(String.valueOf(validateProperty));

		// Se não estiver ativada não realiza verificação
		if (!validate) return true;
		
		// Lê a propriedade maxSizeClass que indica o tamanho máximo do fullyQualifiedName
		int maxLength;
		Object maxLengthProperty = this.getConfiguredProperty(UMLMetafacadeProperties.MAX_SIZE_CLASS);		
		
		if (maxLengthProperty == null)
			return true;

		try { maxLength = Integer.parseInt(String.valueOf(maxLengthProperty)); }
		catch (Exception e) { return true; }
		
		// Faz a verificação se o tamanho do fullyQualifiedName não ultrapassa o especificado
		return getFullyQualifiedName().length() <= maxLength;
	}
 
    public Comparator getComparatorObject(){
    	return new Comparator(){
    		public int compare(Object obj1, Object obj2){
    			if(obj1 instanceof ClassifierFacade && obj2 instanceof ClassifierFacade){
    				ClassifierFacade class1 = (ClassifierFacade)obj1;
    				ClassifierFacade class2 = (ClassifierFacade)obj2;
    				return class1.getName().compareToIgnoreCase(class2.getName());
    			}
    			throw new RuntimeException("Os elementos nao sao ClassifierFacade");
    		}
    	};
    }

	protected boolean handleValidateOpenAccess() {
		if(this.hasStereotype(UMLProfile.STEREOTYPE_OPEN_ACCESS))
		{
			return this.hasStereotype(UMLProfile.STEREOTYPE_SERVICE) || this.hasStereotype(UMLProfile.STEREOTYPE_FRONT_END_USECASE);
		}
		return true;
	}
}
