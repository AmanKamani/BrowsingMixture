package jb.testing.browsingmixture;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.HashMap;

public class Utility {

    public enum SearchEngine {GOOGLE,BING, YOUTUBE,DUCKDUCKGO}

    public static HashMap<Integer,SearchEngine> engine_mapper = new HashMap<>();
    public static HashMap<SearchEngine,String> searchEngine = new HashMap<>();

    static {

        engine_mapper.put(0,SearchEngine.GOOGLE);
        engine_mapper.put(1,SearchEngine.BING);
        engine_mapper.put(3,SearchEngine.YOUTUBE);
        engine_mapper.put(2,SearchEngine.DUCKDUCKGO);

        searchEngine.put(SearchEngine.GOOGLE,"Google");
        searchEngine.put(SearchEngine.BING,"Bing");
        searchEngine.put(SearchEngine.YOUTUBE,"Youtube");
        searchEngine.put(SearchEngine.DUCKDUCKGO,"DuckDuckGo");

    }
    public static Drawable getSearchEngineIcon(Context context,SearchEngine engineType)
    {
        if(engineType == SearchEngine.GOOGLE)
            return context.getResources().getDrawable(R.drawable.google);
        else if(engineType == SearchEngine.BING)
            return context.getResources().getDrawable(R.drawable.bing);
        else if(engineType == SearchEngine.DUCKDUCKGO)
            return context.getResources().getDrawable(R.drawable.duckduckgo);
        else
            return context.getResources().getDrawable(R.drawable.youtube);
    }

    public static String getSearchEngineName(SearchEngine engine){
        return searchEngine.get(engine);
    }
    public static String getSearchUrl(SearchEngine engineType)
    {
        if(engineType == SearchEngine.GOOGLE)
            return "https://www.google.com/search?q=";
        else if(engineType == SearchEngine.BING)
            return "https://www.bing.com/search?q=";
        else if(engineType == SearchEngine.DUCKDUCKGO)
            return "https://www.duckduckgo.com/?q=";
        else if(engineType == SearchEngine.YOUTUBE)
            return "https://www.youtube.com/search?q=";
        else
            return "";
    }
}
