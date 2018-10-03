package org.no.sw.core.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.no.sw.core.model.Source;
import org.springframework.stereotype.Component;

@Component
public class ContentServiceDirectory implements ContentService {

    private final Analyzer analyzer = new StandardAnalyzer();

    private final QueryParser qp = new QueryParser("", analyzer);

    private final Directory directory;

    public ContentServiceDirectory(Directory directory) {
        this.directory = directory;
    }

    @Override
    public Source getById(String id) {
        for (Document document : read(new TermQuery(new Term("id", id)), 1)) {
            return toSource(document);
        }
        return null;
    }

    @Override
    public Collection<Source> getAll(int limit) {
        List<Source> sources = new ArrayList<>();
        for (Document document : read(createQuery("*:*"), limit)) {
            sources.add(toSource(document));
        }
        return sources;
    }

    @Override
    public Collection<Source> find(String field, String value, int limit) {
        List<Source> sources = new ArrayList<>();
        for (Document document : read(new TermQuery(new Term(field, value)), limit)) {
            sources.add(toSource(document));
        }
        return sources;
    }

    private List<Document> read(Query q, int limit) {
        try (IndexReader reader = DirectoryReader.open(directory)) {
            final IndexSearcher searcher = new IndexSearcher(reader);
            TopDocs result = searcher.search(q, limit);
            if (result.totalHits == 0) {
                return null;
            }
            List<Document> documents = new ArrayList<>();
            for (ScoreDoc sd : result.scoreDocs) {
                documents.add(reader.document(sd.doc));
            }
            return documents;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void save(Source... sources) {
        save(Arrays.asList(sources));
    }

    @Override
    public void save(Collection<Source> sources) {
        saveDocuments(createSaveTasks(sources));
    }

    private List<Consumer<IndexWriter>> createSaveTasks(Collection<Source> sources) {
        List<Consumer<IndexWriter>> updates = new ArrayList<>();
        sources.forEach(s -> updates.add(createSaveTask(s)));
        return updates;
    }

    private Consumer<IndexWriter> createSaveTask(Source source) {
        Term term = new Term("id", source.getId());
        return w -> w.updateDocument(term, toDocument(source));
    }

    private void saveDocuments(List<Consumer<IndexWriter>> updaters) {
        try (IndexWriter writer = new IndexWriter(directory, createConfig())) {
            for (Consumer<IndexWriter> updater : updaters) {
                updater.accept(writer);
            }
            writer.commit();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private IndexWriterConfig createConfig() {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return indexWriterConfig;
    }

    private Source toSource(Document document) {
        SortedMap<String, String> properties = new TreeMap<>();
        document.forEach(f -> properties.put(f.name(), f.stringValue()));
        return Source.of(properties);
    }

    private Document toDocument(Source source) {
        Document document = new Document();
        source.getProperties().forEach((fieldName, value)
                -> document.add(new StringField(fieldName, value, Field.Store.YES)));
        return document;
    }

    private Query createQuery(String query) {
        try {
            return qp.parse(query);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private interface Consumer<E> {
        void accept(E e) throws IOException;
    }
}
