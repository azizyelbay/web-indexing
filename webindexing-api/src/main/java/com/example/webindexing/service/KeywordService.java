package com.example.webindexing.service;

import com.example.webindexing.model.Keyword;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class KeywordService {

    public static final String[] stopWords = {"pp","p","–","xe2x80x98I","b'","b","The","\n\n","","a","the","ourselves", "hers", "between", "yourself", "but", "again", "there", "about", "once", "during", "out", "very", "having", "with", "they", "own", "an", "be", "some", "for", "do", "its", "yours", "such", "into", "of", "most", "itself", "other", "off", "is", "s", "am", "or", "who", "as", "from", "him", "each", "the", "themselves", "until", "below", "are", "we", "these", "your", "his", "through", "don", "nor", "me", "were", "her", "more", "himself", "this", "down", "should", "our", "their", "while", "above", "both", "up", "to", "ours", "had", "she", "all", "no", "when", "at", "any", "before", "them", "same", "and", "been", "have", "in", "will", "on", "does", "yourselves", "then", "that", "because", "what", "over", "why", "so", "can", "did", "not", "now", "under", "he", "you", "herself", "has", "just", "where", "too", "only", "myself", "which", "those", "i", "after", "few", "whom", "t", "being", "if", "theirs", "my", "against", "a", "by", "doing", "it", "how", "further", "was", "here", "than"};

    public List<Keyword> findKeywords(String url) {

        Document document = getDocumentFromUrl(url);

        // get title
        String title = document.title();

        // get text from document
        String text = document.body().text();

        // remove punctuations
        text = removePunctuations(text);

        // remove numbers
        text = text.replaceAll("\\d","");

        // remove stopWords
        ArrayList<String> allWords =
                Stream.of(text.toLowerCase().split(" "))
                        .collect(Collectors.toCollection(ArrayList<String>::new));
        allWords.removeAll(Arrays.asList(stopWords));
        String result = allWords.stream().collect(Collectors.joining(" "));


        // count frequency all words
        List<Keyword> list = countFreq(result, title);

        for (int i = 0; i < 50; i++) {
            System.out.println(list.get(i).getWordName() + "   " +  list.get(i).getCount());
        }

        return list;
    }

    static List<Keyword> countFreq(String text, String title) {
        List<Keyword> list = new ArrayList<Keyword>();
        Map<String,Integer> mp=new TreeMap<>();

        // Splitting to find the word
        String textArray[] = text.split(" ");
        String titleArray[] = title.split(" ");
        // Loop to iterate over the words
        for(int i=0;i<textArray.length;i++)
        {
            // Condition to check if the
            // array element is present
            // the hash-map
            if(mp.containsKey(textArray[i].toLowerCase()))
            {
                mp.put(textArray[i].toLowerCase(), mp.get(textArray[i].toLowerCase())+1);
            }
            else
            {
                mp.put(textArray[i].toLowerCase(),1);
            }
        }

        for(int i=0;i<titleArray.length;i++)
        {
            // Condition to check if the
            // array element is present
            // the hash-map
            if(mp.containsKey(titleArray[i].toLowerCase()))
            {
                mp.put(titleArray[i].toLowerCase(), mp.get(titleArray[i].toLowerCase())+30);
            }
            else
            {
                mp.put(titleArray[i].toLowerCase(),30);
            }
        }



        // Loop to iterate over the
        // elements of the map
        for(Map.Entry<String,Integer> entry:
                mp.entrySet())
        {
            list.add(new Keyword(entry.getKey(),entry.getValue()));
        }

        return list.stream()
                .sorted(Comparator.comparingInt(Keyword::getCount).reversed())
                .collect(Collectors.toList());
    }

    public static Document getDocumentFromUrl(String url){
        Document document;

        try{
            document = Jsoup.connect(url).get();
        }catch (Exception ex){
            ex.printStackTrace();
            document = null;
        }

        return document;
    }

    public static String removePunctuations(String source) {
        return source.replaceAll("\\p{Punct}", "");
    }
}
