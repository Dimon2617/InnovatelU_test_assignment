import com.InnovatelU.DocumentManager;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


class DocumentManagerTest {

    public static void main(String[] args) {
        testSaveDocument();
        testSearchDocuments();
        testFindById();
        testFindById_NotFound();
        testSaveExistingDocument();
        testSaveNullDocument();
        System.out.println("All tests passed!");
    }

    static void testSaveDocument() {
        DocumentManager documentManager = new DocumentManager();

        DocumentManager.Document document = DocumentManager.Document.builder()
                .title("Test Document")
                .content("This is a test document.")
                .author(DocumentManager.Author.builder().id("author1").name("John Doe").build())
                .created(Instant.now())
                .build();

        DocumentManager.Document savedDocument = documentManager.save(document);

        assert document.getId() != null : "Document ID should not be null";
        assert document.getTitle().equals("Test Document") : "Document title should match";
    }

    static void testSearchDocuments() {
        DocumentManager documentManager = new DocumentManager();

        DocumentManager.Document doc1 = DocumentManager.Document.builder()
                .title("Test Document 1")
                .content("This is the first test document.")
                .author(DocumentManager.Author.builder().id("author1").name("John Doe").build())
                .created(Instant.now())
                .build();
        documentManager.save(doc1);

        DocumentManager.Document doc2 = DocumentManager.Document.builder()
                .title("Sample Document 2")
                .content("This is the second test document.")
                .author(DocumentManager.Author.builder().id("author2").name("Jane Smith").build())
                .created(Instant.now())
                .build();
        documentManager.save(doc2);

        DocumentManager.SearchRequest request = DocumentManager.SearchRequest.builder()
                .titlePrefixes(Arrays.asList("Test"))
                .build();

        List<DocumentManager.Document> results = documentManager.search(request);

        assert results.size() == 1 : "Search should return one document";
        assert results.get(0).getTitle().equals("Test Document 1") : "Returned document title should match";
    }

    static void testFindById() {
        DocumentManager documentManager = new DocumentManager();

        DocumentManager.Document document = DocumentManager.Document.builder()
                .title("Test Document")
                .content("This is a test document.")
                .author(DocumentManager.Author.builder().id("author1").name("John Doe").build())
                .created(Instant.now())
                .build();
        documentManager.save(document);

        Optional<DocumentManager.Document> foundDocument = documentManager.findById(document.getId());

        assert foundDocument.isPresent() : "Document should be found by ID";
        assert foundDocument.get().getId().equals(document.getId()) : "Found document ID should match";
    }

    static void testFindById_NotFound() {
        DocumentManager documentManager = new DocumentManager();

        Optional<DocumentManager.Document> foundDocument = documentManager.findById("non-existing-id");

        assert !foundDocument.isPresent() : "Document should not be found with a non-existing ID";
    }

    static void testSaveExistingDocument() {
        DocumentManager documentManager = new DocumentManager();

        DocumentManager.Document document = DocumentManager.Document.builder()
                .title("Test Document")
                .content("This is a test document.")
                .author(DocumentManager.Author.builder().id("author1").name("John Doe").build())
                .created(Instant.now())
                .build();
        documentManager.save(document);

        try {
            documentManager.save(document);
            assert false : "Expected a RuntimeException due to duplicate document ID";
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Document with id " + document.getId() + " already exists")
                    : "Unexpected exception message: " + e.getMessage();
        }
    }

    static void testSaveNullDocument() {
        DocumentManager documentManager = new DocumentManager();
        DocumentManager.Document document = null;
        try {
            documentManager.save(document);
        } catch (RuntimeException e) {
            assert e.getMessage().equals("Document cannot be null");
        }

    }

}
