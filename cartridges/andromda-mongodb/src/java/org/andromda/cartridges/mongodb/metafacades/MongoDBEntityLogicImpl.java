package org.andromda.cartridges.mongodb.metafacades;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.andromda.cartridges.mongodb.MongoDBProfile;
import org.andromda.metafacades.uml.AssociationEndFacade;
import org.andromda.metafacades.uml.ClassifierFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;


/**
 * <p/> Provides support for the mongodb inheritance strategies of class
 * (table per hierarchy), subclass (table per subclass in hierarchy) and
 * concrete (table per class). With concrete the strategy can be changed lower
 * down. Also provides for the root class being defined as an interface and the
 * attributes remapped to the subclasses. This is useful in the concrete case
 * becuase it has limitations in the associations.
 * </p>
 * <p/> Also provides support for not generating the entity factory which is
 * useful when using subclass mode.
 * </p>
 *
 * @author Chad Brandon
 * @author Martin West
 * @author Carlos Cuenca
 */
public class MongoDBEntityLogicImpl
    extends MongoDBEntityLogic
{
    public MongoDBEntityLogicImpl(
        java.lang.Object metaObject,
        String context)
    {
        super(metaObject, context);
    }
    
    /**
     * @see org.andromda.metafacades.uml.ClassifierFacade#getProperties()
     */
    public java.util.Collection getProperties()
    {
        Collection properties = this.getAttributes();
        Collection connectingEnds = this.getAssociationEnds();
        CollectionUtils.transform(
            connectingEnds,
            new Transformer()
            {
                public Object transform(Object object)
                {
                    return ((AssociationEndFacade)object).getOtherEnd();
                }
            });
        class NavigableFilter
            implements Predicate
        {
            public boolean evaluate(Object object)
            {
                AssociationEndFacade end = (AssociationEndFacade)object;

                return end.isNavigable() || (end.getOtherEnd().isChild());
            }
        }
        CollectionUtils.filter(
            connectingEnds,
            new NavigableFilter());
        properties.addAll(connectingEnds);

        return properties;
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEntity#getFullyQualifiedEntityName()
     */
    protected String handleGetFullyQualifiedEntityName()
    {
        return MongoDBMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getEntityName(),
            null);
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEntity#getFullyQualifiedEntityImplementationName()
     */
    protected String handleGetFullyQualifiedEntityImplementationName()
    {
        return MongoDBMetafacadeUtils.getFullyQualifiedName(
            this.getPackageName(),
            this.getEntityImplementationName(),
            null);
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEntity#getEntityName()
     */
    protected String handleGetEntityName()
    {
        String entityNamePattern = (String)this.getConfiguredProperty(MongoDBGlobals.ENTITY_NAME_PATTERN);

        return MessageFormat.format(
            entityNamePattern,
            new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEntity#getEntityImplementationName()
     */
    protected String handleGetEntityImplementationName()
    {
        String implNamePattern =
            String.valueOf(this.getConfiguredProperty(MongoDBGlobals.ENTITY_IMPLEMENTATION_NAME_PATTERN));

        return MessageFormat.format(
            implNamePattern,
            new Object[] {StringUtils.trimToEmpty(this.getName())});
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEntity#isTableRequired()
     */
    protected boolean handleIsTableRequired()
    {
        return (this.getGeneralization() == null);
    }

    /**
     * Gets the super entity for this entity (if one exists). If a
     * generalization does not exist OR if it's not an instance of
     * MongoDBEntity then return null.
     *
     * @return the super entity or null if one doesn't exist.
     */
    private MongoDBEntity getSuperEntity()
    {
        MongoDBEntity superEntity = null;

        if ((this.getGeneralization() != null) && this.getGeneralization() instanceof MongoDBEntity)
        {
            superEntity = (MongoDBEntity)this.getGeneralization();
        }

        return superEntity;
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEntity#isRequiresMapping()
     */
    protected boolean handleIsRequiresMapping()
    {
        final MongoDBEntity superEntity = this.getSuperEntity();
        final boolean requiresMapping = this.isRoot() &&
        (
            this.getSpecializations().isEmpty()
        );
        return requiresMapping;
    }
    
    protected boolean handleIsDocument()
    {
    	return this.hasStereotype(MongoDBProfile.STEREOTYPE_DOCUMENT);
    }

    /**
     * Indicates if this entity is a <code>root</code> entity (meaning it
     * doesn't specialize anything).
     */
    private boolean isRoot()
    {
        final MongoDBEntity superEntity = this.getSuperEntity();
        boolean abstractConcreteEntity =
             this.isAbstract();

        return (this.getSuperEntity() == null 
            && !abstractConcreteEntity);
    }

    /**
     * @see org.andromda.cartridges.mongodb.metafacades.MongoDBEntity#getVersion()
     */
    protected int handleGetVersion()
    {
        return Integer.parseInt((String)this.getConfiguredProperty(MongoDBGlobals.MONGODB_VERSION));
    }
    
    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getAggregationChain()
     */
    public String getAggregationChain()
    {
    	ArrayList ends = (ArrayList) this.getAssociationEnds();
    	
    	for (Iterator iterator = this.getAssociationEnds().iterator(); iterator.hasNext();) {
    		MongoDBAssociationEnd end = (MongoDBAssociationEnd) iterator.next();
    		if (end.getOtherEnd().isAggregation()) {
    			return getAggregationName(this, (MongoDBAssociationEnd)end.getOtherEnd()) + "." + this.getName();
    		}
		}
    	
    	return this.getName();
    }
    
    /**
     * @see org.andromda.metafacades.uml.AssociationEndFacade#getAggregationName()
     */
    public String getAggregationName(MongoDBEntity child, MongoDBAssociationEnd target)
    {
    	String res = "";
    	MongoDBEntity ent = null;
    	ArrayList dependencies = (ArrayList) child.getEntityReferences();
    	
    	for (Iterator iterator = child.getNavigableConnectingEnds().iterator(); iterator.hasNext();) {
    		MongoDBAssociationEnd dep = (MongoDBAssociationEnd) iterator.next();
    		if (dep.getName() != null) res += dep.getName() + " ";
		}
    	
    	
    	if (ent == null) return target.getName() + " " + res;
    	else {
	    	//ArrayList ends =  (ArrayList) ent.getAssociationEnds();
	    	
	    	for (Iterator iterator = this.getAssociationEnds().iterator(); iterator.hasNext();) {
	    		MongoDBAssociationEnd end = (MongoDBAssociationEnd) iterator.next();
	    		if (end.getOtherEnd().isAggregation()) {
	    			return getAggregationName(ent, (MongoDBAssociationEnd)end.getOtherEnd()) + "." + target.getName();
	    		}
			}
	    	
    	}
    	return target.getName() + "2" + res;
    }
    
}