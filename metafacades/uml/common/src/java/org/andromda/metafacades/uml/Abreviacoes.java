package org.andromda.metafacades.uml;

import java.util.Properties;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sun.org.apache.bcel.internal.util.ClassLoader;

/**
 * Classe responsável por centralizar a abreviação de palavras.
 * 
 * @author luizvalmont
 */
public class Abreviacoes {
	
	/**
	 * O objeto de log desta classe.
	 */
	private static final Log log = LogFactory.getLog( Abreviacoes.class );
	
	/**
	 * A única instância desta classe.
	 */
	private static final Abreviacoes instance = new Abreviacoes();
	
	/**
	 * O conjunto de propriedades que mapeia uma palavra para sua abreviação.
	 */
	private final Properties properties;
	
	/**
	 * Indica se as abreviações já foram carregadas, evitando que
	 * elas sejam recarregadas sem necessidade.
	 */
	private boolean propriedadesJaForamCarregadas;
	
	private Abreviacoes()
	{
		properties = new Properties();
		propriedadesJaForamCarregadas = false;
	}
	
	/**
	 * Retorna a instância única desta classe.
	 * 
	 * @return a instância única desta classe.
	 */
	public static Abreviacoes getInstance()
	{
		return instance;
	}
	
	/**
	 * Define um novo caminho para o arquivo de propriedades com as abreviações.
	 * O arquivo de propriedades é carregado com sucesso apenas uma única vez.
	 * 
	 * @param caminhoDoArquivoDePropriedades o caminho para o arquivo de propriedades
	 *   que contém as abreviações.
	 * @throws IOException caso ocorra algum erro de E/S durante a carga do arquivo
	 *   de propriedades.
	 */
	public void setCaminhoDoArquivoDePropriedades( File caminhoDoArquivoDePropriedades )
	{
		try {
			if( !propriedadesJaForamCarregadas ) {
				properties.load( new BufferedInputStream( new FileInputStream( caminhoDoArquivoDePropriedades ) ) );
			}
		}
		catch( IOException ioe ) {
			log.info( "Não foi possível carregar o arquivo de abreviações. Nenhuma abreviação será feita!" );
		}
		finally {
			propriedadesJaForamCarregadas = true;
		}
	}
	
	/**
	 * Abrevia uma palavra.
	 * 
	 * @param palavra a palavra a ser abreviada.
	 * @return a abreviação ou a palavra original caso não exista
	 *   uma abreviação configurada.
	 */
	public String abrevie( String palavra ){
		String abreviacao = properties.getProperty( palavra );
		return ( abreviacao != null && !StringUtils.isEmpty( abreviacao ) ) ? abreviacao : palavra;
	}

}
