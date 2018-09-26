package org.no.sw.core.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.no.sw.core.model.SWBase;
import org.springframework.stereotype.Component;

@Component
public class ContentServiceDirectory implements ContentService {

    private final Analyzer analyzer = new StandardAnalyzer();

    private final Directory directory;

    public ContentServiceDirectory() {
        this.directory = new RAMDirectory();
    }

    @Override
    public SWBase get(String id) {
        try (IndexReader reader = DirectoryReader.open(directory)) {
            final IndexSearcher searcher = new IndexSearcher(reader);
            Term term = new Term("id", id);
            TopDocs result = searcher.search(new TermQuery(term), 1);
            if (result.totalHits == 0) {
                return null;
            }
            return map(reader.document(result.scoreDocs[0].doc));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public List<SWBase> getAll() {
        try (IndexReader reader = DirectoryReader.open(directory)) {
            List<SWBase> result = new ArrayList<>();
            for (int i = 0; i < reader.numDocs(); i++) {
                Document document = reader.document(i);
                result.add(map(document));
            }
            return result;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private SWBase map(Document document) {
        SWBase t = new SWBase(document.get("id"));
        document.forEach(f -> {
            if (f.name().equals("id")) {
                return;
            }
            t.getProperties().put(f.name(), f.stringValue());
        });
        return t;
    }

    @Override
    public SWBase create(String id) {
        IndexWriterConfig indexWriterConfig = createConfig();

        Document document;
        try (IndexWriter writer = new IndexWriter(directory, indexWriterConfig)) {
            document = new Document();
            document.add(new StringField("id", id, Store.YES));
            writer.addDocument(document);
            writer.commit();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return map(document);
    }

    @Override
    public SWBase update(SWBase t) {
        Term term = new Term("id", t.getId());

        Document document;
        try (IndexReader reader = DirectoryReader.open(directory)) {
            TopDocs result = new IndexSearcher(reader).search(new TermQuery(term), 1);
            if (result.totalHits == 0) {
                return null;
            }
            document = reader.document(result.scoreDocs[0].doc);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        Map<String, IndexableField> fields = new HashMap<>();
        document.forEach(f -> fields.put(f.name(), f));

        Set<String> fieldNames = new HashSet<>();
        fieldNames.addAll(fields.keySet());
        fieldNames.addAll(t.getProperties().keySet());

        for (String fieldName : fieldNames) {
            if (fieldName.equals("id")) {
                continue;
            }
            IndexableField field = fields.get(fieldName);
            boolean d = false;
            boolean a = false;
            if (field == null) {
                a = true;
            } else {
                if (t.getProperties().get(fieldName) == null) {
                    d = true;
                } else {
                    if (!t.getProperties().get(fieldName).equals(field.stringValue())) {
                        d = true;
                        a = true;
                    }
                }
            }
            if (d) {
                document.removeField(fieldName);
            }
            if (a) {
                document.add(new StringField(fieldName, t.getProperties().get(fieldName), Field.Store.YES));
            }
        }

        try (IndexWriter writer = new IndexWriter(directory, createConfig())) {
            writer.updateDocument(term, document);
            writer.commit();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return map(document);
    }

    private IndexWriterConfig createConfig() {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        return indexWriterConfig;
    }
}
