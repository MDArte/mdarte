package org.andromda.core.cartridge;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.andromda.core.cartridge.template.ModelElement;
import org.andromda.core.cartridge.template.ModelElements;
import org.andromda.core.cartridge.template.Template;
import org.andromda.core.cartridge.template.Type;
import org.andromda.core.common.AndroMDALogger;
import org.andromda.core.common.BasePlugin;
import org.andromda.core.common.ExceptionUtils;
import org.andromda.core.common.Introspector;
import org.andromda.core.common.PathMatcher;
import org.andromda.core.common.ResourceUtils;
import org.andromda.core.common.ResourceWriter;
import org.andromda.core.configuration.Namespaces;
import org.andromda.core.metafacade.MetafacadeBase;
import org.andromda.core.metafacade.MetafacadeBaseModular;
import org.andromda.core.metafacade.MetafacadeFactory;
import org.andromda.core.metafacade.ModelAccessFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * The AndroMDA Cartridge implementation of the Plugin. Cartridge instances are
 * configured from <code>META-INF/andromda-cartridge.xml</code> files discovered
 * on the classpath.
 * 
 * @author <a href="http://www.mbohlen.de">Matthias Bohlen </a>
 * @author Chad Brandon
 */
public class Cartridge extends BasePlugin {
	private static final String ARCHITECTURAL_BREACHES_PROPERTY = "architecturalBreaches";

	private static final Log log = LogFactory.getLog(Cartridge.class);


	private static Document documentTree = null;
	private static HashMap decisionMap = new HashMap();

	public static long tempoGerandoArquivos = 0;
	public static long tempoIgnorandoArquivos = 0;
	public static long tempoGerandoArquivosExistentes = 0;

	private boolean unicWeb = true;

	private static List unicWebTemplateIgnore = new ArrayList();
	private static List nonUnicWebTemplateIgnore = new ArrayList();

	/**
	 * The forward slash constant.
	 */
	private static final String FORWARD_SLASH = "/";

	/**
	 * Stores the loaded resources to be processed by this cartridge intance.
	 */
	private final List resources = new ArrayList();

	public Cartridge() {
		super();

		// -----------------
		// Se for gerar um web unico nao processar os seguintes templates:
		// ----------------

		// Apaga Sessao nao sera mais necessario pois nao havera mais copia de sessao
		unicWebTemplateIgnore.add("templates/bpm4struts/actions/controleAcesso/modulo/ApagaSessao.java.vsl");

		// NovaConexao sera substituido pelo NovaConexao.java comum
		unicWebTemplateIgnore.add("templates/bpm4struts/actions/controleAcesso/NovaConexao.java.vsl");
		unicWebTemplateIgnore.add("templates/bpm4struts/actions/controleAcesso/modulo/NovaConexao.java.vsl");

		// Sera criado um struts-config.xml com todos os parametros comuns, os outros serao criados com o template struts-config-modulo.xml.vsl
		unicWebTemplateIgnore.add("templates/bpm4struts/configuration/struts-config-global.xml.vsl");
		unicWebTemplateIgnore.add("templates/bpm4struts/configuration/struts-config.xml.vsl");

		// Sera criado um jboss-web.xml unico com o path /siconv
		unicWebTemplateIgnore.add("templates/bpm4struts/modulo/jboss-web.xml.vsl");

		// Sera gerado um web.xml com todos os parametros comuns
		unicWebTemplateIgnore.add("templates/bpm4struts/configuration/web.xml.vsl");

		unicWebTemplateIgnore.add("templates/bpm4struts/configuration/struts-config-actions-uc.xml.vsl");

		unicWebTemplateIgnore.add("templates/bpm4struts/actions/Action.java.vsl");
		unicWebTemplateIgnore.add("templates/bpm4struts/actions/ForwardAction.java.vsl");
		unicWebTemplateIgnore.add("templates/bpm4struts/actions/Main.java.vsl");

		unicWebTemplateIgnore.add("templates/bpm4struts/actions/controleAcesso/Login.java.vsl");
		unicWebTemplateIgnore.add("templates/bpm4struts/actions/controleAcesso/NovaConexao.java.vsl");
		unicWebTemplateIgnore.add("templates/bpm4struts/actions/controleAcesso/TrocaDeSenhaObrigatoriaProcessar.java.vsl");

		unicWebTemplateIgnore.add("templates/andromda-hibernate/src/templates/hibernate/LogInterceptor.vsl");

		// ---------------------------		
		// Se nao for gerar um web unico, nao processar os seguintes templates:
		// ----------------------------

		nonUnicWebTemplateIgnore.add("templates/bpm4struts/configuration/web-common/jboss-web.xml.vsl");
		nonUnicWebTemplateIgnore.add("templates/bpm4struts/configuration/web-common/struts-config.xml.vsl");
		nonUnicWebTemplateIgnore.add("templates/bpm4struts/configuration/web-common/struts-config-actions-uc.xml.vsl");
		nonUnicWebTemplateIgnore.add("templates/bpm4struts/configuration/web-common/struts-config-modulo.xml.vsl");

		nonUnicWebTemplateIgnore.add("templates/bpm4struts/actions/web-common/Action.java.vsl");
		nonUnicWebTemplateIgnore.add("templates/bpm4struts/actions/web-common/ForwardAction.java.vsl");
		nonUnicWebTemplateIgnore.add("templates/bpm4struts/actions/web-common/Main.java.vsl");

		nonUnicWebTemplateIgnore.add("templates/bpm4struts/actions/controleAcesso/web-common/Login.java.vsl");
		nonUnicWebTemplateIgnore.add("templates/bpm4struts/actions/controleAcesso/web-common/NovaConexao.java.vsl");
		nonUnicWebTemplateIgnore.add("templates/bpm4struts/actions/controleAcesso/web-common/TrocaDeSenhaObrigatoriaProcessar.java.vsl");
	}

	/**
	 * Adds a resource to the list of defined resources.
	 * 
	 * @param resource
	 *            the new resource to add
	 */
	public void addResource(final Resource resource) {
		ExceptionUtils.checkNull("Cartridge.addResource", "resource", resource);
		resource.setCartridge(this);
		resources.add(resource);
	}

	private String getPackageName(final Map templateContext, String key) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		if (templateContext.containsKey(key)) {
			Object o = templateContext.get(key);
			if (key.equals("table")) {
				if (o.getClass().getMethod("getTableDecoratorPackageName", null) != null) {
					return (String) o.getClass().getMethod("getTableDecoratorPackageName", null).invoke(o, null);
				}

			} else {
				if (o.getClass().getMethod("getPackageName", null) != null) {
					return (String) o.getClass().getMethod("getPackageName", null).invoke(o, null);
				}
			}
		}
		return null;
	}

	/**
	 * Returns the list of templates configured in this cartridge.
	 * 
	 * @return List the template list.
	 */
	public List getResources() {
		return this.resources;
	}

	private void gravaArquivoGeracao(String packageName, String outlet) {
		String caminho = outlet + '/' + packageName.replace('.', '/');

		//AndroMDALogger.info("\ncaminho gravar: "+caminho);

		File packageFile = new File(caminho + "/ger.tmp");
		OutputStream bos;
		try {
			bos = new FileOutputStream(packageFile);
			String resposta = new String("pacote :" + packageName + "\nGerado em:" + (new Date(System.currentTimeMillis())));
			bos.write(resposta.getBytes());
			bos.close();
		} catch (Exception e) {
			AndroMDALogger.info("\nErro ao gravar arquivo de pacote gerado: " + packageName + "\n");
		}
	}

	private Boolean isAutorizado(String packageName, String outlet) {

		if (outlet != null && packageName != null) {

			Date packageDate = leDataPacoteGeracao(packageName, outlet);

			if (packageDate == null) {
				gravaArquivoGeracao(packageName, outlet);
				return Boolean.TRUE;
			}

			try {
				if (this.documentTree == null) {
					String xmlStr = lerArquivoXml(outlet);
					if (xmlStr != null) {
						//AndroMDALogger.info("Arquivo lido");

						this.documentTree = DocumentHelper.parseText(xmlStr);
					}
				}

				if (this.documentTree != null) {
					Document document = this.documentTree;

					Element elemRoot = document.getRootElement();
					Iterator iRoot = elemRoot.elementIterator();
					while (iRoot.hasNext()) {
						Element elemModulo = (Element) iRoot.next();

						if (elemModulo.getName().equals("modulo")) {
							String modulo = elemModulo.attributeValue("nome");
							//AndroMDALogger.info("modulo: "+modulo);	

							if (packageName.indexOf("." + modulo + ".") >= 0) {
								Iterator iModulo = elemModulo.elementIterator();

								Date dateModeloFileSystem = leDataModeloGeracao(modulo, outlet);
								if (dateModeloFileSystem != null && (dateModeloFileSystem.compareTo(packageDate) < 0)) {
									packageDate = dateModeloFileSystem;
								}

								while (iModulo.hasNext()) {
									Element elemModuloProperty = (Element) iModulo.next();

									if (elemModuloProperty.getName().equals("arquivo")) {
										String nomePacote = elemModuloProperty.attributeValue("pacote");
										//AndroMDALogger.info("nomePacote: "+nomePacote);

										if (packageName.indexOf(nomePacote) >= 0) {// || nomePacote.indexOf(packageName) >= 0) {
											Date data = new Date(Long.parseLong(elemModuloProperty.attributeValue("data")));

											//AndroMDALogger.info("packageDate: "+packageDate);
											//AndroMDALogger.info("data: "+data);

											if (packageDate.compareTo(data) <= 0) {
												//AndroMDALogger.info("retorna true");		
												gravaArquivoGeracao(packageName, outlet);
												return Boolean.TRUE;
											}

											//AndroMDALogger.info("retorna false");
											return Boolean.FALSE;
										}

									}
								}
								return Boolean.FALSE;
							}
						}
					}
				}
				gravaArquivoGeracao(packageName, outlet);
				return Boolean.TRUE;
			} catch (Exception e) {
				AndroMDALogger.info("Erro ao interpretar xml");
			}
		}
		return Boolean.TRUE;
	}

	public boolean isUnicWeb() {
		return unicWeb;
	}

	private Date leDataModeloGeracao(String modelo, String outlet) {
		String arquivo = outlet.substring(0, outlet.indexOf("mda/src") + "mda/src".length());

		String caminho = arquivo + "/uml/";

		File packageFile = new File(caminho);
		try {
			File[] files = packageFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.getName().endsWith(".tmp") && (file.getName().indexOf(modelo) >= 0)) {
					Date dataModelo = new Date(file.lastModified());
					return dataModelo;
				}
			}
		} catch (Exception e) {
			AndroMDALogger.info("\nErro ao ler arquivo de modelo gerado: " + modelo + "\n");
		}

		return null;
	}

	private Date leDataPacoteGeracao(String packageName, String outlet) {
		String caminho = outlet + '/' + packageName.replace('.', '/');

		File packageFile = new File(caminho + "/ger.tmp");
		try {
			if (packageFile.exists()) {
				Date fileDate = new Date(packageFile.lastModified());
				return fileDate;
			}

		} catch (Exception e) {
			AndroMDALogger.info("\nErro ao gravar arquivo de modelo gerado - pacote: " + packageName + "\n");
		}

		return null;
	}

	private String lerArquivoXml(String outlet) {
		if (outlet == null)
			return null;

		String arquivo = null;
		try {
			arquivo = outlet.substring(0, outlet.indexOf("mda/src")) + "modulos.xml";
			File file = new File(arquivo);
			if (file.exists()) {
				//AndroMDALogger.info("Arquivo existe");
				BufferedReader in = new BufferedReader(new FileReader(arquivo));
				StringBuffer str = new StringBuffer();
				while (in.ready()) {
					str.append(in.readLine());
					str.append("\n");
				}
				in.close();
				return str.toString();
			}
		} catch (IOException e) {
			AndroMDALogger.info("\n Erro no cartucho: erro na abertura do arquivo XML de arvore para geração - arquivo: " + arquivo
					+ " . \n");
		}
		return null;
	}

	/**
	 * Processes all model elements with relevant stereotypes by retrieving the
	 * model elements from the model facade contained within the context.
	 * 
	 * @param factory
	 *            the metafacade factory (which is used to manage the lifecycle
	 *            of metafacades).
	 */
	public void processModelElements(final MetafacadeFactory factory) {
		System.out.print("\n");
		AndroMDALogger.info("Gerando com cartucho " + getNamespace() + ".");

		final String methodName = "Cartridge.processModelElements";
		ExceptionUtils.checkNull(methodName, "factory", factory);

		final Collection resources = this.getResources();
		if (resources != null && !resources.isEmpty()) {
			int size = resources.size();
			int count = 1;

			for (final Iterator iterator = resources.iterator(); iterator.hasNext();) {
				Double p = new Double(count * 100 / size);

				final Resource resource = (Resource) iterator.next();
				if (resource instanceof Template) {
					Template template = (Template) resource;

					if (this.isUnicWeb()) {
						if (unicWebTemplateIgnore.contains(template.getPath())) {
							//AndroMDALogger.info("Template IGNORADO: " + template.getPath());
							continue;
						}
					} else {
						if (nonUnicWebTemplateIgnore.contains(template.getPath())) {
							//AndroMDALogger.info("Template IGNORADO");
							continue;
						}
					}

					this.processTemplate(factory, (Template) resource);
				} else {
					this.processResource(resource);
				}
				count++;
			}
		}
	}

	/**
	 * Determines if any property templates need to be processed (that is
	 * templates that are processed given related <em>properties</em> of a
	 * metafacade).
	 * 
	 * @param template
	 *            the template to use for processing.
	 * @param metafacade
	 *            the metafacade instance (the property value is retrieved from
	 *            this).
	 * @param templateContext
	 *            the template context containing the instance to pass to the
	 *            template.
	 * @param outlet
	 *            the outlet to which output will be written.
	 * @param modelElement
	 *            the model element from which we retrieve the corresponding
	 *            types and then properties to determine if any properties have
	 *            been mapped for template processing.
	 * @return true if any property templates have been evaluated (false
	 *         othewise).
	 */
	private final boolean processPropertyTemplates(final Template template, final Object metafacade, final Map templateContext,
			final String outlet, final ModelElement modelElement) {
		boolean propertyTemplatesEvaluated = false;
		for (final Iterator types = modelElement.getTypes().iterator(); types.hasNext();) {
			final Type type = (Type) types.next();
			for (final Iterator properties = type.getProperties().iterator(); properties.hasNext();) {
				final Type.Property property = (Type.Property) properties.next();
				final String variable = property.getVariable();
				propertyTemplatesEvaluated = variable != null && variable.trim().length() > 0;
				if (propertyTemplatesEvaluated) {
					final Object value = Introspector.instance().getProperty(metafacade, property.getName());
					if (value instanceof Collection) {
						for (final Iterator values = ((Collection) value).iterator(); values.hasNext();) {
							templateContext.put(variable, values.next());
							this.processWithTemplate(template, templateContext, outlet, null, null);
						}
					} else {
						templateContext.put(variable, value);
						this.processWithTemplate(template, templateContext, outlet, null, null);
					}
				}
			}
		}
		return propertyTemplatesEvaluated;
	}

	/**
	 * Processes the given <code>resource</code>
	 * 
	 * @param resource
	 *            the resource to process.
	 */
	protected void processResource(final Resource resource) {
		final String methodName = "Cartridge.processResource";
		ExceptionUtils.checkNull(methodName, "resource", resource);

		URL resourceUrl = ResourceUtils.getResource(resource.getPath(), this.getMergeLocation());
		if (resourceUrl == null) {
			// - if the resourceUrl is null, the path is probably a regular
			//   expression pattern so we'll see if we can match it against
			//   the contents of the plugin and write any contents that do match
			final List contents = this.getContents();
			if (contents != null) {
				AndroMDALogger.setSuffix(this.getNamespace());
				for (final Iterator iterator = contents.iterator(); iterator.hasNext();) {
					final String content = (String) iterator.next();
					if (content != null && content.trim().length() > 0) {
						if (PathMatcher.wildcardMatch(content, resource.getPath())) {
							resourceUrl = ResourceUtils.getResource(content, this.getMergeLocation());
							this.writeResource(resource, resourceUrl);
						}
					}
				}
				AndroMDALogger.reset();
			}
		} else {
			this.writeResource(resource, resourceUrl);
		}
	}

	/**
	 * Processes the given <code>template</code>.
	 * 
	 * @param factory
	 *            the metafacade factory instance.
	 * @param template
	 *            the Template instance to process.
	 */
	protected void processTemplate(final MetafacadeFactory factory, final Template template) {
		final String methodName = "Cartridge.processTemplate";
		ExceptionUtils.checkNull(methodName, "template", template);
		final ModelElements templateModelElements = template.getSupportedModeElements();

		// - handle the templates WITH model elements
		if (templateModelElements != null && !templateModelElements.isEmpty()) {
			for (final Iterator iterator = templateModelElements.getModelElements().iterator(); iterator.hasNext();) {
				final ModelElement templateModelElement = (ModelElement) iterator.next();

				// - if the template model element has a stereotype
				//   defined, then we filter the metafacades based
				//   on that stereotype, otherwise we get all metafacades
				//   and let the templateModelElement perform filtering on the
				//   metafacades by type and properties
				if (templateModelElement.hasStereotype()) {
					templateModelElement.setMetafacades(factory.getMetafacadesByStereotype(templateModelElement.getStereotype()));
				} else if (templateModelElement.hasTypes()) {
					templateModelElement.setMetafacades(factory.getAllMetafacades());
				}
			}

			this.processTemplateWithMetafacades(factory, template);
		} else {
			// - handle any templates WITHOUT metafacades.
			this.processTemplateWithoutMetafacades(template);
		}
	}

	/**
	 * Processes all <code>modelElements</code> for this template.
	 * 
	 * @param factory
	 *            the metafacade factory
	 * @param template
	 *            the template to process
	 */
	protected void processTemplateWithMetafacades(final MetafacadeFactory factory, final Template template) {
		final String methodName = "Cartridge.processTemplateWithMetafacades";
		ExceptionUtils.checkNull(methodName, "template", template);
		final ModelElements modelElements = template.getSupportedModeElements();
		if (modelElements != null && !modelElements.isEmpty()) {
			final String outlet = Namespaces.instance().getPropertyValue(this.getNamespace(), template.getOutlet());

			if (outlet != null) {
				try {
					final Collection allMetafacades = modelElements.getAllMetafacades();
					AndroMDALogger.info("Processing " + template.getPath() + " with " + allMetafacades.size() + " metafacades from "
							+ modelElements.getModelElements().size() + " model elements");
					// - if outputToSingleFile is true AND outputOnEmptyElements
					//   is true or we have at least one metafacade in the
					//   allMetafacades collection, then we collect the template
					//   model elements and place them into the template context
					//   by their variable names.
					if (template.isOutputToSingleFile() && (template.isOutputOnEmptyElements() || !allMetafacades.isEmpty())) {
						final Map templateContext = new HashMap();

						// - first place all relevant model elements by the
						//   <modelElements/> variable name. If the variable
						//   isn't defined (which is possible), ignore.
						final String modelElementsVariable = modelElements.getVariable();
						if (modelElementsVariable != null && modelElementsVariable.trim().length() > 0) {
							templateContext.put(modelElements.getVariable(), allMetafacades);
						}

						// - now place the collections of elements by the given variable names. 
						//   (skip if the variable is NOT defined)
						for (final Iterator iterator = modelElements.getModelElements().iterator(); iterator.hasNext();) {
							final ModelElement modelElement = (ModelElement) iterator.next();
							final String modelElementVariable = modelElement.getVariable();
							if (modelElementVariable != null && modelElementVariable.trim().length() > 0) {
								// - if a modelElement has the same variable defined
								//   more than one time, then get the existing
								//   model elements added from the last iteration
								//   and add the new ones to that collection
								Collection metafacades = (Collection) templateContext.get(modelElementVariable);
								if (metafacades != null) {
									metafacades.addAll(modelElement.getMetafacades());
								} else {
									metafacades = modelElement.getMetafacades();
									templateContext.put(modelElementVariable, new LinkedHashSet(metafacades));
								}
							}
						}

						this.processWithTemplate(template, templateContext, outlet, null, null);
					} else {
						// - if outputToSingleFile isn't true, then
						//   we just place the model element with the default
						//   variable defined on the <modelElements/> into the
						//   template.
						for (final Iterator iterator = allMetafacades.iterator(); iterator.hasNext();) {
							final Map templateContext = new HashMap();
							final MetafacadeBase metafacade = (MetafacadeBase) iterator.next();
							final ModelAccessFacade model = factory.getModel();
							for (final Iterator elements = modelElements.getModelElements().iterator(); elements.hasNext();) {
								final ModelElement modelElement = (ModelElement) elements.next();
								String variable = modelElement.getVariable();

								// - if the variable isn't defined on the <modelElement/>, try
								//   the <modelElements/>
								if (variable == null || variable.trim().length() == 0) {
									variable = modelElements.getVariable();
								}

								// - only add the metafacade to the template context if the variable
								//   is defined (which is possible)
								if (variable != null && variable.trim().length() > 0) {
									templateContext.put(variable, metafacade);
								}

								/* ==================================================================
								    [Moises-COPPETEC] Código usado para redirecionar o outlet para o módulo correto
								   ================================================================== */

								String outletReplaced = new String(outlet);

								if (metafacade instanceof MetafacadeBaseModular)
									outletReplaced = ((MetafacadeBaseModular) metafacade).insertModuleNameIntoOutlet(outletReplaced);

								// - now we process any property templates (if any 'variable' attributes are defined on one or
								//   more type's given properties), otherwise we process the single metafacade as usual
								if (!this.processPropertyTemplates(template, metafacade, templateContext, outletReplaced, modelElement)) {
									this.processWithTemplate(template, templateContext, outletReplaced, model.getName(metafacade),
											model.getPackageName(metafacade));
								}
							}
						}
					}
				} catch (final Throwable throwable) {
					throw new CartridgeException(throwable);
				}
			}
		}
	}

	/**
	 * Processes the <code>template</code> without metafacades. This is useful
	 * if you need to generate something that is part of your cartridge, however
	 * you only need to use a property passed in from a namespace or a template
	 * object defined in your cartridge descriptor.
	 * 
	 * @param template
	 *            the template to process.
	 */
	protected void processTemplateWithoutMetafacades(final Template template) {
		final String methodName = "Cartridge.processTemplateWithoutMetafacades";
		ExceptionUtils.checkNull(methodName, "template", template);
		final String outlet = Namespaces.instance().getPropertyValue(this.getNamespace(), template.getOutlet());
		if (outlet != null) {
			final Map templateContext = new HashMap();
			this.processWithTemplate(template, templateContext, outlet, null, null);
		}
	}

	/**
	 * <p>
	 * Perform processing with the <code>template</code>.
	 * </p>
	 * 
	 * @param template
	 *            the Template containing the template path to process.
	 * @param templateContext
	 *            the context to which variables are added and made available to
	 *            the template engine for processing. This will contain any
	 *            model elements being made avaiable to the template(s) as well
	 *            as properties/template objects.
	 * @param outlet
	 *            the location or pattern defining where output will be written.
	 * @param metafacadeName
	 *            the name of the model element (if we are processing a single
	 *            model element, otherwise this will be ignored).
	 * @param metafacadePackage
	 *            the name of the package (if we are processing a single model
	 *            element, otherwise this will be ignored).
	 */
	private void processWithTemplate(final Template template, final Map templateContext, final String outlet, final String metafacadeName,
			final String metafacadePackage) {
		final String methodName = "Cartridge.processWithTemplate";
		ExceptionUtils.checkNull(methodName, "template", template);
		ExceptionUtils.checkNull(methodName, "templateContext", templateContext);
		ExceptionUtils.checkNull(methodName, "outlet", outlet);

		long milisIni = System.currentTimeMillis();

		File outputFile = null;
		try {
			String packageName = null;

			String[] keys = { "jsp", "action", "operation", "controller", "table", "pacote", "page", "service", "entity", "enumeration",
					"sessionObject", "package", "class" }; //"applicationUseCases", 

			for (int i = 0; packageName == null && i < keys.length; i++) {
				packageName = getPackageName(templateContext, keys[i]);
			}

			Boolean isAutorizado = Boolean.TRUE;
			if (decisionMap.containsKey(packageName))
				isAutorizado = (Boolean) decisionMap.get(packageName);
			else {
				isAutorizado = isAutorizado(packageName, outlet);
				decisionMap.put(packageName, isAutorizado);
			}

			if (Boolean.TRUE.equals(isAutorizado)) {
				// - populate the template context will cartridge descriptor
				//   properties and template objects
				this.populateTemplateContext(templateContext);

				final StringWriter output = new StringWriter();

				// process the template with the set TemplateEngine
				this.getTemplateEngine().processTemplate(template.getPath(), templateContext, output);

				outputFile = template.getOutputLocation(metafacadeName, metafacadePackage, new File(outlet), this.getTemplateEngine()
						.getEvaluatedExpression(template.getOutputPattern(), templateContext));

				if (outputFile != null) {
					// - only write files that do NOT exist, and
					//   those that have overwrite set to 'true'
					if (!outputFile.exists() || template.isOverwrite()) {
						final String outputString = output.toString();
						AndroMDALogger.setSuffix(this.getNamespace());

						// - check to see if generateEmptyFiles is true and if
						//   outString is not blank
						if ((outputString != null && outputString.trim().length() > 0) || template.isGenerateEmptyFiles()) {
							ResourceWriter.instance().writeStringToFile(outputString, outputFile, this.getNamespace());
							AndroMDALogger.debug("Output: '" + outputFile.toURI() + "'");
						} else {
							if (this.getLogger().isDebugEnabled()) {
								this.getLogger().debug("Empty Output: '" + outputFile.toURI() + "' --> not writing");
							}
						}
						AndroMDALogger.reset();

						long milisFim = System.currentTimeMillis();
						tempoGerandoArquivos += (milisFim - milisIni);

						//AndroMDALogger.info("\n Arquivo gerado: '" + outputFile.getAbsolutePath() + "' - Tempo:"+(milisFim-milisIni));
					} else {
						long milisFim = System.currentTimeMillis();
						tempoGerandoArquivosExistentes += (milisFim - milisIni);
						//AndroMDALogger.info("\n Arquivo já existente: '" + outputFile.getAbsolutePath() + "' - Tempo:"+(milisFim-milisIni));
					}
				}
			}
		} catch (final Throwable throwable) {
			if (outputFile != null) {
				outputFile.delete();
				this.getLogger().info("Removed: '" + outputFile + "'");
			}
			final String message = "Error processing template '" + template.getPath() + "' with template context '" + templateContext
					+ "' using cartridge '" + this.getNamespace() + "'";
			throw new CartridgeException(message, throwable);
		}
	}

	public void setUnicWeb(boolean unicWeb)
	{
		this.unicWeb = unicWeb;
	}

	/**
	 * Override to provide cartridge specific shutdown (
	 * 
	 * @see org.andromda.core.common.Plugin#shutdown()
	 */
	public void shutdown()
	{
		super.shutdown();
	}

	/**
	 * Writes the contents of <code>resourceUrl</code> to the outlet specified
	 * by <code>resource</code>.
	 * 
	 * @param resource
	 *            contains the outlet where the resource is written.
	 * @param resourceUrl
	 *            the URL contents to write.
	 */
	private void writeResource(final Resource resource, final URL resourceUrl) {
		File outFile = null;
		try {
			String outlet = Namespaces.instance().getPropertyValue(this.getNamespace(), resource.getOutlet());
			if (outlet != null) {
				// - make sure we don't have any back slashes
				final String resourceUri = resourceUrl.toString().replaceAll("\\\\", FORWARD_SLASH);
				final String uriSuffix = resourceUri.substring(resourceUri.lastIndexOf(FORWARD_SLASH), resourceUri.length());
				if (outlet.endsWith(FORWARD_SLASH)) {
					// - remove the extra slash
					outlet = outlet.replaceFirst(FORWARD_SLASH, "");
				}

				final Map templateContext = new HashMap();
				this.populateTemplateContext(templateContext);
				outFile = resource.getOutputLocation(new String[] { uriSuffix }, new File(outlet), this.getTemplateEngine()
						.getEvaluatedExpression(resource.getOutputPattern(), templateContext));

				// - only write files that do NOT exist, and
				//   those that have overwrite set to 'true'
				if (!outFile.exists() || resource.isOverwrite()) {
					ResourceWriter.instance().writeUrlToFile(resourceUrl, outFile.toString());
					AndroMDALogger.debug("Output: '" + outFile.toURI() + "'");
				}
			}
		} catch (final Throwable throwable) {
			if (outFile != null) {
				outFile.delete();
				this.getLogger().info("Removed: '" + outFile + "'");
			}
			throw new CartridgeException(throwable);
		}
	}
}
