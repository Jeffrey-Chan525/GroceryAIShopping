package com.smartspend.model;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class webScraper {
    @Tool("This tool statically scrapes from an URL to retrieve the text from the HTML file")
    public String scrape(@P("The URL to be scraped") String Url){
        try{
            Document document = Jsoup.connect(Url).get();
            System.out.print("The AI retrieved the following text" + document.text());
            return document.text();
        } catch (IOException e){
            System.out.print("IOException: " + e);
        }
        return "";
    }
}
