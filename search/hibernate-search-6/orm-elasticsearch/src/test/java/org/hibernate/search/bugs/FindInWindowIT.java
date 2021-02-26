package org.hibernate.search.bugs;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.engine.search.predicate.SearchPredicate;
import org.hibernate.search.engine.search.predicate.dsl.SearchPredicateFactory;
import org.hibernate.search.engine.search.query.SearchQuery;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.scope.SearchScope;
import org.hibernate.search.mapper.orm.session.SearchSession;

import org.junit.Test;

public class FindInWindowIT extends SearchTestBase {
	private static final Calendar ONE_YEAR_AGO = Calendar.getInstance();
    private static final Calendar ONE_MONTH_AGO = Calendar.getInstance();
    private static final Calendar ONE_YEAR_FROM_NOW = Calendar.getInstance();

    static {
        ONE_YEAR_AGO.add(Calendar.YEAR, -1);
        ONE_MONTH_AGO.add(Calendar.MONTH, -1);
        ONE_YEAR_FROM_NOW.add(Calendar.YEAR, 1);
    }

	@Override
	public Class<?>[] getAnnotatedClasses() {
		return new Class<?>[] {
			Asset.class,
			DataPackage.class,
			SearchResult.class,
			Series.class
		};
	}

	@Test
	public void testFindInWindow() {
		try (Session session = getSessionFactory().openSession()) {
			Asset seriesOneEpisodeOne = createAsset(1L, "Episode One", createDataPackage(1L, true));
			Asset seriesOneEpisodeTwo = createAsset(2L, "Episode Two", createDataPackage(2L, true));
			Asset seriesOneEpisodeThree = createAsset(3L, "Episode Three", createDataPackage(3L, false));
			Series seriesOne = createSeries(1L, "Series One", seriesOneEpisodeOne, seriesOneEpisodeTwo, seriesOneEpisodeThree);
			Asset seriesTwoEpisodeOne = createAsset(4L, "Episode One", createDataPackage(4L, false));
			Asset seriesTwoEpisodeTwo = createAsset(5L, "Episode Two", createDataPackage(5L, false));
			Series seriesTwo = createSeries(2L, "Series Two", seriesTwoEpisodeOne, seriesTwoEpisodeTwo);
			// Asset A: In window
			Asset movieA = createAsset(6L, "Movie A", createDataPackage(6L, true));
			// Asset B: Out of window
			Asset movieB = createAsset(7L, "Movie B", createDataPackage(7L, false));
			// Asset C: In window
			Asset movieC = createAsset(8L, "Movie C", createDataPackage(8L, true));
	
			Transaction transaction = session.beginTransaction();
			session.persist(seriesOne);
			session.persist(seriesTwo);
			session.persist(movieA);
			session.persist(movieB);
			session.persist(movieC);
			transaction.commit();
		}

		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {}

		try (Session session = getSessionFactory().openSession()) {
			SearchSession searchSession = Search.session(session);
			SearchScope<SearchResult> scope = searchSession.scope(Arrays.asList(Series.class, Asset.class));
			SearchPredicateFactory assetPredicateFactory = scope.predicate();
			SearchPredicate nestedPredicate = assetPredicateFactory.nested().objectField("dataPackages").nest(
				buildPredicate(assetPredicateFactory, "")
			).toPredicate();
			SearchPredicate assetPredicate = assetPredicateFactory.bool().filter(nestedPredicate).mustNot(
				assetPredicateFactory.exists().field("series")
			).toPredicate();
			SearchPredicateFactory seriesPredicateFactory = scope.predicate();
			SearchPredicate seriesPredicate = seriesPredicateFactory.nested().objectField("episodes.dataPackages").nest(
				buildPredicate(seriesPredicateFactory, "episodes.")
			).toPredicate();
			SearchQuery<SearchResult> query = searchSession.search(scope).where(
				scope.predicate().bool().should(assetPredicate).should(seriesPredicate).toPredicate()
			).toQuery();
			System.out.println(query);
			List<SearchResult> results = query.fetchAllHits();

			assertThat(results.size()).isEqualTo(3);
			assertThat(results.get(0)).isInstanceOf(Series.class);
			assertThat(((Series) results.get(0)).getName()).isEqualTo("Series One");
			assertThat(results.get(1)).isInstanceOf(Asset.class);
			assertThat(((Asset) results.get(1)).getTitle()).isEqualTo("Movie A");
			assertThat(results.get(2)).isInstanceOf(Asset.class);
			assertThat(((Asset) results.get(2)).getTitle()).isEqualTo("Movie C");
		}
	}

    private static SearchPredicate buildPredicate(SearchPredicateFactory factory, String prefix) {
        return factory.bool().filter(
            factory.range().field(prefix + "dataPackages.startDate").lessThan(Calendar.getInstance())
        ).filter(
            factory.range().field(prefix + "dataPackages.endDate").greaterThan(Calendar.getInstance())
        ).toPredicate();
    }

    private static DataPackage createDataPackage(Long id, boolean inWindow) {
        DataPackage dataPackage = new DataPackage();
		dataPackage.setId(id);
        if (inWindow) {
            dataPackage.setStartDate(ONE_MONTH_AGO);
            dataPackage.setEndDate(ONE_YEAR_FROM_NOW);
        } else {
            dataPackage.setStartDate(ONE_YEAR_AGO);
            dataPackage.setEndDate(ONE_MONTH_AGO);
        }
        return dataPackage;
    }

    private static Asset createAsset(Long id, String title, DataPackage... dataPackages) {
        Asset asset = new Asset();
		asset.setId(id);
		asset.setTitle(title);
		asset.setDataPackages(new LinkedHashSet<>(Arrays.asList(dataPackages)));
        for (DataPackage dataPackage : dataPackages) {
            dataPackage.setAsset(asset);
        }
        return asset;
    }

    private static Series createSeries(Long id, String name, Asset... episodes) {
        Series series = new Series();
		series.setId(id);
		series.setName(name);
		series.setEpisodes(new LinkedHashSet<>(Arrays.asList(episodes)));
        for (Asset episode : episodes) {
			episode.setSeries(series);
		}
        return series;
    }
}
