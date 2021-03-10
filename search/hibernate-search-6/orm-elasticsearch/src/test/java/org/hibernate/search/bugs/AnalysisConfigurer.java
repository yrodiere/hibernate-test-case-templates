package org.hibernate.search.bugs;

import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurationContext;
import org.hibernate.search.backend.elasticsearch.analysis.ElasticsearchAnalysisConfigurer;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;

public class AnalysisConfigurer implements ElasticsearchAnalysisConfigurer, LuceneAnalysisConfigurer {
	@Override
	public void configure(ElasticsearchAnalysisConfigurationContext context) {
		context.analyzer( "customAnalyzer" ).custom().tokenizer( "whitespace" ).tokenFilters( "lowercase" );
	}

	@Override
	public void configure(LuceneAnalysisConfigurationContext context) {
		context.analyzer( "customAnalyzer" ).custom().tokenizer( WhitespaceTokenizerFactory.class )
				.tokenFilter( LowerCaseFilterFactory.class );
	}
}
