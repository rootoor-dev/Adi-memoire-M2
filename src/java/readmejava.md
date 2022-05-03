# Idées de développement de l'outil en JAVA

Dans ce mémoire, les collections ou arrays seront beaucoup manipulés.
A défaut d'utiliser des librairies externes, il est conseillé de développer ses propres classes ou librairies.


```Java
/******************************************************************************

Welcome to GDB Online.
GDB online is an online compiler and debugger tool for C, C++, Python, Java, PHP, Ruby, Perl,
C#, VB, Swift, Pascal, Fortran, Haskell, Objective-C, Assembly, HTML, CSS, JS, SQLite, Prolog.
Code, Compile, Run and Debug online from anywhere in world.

*******************************************************************************/
public class Main
{
		// Driver program
	public static void main(String arg[])
	{
	// Declaring number of rows and columns
		int n = 3, m = 3;
		int array[]=new int[100];
	
		// Initialising a 2-d array
		int grid[][] = {{1, 2, 3},
						{4, 5, 6},
						{7, 8, 9}};
	
		// storing elements in 1-d array
		int i, j, k = 0;
		for (i = 0; i < n; i++)
		{
			for (j = 0; j < m; j++)
			{
				k = i * m + j;
				array[k] = grid[i][j];
				k++;
			}
		}
	
		// displaying elements in 1-d array
		for (i = 0; i < n; i++)
		{
			for (j = 0; j < m; j++)
				System.out.print((array[i * m + j])+" ");
			System.out.print("\n");
		}
}    
```




Saturday, September 27, 2008
IR Math with Java : Similarity Measures
Last week, I wrote about building term document matrices based on Dr. Manu Konchady's Text Mining Application Programming book. This week, I continue working on computing some similarity metrics from the same set of data. Most of the material is covered in Chapter 8 of the book.

As before, our document collection consists of 7 document titles as shown below:

1
2
3
4
5
6
7
D1      Human machine interface for computer applications
D2      A survey of user opinion of computer system response time
D3      The EPS user interface management system
D4      System and human system engineering testing of EPS
D5      The generation of random, binary and ordered trees
D6      The intersection graph of paths in trees
D7      Graph minors: A survey
The document collection is first converted to a raw term frequency matrix. This is further normalized using either TF/IDF or LSI. The matrix thus normalized is the input to our similarity computations.

A document is represented by a column of data in our (normalized) term document matrix. To compute the similarity between two documents, we perform computations between two columns of the matrix. Conceptually, each document as a point in an n-dimensional term space, where each term corresponds to a dimension. So in our example, we have 23 terms, therefore our term space is a 23-dimensional space. In case this is hard to visualize (it was for me), it helps to think of documents with 2 terms (and hence a two dimensional term space), and extrapolate from there.

Jaccard Similarity
Jaccard Similarity is a simple and very intuitive measure of document to document similarity. It is defined as follows:

  similarity(A,B) = n(A ∩ B) / n(A ∪ B)
Using the Inclusion-Exclusion Principle, this reduces to:

  similarity(A,B) = n(A ∩ B) / n(A) + n(B) - n(A ∩ B)
  where:
    n(A ∩ B) = Σ min(Ai, Bi)
    n(A) = Σ Ai
    n(B) = Σ Bi
  for i = [0..n-1], where n = number of terms in our term-document matrix.
AbstractSimilarity.java

The AbstractSimilarity class shown below contains the code to compare all document pairs from our collection. This has a complexity of O(n2), which we can reduce to about half if we code in the knowledge that similarity(A,B) == similarity(B,A) and similarity(A,A) == 1.0, but which is still too high for large number of documents. I haven't added these knowledge here, though, since this is just toy data and the performance is tolerable.

 1
 2
 3
 4
 5
 6
 7
 8
 9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
// Source: src/main/java/com/mycompany/myapp/similarity/AbstractSimilarity.java
package com.mycompany.myapp.similarity;

import org.apache.commons.collections15.Transformer;

import Jama.Matrix;

public abstract class AbstractSimilarity 
    implements Transformer<Matrix, Matrix> {

  public Matrix transform(Matrix termDocumentMatrix) {
    int numDocs = termDocumentMatrix.getColumnDimension();
    Matrix similarityMatrix = new Matrix(numDocs, numDocs);
    for (int i = 0; i < numDocs; i++) {
      Matrix sourceDocMatrix = termDocumentMatrix.getMatrix(
        0, termDocumentMatrix.getRowDimension() - 1, i, i); 
      for (int j = 0; j < numDocs; j++) {
        Matrix targetDocMatrix = termDocumentMatrix.getMatrix(
          0, termDocumentMatrix.getRowDimension() - 1, j, j);
        similarityMatrix.set(i, j, 
          computeSimilarity(sourceDocMatrix, targetDocMatrix));
      }
    }
    return similarityMatrix;
  }

  protected abstract double computeSimilarity(
      Matrix sourceDoc, Matrix targetDoc);
}
JaccardSimilarity.java

For the JaccardSimilarity class, we define the abstract method computeSimilarity() as shown below (based on our formula above). I have cheated here slightly - in Jama, norm1() is defined as the maximum column sum, but since our document matrices are column-matrices (ie 1 column), norm1() returns the same value as the sum of all elements in the document matrix. It does make the code less readable, in the sense that the reader/maintainer would have to go through an extra mental hoop, but it makes the code concise.

 1
 2
 3
 4
 5
 6
 7
 8
 9
10
11
12
13
14
15
16
17
18
19
20
21
// Source: src/main/java/com/mycompany/myapp/similarity/JaccardSimilarity.java
package com.mycompany.myapp.similarity;

import Jama.Matrix;

public class JaccardSimilarity extends AbstractSimilarity {

  @Override
  protected double computeSimilarity(Matrix source, Matrix target) {
    double intersection = 0.0D;
    for (int i = 0; i < source.getRowDimension();i++) {
      intersection += Math.min(source.get(i, 0), target.get(i, 0));
    }
    if (intersection > 0.0D) {
      double union = source.norm1() + target.norm1() - intersection;
      return intersection / union;
    } else {
      return 0.0D;
    }
  }
}
The result of our Jaccard similarity computation is shown below. The JUnit test to return this is SimilarityTest.java which is shown later in this post. The two matrices are derived from our raw term document matrix normalized using TF/IDF and LSI respectively. As you can see, the similarity matrix created off the LSI normalized vector shows more inter-document similarity than the one created off the TF/IDF normalized vector.

 1
 2
 3
 4
 5
 6
 7
 8
 9
10
11
12
13
14
15
16
17
18
19
=== Jaccard Similarity (TF/IDF) ===
            D1      D2      D3      D4      D5      D6      D7
    D1  1.0000  0.0000  0.0000  0.0818  0.0000  0.0000  0.0000
    D2  0.0000  1.0000  0.0000  0.0000  0.0000  0.0000  0.0710
    D3  0.0000  0.0000  1.0000  0.2254  0.0000  0.0000  0.0000
    D4  0.0818  0.0000  0.2254  1.0000  0.0000  0.0000  0.0000
    D5  0.0000  0.0000  0.0000  0.0000  1.0000  0.0000  0.0000
    D6  0.0000  0.0000  0.0000  0.0000  0.0000  1.0000  0.1781
    D7  0.0000  0.0710  0.0000  0.0000  0.0000  0.1781  1.0000

=== Jaccard Similarity (LSI) ===
            D1      D2      D3      D4      D5      D6      D7
    D1  1.0000  0.0000  1.0000  1.0000  0.0000  0.0000  0.0000
    D2  0.0000  1.0000  0.0000  0.0000  1.0000  1.0000  1.0000
    D3  1.0000  0.0000  1.0000  1.0000  0.0000  0.0000  0.0000
    D4  1.0000  0.0000  1.0000  1.0000  0.0000  0.0000  0.0000
    D5  0.0000  1.0000  0.0000  0.0000  1.0000  1.0000  1.0000
    D6  0.0000  1.0000  0.0000  0.0000  1.0000  1.0000  1.0000
    D7  0.0000  1.0000  0.0000  0.0000  1.0000  1.0000  1.0000
Cosine Similarity
Cosine Similarity is the more popular but also a slightly more complex measure of similarity. It is defined as:

  similarity(A,B) = cos θ = (A ⋅ B) / (|A| * |B|)
  where:
    A ⋅ B = Σ Ai * Bi
    |A| = sqrt(Σ Ai2)
    |B| = sqrt(Σ Bi2)
  for i = [0..n-1], where n = number of terms in our term-document matrix.
Here, θ is the angle between the gradients of the documents A and B in the n-dimensional term space. A and B are very similar as θ approaches 0° (cos(0) = 1), and very dissimilar as θ approaches 90° (cos(90) = 0).

Dr. E. Garcia has a very informative tutorial on Cosine Similarity that explains the formula above in much more detail (and with more humor).

CosineSimilarity.java

The code below computes the cosine similarity based on the formula above. Here, normF() is the Frobenius norm, the square root of the sum of all elements, and norm1() is the maximum column sum, which works for me because we are computing it off a one-dimensional matrix.

 1
 2
 3
 4
 5
 6
 7
 8
 9
10
11
12
13
14
15
// Source: src/main/java/src/com/mycompany/myapp/similarity/CosineSimilarity.jav
a
package com.healthline.jrocker.similarity;

import Jama.Matrix;

public class CosineSimilarity extends AbstractSimilarity {

  @Override
  protected double computeSimilarity(Matrix sourceDoc, Matrix targetDoc) {
    double dotProduct = sourceDoc.arrayTimes(targetDoc).norm1();
    double eucledianDist = sourceDoc.normF() * targetDoc.normF();
    return dotProduct / eucledianDist;
  }
}
The results of this computation for a term document vector normalized with TF/IDF and LSI respectively are shown below. As before, the inter-document similarity is higher for the LSI normalized vector.

 1
 2
 3
 4
 5
 6
 7
 8
 9
10
11
12
13
14
15
16
17
18
19
=== Cosine Similarity (TF/IDF) ===
            D1      D2      D3      D4      D5      D6      D7
    D1  1.0000  0.0000  0.0000  0.1316  0.0000  0.0000  0.0000
    D2  0.0000  1.0000  0.0000  0.0000  0.0000  0.0000  0.1680
    D3  0.0000  0.0000  1.0000  0.4198  0.0000  0.0000  0.0000
    D4  0.1316  0.0000  0.4198  1.0000  0.0000  0.0000  0.0000
    D5  0.0000  0.0000  0.0000  0.0000  1.0000  0.0000  0.0000
    D6  0.0000  0.0000  0.0000  0.0000  0.0000  1.0000  0.3154
    D7  0.0000  0.1680  0.0000  0.0000  0.0000  0.3154  1.0000

=== Cosine Similarity (LSI) ===
            D1      D2      D3      D4      D5      D6      D7
    D1  1.0000  0.0000  1.0000  1.0000  0.0000  0.0000  0.0000
    D2  0.0000  1.0000  0.0000  0.0000  1.0000  1.0000  1.0000
    D3  1.0000  0.0000  1.0000  1.0000  0.0000  0.0000  0.0000
    D4  1.0000  0.0000  1.0000  1.0000  0.0000  0.0000  0.0000
    D5  0.0000  1.0000  0.0000  0.0000  1.0000  1.0000  1.0000
    D6  0.0000  1.0000  0.0000  0.0000  1.0000  1.0000  1.0000
    D7  0.0000  1.0000  0.0000  0.0000  1.0000  1.0000  1.0000
Searching - Similarity against a Query Vector
Using the similarity vectors above, it is now possible to build a Searcher. The objective is to find the most similar documents that match our query. For this, we will need to build a query vector consisting of the terms in the query, and then compute the similarity of the documents against the query vector. The computation is similar to what we have already done above if we treat the query vector as a specialized instance of a document vector. We use Cosine Similarity for our computations here.

Searcher.java

 1
 2
 3
 4
 5
 6
 7
 8
 9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
75
76
77
78
79
80
81
82
83
84
85
86
87
88
89
90
91
92
93
94
95
96
97
// Source: src/main/java/com/mycompany/myapp/similarity/Searcher.java
package com.healthline.jrocker.similarity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Jama.Matrix;

public class Searcher {

  public class SearchResult {
    public String title;
    public double score;
    public SearchResult(String title, double score) {
      this.title = title;
      this.score = score;
    }
  };
  
  private Matrix termDocumentMatrix;
  private List<String> documents;
  private List<String> terms;
  private AbstractSimilarity similarity;
  
  public void setTermDocumentMatrix(Matrix termDocumentMatrix) {
    this.termDocumentMatrix = termDocumentMatrix;
  }
  
  public void setDocuments(String[] documents) {
    this.documents = Arrays.asList(documents);
  }
  
  public void setTerms(String[] terms) {
    this.terms = Arrays.asList(terms);
  }
  
  public void setSimilarity(AbstractSimilarity similarity) {
    this.similarity = similarity;
  }
  
  public List<SearchResult> search(String query) {
    // build up query matrix
    Matrix queryMatrix = getQueryMatrix(query);
    final Map<String,Double> similarityMap = 
      new HashMap<String,Double>();
    for (int i = 0; i < termDocumentMatrix.getColumnDimension(); i++) {
      double sim = similarity.computeSimilarity(queryMatrix, 
        termDocumentMatrix.getMatrix(
          0, termDocumentMatrix.getRowDimension() - 1, i, i));
      if (sim > 0.0D) {
        similarityMap.put(documents.get(i), sim);
      }
    }
    return sortByScore(similarityMap);
  }
  
  private Matrix getQueryMatrix(String query) {
    Matrix queryMatrix = new Matrix(terms.size(), 1, 0.0D);
    String[] queryTerms = query.split("\\s+");
    for (String queryTerm : queryTerms) {
      int termIdx = 0;
      for (String term : terms) {
        if (queryTerm.equalsIgnoreCase(term)) {
          queryMatrix.set(termIdx, 0, 1.0D);
        }
        termIdx++;
      }
    }
    queryMatrix = queryMatrix.times(1 / queryMatrix.norm1());
    return queryMatrix;
  }
  
  private List<SearchResult> sortByScore(
      final Map<String,Double> similarityMap) {
    List<SearchResult> results = new ArrayList<SearchResult>();
    List<String> docNames = new ArrayList<String>();
    docNames.addAll(similarityMap.keySet());
    Collections.sort(docNames, new Comparator<String>() {
      public int compare(String s1, String s2) {
        return similarityMap.get(s2).compareTo(similarityMap.get(s1));
      }
    });
    for (String docName : docNames) {
      double score = similarityMap.get(docName);
      if (score < 0.00001D) {
        continue;
      }
      results.add(new SearchResult(docName, score));
    }
    return results;
  }
}
The results of searching with the query "human computer interface" against a similarity matrix built off a TF/IDF and an LSI normalization are shown below. Notice that we get a few more results from a LSI normalized vector. I have manually copied the titles over to make the result more readable. I suppose I could make the code do it, and I probably would have, if the code was going to be used by anybody other than myself.

1
2
3
4
5
6
7
8
Results for query: [human computer interface] using TF/IDF
D1 (score =   0.8431) Human machine interface for computer applications
D4 (score =   0.1881) System and human system engineering testing of EPS

Results for query: [human computer interface] using LSI
D4 (score =   0.2467) System and human system engineering testing of EPS
D3 (score =   0.2467) The EPS user interface management system
D1 (score =   0.2467) Human machine interface for computer applications
Calling the code - the JUnit test
For completeness, as well as to show you how the components above should be called, I show here the JUnit test that was used to run these components and derive results for this post.

  1
  2
  3
  4
  5
  6
  7
  8
  9
 10
 11
 12
 13
 14
 15
 16
 17
 18
 19
 20
 21
 22
 23
 24
 25
 26
 27
 28
 29
 30
 31
 32
 33
 34
 35
 36
 37
 38
 39
 40
 41
 42
 43
 44
 45
 46
 47
 48
 49
 50
 51
 52
 53
 54
 55
 56
 57
 58
 59
 60
 61
 62
 63
 64
 65
 66
 67
 68
 69
 70
 71
 72
 73
 74
 75
 76
 77
 78
 79
 80
 81
 82
 83
 84
 85
 86
 87
 88
 89
 90
 91
 92
 93
 94
 95
 96
 97
 98
 99
100
101
102
103
104
105
106
107
108
109
110
111
112
113
114
115
116
117
118
119
120
121
122
123
124
125
126
127
128
129
130
131
132
133
134
135
136
137
138
139
140
141
142
143
144
145
146
147
148
149
150
151
152
// Source: src/test/java/com/mycompany/myapp/similarity/SimilarityTest.java
package com.mycompany.myapp.similarity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import Jama.Matrix;

import com.mycompany.myapp.indexers.IdfIndexer;
import com.mycompany.myapp.indexers.LsiIndexer;
import com.mycompany.myapp.indexers.VectorGenerator;
import com.mycompany.myapp.similarity.Searcher.SearchResult;

public class SimilarityTest {

  private VectorGenerator vectorGenerator;
  
  @Before
  public void setUp() throws Exception {
    vectorGenerator = new VectorGenerator();
    vectorGenerator.setDataSource(new DriverManagerDataSource(
      "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/tmdb", 
      "tmdb", "irstuff"));
    Map<String,Reader> documents = 
      new LinkedHashMap<String,Reader>();
    BufferedReader reader = new BufferedReader(
      new FileReader("src/test/resources/data/indexing_sample_data.txt"));
    String line = null;
    while ((line = reader.readLine()) != null) {
      String[] docTitleParts = StringUtils.split(line, ";");
      documents.put(docTitleParts[0], new StringReader(docTitleParts[1]));
    }
    vectorGenerator.generateVector(documents);
  }

  @Test
  public void testJaccardSimilarityWithTfIdfVector() throws Exception {
    IdfIndexer indexer = new IdfIndexer();
    Matrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());
    JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
    Matrix similarity = jaccardSimilarity.transform(termDocMatrix);
    prettyPrintMatrix("Jaccard Similarity (TF/IDF)", similarity, 
      vectorGenerator.getDocumentNames(), new PrintWriter(System.out, true));
  }
  
  @Test
  public void testJaccardSimilarityWithLsiVector() throws Exception {
    LsiIndexer indexer = new LsiIndexer();
    Matrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());
    JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
    Matrix similarity = jaccardSimilarity.transform(termDocMatrix);
    prettyPrintMatrix("Jaccard Similarity (LSI)", similarity, 
      vectorGenerator.getDocumentNames(), new PrintWriter(System.out, true));
  }

  @Test
  public void testCosineSimilarityWithTfIdfVector() throws Exception {
    IdfIndexer indexer = new IdfIndexer();
    Matrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());
    CosineSimilarity cosineSimilarity = new CosineSimilarity();
    Matrix similarity = cosineSimilarity.transform(termDocMatrix);
    prettyPrintMatrix("Cosine Similarity (TF/IDF)", similarity, 
      vectorGenerator.getDocumentNames(), new PrintWriter(System.out, true));
  }
  
  @Test
  public void testCosineSimilarityWithLsiVector() throws Exception {
    LsiIndexer indexer = new LsiIndexer();
    Matrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());
    CosineSimilarity cosineSimilarity = new CosineSimilarity();
    Matrix similarity = cosineSimilarity.transform(termDocMatrix);
    prettyPrintMatrix("Cosine Similarity (LSI)", similarity, 
      vectorGenerator.getDocumentNames(), new PrintWriter(System.out, true));
  }
  
  @Test
  public void testSearchWithTfIdfVector() throws Exception {
    // generate the term document matrix via the appropriate indexer
    IdfIndexer indexer = new IdfIndexer();
    Matrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());
    // set up the query
    Searcher searcher = new Searcher();
    searcher.setDocuments(vectorGenerator.getDocumentNames());
    searcher.setTerms(vectorGenerator.getWords());
    searcher.setSimilarity(new CosineSimilarity());
    searcher.setTermDocumentMatrix(termDocMatrix);
    // run the query
    List<SearchResult> results = 
      searcher.search("human computer interface");
    prettyPrintResults("human computer interface", results);
  }

  @Test
  public void testSearchWithLsiVector() throws Exception {
    // generate the term document matrix via the appropriate indexer
    LsiIndexer indexer = new LsiIndexer();
    Matrix termDocMatrix = indexer.transform(vectorGenerator.getMatrix());
    // set up the query
    Searcher searcher = new Searcher();
    searcher.setDocuments(vectorGenerator.getDocumentNames());
    searcher.setTerms(vectorGenerator.getWords());
    searcher.setSimilarity(new CosineSimilarity());
    searcher.setTermDocumentMatrix(termDocMatrix);
    // run the query
    List<SearchResult> results = 
      searcher.search("human computer interface");
    prettyPrintResults("human computer interface", results);
  }

  private void prettyPrintMatrix(String legend, Matrix matrix, 
      String[] documentNames, PrintWriter writer) {
    writer.printf("=== %s ===%n", legend);
    writer.printf("%6s", " ");
    for (int i = 0; i < documentNames.length; i++) {
      writer.printf("%8s", documentNames[i]);
    }
    writer.println();
    for (int i = 0; i < documentNames.length; i++) {
      writer.printf("%6s", documentNames[i]);
      for (int j = 0; j < documentNames.length; j++) {
        writer.printf("%8.4f", matrix.get(i, j));
      }
      writer.println();
    }
    writer.flush();
  }
  
  private void prettyPrintResults(String query, 
      List<SearchResult> results) {
    System.out.printf("Results for query: [%s]%n", query);
    for (SearchResult result : results) {
      System.out.printf("%s (score = %8.4f)%n", result.title, result.score);
    }
  }
}
Conclusion
I hope this post was helpful. I had looked at Term Vectors in Lucene in the past, but the idea of an n-dimensional term space never quite sank in until I started working through the stuff I describe above. I know that the data I am using is toy data, and that the computations described in the blog are computationally too complex and impractical in real life, but I believe that knowing this stuff helps in building models that would be useful and practical.

Update 2009-04-26: In recent posts, I have been building on code written and described in previous posts, so there were (and rightly so) quite a few requests for the code. So I've created a project on Sourceforge to host the code. You will find the complete source code built so far in the project's SVN repository.

Posted by Sujit Pal at 9:56 AM


Labels: cosine-similarity, data-mining, information-retrieval, jaccard-similarity, java, math, similarity
